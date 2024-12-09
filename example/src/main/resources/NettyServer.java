package io.netty.example.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {
    public static void main(String[] args) {
        //1.启动器,负责组装netty组件,启动服务器
        new ServerBootstrap()
                // 2. BossEventLoop, WorkerEventLoop(selector,threead),group组
                .group(new NioEventLoopGroup())
                //3.选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)//01IO BIO
                //4.boss负责处理连接worker(child)负责处理读写,决定了worker(child)能执行哪些操作(handler)
                .childHandler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        //5.channel代表和客户端进行数据读写的通道 Init.ializer 初始化,负责添加别的handler
                        ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(new StringDecoder());//将ByteBuf转换为字符串
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);//打印上一步转换好的字符串
                            }
                        });
                    }
                })
                //7.绑定监听端口
                .bind(8080);

    }


}
