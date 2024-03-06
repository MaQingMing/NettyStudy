package com.atguigu.bio;



import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/5 20:06
 */
public class BioServer {
    public static void main(String[] args) throws IOException {

        //线程池机制

        //思路
        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程与之连接
        ExecutorService executorService = Executors.newCachedThreadPool();

        //开启一个服务器
        final ServerSocket socket = new ServerSocket(6666);
        System.out.println("服务器启动了！");
        while (true){
            //接受请求
            Socket accept = socket.accept();
            System.out.println("连接一个客户端");
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //重写
                    try {
                        handler(accept);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            InputStream inputStream = accept.getInputStream();

        }

    }

    //编写一个handle代码,与客户端通信
    public static void handler(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        //获取输入流
        try {

            InputStream inputStream = socket.getInputStream();
            System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            while (true){
                int read = inputStream.read(bytes);
                if (read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            System.out.println("关闭连接了");
            socket.close();
        }
    }
}
