package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/7 14:46
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {

        //创建一个ServerSocketChannel 相当于ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //创建一个selecor 对象
        Selector selector = Selector.open();

        //绑定一个端口，在服务端进行监听
        serverSocketChannel.socket().bind(new InetSocketAddress(
                6666
        ));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把服务端注册到selector 关心 事件为 OP_ACCEPT
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户连接
        while (true){
            //在这里等待一秒。如果没没有事件发生就返回
            if (selector.select(1000)==0){       //没有事件发生
                System.out.println(" 服务器等待了一秒，无连接" );
                continue;
            }
            //如果返回的》0 ，或获取到相关的selectKeys 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //迭代器循环Set集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                //获取到selectionkey
                SelectionKey key = iterator.next();
                if (key.isAcceptable()){
                    //如果是OP_APPECT ，有新的客户连接
                    //该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);
                    //注册到selector上面  关注事件为OP_READ
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    if (key.isReadable()){
                        //通过key 反向获取到对应的channel
                        SocketChannel channel =(SocketChannel) key.channel();

                        //获取该channel的buffer
                        ByteBuffer attachment =(ByteBuffer) key.attachment();

                        channel.read(attachment);

                        System.out.println("from 客户端" + new String(attachment.array()));
                    }

                    //手动从集合中移动当前的selectionKey,防止重负操作
                    iterator.remove();
                }
            }
        }
}
}
