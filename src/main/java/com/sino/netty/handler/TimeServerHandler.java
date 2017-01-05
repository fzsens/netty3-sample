package com.sino.netty.handler;

import com.sino.netty.pojo.UnixTime;
import com.sino.netty.server.TimeServer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeServerHandler extends SimpleChannelHandler {

    /**
     * Connection������ʱ�򱻵���
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel ch = e.getChannel();
        //����
//        ChannelBuffer time = ChannelBuffers.buffer(4);
        //д�뵽Buffer
//        time.writeInt((int)(System.currentTimeMillis() / 1000L));
        //д��Channel
        System.out.println("1");
        ChannelFuture f = ch.write(new UnixTime((int)System.currentTimeMillis()/1000));
        f.addListener(future -> {
            Channel ch1 = future.getChannel();
            //�ر�Ҳ�Ƿ���ChannelFuture
            ch1.close();
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        TimeServer.allChannels.add(e.getChannel());
    }
}
