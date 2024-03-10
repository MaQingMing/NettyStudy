package com.atguigu.groupChatByNetty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/10 15:07
 */
public class GroupAdpaterHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel   组，管理所有的channel
    //GlobalEventExecutor.INSTANCE  是全局的事件执行器，是一个单列
    private static ChannelGroup channelGroup =new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded表示连接建立时，一旦连接，第一个被执行
    //将当前channel 加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线执行的客户端
        /**
         * 该方法会将channelGroup 中的所有的channel 遍历，并发送消息
         */
        channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+"加入多人聊天"+sdf.format(new Date()));
        channelGroup.add(channel);
    }

    //处于活动状态，提示状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println( socketAddress+"上线了");
    }

    //不活跃状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("下线了" + ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+"下线了");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到channel
        Channel channel = ctx.channel();
        channelGroup.forEach(channel1 -> {
            if (channel1!=channel){
                channel1.writeAndFlush("[客户]"+channel.remoteAddress()+"发送消息"+msg);
            }else {
                channel1.writeAndFlush("[自己]发送了消息"+msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
