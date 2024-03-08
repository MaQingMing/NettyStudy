package com.atguigu.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/8 15:35
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //挡通道就绪时会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server",CharsetUtil.UTF_8));

    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf=(ByteBuf) msg;
        String string = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("服务区回复的消息 = " + string);
        System.out.println("服务器的地址 " + ctx.channel().remoteAddress());
    }
    //有异常的时候读取
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
