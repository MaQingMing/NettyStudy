package com.atguigu.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.nio.channels.Channel;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/8 15:27
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {

        //客户端需要一个事件循环组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        //创建客户端启动对象
        //注意客户端使用的不是ServerBootStrap 而是bootStrap
        Bootstrap bootstrap = new Bootstrap();
        try {
            //设置相关参数
            bootstrap.group(eventExecutors)   //设置线程组
                    .channel(NioSocketChannel.class) // 设置于客户端通道的实现类(反射)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler()) ;  //将自己加入处理器
                        }
                    });
            System.out.println(" 客户端 OK ");

            //启动客户端去连接服务器
            //关羽==关于Channelfuture 要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",6668).sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
