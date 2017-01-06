package com.sino.netty.codec;

import com.sino.netty.pojo.UnixTime;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * ����
 * Created by thierry.fu on 2017/1/5.
 */
public class TimeEncoder extends SimpleChannelHandler {
    /**
     * ������ÿһ��Request����
     */
    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        UnixTime time = (UnixTime) e.getMessage();
        ChannelBuffer buffer = ChannelBuffers.buffer(4);
        buffer.writeInt(time.getValue());
        //ctx.sendDownStream
        //TimeServer��д���Message�����ڴ˴�����encode
        System.out.println(this+"2");
        Channels.write(ctx,e.getFuture(),buffer);
    }
}
