package com.atguigu.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.channels.Channel;
import java.util.concurrent.TimeUnit;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/8 15:00
 */

/**
 * 1.我们自定义一个Handler 需要继承netty 规定好的某个HandlerAdapter(规范)
 * 2.这是我们自定义一个Handler ,才能称为一个handler
 */
public class NettyServerHandle extends ChannelInboundHandlerAdapter {
    //读取数据实际（这里我们可以读取客户端发送的消息）

    /**
     * ChannelHandlerContext ctx :上下文对象，含有管道pipeline ,通道channel ,地址
     * Object msg : 就是客户端发送的数据 ： 默认Object
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        //将msg 装成一个ByteBuf
        //ByteBuf 是netty提供的，不是NIO的ByteBuffer

        //用户自定义事件执行事件 任务提交到TaskQueue
        try {
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        //用户自定义事件执行事件,上面有一个任务执行时间是10秒，现在是等待20秒，等待30秒之后才执行该任务
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(20*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });


        //用户自定义定时任务 任务提交到 ScheduleTaskQueue 中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        },5, TimeUnit.SECONDS);



        ByteBuf byteBuf=(ByteBuf) msg;
        System.out.println("客户端发送消息是： " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址是：  " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write + flush
        //将数据写入缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端", CharsetUtil.UTF_8));
    }

    //处理异常，一般都是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
