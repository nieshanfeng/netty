package io.netty.example.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

public class NettyServer {
    public static void main(String[] args) throws Exception{
        // 创建boss线程组，用于接受客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 创建worker线程组，用于进行SocketChannel的数据读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap，它是Netty服务端的启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 绑定线程组到ServerBootstrap
            serverBootstrap.group(bossGroup, workerGroup)
                    // 指定使用NioServerSocketChannel接受进来的连接
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //4.boss负责处理连接worker(child)负责处理读写,决定了worker(child)能执行哪些操作(handler)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //5.channel代表和客户端进行数据读写的通道 Init.ializer 初始化,负责添加别的handler
                    ChannelPipeline pipeline = ch.pipeline().addLast(new LoggingHandler());
                    // 添加字符串解码器,将ByteBuf转换为字符串
                    pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
                    pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
                    //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer. MAX_VALUE, Delimiters.lineDelimiter()[0]));
                    ch.pipeline().addLast(new NettyServerHandler());
                }
            });
            // 打印服务启动信息
            System.out.println("netty server start。。");
            // 绑定端口号，并同步等待成功，即启动服务端
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            // 等待服务器socket关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅地关闭boss线程组
            bossGroup.shutdownGracefully();
            // 优雅地关闭worker线程组
            workerGroup.shutdownGracefully();
        }
    }

}
