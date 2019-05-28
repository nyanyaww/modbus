package com.lcs.Server;

import java.net.*;
import java.io.*;

public class Server<TODO> extends Thread {
    private ServerSocket serverSocket;
    private int port;

    public Server() throws IOException {
        this.port = 501;
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("谢谢连接我：" + server.getLocalSocketAddress() + "\nGoodbye!");
                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // TODO:明天修改client类，使其能够循环监听

    public static void main(String[] args) {
        try {
            Thread t = new Server();
            t.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}