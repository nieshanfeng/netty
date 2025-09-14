package io.netty.example.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    // 当从服务器接收到消息时，会调用此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端开始读取数据");
        // 打印接收到的服务器消息
        System.out.println("收到服务器消息：" + msg.toString());
        ctx.channel().attr(AttributeKey.valueOf("ChannelKey")).set(msg.toString());
        ctx.channel().close();
    }

    // 当通道激活时，会调用此方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 定义要发送的字符串消息=.
        //String msg = "字符串消息";
        // 打印发送消息的日志信息
        //System.out.println("NettyClientHandler发送数据：" + msg);
        // 向服务器发送消息
        //ctx.writeAndFlush(msg);
    }
}
