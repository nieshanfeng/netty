package io.netty.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 当读取到客户端发送的消息时，会调用此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 打印从客户端读取到的字符串消息
        //System.out.println("Server received: " + msg);
        //ctx.writeAndFlush("Server received: " + msg);
        System.out.println("开始读取数据");
        if(msg instanceof ByteBuf){
            ByteBuf req = (ByteBuf)msg;
            String content = req.toString(Charset.defaultCharset());
            System.out.println(content);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送消息给客户端
       // ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息", CharsetUtil.UTF_8));
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
