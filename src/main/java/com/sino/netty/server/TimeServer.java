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
 * 废弃服务
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeServer {

    public static void main(String[] args) {
        //创建和管理Channel，第一个为Boss负责关联IO请求，第二个为Work负责执行IO
        ChannelFactory factory =
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool());

        //启动辅助
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        //当server接受新的连接通过ChannelPipelineFactory创建Pipeline
        //如果需要添加很多的Handler，应该将这个Factory提取出来
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
//                ChannelPipeline pipeline = Channels.pipeline(new DiscardHandler());
                ChannelPipeline pipeline = Channels.pipeline(new TimeHandler());
                return pipeline;
            }
        });

        //child 开头的配置Channel，否则配置ServerSocketChannel
        bootstrap.setOption("child.tcpNoDelay",true);
        bootstrap.setOption("child.keepAlive",true);
        //启动
        bootstrap.bind(new InetSocketAddress(8080));
    }
}
