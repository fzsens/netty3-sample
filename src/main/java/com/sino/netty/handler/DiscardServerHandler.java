package com.sino.netty.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

/**
 * Created by thierry.fu on 2017/1/4.
 */
public class DiscardServerHandler extends SimpleChannelHandler{


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("3");
        //netty4 ByteBuf ���ڴ洢���л����ֽ�
//        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
//        //����telnet����ֱ�Ӵ�ӡ�ֽ�
//        while (buffer.readable()) {
//            System.out.println((char)buffer.readByte());
//            System.out.flush();
//        }
        super.messageReceived(ctx, e);
        ctx.sendDownstream(e);
    }

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("3");
        //������һ��
        super.writeRequested(ctx, e);
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
