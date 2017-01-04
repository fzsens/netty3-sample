package com.sino.netty.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

/**
 * Created by thierry.fu on 2017/1/4.
 */
public class DiscardHandler extends SimpleChannelHandler{


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        //netty4 ByteBuf 用于存储序列化的字节
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        //接收telnet输入直接打印字节
        while (buffer.readable()) {
            System.out.println((char)buffer.readByte());
            System.out.flush();
        }
    }

    /**
     * when exception raised.
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
}
