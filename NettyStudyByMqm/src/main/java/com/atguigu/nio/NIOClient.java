package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/7 15:09
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel open = SocketChannel.open();
        open.configureBlocking(false);
        //提供服务器的ip，端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if (open.connect(inetSocketAddress)){
            while (!open.finishConnect()){
                System.out.println("连接需要时间，不阻塞，可以做别的事情");
            }
        }
        String str =" hello,world";
        ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
        open.write(wrap);
    }
}
