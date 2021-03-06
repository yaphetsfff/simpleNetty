package com.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author FengYaPeng
 * @Description: netty客户端handler
 * @date 2018/11/2
 */
@ChannelHandler.Sharable //1@Sharable 标记这个类的实例可以在 channel 里共享

public class EchoClientHandler extends
        SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", //2当被通知该 channel 是活动的时候就发送信息
                CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             ByteBuf in) {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8)); //3记录接收到的消息
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) { //4记录日志错误并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }
}

