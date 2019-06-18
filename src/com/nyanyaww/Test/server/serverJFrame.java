package com.nyanyaww.Test.server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.nyanyaww.Test.utils.IOUtil;
import com.nyanyaww.Test.utils.NetUtil;

public class serverJFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 接收到的客户
     */
    private Socket socket;

    /**
     * 服务器
     */
    private ServerSocket server;

    /**
     * 用户列表
     */
    private List<ForwardThread> list = new ArrayList<ForwardThread>();

    /**
     * 在线用户列表文本域
     */
    private JTextArea userlistarea;

    /**
     * 服务器结束按钮
     */
    private JButton stopButton;

    /**
     * 服务器开启按钮
     */
    private JButton openButton;

    /**
     * 端口号文本框
     */
    private JTextField serverPort;

    /**
     * 服务器IP文本框
     */
    private JTextField serverIP;

    /**
     * 循环接收用户上线的线程
     */
    private AcceptClientThread rcvt;

    /**
     * 线程停止标识符
     */
    private boolean BL = false;

    /**
     * serverJFrame有参构造函数；
     *
     * @param
     */
    public serverJFrame(String title) {
        super(title);
        init();
    }

    /**
     * serverJFrame无参构造函数，默认标题server；
     */
    public serverJFrame() {
        this("server");
    }

    /**
     * @author Administrator 接收用户连接的线程
     */
    private class AcceptClientThread extends Thread {

        public void run() {
            try {
                while (true) {
                    if (BL) {
                        break;
                    }
                    // 接收客户端的连接
                    socket = server.accept();
                    // 为接受的用户创建转发线程
                    ForwardThread ft = new ForwardThread(socket);
                    // 存储接收到的用户的转发线程
                    list.add(ft);
                    // 开启线程
                    ft.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @author Administrator 转发线程类
     */
    private class ForwardThread extends Thread {
        private Socket socket;
        private DataOutputStream out;
        private DataInputStream in;
        private String nickname;

        /**
         * ForwradThread有参构造函数
         *
         * @param socket 传进接收到的用户socket
         * @throws IOException
         */
        public ForwardThread(Socket socket) throws IOException {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }

        public void run() {
            try {
                while (true) {
                    if (BL) {
                        break;
                    }

                    if (socket.isClosed()) {
                        break;
                    }

                    // 接收到信息
                    String msg = in.readUTF();

                    // 判断信息类型
                    if (msg.startsWith("ON@")) {// 用户上线
                        // 判断是新上线的用户，然后给该用户输出所有list里边的在线用户,实现所有用户列表同步
                        for (ForwardThread ft : list) {
                            if (ft != this) {
                                String otherusermsg = "ON@" + ft.nickname + "["
                                        + ft.socket.getRemoteSocketAddress().toString() + "]";
                                out.writeUTF(otherusermsg);
                            }
                        }

                        nickname = msg.substring(3);
                        // 这个带IP的信息是不是可以发给其他所有用户
                        String usermsg = nickname + "[" + socket.getRemoteSocketAddress().toString() + "]";
                        userlistarea.append(usermsg + "\n");
                        for (ForwardThread ft : list) {
                            // 这里不判断是不是自己，连自己都发，然后用户创建自己的时候就不用再往userlistarea添加自己了
                            ft.out.writeUTF("ON@" + usermsg);
                        }

                    } else if (msg.startsWith("OFF@")) {// 用户下线
                        // 给在线的其他用户发送该用户的下线消息
                        for (ForwardThread ft : list) {
                            if (ft != this) {
                                ft.out.writeUTF(msg);
                            }
                        }
                        // 从list中移除该用户
                        list.remove(this);
                        // 从服务器userlistarea中移除该用户，就是刷新一遍
                        userlistarea.setText("");
                        for (ForwardThread ft : list) {
                            String flushusermsg = ft.nickname + "[" + ft.socket.getRemoteSocketAddress().toString()
                                    + "]";
                            userlistarea.append(flushusermsg + "\n");
                        }
                        // 为其他所有在线客户再发一遍当前在线客户，来刷新其他在线用户的userlistarea
                        for (ForwardThread ft : list) {
                            for (ForwardThread ft2 : list) {
                                String flushusermsg = "ON@" + ft2.nickname + "["
                                        + ft2.socket.getRemoteSocketAddress().toString() + "]";
                                ft.out.writeUTF(flushusermsg);
                            }
                        }
                        // 结束当前要下线的用户的Socket,in,out
                        IOUtil.close(in);
                        IOUtil.close(out);
                        NetUtil.close(socket);

                        // 当前要下线的用户，服务器为他创建的转发线程结束；
                        break;

                    } else if (msg.startsWith("MSG@")) {// 普通聊天信息
                        // 转发出去，不用给自己发
                        for (ForwardThread ft : list) {
                            if (ft != this) {
                                ft.out.writeUTF(msg);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把窗口放在屏幕中心
     *
     * @param
     */
    public void setcenter(boolean bl) {
        if (bl == true) {
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screen = kit.getScreenSize();
            this.setLocation((screen.width - this.getWidth()) / 2, (screen.height - this.getHeight()) / 2);
        }
    }

    /**
     * 创建服务器IP文本框
     *
     * @return JTextField
     */
    public JTextField getserverIP() {
        serverIP = new JTextField(12);
        serverIP.setText("127.0.0.1");
        return serverIP;
    }

    /**
     * 创建端口号文本框
     *
     * @return JTextField
     */
    public JTextField getserverPort() {
        serverPort = new JTextField(4);
        serverPort.setText("8000");
        return serverPort;
    }

    /**
     * 创建服务器开启按钮，并设置监听器，获取端口号文本框中的端口号开启服务器
     *
     * @return JButton
     */
    public JButton getOpenButton() {
        openButton = new JButton("开启");
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(serverPort.getText());

                try {
                    // 创建服务器
                    server = new ServerSocket(port);
                    userlistarea.setText("服务器状态：开启\n");
                    // 创建循环接收用户连接的线程
                    BL = false;
                    rcvt = new AcceptClientThread();
                    rcvt.start();
                    // 设置按钮
                    openButton.setEnabled(false);
                    stopButton.setEnabled(true);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        return openButton;
    }

    /**
     * 创建服务器结束按钮，并设置监听器，结束服务器
     *
     * @return
     */
    public JButton getStopButton() {
        stopButton = new JButton("停止");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 向所有在线客户发送STOP信息
                    for (ForwardThread ft : list) {
                        ft.out.writeUTF("STOP");
                    }
                    // 关闭所有对应的在线客户端Socket,输入输出流
                    for (ForwardThread ft : list) {
                        IOUtil.close(ft.in);
                        IOUtil.close(ft.out);
                        NetUtil.close(ft.socket);
                    }
                    // 关闭循环接收用户上线线程
                    BL = true;
                    // 关闭服务器
                    NetUtil.close(server);

                    list.clear();

                    // 设置按钮
                    stopButton.setEnabled(false);
                    openButton.setEnabled(true);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                userlistarea.setText("");
                userlistarea.append("服务器关闭\n");
            }
        });
        return stopButton;
    }

    /**
     * 创建北边JPanel组件，里边放服务器IP标签和文本框，端口号标签和文本框，连接按钮，结束按钮
     *
     * @return
     */
    public JPanel NorthPanel() {
        // 定义北边JPanel
        JPanel north = new JPanel();
        north.setBorder(new TitledBorder("服务器设置"));
        // 往north里边放组件
        // 放端口号标签
        north.add(new JLabel("端口号："));
        // 放端口号文本框
        north.add(getserverPort());
        // 放开启、结束按钮
        north.add(getOpenButton());
        north.add(getStopButton());

        return north;
    }

    public JScrollPane getUserListArea() {
        userlistarea = new JTextArea();
        JScrollPane userlistscroll = new JScrollPane(userlistarea);
        return userlistscroll;
    }

    /**
     * 创建中间JPanel容器,里边有在线用户列表文本域
     *
     * @return JPanel
     */
    public JPanel CenterPanel() {
        JPanel center = new JPanel();
        center.setBorder(new TitledBorder("在线用户信息"));
        BorderLayout layout = new BorderLayout();
        center.setLayout(layout);
        // 放入在线用户列表文本域
        center.add(getUserListArea());

        return center;
    }

    /**
     * 初始化服务器窗口
     */
    public void init() {

        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setcenter(true);
        // 获取内容面板
        Container conPane = this.getContentPane();
        // 往内容面板里放组件
        // 放入北边组件
        conPane.add(NorthPanel(), BorderLayout.NORTH);
        // 放入中间组件
        conPane.add(CenterPanel(), BorderLayout.CENTER);

        this.setVisible(true);

    }

    public static void main(String[] args) {
        new serverJFrame();
    }

}

