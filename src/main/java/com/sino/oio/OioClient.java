package com.sino.oio;

import java.io.*;
import java.net.Socket;

/**
 * Created by thierry.fu on 2017/1/10.
 */
public class OioClient {

    private String no ;

    public OioClient(String no) {
        this.no = no;
    }

    public void connect() throws IOException {
        Socket socket = new Socket("127.0.0.1",80);
        OutputStream out = socket.getOutputStream();
        Writer writer = new OutputStreamWriter(out);
        writer.write("Client-"+ this.no + " Hello World!");
        writer.write("eof");
        writer.flush();

        InputStream in = socket.getInputStream();
        Reader reader = new InputStreamReader(in);
        char[] buffer = new char[64];
        StringBuilder stringBuilder = new StringBuilder();
        int len;
        while ((len = reader.read(buffer)) != -1) {
            String temp = new String(buffer, 0, len);
            int index ;
            if((index = temp.indexOf("eof")) != -1) {
                stringBuilder.append(temp.substring(0,index));
                break;
            }
            stringBuilder.append(temp);
        }
        System.out.println("from server :" + stringBuilder);

        writer.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 10; i++) {
            OioClient client = new OioClient("#"+i);
            client.connect();
        }

    }
}
