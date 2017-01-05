package com.sino.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by fzsens on 2017/1/1.
 */
public class NioServer {

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private volatile boolean stop = false;

    public void start() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        //监听端口
        serverSocketChannel.bind(new InetSocketAddress(9090));
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        //只关心ACCEPT，即客户端链接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(true) {
            if(stop) {
                break;
            }
            //block util some registered event read.
            System.out.println("server waiting.");
            int readChannels = selector.select();
            if(readChannels <= 0) {
                continue;
            }
            //selectedKeys not keys.
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if(key.isAcceptable()) {
                    //accept event. 当客户端连接进来的时候，注册READ事件
                    ServerSocketChannel sChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = sChannel.accept();
                    channel.configureBlocking(false);
                    channel.write(ByteBuffer.wrap("from server to client".getBytes()));
                    //当新的客户端连接之后，注册READ监听从客户端的可读数据
                    channel.register(selector,SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel)key.channel();
                    buffer.clear();
                    channel.read(buffer);
                    System.out.println("from client : "+ new String(buffer.array()));
                    buffer.flip();
                    channel.write(buffer);
                }
                //forbidden repeat .
                keyIterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer();
        server.start();
    }
}
