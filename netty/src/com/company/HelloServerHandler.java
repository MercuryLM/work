package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetAddress;

//一开始自己编写Handler类弄得比较乱，查找资料可以继承SimpleChannelInboundHandler<String>
public class HelloServerHandler extends SimpleChannelInboundHandler<String>{
    //下面两个函数都是ChannelInboundHandlerAdapter下面的函数，根据不同参数调用不同函数
    //收到消息时使服务端对客户端输入的文本信息加入hello前缀进行返回
    protected void channelRead0(ChannelHandlerContext ctx,String msg) throws Exception{
        System.out.println(ctx.channel().remoteAddress() + ": " + msg);
        //channelHandlerContent中的writeAndFlush方法。作用是写入Buffer并刷入。
        ctx.writeAndFlush("hello" + msg + "\n");//字符串以\n结束，解码器DelimiterBasedFrameDecoder根据字符串“\n”来结尾的
    }
    //连接成功时输出客户端的相关信息，并向客户端发出问候语 Welcome
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("RamoteAddress:"+ ctx.channel().remoteAddress() + " active!");
        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostAddress() + " service!\n");
        super.channelActive(ctx);
    }
}
