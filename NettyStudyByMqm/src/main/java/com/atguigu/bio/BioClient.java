package com.atguigu.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/5 20:17
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        //创建一个连接客户端
        try {
            System.out.println("客户端发起连接");
            socket = new Socket("localhost",6666);
            while (true){
                OutputStream outputStream = socket.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                System.out.println("请输入聊天内容" );
                String message = scanner.nextLine();
                outputStreamWriter.write(message+"\n");
                outputStreamWriter.flush();
                if (message.equals("bye")){
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(scanner != null ) {
                scanner.close();
            }
        }
    }
}
