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
        //NioClientSocketChannelFactory �� NioServerSocketChannelFactory
        ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        //�ͻ���
        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(() -> Channels.pipeline(new TimeDecoder() ,new TimeClientHandler()));
        //����Ҫchild prefix
        bootstrap.setOption("tcpNoDelay",true);
        bootstrap.setOption("keepAlive",true);
        ChannelFuture f =  bootstrap.connect(new InetSocketAddress(host, port));
        //��Ҫ���Źر�
        //�ȴ�f ����
        f.awaitUninterruptibly();
        if(!f.isSuccess()) {
            f.getCause().printStackTrace();
        }
        //ÿһ��Channel�����Լ���closeFuture������֮���ִ�йرղ���
        //���Channel��Connection����Ҳ���Զ��ر�
        f.getChannel().getCloseFuture().awaitUninterruptibly();
        //�ͷ���Դ�������̳߳غ�NIO��Selector��
        factory.releaseExternalResources();
    }
}
