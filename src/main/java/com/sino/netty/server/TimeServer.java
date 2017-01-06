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
 * 废弃服务
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeServer {

    /**
     * 统一管理所有已经打开的Channel
     */
    public static final ChannelGroup allChannels = new DefaultChannelGroup("time-server");

    public static void main(String[] args) {
        //创建和管理Channel，第一个为Boss负责关联IO请求，第二个为Work负责执行IO
        ChannelFactory factory =
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool());

        //启动辅助
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        //当server接受新的连接通过ChannelPipelineFactory创建Pipeline
        //如果需要添加很多的Handler，应该将这个Factory提取出来
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline =
                    Channels.pipeline(new TimeEncoder(),new DiscardServerHandler());
            pipeline.addLast("2",new TimeServerHandler());
            return pipeline;
        });

        //child 开头的配置Channel，否则配置ServerSocketChannel
        bootstrap.setOption("child.tcpNoDelay",true);
        bootstrap.setOption("child.keepAlive",true);
        //启动
        Channel ch =  bootstrap.bind(new InetSocketAddress(8080));

        //关闭Client较为容易，而关闭Server，则需要考虑
        //1.解除端口绑定
        //2.关闭所有已经打开的链接(*)

        allChannels.add(ch);
        //等待停止信号
        //waitforshutdownsignl()
//        ChannelGroupFuture futures = allChannels.close();
//        futures.awaitUninterruptibly();
//        factory.releaseExternalResources();
    }
}
