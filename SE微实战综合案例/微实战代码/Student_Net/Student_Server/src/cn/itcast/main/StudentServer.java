package cn.itcast.main;

import java.net.ServerSocket;
import java.net.Socket;

/*
   服务端主程序
   每个客户端连接后要开启一个线程来处理这个客户端的数据交互
 */
public class StudentServer {
    public static void main(String[] args)throws Exception {
        //创建ServerSocket对象
        ServerSocket server = new ServerSocket(8888);
        System.out.println("学生管理系统服务器启动...");
        //等待客户端连接
        while(true){
            Socket socket = server.accept();
            //等来一个客户端连接,我们就开一个线程
            new ServerThread(socket).start();
        }


    }
}
