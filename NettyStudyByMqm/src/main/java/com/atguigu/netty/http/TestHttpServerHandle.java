package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.file.attribute.UserPrincipalLookupService;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/9 15:21
 */

/**
 * 自定义一个handle需要继承netty规定好的 某个HandlerAdpater
 * 这是我们自定义一个handle
 */
public class TestHttpServerHandle extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg 是不是 httpquest 请求
        if (msg instanceof HttpRequest){
            System.out.println("msg类型 " + msg.getClass());
            System.out.println("客户端地址 " + ctx.channel().remoteAddress());

            //回复信息给浏览器[http 协议]
            ByteBuf content = Unpooled.copiedBuffer("hello ,我是服务器", CharsetUtil.UTF_8);

            //构造一个http的响应，即httpresponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //将构建好的response 返回
            ctx.writeAndFlush(response);
        }
    }
}
