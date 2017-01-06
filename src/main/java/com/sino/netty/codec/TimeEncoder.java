package com.sino.netty.codec;

import com.sino.netty.pojo.UnixTime;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 编码
 * Created by thierry.fu on 2017/1/5.
 */
public class TimeEncoder extends SimpleChannelHandler {
    /**
     * 会拦截每一个Request请求
     */
    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        UnixTime time = (UnixTime) e.getMessage();
        ChannelBuffer buffer = ChannelBuffers.buffer(4);
        buffer.writeInt(time.getValue());
        //ctx.sendDownStream
        //TimeServer中写入的Message，会在此处进行encode
        System.out.println(this+"2");
        Channels.write(ctx,e.getFuture(),buffer);
    }
}
