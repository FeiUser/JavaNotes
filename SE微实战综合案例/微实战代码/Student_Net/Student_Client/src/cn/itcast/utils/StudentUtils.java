package cn.itcast.utils;

import cn.itcast.pojo.Student;

import java.util.ArrayList;

public class StudentUtils {
    //打印ArrayList<Student>的方法,查看所有学生信息
    public static void printStudentList(ArrayList<Student> stuList) {
        System.out.println("--------------------------------------------------");
        System.out.println("编号\t\t姓名\t\t\t性别\t\t年龄");
        for (int i = 0; i < stuList.size(); i++) {
            Student stu = stuList.get(i);
            System.out.println(stu.getId() + "\t\t" + stu.getName() + "\t\t\t" + stu.getSex() + "\t\t" + stu.getAge());
        }
        System.out.println("--------------------------------------------------");
    }
	
    //打印单个学生的方法
    public static void printStudent(Student stu) {
        System.out.println("--------------------------------------------------");
        System.out.println("编号\t\t姓名\t\t\t性别\t\t年龄");
        System.out.println(stu.getId() + "\t\t" + stu.getName() + "\t\t\t" + stu.getSex() + "\t\t" + stu.getAge());
        System.out.println("--------------------------------------------------");
    }

}
