package com.sino.netty.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

/**
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeHandler extends SimpleChannelHandler {

    /**
     * Connection创建的时候被调用
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel ch = e.getChannel();
        //分配
        ChannelBuffer time = ChannelBuffers.buffer(4);
        //写入到Buffer
        time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));
        //写入Channel
        ChannelFuture f = ch.write(time);
        f.addListener(new ChannelFutureListener() {
            //操作完成回调
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel ch = future.getChannel();
                //关闭也是返回ChannelFuture
                ch.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
