package io.netty.example.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
public class NettyServerHandler extends ChannelInboundHandlerAdapter<String> {

    // 当读取到客户端发送的消息时，会调用此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, String msg) throws Exception {
        // 打印从客户端读取到的字符串消息
        System.out.println("Server received: " + msg);
       // ctx.writeAndFlush("Server received: " + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送消息给客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息", CharsetUtil.UTF_8));
    }


    // 当发生异常时，会调用此方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常堆栈信息
        cause.printStackTrace();

        // 关闭通道
        ctx.close();
    }
}
