package com.company;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloServer {

    private static final int portNumber = 8888;//定义一个静态终端服务器绑定端口8888

    public static void main(String[] args) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();//EventLoopGroup用于管理Channel连接，定义两个Group
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();//ServerBootstrap对服务器做配置和启动的类
            b.group(bossGroup,workerGroup);//设置group
            b.channel(NioServerSocketChannel.class);//设置channel为NioServerSocketChannel
            b.childHandler(new ChannelInitializer<SocketChannel>(){//设置childHandler实例化实现
                @Override
                protected void initChannel(SocketChannel ch) throws Exception{
                    ChannelPipeline pipeline = ch.pipeline();//Netty处理请求的责任链，是一个ChannelHandler的链表
                    //DelimiterBasedFrameDecoder基于分隔符的帧解码器
                    pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8888, Delimiters.lineDelimiter()));
                    //StringDecoder字符串解码器
                    pipeline.addLast("decoder",new StringDecoder());
                    //StringEncoder字符串编码器
                    pipeline.addLast("encoder",new StringEncoder());
                    //调用HelloServerHandler，具体逻辑实现
                    pipeline.addLast("handler",new HelloServerHandler());
                }
            });
            //对于bind过程具体实现尚未了解，调用doBind0()方法，调用了initAndRegister()方法进行初始化和register操作
            ChannelFuture f = b.bind(portNumber).sync();//bind过程，sync函数监听端口

            f.channel().closeFuture().sync();//监听端口关闭，防止线程停止
        }finally {
            bossGroup.shutdownGracefully();//优雅的全部关闭
            workerGroup.shutdownGracefully();
        }
        // write your code here
    }
}
