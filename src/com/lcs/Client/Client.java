package com.lcs.Client;

import java.net.*;
import java.io.*;

public class Client {
    private String ip;
    private int port;

    private Client() {
        this.ip = "localhost";
        this.port = 501;
    }

    void init() {

    }

    private void run() {
        try {
            System.out.println("连接到主机：" + ip + " ，端口号：" + port);
            Socket client = new Socket(ip, port);
            System.out.println("远程主机地址：" + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应： " + in.readUTF());


            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}