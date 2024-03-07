package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/7 15:51
 */
public class GroupChatServer {

    //选择器
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private static final int port = 6667;

    public GroupChatServer(){
        try {
            //得到选择器
            selector= Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            //注册到选择器上面
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
                e.printStackTrace();
        }
    }

    public  void listen(){
        try {
            while (true){
                int select = selector.select(2000);
                if (select>0){   //有事件处理
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()){        //说明处理连接状态
                            SocketChannel accept = serverSocketChannel.accept();
                            accept.configureBlocking(false);
                            SelectionKey register = accept.register(selector, SelectionKey.OP_READ);

                            String str =" 服务器"+accept.getRemoteAddress()+"上线了";
                            sendMessage(accept,str);
                        }
                        if (key.isReadable()){
                            readClient(key);
                        }
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待事件处理");
                }
            }

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    //接受客户端的消息
    public void  readClient(SelectionKey selectionKey) throws IOException {
        SocketChannel channel =null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            int read = channel.read(allocate);
            if (read>0){
                //把数据从缓冲区里面拿出来
                byte[] array = allocate.array();
                String msg= new String(array);
                System.out.println("客户端 = " + msg);
            }
        }catch (IOException e ){
            System.out.println(" 服务器下线了 " + channel.getRemoteAddress());
            //取消注册
            selectionKey.cancel();
            channel.close();
        }
    }

    public void sendMessage(SocketChannel socketChannel , String msg){
        System.out.println("服务器发送消息中");
        try {
            selector.keys().forEach(selectionKey -> {

                Channel channel = (Channel)selectionKey.channel();

                if (channel instanceof SocketChannel && socketChannel!=channel){
                    SocketChannel dest= (SocketChannel) channel;
                    ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                    try {
                        dest.write(wrap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {

        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
