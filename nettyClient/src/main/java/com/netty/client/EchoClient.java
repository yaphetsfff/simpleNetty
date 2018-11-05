package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author FengYaPeng
 * @Description: netty客户端
 * @date 2018/11/2
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); //1创建 Bootstrap
            b.group(group) //2指定 EventLoopGroup 来处理客户端事件。由于我们使用 NIO 传输，所以用到了NioEventLoopGroup 的实现
                    .channel(NioSocketChannel.class) //3使用的 channel 类型是一个用于 NIO 传输
                    .remoteAddress(new InetSocketAddress(host, port)) //4设置服务器的 InetSocketAddress
                    .handler(new ChannelInitializer<SocketChannel>() { //5当建立一个连接和一个新的通道时，创建添加到 EchoClientHandler 实例 到 channelpipeline
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync(); //6连接到远程;等待连接完成
            f.channel().closeFuture().sync(); //7阻塞直到 Channel 关闭
        } finally {
            group.shutdownGracefully().sync(); //8调用 shutdownGracefully() 来关闭线程池和释放所有资源
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(
                    "Usage: " + EchoClient.class.getSimpleName() +
                            " <host> <port>");
            return;
        }
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }
}

