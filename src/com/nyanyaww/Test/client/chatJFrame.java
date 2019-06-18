package com.nyanyaww.Test.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.nyanyaww.Test.utils.IOUtil;
import com.nyanyaww.Test.utils.NetUtil;

public class chatJFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/** 接收消息的线程 */
	private ReceiveMsgThread rcvmt;

	/** 当前用户输入流 */
	private DataInputStream in;

	/** 当前用户输出流 */
	private DataOutputStream out;

	/** 用户Socket */
	private Socket client;

	/** 聊天记录文本域 */
	private JTextArea recordlistarea;

	/** 在线用户列表文本域 */
	private JTextArea userlistarea;

	/** 结束按钮 */
	private JButton stopButton;

	/** 连接按钮 */
	private JButton connectButton;

	/** 昵称文本框 */
	private JTextField nicknameField;

	/** 昵称 */
	private String nickName;

	/** 端口号文本框 */
	private JTextField serverPort;

	/** 服务器IP文本框 */
	private JTextField serverIP;

	/** 发送聊天信息的文本框 */
	private JTextField sendField;

	/** 发送按钮 */
	private JButton sendButton;

	/** 线程关闭标识符 */
	private boolean BL = false;

	private class ReceiveMsgThread extends Thread {

		public void run() {
			while (true) {
				if (BL) {
					break;
				}

				if (client.isClosed()) {
					break;
				}

				try {
					// 接收到的信息
					String msg = in.readUTF();
					// 判断消息：共有四种消息
					if (msg.startsWith("ON@")) {// 用户上线
						String usermsg = msg.substring(3);
						userlistarea.append(usermsg + "\n");
					} else if (msg.startsWith("OFF@")) {// 用户下线
						userlistarea.setText("");
					} else if (msg.startsWith("MSG@")) {// 普通聊天信息
						String recordmsg = msg.substring(4);
						recordlistarea.append(recordmsg + "\n");
					} else if (msg.equals("STOP")) {// 服务器关闭信息
						// 服务器要关闭了，关闭 当前socket对应的输入流 输出流，socket 以及 接收消息的线程。
						// 关闭输入输出流
						recordlistarea.append("服务器已关闭\n");
						IOUtil.close(in);
						IOUtil.close(out);
						// 关闭socket
						NetUtil.close(client);
						client = null;
						// 清空在线用户列表
						userlistarea.setText("");
						// 设置按钮
						stopButton.setEnabled(false);
						connectButton.setEnabled(true);
						break;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			} // while

		}

	}

	/**
	 * chatJFrame有参构造函数
	 *
	 * @param title
	 *            窗口名
	 */
	public chatJFrame(String title) {
		super(title);
		init();
	}

	/**
	 * chatJFrame无参构造函数
	 */
	public chatJFrame() {
		this("聊天");
	}

	/**
	 * 把窗口放在屏幕中心
	 *
	 * @param bl是否把窗口放在屏幕中心
	 */
	public void setcenter(boolean bl) {
		if (bl == true) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screen = kit.getScreenSize();
			this.setLocation((screen.width - this.getWidth()) / 2, (screen.height - this.getHeight()) / 2);
		}
	}

	/**
	 * 创建发送聊天信息的文本框
	 *
	 * @return JTextField
	 */
	public JTextField getSendField() {
		sendField = new JTextField(20);
		return sendField;
	}

	/**
	 * 创建发送按钮，并设置监听器，获得发送信息后，清空信息文本框
	 *
	 * @return JButton
	 */
	public JButton getSendButton() {
		sendButton = new JButton("发送");
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断当前用户是否连接到服务器
				if (client == null) {
					JOptionPane.showMessageDialog(null, "服务器未连接，请连接服务器");
				}
				// 读取消息文本框中的信息
				String msg = sendField.getText();
				try {
					// 判断文本框消息是不是为空，不为空，则包装消息并发送给服务器，最后清空消息文本框
					if (!msg.isEmpty()) {
						String sendmsg = "MSG@" + nickName + ":" + msg;
						// 添加到自己的recordlistarea,因为服务器转发普通聊天信息只给其他用户，不包括自己
						recordlistarea.append(nickName + ":" + msg + "\n");
						out.writeUTF(sendmsg);
						sendField.setText("");
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		return sendButton;
	}

	/**
	 * 南边JPanel容器,放发送聊天信息文本框，和发送按钮
	 *
	 * @return JPanel
	 */
	public JPanel SouthPanel() {
		// 定义南边JPanel
		JPanel south = new JPanel();
		south.setBorder(new TitledBorder("消息发送"));
		// 设置边框布局
		BorderLayout layout = new BorderLayout();
		south.setLayout(layout);
		// 往south里边放聊天文本框
		south.add(getSendField());
		// 往south里边放发送按钮,设置边框布局后，让按钮靠东边
		south.add(getSendButton(), BorderLayout.EAST);
		return south;
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
	 * 创建昵称文本框
	 *
	 * @return JTextField
	 */
	public JTextField getNickName() {
		nicknameField = new JTextField(8);
		nicknameField.setText("喵喵");
		nickName = nicknameField.getText();
		return nicknameField;
	}

	/**
	 * 创建连接按钮，并设置监听器，利用服务器Ip和端口号连接服务器
	 *
	 * @return JButton
	 */
	public JButton getConnectButton() {
		connectButton = new JButton("连接");

		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取端口号
				int port = Integer.parseInt(serverPort.getText());
				String host = serverIP.getText();
				nickName = nicknameField.getText();
				try {
					// 创建用户Socket
					client = new Socket(host, port);
					recordlistarea.append("已连接服务器\n");
					// 初始化当前客户端的输入输出流
					in = new DataInputStream(client.getInputStream());
					out = new DataOutputStream(client.getOutputStream());
					// 向服务器发送上线信息
					out.writeUTF("ON@" + nickName);
					// 添加当前用户的信息到userlistarea
					// userlistarea.append(nickName+"["+client.getLocalSocketAddress()+"]");
					// 启动接收消息的线程
					BL = false;
					rcvmt = new ReceiveMsgThread();
					rcvmt.start();
					// 设置按钮
					connectButton.setEnabled(false);
					stopButton.setEnabled(true);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		return connectButton;
	}

	/**
	 * 创建结束按钮，并设置监听器，用来断开与服务器的连接
	 *
	 * @return JButton
	 */
	public JButton getStopButton() {
		stopButton = new JButton("结束");
		stopButton.setEnabled(false);

		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 包装下线信息，格式“OFF@昵称”
				String offmsg = "OFF@" + nickName;
				recordlistarea.append("聊天结束\n");

				try {
					// 向服务器发送下线信息
					out.writeUTF(offmsg);
					// 关闭接收消息的线程
					BL = true;
					// 关闭输入输出流
					IOUtil.close(in);
					IOUtil.close(out);
					// 关闭socket
					NetUtil.close(client);
					client = null;
					// 清空在线用户列表
					userlistarea.setText("");
					// 设置按钮
					stopButton.setEnabled(false);
					connectButton.setEnabled(true);

					// chatJFrame.this.dispose();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

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
		north.setBorder(new TitledBorder("连接设置"));
		// 往north里边放组件
		// 放标签服务器IP
		north.add(new JLabel("服务器IP："));
		// 放服务器IP文本框
		north.add(getserverIP());
		// 放端口号标签
		north.add(new JLabel("端口号："));
		// 放端口号文本框
		north.add(getserverPort());
		// 放昵称标签和昵称文本框
		north.add(new JLabel("昵称"));
		north.add(getNickName());
		// 放连接、结束按钮
		north.add(getConnectButton());
		north.add(getStopButton());
		return north;
	}

	/**
	 * 创建在线用户列表文本域
	 *
	 * @return JTextArea
	 */
	public JScrollPane getUserListArea() {
		userlistarea = new JTextArea();
		JScrollPane userlistscroll = new JScrollPane(userlistarea);
		userlistscroll.setBorder(new TitledBorder("在线用户"));
		return userlistscroll;
	}

	/**
	 * 创建聊天记录文本域
	 *
	 * @return JTextArea
	 */
	public JScrollPane getRecordListArea() {
		recordlistarea = new JTextArea();
		JScrollPane recordlistscroll = new JScrollPane(recordlistarea);
		recordlistscroll.setBorder(new TitledBorder("聊天信息"));
		return recordlistscroll;
	}

	/**
	 * 创建中间JSplitPane分隔容器组件，放在线用户列表和聊天记录
	 *
	 * @return JSplitPane
	 */
	public JSplitPane CenterPane() {
		// 创建一个分隔面板 指定将该面板从水平方向 分隔为两个部分
		JSplitPane center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		center.setDividerLocation(160);
		center.setLeftComponent(getUserListArea());
		center.setRightComponent(getRecordListArea());
		return center;
	}

	/**
	 * 初始化聊天窗口
	 */
	public void init() {

		this.setSize(700, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setcenter(true);
		// 获得内容面板
		Container conPane = this.getContentPane();
		// 往内容面板里放组件
		// 放南边的组件
		conPane.add(SouthPanel(), BorderLayout.SOUTH);
		// 放北边的组件
		conPane.add(NorthPanel(), BorderLayout.NORTH);
		// 放中间的组件
		conPane.add(CenterPane(), BorderLayout.CENTER);

		this.setVisible(true);
	}

	public static void main(String[] args) {
		new chatJFrame();
	}

}
