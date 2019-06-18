package com.nyanyaww.Server;

import java.io.*;
import java.net.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class ServerUI extends JFrame {
    JTextArea mainArea;//主文本框

    JTextField sendArea;//端口文本框

    JTextField indexArea;//消息文本框

    SvrCom server;//线程对象

    public void setServer(SvrCom server) {
        this.server = server;
    }

    public ServerUI() {
        super("服务器");
        Container contain = getContentPane();
        contain.setLayout(new BorderLayout());//布局器
        mainArea = new JTextArea(10, 30);//10行的多行文本
        JScrollPane mainAreaP = new JScrollPane(mainArea);
        JPanel panel = new JPanel();
        JPanel myPanel = new JPanel();
        //端口文本框内容及布局。
        JLabel myLabel2 = new JLabel("端口：", SwingConstants.LEFT);
        JTextField myField1 = new JTextField(30);
        JButton button1 = new JButton("Start");
        //监听器判断是否开始连接。
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                server.sendMsg("PORT:1234");
                mainArea.append("服务开始>>>" + "\n");
                sendArea.setText("");
            }
        });
        //端口文本框添加到布局器中。
        myField1.setText("1234");
        myPanel.add(myLabel2);
        myPanel.add(myField1);
        myPanel.add(button1);
        panel.setLayout(new BorderLayout());
        sendArea = new JTextField(25);//新建发送消息框
        JButton sendBtn = new JButton("发送");
        sendBtn.addActionListener(new ActionListener()// 注册动作监听器
        {
            public void actionPerformed(ActionEvent ae) {
                server.sendMsg(sendArea.getText());// 把信息传递到客户端
                mainArea.append("服务器：" + sendArea.getText() + "\n");// 把信息显示在服务器的聊天记录区域
                sendArea.setText("");
            }
        });
        //主文本框及框架的布局。
        panel.add(new JLabel("Say:"), BorderLayout.WEST);
        panel.add(sendBtn, BorderLayout.EAST);
        panel.add(sendArea, BorderLayout.CENTER);
        contain.add(myPanel, BorderLayout.NORTH);
        contain.add(mainAreaP, BorderLayout.CENTER);
        contain.add(panel, BorderLayout.SOUTH);
        setSize(500, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String[] args) {
        ServerUI ui = new ServerUI();
        SvrCom server = new SvrCom(ui);// 创建并启动网络通讯线程，准备接受客户端数据包
    }
}

class SvrCom extends Thread {
    Socket client;

    ServerSocket soc;

    BufferedReader in;

    PrintWriter out;

    ServerUI ui;

    public SvrCom(ServerUI ui) { // 构造函数
        this.ui = ui;
        ui.setServer(this);
        try {
            soc = new ServerSocket(1234); // 开设服务器端口1234
            System.out.println("启动服务器成功，等待端口号：1234");
            client = soc.accept();// 与客户端开启连接。
            System.out.println("连接成功！来自" + client.toString());
            //读入客户端的消息。
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //将消息发出。
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        start();
    }

    public void run() {// 重新定义run函数
        String msg = "";
        while (true) {
            try {
                msg = in.readLine();// 从in对象上读数据信息
            } catch (SocketException ex) {
                System.out.println(ex);
                break;
            } catch (Exception ex) {
                System.out.println(ex);
            }
            //判断消息不为空则输出消息。
            if (msg != null && msg.trim() != "") {
                System.out.println(">>" + msg);
                ui.mainArea.append(msg + "\n");
            }
        }
    }

    public void sendMsg(String msg) {//在服务器界面显示输出消息
        try {
            out.println("服务器：" + msg);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
