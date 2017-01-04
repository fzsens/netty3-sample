package com.sino.netty.handler;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 每一个Handler可以单独设计一种协议
 * Created by thierry.fu on 2017/1/4.
 */
public class EchoHandler extends SimpleChannelHandler {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Channel ch = e.getChannel();
        ch.write(e.getMessage());
    }
}
