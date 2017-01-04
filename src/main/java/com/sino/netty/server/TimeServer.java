package com.sino.netty.server;

import com.sino.netty.handler.EchoHandler;
import com.sino.netty.handler.TimeHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * ��������
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeServer {

    public static void main(String[] args) {
        //�����͹���Channel����һ��ΪBoss�������IO���󣬵ڶ���ΪWork����ִ��IO
        ChannelFactory factory =
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool());

        //��������
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        //��server�����µ�����ͨ��ChannelPipelineFactory����Pipeline
        //�����Ҫ��Ӻܶ��Handler��Ӧ�ý����Factory��ȡ����
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
//                ChannelPipeline pipeline = Channels.pipeline(new DiscardHandler());
                ChannelPipeline pipeline = Channels.pipeline(new TimeHandler());
                return pipeline;
            }
        });

        //child ��ͷ������Channel����������ServerSocketChannel
        bootstrap.setOption("child.tcpNoDelay",true);
        bootstrap.setOption("child.keepAlive",true);
        //����
        bootstrap.bind(new InetSocketAddress(8080));
    }
}
