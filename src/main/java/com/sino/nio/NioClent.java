package com.sino.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by fzsens on 2017/1/2.
 */
public class NioClent {
    private SocketChannel channel;
    private Selector selector;

    public void start() throws IOException {
        channel = SocketChannel.open();
        selector = Selector.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1",9090));
        channel.register(selector, SelectionKey.OP_CONNECT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(true) {
            int readyChannels = selector.select();
            if(readyChannels <= 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.keys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            if(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if(key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    //如果正在连接，等待连接完成
                    if(channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);
                    channel.write(ByteBuffer.wrap("echo from client".getBytes()));
                    //注册新的监听
                    channel.register(selector,SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel)key.channel();
                    buffer.clear();
                    channel.read(buffer);
                    System.out.println(new String(buffer.array()));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NioClent client = new NioClent();
        client.start();
    }
}
