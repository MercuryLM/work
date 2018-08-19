# Netty框架简单实现周作业



**题目：使用Netty4.x创建一个服务器与客户端，创建一个Echo协议的服务端，服务端要求接收客户端发送的文本信息并且在前头加上hello字符串后将信息返回给客户端。**

**一、准备工作：创建一个java项目，导入Netty框架依赖的包**

![导入jar包](https://github.com/MercuryLM/work/blob/master/image/netty/1.png "导入jar包")

**二、具体实现**

项目整体结构：

![整体结构](https://github.com/MercuryLM/work/blob/master/image/netty/1.png "整体结构")
   
   1.创建服务器Server
    
    （1）创建HelloServer类
	
```java
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

```

   （2）创建HelloServerHandler类（具体逻辑实现类）
```java
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
```
2.创建客户端Client
    
    （1）创建HelloClient类
```java
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
```
   （2）创建HelloClientHandler类（具体实现类）
```java
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

```

**三、最后结果**

1.开启一个服务端，两个客户端

![](https://github.com/MercuryLM/work/blob/master/image/netty/3.1.png)

![](https://github.com/MercuryLM/work/blob/master/image/netty/3.2.png)

2.在两个客户端窗口分别输入相关文本信息，服务端返回带有“hello”前缀的客户端发出的文本信息

![](https://github.com/MercuryLM/work/blob/master/image/netty/4.1.png)

![](https://github.com/MercuryLM/work/blob/master/image/netty/4.2.png)

3.服务端窗口记录客户端发出的文本信息

![](https://github.com/MercuryLM/work/blob/master/image/netty/5.png)
