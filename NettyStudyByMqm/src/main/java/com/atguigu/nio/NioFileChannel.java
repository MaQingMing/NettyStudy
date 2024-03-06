package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/6 19:06
 */

/**
 * 通道以及Buffer的Api使用
 *
 */
public class NioFileChannel {
    public static void main(String[] args) throws Exception {
        channel();
        FileRead();
        FileChannleTransferFrom();
    }

    public static void FileRead() throws Exception{
        File file = new File("d:\\fileChannel.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());

        channel.read(allocate);

        System.out.println(allocate.array());

        fileInputStream.close();
    }


    public static void FileChannleTransferFrom() throws Exception{

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\a.jpg");
        FileInputStream fileInputStream = new FileInputStream("d:\\a2.jpg");

        //获取通道
        FileChannel channel = fileInputStream.getChannel();
        FileChannel channelfileout = fileOutputStream.getChannel();

        channelfileout.transferFrom(channel,0,channel.size());
        channel.close();
        channelfileout.close();
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void channel() throws Exception{

        String str = "hello,world";

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\fileChannel");

        //获取文件上传通道
        FileChannel channel = fileOutputStream.getChannel();

        //创建字节缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        //写入缓冲区
        allocate.put(str.getBytes());

        //进行反转，刚才数据缓冲区是写入操作，现在就是读
        allocate.flip();
        //文件上传通道读取缓冲区里面的数据
        channel.write(allocate);
        //关闭文件流
        fileOutputStream.close();
    }



}
