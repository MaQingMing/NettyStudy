package com.atguigu.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/9 15:20
 */
public class TestServer {


    public static void main(String[] args) throws Exception {

        //首先创建两个线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup=  new NioEventLoopGroup();

        try{
            //创建服务器
            ServerBootstrap serverBootstrap =new ServerBootstrap();
            //把两个线程组，加入其中
            serverBootstrap.group(bossGroup,workGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new TestHttpServerHandle());          //设置处理器
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();        //绑定端口

            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();     //优雅的关闭
            workGroup.shutdownGracefully();
        }
    }
}
