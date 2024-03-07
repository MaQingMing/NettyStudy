package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/7 16:51
 */
public class GroupChatClient {

    private Selector selector;
    private SocketChannel socketChannel;
    private static final String host = "127.0.0.1";
    private String username;
    private static final int port = 6667;

    public  GroupChatClient(){
        try {
            selector = Selector.open();
            socketChannel=SocketChannel.open(new InetSocketAddress(host,port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username+"is ok");
        }catch (Exception e ){
            e.printStackTrace();
        }

    }
    public void resiveMessage(){
        try {
            //先连接
            int select = selector.select();
            if (select>0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        channel.read(allocate);
                        System.out.println("allocate = " + allocate.array());
                    }
                }
                iterator.remove();
            }else {
                System.out.println(" 没有可用通道");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void sendMessage(String str){
        str = username + "说" + str;
        try {
            socketChannel.write(ByteBuffer.wrap(str.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient GroupChatClient = new GroupChatClient();
        new Thread(()->{
            while (true){
                GroupChatClient.resiveMessage();
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()){
            GroupChatClient.sendMessage(scanner.nextLine());
        }
    }
}
