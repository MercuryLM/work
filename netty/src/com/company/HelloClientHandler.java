package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

//一开始自己编写Handler类弄得比较乱，查找资料可以继承SimpleChannelInboundHandler<String>
public class HelloClientHandler extends SimpleChannelInboundHandler<String>{
    @Override
    //下面三个函数都是ChannelInboundHandlerAdapter下面的函数，根据不同参数调用不同函数，在这里进行函数复写
    //接受到了服务器的信息，输出服务器返回的消息
    protected void channelRead0(ChannelHandlerContext ctx,String msg) throws Exception{
        System.out.println("Server say:" + msg);

    }
    @Override
    //在连接成功时返回客户端的状态，fireChannelActive
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("Client active");
        super.channelActive(ctx);
    }
    //返回关闭状态，firechannelInactive
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("Client close");
        super.channelInactive(ctx);
    }
}
