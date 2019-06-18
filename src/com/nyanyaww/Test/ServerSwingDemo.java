package com.nyanyaww.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;

public class ServerSwingDemo {
	private PrintWriter pwtoclien = null;

	private ServerSocket serverSocket = null;

	private Socket socket = null;

	private JFrame frame = new JFrame("Server");

	private JTextArea textAreaLeft = new JTextArea(5, 10);

	private JTextArea textAreaRight = new JTextArea(5, 10);

	private JScrollPane scrollPane = new JScrollPane(textAreaLeft);

	private JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, scrollPane, textAreaRight);

	private JPanel panel = new JPanel();

	private JButton button = new JButton("发送");

	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topSplitPane, panel);


	private void swing() {

		textAreaLeft.setEnabled(false);
		textAreaRight.addKeyListener(new MyListener());

		topSplitPane.setDividerLocation(160);
		topSplitPane.setDividerSize(2);

		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.setSize(600, 20);
		panel.add(button);

		splitPane.setDividerLocation(230);
		splitPane.setDividerSize(2);

		frame.setSize(500,300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(splitPane);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void sendMsg() {
		button.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textAreaRight.getText() != null && !textAreaRight.getText().isEmpty()) {
					textAreaLeft.setLayout(new FlowLayout(FlowLayout.RIGHT));

					String msg = "\nServer : \n    " + textAreaRight.getText();
					textAreaLeft.append(msg);
					pwtoclien.println(msg);
					pwtoclien.flush();
					scrollBottom();
				}
			}
		});
	}

	private void getMsg() {
		try {
			Scanner inScanner = new Scanner(socket.getInputStream());
			while (inScanner.hasNextLine()) {
				String indata = inScanner.nextLine();
				if (!indata.isEmpty()) {
					textAreaLeft.append("\n" + indata);
					scrollBottom();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void scrollBottom() {
		textAreaLeft.setSelectionStart(textAreaLeft.getText().length());
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	class MyListener implements KeyListener {
		@Override // 按下
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) {
				button.doClick();
			}
		}
		@Override // 松开
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == 10) {
				textAreaRight.setText("");
			}
		}

		@Override // 输入的内容
		public void keyTyped(KeyEvent e) {}
	}

	private void server() {
		swing();
		textAreaLeft.setText("Waiting for the connection");

		try {
			serverSocket = new ServerSocket(6666);
			//允许连接
			socket = serverSocket.accept();

			textAreaLeft.setText(socket.getInetAddress() + " connection is successful");

			//字符输出流 到客户端
			pwtoclien = new PrintWriter(socket.getOutputStream());
			pwtoclien.println("\n The connection to the remote server has been successful！");
			pwtoclien.flush();

			sendMsg();
			getMsg();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pwtoclien.close();
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ServerSwingDemo serverSwingDemo = new ServerSwingDemo();
		serverSwingDemo.server();
	}
}
