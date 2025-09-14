package io.netty.example.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


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
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 设置通道初始化处理器
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 获取通道的管道
                            ChannelPipeline pipeline = ch.pipeline();
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                            pipeline.addLast(new StringDecoder());
                            // 添加自定义的处理器
                            pipeline.addLast(new NettyClientHandler());
                            //第1种 添加字符串编码器
                            pipeline.addLast(new StringEncoder());
                        }
                    });

            // 打印客户端启动信息
            System.out.println("netty client start。。");
            // 连接到服务器，同步等待成功，即启动客户端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();

            String person="张三\r\n";
            //channelFuture.channel().writeAndFlush(person.getBytes(StandardCharsets.UTF_8));
            //第1种
            channelFuture.channel().writeAndFlush(person);


            //第2种
          /*  ByteBuf buf = PooledByteBufAllocator. DEFAULT.buffer();
            buf.writeBytes(person.getBytes(StandardCharsets.UTF_8));
            channelFuture.channel().writeAndFlush(buf);*/
            //channelFuture.channel().writeAndFlush(Delimiters.lineDelimiter()[0]);

            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
           Object result= channelFuture.channel().attr(AttributeKey.valueOf("ChannelKey")).get();
            System.out.println(result);
        } finally {
            // 优雅地关闭事件循环组
            group.shutdownGracefully();
        }
    }
}