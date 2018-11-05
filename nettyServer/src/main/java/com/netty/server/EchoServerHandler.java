package com.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author FengYaPeng
 * @Description: netty服务端handler
 * @date 2018/11/2
 */
// 1.@Sharable 标识这类的实例之间可以在 channel 里面共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        // 2.日志消息输出到控制台
        ctx.write(in); // 3.将所接收的消息返回给发送者。注意，这还没有冲刷数据
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)// 4.冲刷所有待审消息到远程节点。关闭通道后，操作完成
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace(); // 5.打印异常堆栈跟踪
        ctx.close(); // 6.关闭通道
    }

}
