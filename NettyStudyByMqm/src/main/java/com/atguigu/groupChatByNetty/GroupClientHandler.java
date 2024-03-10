package com.atguigu.groupChatByNetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;


import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;


/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/10 15:35
 */
public class GroupClientHandler {

    private final String host;
    private final int port;

    public GroupClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run () throws Exception{
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new StringDecoder())
                                    .addLast("encoder",new StringEncoder())
                                    .addLast(new GroupCilentHandlerSimple());
                        }
                    });
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            Channel channel = sync.channel();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                channel.writeAndFlush(s+"\n");
            }
        }finally {
            eventExecutors.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        GroupClientHandler groupClientHandler = new GroupClientHandler("127.0.0.1",7000);
        groupClientHandler.run();
    }
}
