package cn.itcast.main;

import cn.itcast.dao.StudentDao;
import cn.itcast.pojo.Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/*
   快速将选中的代码放在try中-->ctrl+alt+T

   服务端通用步骤 :
        1.将客户端传递过来要添加的数据封装成Student对象
        2.调用StudentDao的增删改查方法
        3.将结果反馈给客户端
        4.释放资源
 */
public class ServerThread extends Thread{
    private Socket socket;
    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //将客户端想要执行的功能的代号获取出来,我们才能够判断,应该执行什么功能的方法
         //读取每一个客户端发过来的请求(想要执行哪个功能的代号[1][2][3]...)
        try(InputStream in = socket.getInputStream();){
            byte[] bytes = new byte[1024];
            int len = in.read(bytes);
            //假如我们读取了->"[1]柳岩,女,36"
            String msg = new String(bytes, 0, len);
            //分析数据将序号获取出来
            String num = msg.substring(1, 2);//"[1]柳岩,女,36"
            //将功能的代号获取出来之后,我们就可以去执行对应的增删改查的方法了
            switch (num) {
                case "1":
                    //添加学生
                    addStudent(msg);
                    break;
                case "2":
                    //根据ID查询
                    findStudent(msg);
                    break;
                case "3":
                    //修改数据
                    updateStudent(msg);
                    break;
                case "4":
                    //查询所有
                    findStudents(msg);
                    break;
                case "5":
                    //删除一条
                    deleteStudent(msg);
                    break;
                default:
                    //客户端数据有误~~
                    System.out.println("该客户端数据有误~~");
                    socket.close();
                    break;
            }

        }catch (Exception e){

        }
    }

    //添加学生
    public void addStudent(String msg) {
        //假如msg:[1]柳岩,女,36
        //1.将客户端传递过来要添加的数据封装成Student对象
        String stuMsg = msg.substring(3);//柳岩,女,36
        String[] msgArr = stuMsg.split(",");
        Student student = new Student(msgArr[0], msgArr[1], Integer.parseInt(msgArr[2]));
        //2.调用StudentDao的增删改查方法
        StudentDao.addStudent(student);
        //3.将结果反馈给客户端
        try ( OutputStream out = socket.getOutputStream();){
            out.write(1);
            //4.释放资源
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据ID查询学生
    public void findStudent(String msg) {
       //msg:[2]1
        //1.封装数据
        int findID = Integer.parseInt(msg.substring(3));//1
        //2.调用StudentDao的增删改查方法
        Student student = StudentDao.findById(findID);
        //3.将结果反馈给客户端
        try{
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            //将从文件中读取出来的集合序列化给客户端
            oos.writeObject(student);
            //4.释放资源
            oos.close();
            out.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //修改学生
    public void updateStudent(String msg) {
       //msg: [3]1,柳岩,女,36
        //1.封装数据,将数据获取出来,封装到Student对象中
        String msgStu = msg.substring(3);
        String[] msgArr = msgStu.split(",");
        Student student = new Student();
        student.setId(Integer.parseInt(msgArr[0]));
        student.setName(msgArr[1]);
        student.setSex(msgArr[2]);
        student.setAge(Integer.parseInt(msgArr[3]));

        //2.调用StudentDao中的增删改查方法
        StudentDao.updateStudent(student);

        //3.将结果反馈给客户端
        try {
            OutputStream out = socket.getOutputStream();
            out.write(1);
            //4.释放资源
            out.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //查询所有学生
    public void findStudents(String msg) {
        /*
           1.将客户端传递过来要添加的数据封装成Student对象
             由于查询所有不需要封装数据,所以我们不封装了
         */
        //2.调用StudentDao的增删改查方法
        ArrayList<Student> stuList = StudentDao.readAll();
        //3.将结果反馈给客户端
        try{
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            //将从文件中读取出来的集合序列化给客户端
            oos.writeObject(stuList);
            //4.释放资源
            oos.close();
            out.close();
            socket.close();
        }catch(Exception e){
           e.printStackTrace();
        }

    }

    //根据ID删除学生
    public void deleteStudent(String msg) {
       //msg:  [5]2
        //1.封装数据
        int deleteID = Integer.parseInt(msg.substring(3));//2
        //2.调用StudentDao中的增删改查方法
        StudentDao.deleteById(deleteID);
        //3.将结果反馈给客户端
        try {
            OutputStream out = socket.getOutputStream();
            out.write(1);
            //4.释放资源
            out.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
