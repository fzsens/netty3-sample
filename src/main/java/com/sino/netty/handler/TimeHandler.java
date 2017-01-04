package com.sino.netty.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

/**
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeHandler extends SimpleChannelHandler {

    /**
     * Connection������ʱ�򱻵���
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel ch = e.getChannel();
        //����
        ChannelBuffer time = ChannelBuffers.buffer(4);
        //д�뵽Buffer
        time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));
        //д��Channel
        ChannelFuture f = ch.write(time);
        f.addListener(new ChannelFutureListener() {
            //������ɻص�
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel ch = future.getChannel();
                //�ر�Ҳ�Ƿ���ChannelFuture
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
