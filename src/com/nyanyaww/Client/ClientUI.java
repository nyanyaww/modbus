package com.nyanyaww.Client;


import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class ClientUI extends JFrame {
    JTextArea mainArea;

    JTextField sendArea;

    ChatClient client;

    JTextField ipArea;
    JTextField portArea;

    JButton btnLink;
    //构造函数
    public void setClient(ChatClient client){
        this.client = client;
    }

    public ClientUI() {
        super("客户端");
        Container contain = getContentPane();
        contain.setLayout(new BorderLayout());
        mainArea = new JTextArea(10,30);//多行文本框
        JScrollPane mainAreaP = new JScrollPane(mainArea);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        sendArea = new JTextField(25);//发送文本框
        JButton sendBtn = new JButton("发送");
        //监听连接消息。
        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                client.sendMsg(sendArea.getText());
                mainArea.append("客户端：" + sendArea.getText() + "\n");
                sendArea.setText("");
            }
        });
        JPanel ipPanel = new JPanel();
        ipPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        //连接消息文本框及布局。
        ipPanel.add(new JLabel("Server IP:"));
        ipArea = new JTextField(15);
        ipArea.setText("127.0.0.1");
        ipPanel.add(ipArea);
        ipPanel.add(new JLabel("端口："));
        portArea =new JTextField(8);
        ipPanel.add(portArea);
        btnLink = new JButton("连接!");
        ipPanel.add(btnLink);
        //连接按钮的监听器。
        btnLink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String port=portArea.getText();
                //判断端口是否正确。
                if( port.equals( "1234")){
                    mainArea.append("连接成功" + "\n");
                    client = new ChatClient(ipArea.getText(), 1234, ClientUI.this);
                    ClientUI.this.setClient(client);
                }
                else {
                    mainArea.append("端口错误" + "\n");
                }
            }
        });
        //发送文本框及布局。
        panel.add(new JLabel("Say:"),BorderLayout.WEST);
        panel.add(sendBtn, BorderLayout.EAST);
        panel.add(sendArea, BorderLayout.CENTER);
        //主文本框布局。
        contain.add(ipPanel, BorderLayout.NORTH);
        contain.add(mainAreaP, BorderLayout.CENTER);
        contain.add(panel, BorderLayout.SOUTH);
        setSize(500, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String[] args) {
        ClientUI ui = new ClientUI();
    }
}


class ChatClient extends Thread {
    Socket sc;

    BufferedReader in;

    PrintWriter out;

    ClientUI ui;
    //构造函数
    public ChatClient(String ip, int port, ClientUI ui) {
        this.ui = ui;
        try {
            sc = new Socket(ip, port);
            //发送信息。
            out = new PrintWriter(sc.getOutputStream(), true);
            //读取信息。
            in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
        } catch (Exception e) {
            System.out.println(e);
        }
        start();
    }
    //重新定义run函数。
    public void run() {
        String msg = "";
        while (true) {
            try {
                msg = in.readLine();
            } catch (SocketException ex) {
                System.out.println(ex);
                break;
            } catch (Exception ex) {
                System.out.println(ex);
            }
            //判断服务器发送信息是否为空。
            if (msg != null && msg.trim() != "") {
                System.out.println(">>" + msg);
                ui.mainArea.append(msg + "\n");
            }
        }
    }
    public void sendMsg(String msg) {
        try {
            out.println("客户端：" + msg);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}