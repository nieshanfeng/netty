package io.netty.example.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;


public class NettyClient {
    // Netty客户端的主方法
    public static void main(String[] args) throws Exception {
        // 创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建Bootstrap，它是Netty客户端的启动助手
            Bootstrap bootstrap = new Bootstrap();
            // 绑定事件循环组到Bootstrap
            bootstrap.group(group).channel(NioSocketChannel.class)
                    // 设置通道初始化处理器
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 获取通道的管道
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            // 添加字符串编码器
                            pipeline.addLast(new StringEncoder());
                            // 添加自定义的处理器
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            // 打印客户端启动信息
            System.out.println("netty client start。。");
            // 连接到服务器，同步等待成功，即启动客户端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅地关闭事件循环组
            group.shutdownGracefully();
        }
    }
}