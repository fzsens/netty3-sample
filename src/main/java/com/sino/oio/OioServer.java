package com.sino.oio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by thierry.fu on 2017/1/10.
 */
public class OioServer {

    private ServerSocket serverSocket;


    public void init() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(80));
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream in = socket.getInputStream();
            Reader reader = new InputStreamReader(in);
            char[] buffer = new char[64];
            StringBuilder stringBuilder = new StringBuilder();
            int len;
            while ((len = reader.read(buffer)) != -1) {
                String temp = new String(buffer, 0, len);
                int index;
                //reader.read方法会一直堵塞直到客户端的Socket断开，使用这种方式隐藏字符长度的bug
                if((index = temp.indexOf("eof")) != -1) {
                    stringBuilder.append(temp.substring(0,index));
                    break;
                }
                stringBuilder.append(temp);
            }
            System.out.println("from client :" + stringBuilder);

            OutputStream out = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(out);
            writer.write("server accepted!");
            writer.write("eof");
            writer.flush();

            writer.close();
            out.close();

            reader.close();
            in.close();
            socket.close();
        }
//        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        OioServer server = new OioServer();
        server.init();
    }
}
