package cn.itcast.main;
import cn.itcast.pojo.Student;
import cn.itcast.utils.StudentUtils;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
/*
     客户端通用的代码编写流程:
        a.客户端得到数据(键盘录入)
        b.使用TCP连接将数据发送给服务端->发请求
        c.读取服务端发送回来的数据->读响应
        d.释放资源
 */
public class StudentClient {
    //定义一个静态的Scanner对象
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("欢迎使用学生管理系统~~~");
        while (true) {
            //1.菜单
            System.out.println();//空行,为了美观
            System.out.println("1.添加学生  2.修改学生  3.删除学生  4.查询学生 5.查看所有学生  6.退出");
            //2.键盘录入
            System.out.println("请选择:");
            int user = sc.nextInt();
            //3.判断
            switch (user) {
                case 1:
                    //添加学生
                    addStudent();
                    break;
                case 2:
                    //修改学生
                    updateStudent();
                    break;
                case 3:
                    //删除学生
                    deleteStudent();
                    break;
                case 4:
                    //根据ID查询
                    findStudent();
                    break;
                case 5:
                    //查询所有学生
                    findStudents();
                    break;
                case 6:
                    //退出
                    System.out.println("欢迎下次光临~~");
                    System.exit(0);
                default:
                    //提示输入有误
                    System.out.println("您输入的功能有误~~");
                    break;
            }
        }

    }

    //添加学生
    public static void addStudent(){
        System.out.println("【添加学生】");
        //a.客户端得到数据(键盘录入)
        System.out.println("请你输入姓名:");
        String name = sc.next();

        System.out.println("请你输入性别:");
        String sex = sc.next();

        System.out.println("请你输入年龄:");
        int age = sc.nextInt();
        //b.使用TCP连接将数据发送给服务端->发请求
        Socket socket = getSocket();
        if (socket==null){
            System.out.println("服务器暂时无法连接...");
            return;
        }

        //客户端往服务端发送要操作的请求->添加

        try (OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream();){
             out.write(("[1]"+name+","+sex+","+age).getBytes());
            //c.读取服务端发送回来的数据->读响应

            //服务端响应回来的结果以0  和  1表示   1表示成功
            int result = in.read();
            if (result==1){
                System.out.println("[添加成功]");
            }else{
                System.out.println("[添加失败,请联系系统联系员~~~]");
            }
            //d.释放资源
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //修改学生[3]
    public static void updateStudent() {
        System.out.println("【修改学生】");
        //a.客户端得到数据(键盘录入)
        System.out.println("请输入要修改的学生ID:");
        int ID = sc.nextInt();
        //b.使用TCP连接将数据发送给服务端->发请求
        Socket socket = getSocket();
        if (socket==null){
            System.out.println("服务器暂时无法连接...");
            return;
        }

        /*
           我们修改学生之前,首先要查询该学生,如果不存在,提示不存在
           如果存在,再让用户继续输入其他要修改的信息->根据id查询学生
         */

        Student student = null;
        try(OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

           ){
            //告知服务器要执行什么功能的操作

            out.write(("[2]"+ID).getBytes());
            //c.读取服务端发送回来的数据->读响应
        /*
            由于我们要查询学生的信息(属性值),所以服务端那边需要返回给我一个Student对象,
            我们要反序列化,读取服务端写过来的Student对象
         */
            ObjectInputStream ois = new ObjectInputStream(in);
            student = (Student) ois.readObject();
            if (student==null){
                System.out.println("[查无此人]");
                return;
            }else{
                StudentUtils.printStudent(student);
            }
            //d.释放资源
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //================以下是修改学生的代码实现======================
        /*
            如果存在,再让用户继续输入其他要修改的信息
            如果程序执行到这里了,证明有要修改的学生,我们就可以修改了
          */

        //1.客户端得到数据(用户输入其他的信息)
        System.out.println("请输入新的姓名(如果不修改输入0):");
        String name = sc.next();
        System.out.println("请输入新的性别(如果不修改输入0):");
        String sex = sc.next();
        System.out.println("请输入新的年龄(如果不修改输入0):");
        int age = sc.nextInt();

        //用户输入完毕之后,我们先判断
        if (!"0".equals(name)){
            student.setName(name);
        }

        if (!"0".equals(sex)){
            student.setSex(sex);
        }

        if (age!=0){
            student.setAge(age);
        }


        //2.使用TCP连接将数据发送给服务器
        Socket socketUpdate = getSocket();
        if (socketUpdate==null){
            System.out.println("服务器暂时无法连接...");
            return;
        }

        try(OutputStream out = socketUpdate.getOutputStream();
            InputStream in = socketUpdate.getInputStream();){
            //告知服务器我们要修改数据了,你执行修改去吧[3]

            out.write(("[3]"+ID+","+ student.getName()+","+student.getSex()+","+student.getAge()).getBytes());
            //c.读取服务端发送回来的数据->读响应

            int result = in.read();
            if (result==1){
                System.out.println("[修改成功]");
            }else{
                System.out.println("[修改失败,请联系管理员~~~]");
            }
            //d.释放资源
            socketUpdate.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //删除学生
    public static void deleteStudent() {
        System.out.println("【删除学生】");
        //a.客户端得到数据(键盘录入)
        System.out.println("请您输入要删除的学生ID:");
        int ID = sc.nextInt();
        //由于删除学生的时候先要根据我们要删除的学生id展示一下学生信息,所以我们要先根据id查询学生信息
        //b.使用TCP连接将数据发送给服务端->发请求
        //连接服务器
        Socket socket = getSocket();
        if (socket==null){
            System.out.println("服务器暂时无法连接...");
            return;
        }
        try(OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
        ){
            //告知服务器要执行什么功能的操作

            out.write(("[2]"+ID).getBytes());
            //c.读取服务端发送回来的数据->读响应
        /*
            由于我们要查询学生的信息(属性值),所以服务端那边需要返回给我一个Student对象,
            我们要反序列化,读取服务端写过来的Student对象
         */
            ObjectInputStream ois = new ObjectInputStream(in);
            Student student = (Student) ois.readObject();
            if (student==null){
                System.out.println("[查无此人]");
                return;
            }else{
                StudentUtils.printStudent(student);
            }
            //d.释放资源
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        /*
           用户输入完ID之后,我们不应该立即发送给服务器进行删除的消息
           应该先把id学生查询出来,然后让用于确定要不要删除
         */
        System.out.println("您确定要删除以上信息的学生吗:(y/n)?");
        String user = sc.next();
        if (!"y".equals(user)){
            System.out.println("[删除操作已经取消]");
            return;
        }
        //b.使用TCP连接将数据发送给服务端->发请求
        Socket socketDelete = getSocket();
        if (socketDelete==null){
            System.out.println("服务器暂时无法连接...");
            return;
        }

        try( OutputStream out = socketDelete.getOutputStream();
             InputStream in = socketDelete.getInputStream();){
            //告知服务器我们要删除
            out.write(("[5]"+ID).getBytes());
            //c.读取服务端发送回来的数据->读响应
            int result = in.read();
            if (result==1){
                System.out.println("[删除成功]");
            }else{
                System.out.println("[删除失败,别瞎删除,你要跑路吗?别跑~~~]");
            }
            //d.释放资源
            socketDelete.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //根据ID查询
    public static void findStudent() {
        System.out.println("【根据ID查询】");
        //a.客户端得到数据(键盘录入)
        System.out.println("请你输入要查询的学生ID:");
        int ID = sc.nextInt();
        //b.使用TCP连接将数据发送给服务端->发请求
          //连接服务器
        Socket socket = getSocket();
        if (socket==null){
            System.out.println("服务器暂时无法连接...");
            return;
        }
        try(OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
           ){
            //告知服务器要执行什么功能的操作

            out.write(("[2]"+ID).getBytes());
            //c.读取服务端发送回来的数据->读响应
        /*
            由于我们要查询学生的信息(属性值),所以服务端那边需要返回给我一个Student对象,
            我们要反序列化,读取服务端写过来的Student对象
         */
            ObjectInputStream ois = new ObjectInputStream(in);
            Student student = (Student) ois.readObject();
            if (student==null){
                System.out.println("[查无此人]");
                return;
            }else{
                StudentUtils.printStudent(student);
            }
            //d.释放资源
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //查询所有学生[4]
    public static void findStudents() {
        System.out.println("【查询所有学生】");
        //a.客户端得到数据(键盘录入)->由于我们要查询所有学生,所以我们没必要再去输入了
        //b.使用TCP连接将数据发送给服务端->发请求
        Socket socket = getSocket();
        if (socket==null){
            System.out.println("服务器暂时无法连接...");
            return;
        }


        try(OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

           ){

            //告知服务器我们要查询所有,发送序号[4]
            out.write("[4]".getBytes());

        /*
           c.读取服务端发送回来的数据->读响应
             查询所有,服务器返回的是一个放有多个Student的ArrayList集合
             所以,我们反序列化的就是ArrayList集合
         */
            ObjectInputStream ois = new ObjectInputStream(in);
            ArrayList<Student> students =(ArrayList<Student>) ois.readObject();
            if (students==null || students.size()==0){
                System.out.println("[服务器暂无学生信息]");
            }else{
                //调用查询所有学生的工具类方法
                StudentUtils.printStudentList(students);
            }

            //d.释放资源
            socket.close();
        }catch (Exception e){
           e.printStackTrace();
        }

    }

    //创建客户端的方法
    public static Socket getSocket() {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }
}
