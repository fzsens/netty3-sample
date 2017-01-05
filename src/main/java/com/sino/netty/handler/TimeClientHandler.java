package com.sino.netty.handler;

import com.sino.netty.pojo.UnixTime;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import java.util.Date;

/**
 * Created by thierry.fu on 2017/1/5.
 */
public class TimeClientHandler extends SimpleChannelHandler {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        //����Э��
//        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
//        long currentTimeMillis = buffer.readInt() * 1000L;
//        System.out.println(new Date(currentTimeMillis));
        UnixTime time = (UnixTime) e.getMessage();
        System.out.println(time);
        e.getChannel().close();
        //�����µ����ݽ������ٴε���messageReceived
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
