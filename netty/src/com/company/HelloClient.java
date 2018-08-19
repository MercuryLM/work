package com.company;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloClient {
    public static int port = 8888;//定义一个静态终端服务器绑定端口8888
    public static String host = "localhost";//定义一个本地地址

    public static void main(String[] args) throws InterruptedException,IOException{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>(){//设置childHandler实例化实现
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    //DelimiterBasedFrameDecoder基于分隔符的帧解码器
                    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8888, Delimiters.lineDelimiter()));
                    //StringDecoder字符串解码器
                    pipeline.addLast("decoder", new StringDecoder());
                    //StringEncoder字符串编码器
                    pipeline.addLast("encoder", new StringEncoder());
                    //调用HelloClientHandler，具体逻辑实现
                    pipeline.addLast("handler", new HelloClientHandler());
                }
            });

            Channel ch = b.connect(host,port).sync().channel();//bind过程，sync函数监听端口

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));//利用BufferedReader进行输入缓存
            for (;;){   //设置死循环
                String line = in.readLine();  //客户端输入信息
                if (line.equals("bye")) {    //判断如果输入的信息为“bye”，则退出，连接断开
                    System.out.println("连接断开");
                    break;
                }
                ch.writeAndFlush(line + "\r\n");  //返回给服务端，\r起到换行的作用，\n为字符串结尾，便于基于分隔符的帧解码器解码
            }
        } finally {
            group.shutdownGracefully();//优雅的全部关闭
        }
    }
}
