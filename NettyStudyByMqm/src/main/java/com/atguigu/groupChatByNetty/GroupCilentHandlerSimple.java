package com.atguigu.groupChatByNetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/10 15:53
 */
public class GroupCilentHandlerSimple extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("msg = " + msg);
    }
}
