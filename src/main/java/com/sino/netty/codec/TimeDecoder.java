package com.sino.netty.codec;

import com.sino.netty.pojo.UnixTime;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * ��decode��������
 * Created by thierry.fu on 2017/1/5.
 */
public class TimeDecoder extends FrameDecoder{
    /**
     *
     * @param ctx      the context of this handler
     * @param channel  the current channel
     * @param buffer   the cumulative buffer of received packets so far.
     *                 Note that the buffer might be empty, which means you
     *                 should not make an assumption that the buffer contains
     *                 at least one byte in your decoder implementation.
     *
     * @return
     * @throws Exception
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {

        System.out.println("decode. start. ");
        if(buffer.readableBytes() < 4) {
            System.out.println("return null");
            //���ݲ��㣬�����յ��������ݵ�ʱ�򣬻��ٴε���
            return null;
        }
        System.out.println("readBytes(4)");
        //�ɹ����룬����ɹ���FrameDecoder���������decode������ֱ������null
        return new UnixTime(buffer.readInt());
    }
}
