package cn.itcast.dao;

import cn.itcast.pojo.Student;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;

public class StudentDao {
    //将集合中所有学生对象,写入到文件中
    public static void writeAll(ArrayList<Student> stuList) {
        try (FileWriter out = new FileWriter("Student_Server\\student.txt")) {
            for (Student stu : stuList) {
                //格式: id,姓名,性别,年龄
                out.write(stu.getId() + "," + stu.getName() + "," + stu.getSex() + "," + stu.getAge());
                //换行
                out.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //从文件中读取所有学生的信息,返回学生集合
    public static ArrayList<Student> readAll() {
        ArrayList<Student> stuList = new ArrayList<>();
        /*
		  1.创建File对象
		    如果没等添加学生呢,就开始读取学生了,那么我们需要先把文件创建出来
		*/
        File file = new File("Student_Server\\student.txt");
        if (!file.exists()) {
            try {
                //2.如果不存在,则创建,否则读取会抛异常
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //3.读数据,一次一行 格式: id,姓名,性别,年龄
        try (BufferedReader bufIn = new BufferedReader(
                new FileReader("Student_Server\\student.txt"))) {
            String line = null;
            while ((line = bufIn.readLine()) != null) {
                //4.一行切割成一个数组,[id,姓名,性别,年龄]
                String[] rowArray = line.split(",");
                //5.创建学生对象,封装数据
                Student stu = new Student();
				//因为获取出来0索引的元素是String,所以我们需要将String变成int
                stu.setId(Integer.parseInt(rowArray[0]));
                stu.setName(rowArray[1]);
                stu.setSex(rowArray[2]);
                stu.setAge(Integer.parseInt(rowArray[3]));
                //6.添加到集合中
                stuList.add(stu);
            }
        } catch (IOException e) {
            return null;
        }
        //7.返回整个集合
        return stuList;
    }

    //添加一个学生,返回boolean代表是否添加成功
    public static boolean addStudent(Student student) {
        //1.先读取所有学生
        ArrayList<Student> stuList = readAll();
        if (stuList == null) {//说明读取文件出错
            return false;
        }

        //2.获取最后一个学生的id,加1后作为新学生的id
        if (stuList.size() != 0) {
            student.setId(stuList.get(stuList.size() - 1).getId() + 1);//取最后一个对象的id + 1
        } else {
            //3.如果没有学生,说明是第一个,则id设置为1
            student.setId(1);//第一次添加，文件中没有内容
        }
        //4.添加到集合中
        stuList.add(student);
        //5.把集合重写写入到文件中
        writeAll(stuList);
        //6.返回添加成功
        return true;
    }
	
    //根据id删除一个学生,返回boolean代表是否删除成功
    public static boolean deleteById(int id) {
        //1.先读取所有学生
        ArrayList<Student> stuList = readAll();
        if (stuList == null) {//说明读取文件出错
            return false;
        }
        //2.遍历集合
        for (int i = 0; i < stuList.size(); i++) {
            Student stu = stuList.get(i);
            //3.判断学生的id是否和要删除的id相等
            if (stu.getId() == id) {
                //4.从集合中删除学生
                stuList.remove(i);
                //5.重写写入到文件中
                writeAll(stuList);
                //6.返回成功
                return true;
            }
        }
        //7.如果没找到学生返回失败
        return false;
    }
    //修改学生,返回boolean代表是否修改成功
    public static boolean updateStudent(Student student) {
        //1.先读取所有学生
        ArrayList<Student> stuList = readAll();
        if (stuList == null) {//说明读取文件出错
            return false;
        }
        System.out.println("修改的数据：" + student);
        //2.遍历集合
        for (int i = 0; i < stuList.size(); i++) {
            Student stu = stuList.get(i);
            //3.判断哪个学生id和要修改的学生id相同
            if (stu.getId() == student.getId()) {
                //4.将学生改为新的学生
                stuList.set(i, student);
                //5.重写将集合写入到文集中
                writeAll(stuList);//写回文件
                //6.返回成功
                return true;
            }
        }
        //7.返回失败
        return false;//没找到
    }
    //根据id查询学生,返回查询到的学生
    public static Student findById(int id) {
        //1.先读取所有学生
        ArrayList<Student> stuList = readAll();
        if (stuList == null) {//说明读取文件出错
            return null;
        }
        //2.遍历集合
        for (int i = 0; i < stuList.size(); i++) {
            Student stu = stuList.get(i);
            //3.比较id
            if (stu.getId() == id) {
                //4.找到返回学生对象
                return stu;
            }
        }
        //5.找不到返回null
        return null;
    }
}
