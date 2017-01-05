package com.sino.netty.client;

import com.sino.netty.codec.TimeDecoder;
import com.sino.netty.handler.TimeClientHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by thierry.fu on 2017/1/4.
 */
public class TimeClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        //NioClientSocketChannelFactory 和 NioServerSocketChannelFactory
        ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        //客户端
        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(() -> Channels.pipeline(new TimeDecoder() ,new TimeClientHandler()));
        //不需要child prefix
        bootstrap.setOption("tcpNoDelay",true);
        bootstrap.setOption("keepAlive",true);
        ChannelFuture f =  bootstrap.connect(new InetSocketAddress(host, port));
        //需要优雅关闭
        //等待f 返回
        f.awaitUninterruptibly();
        if(!f.isSuccess()) {
            f.getCause().printStackTrace();
        }
        //每一个Channel都有自己的closeFuture，触发之后会执行关闭操作
        //如果Channel的Connection出错，也会自动关闭
        f.getChannel().getCloseFuture().awaitUninterruptibly();
        //释放资源，包括线程池和NIO的Selector等
        factory.releaseExternalResources();
    }
}
