package com.sino.netty.server;

import com.sino.netty.codec.TimeEncoder;
import com.sino.netty.handler.DiscardServerHandler;
import com.sino.netty.handler.TimeServerHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * ��������
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeServer {

    /**
     * ͳһ���������Ѿ��򿪵�Channel
     */
    public static final ChannelGroup allChannels = new DefaultChannelGroup("time-server");

    public static void main(String[] args) {
        //�����͹���Channel����һ��ΪBoss�������IO���󣬵ڶ���ΪWork����ִ��IO
        ChannelFactory factory =
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool());

        //��������
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        //��server�����µ�����ͨ��ChannelPipelineFactory����Pipeline
        //�����Ҫ��Ӻܶ��Handler��Ӧ�ý����Factory��ȡ����
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline =
                    Channels.pipeline(new TimeEncoder(),new DiscardServerHandler());
            pipeline.addLast("2",new TimeServerHandler());
            return pipeline;
        });

        //child ��ͷ������Channel����������ServerSocketChannel
        bootstrap.setOption("child.tcpNoDelay",true);
        bootstrap.setOption("child.keepAlive",true);
        //����
        Channel ch =  bootstrap.bind(new InetSocketAddress(8080));

        //�ر�Client��Ϊ���ף����ر�Server������Ҫ����
        //1.����˿ڰ�
        //2.�ر������Ѿ��򿪵�����(*)

        allChannels.add(ch);
        //�ȴ�ֹͣ�ź�
        //waitforshutdownsignl()
//        ChannelGroupFuture futures = allChannels.close();
//        futures.awaitUninterruptibly();
//        factory.releaseExternalResources();
    }
}
