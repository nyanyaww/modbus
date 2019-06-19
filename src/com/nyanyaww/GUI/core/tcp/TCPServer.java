package com.nyanyaww.GUI.core.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.nyanyaww.GUI.controller.ConfigName;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import com.nyanyaww.GUI.util.ThreadPoll;
import com.nyanyaww.GUI.util.UiUpdaer;
import com.nyanyaww.GUI.util.Utility;

public class TCPServer
{
	public static ServerSocket serverSocket = null;
	// 需要更新的UI控件 即 日志显示区
	private static TextArea textArea;
	private static Map<String, String> config = null;
	private static Label label;
	// 所有被接受的socket
	public static Set<Socket> acceptedSockets = new HashSet<Socket>();
	public static void createTcpServer(String ip, String port, Map<String, String> config, TextArea textArea,Label label)
			throws Exception
	{
		TCPServer.textArea = textArea;
		TCPServer.config = config;
		TCPServer.label = label;
		createServerSocket(ip, port);
		initServerSocket();
		System.out.println("服务器准备就绪～");
		System.out.println("服务器信息：" + serverSocket.getInetAddress() + " P:" + serverSocket.getLocalPort());
		// 新线程开启监听。
		startListen();

	}
	public static void startListen()
	{
		ThreadPoll.execute(() -> {
			while (!serverSocket.isClosed())
			{
				try
				{
					Socket client = serverSocket.accept();
					acceptedSockets.add(client);
					// 更新连接数
					updateLable(acceptedSockets.size());
					// 客户端构建异步线程
					ClientHandler clientHandler = new ClientHandler(client, textArea, config);
					// 启动线程
					ThreadPoll.execute(clientHandler);
				} catch (Exception e)
				{
					Utility.alertBox(e.getMessage());
				}
			}
		});

	}
	
	private static void updateLable(int connect_nums) {
		String content=" 已连接"+connect_nums+"个";
		UiUpdaer uiUpdaer = new UiUpdaer(label);
		uiUpdaer.resetAndUpdate(content);
	}
	private static void createServerSocket(String ip, String port) throws IOException
	{

		InetSocketAddress address = new InetSocketAddress(ip, Integer.valueOf(port));
		serverSocket = new ServerSocket();
		serverSocket.bind(address);
	}

	private static void initServerSocket() throws IOException
	{
		// 是否复用未完全关闭的地址端口
		serverSocket.setReuseAddress(true);
		// 等效Socket#setReceiveBufferSize
		serverSocket.setReceiveBufferSize(64 * 1024 * 1024);

		// 设置serverSocket#accept超时时间
		// serverSocket.setSoTimeout(20000);

		// 设置性能参数：短链接，延迟，带宽的相对重要性
		serverSocket.setPerformancePreferences(1, 1, 1);
	}

	/**
	 * 客户端消息处理
	 */
	private static class ClientHandler implements Runnable
	{
		private Socket socket;
		private TextArea textArea;
		private Map<String, String> config;

		public ClientHandler(Socket socket, TextArea textArea, Map<String, String> config)
		{
			super();
			this.socket = socket;
			this.textArea = textArea;
			this.config = config;
		}

		@Override
		public void run()
		{
			System.out.println("新客户端连接：" + socket.getInetAddress() + " P:" + socket.getPort());
			try
			{
				BufferedReader reader = Utility.ins2BufferedReader(socket.getInputStream());
				// OutputStream os = socket.getOutputStream();
				while (true)
				{

					StringBuffer buffer = new StringBuffer();
					System.out.println("reading now");
					String line = reader.readLine();
					// socket连接时 会阻塞 直到读出一行值 客户端断开时，此socket不会阻塞，读取值为 null
					if (line == null)
					{
						throw new Exception("客户端连接断开");
					}
					buffer.append(line);
					if (Utility.isEmpty(buffer.toString()))
						continue;
					String log_content = Utility.processStringTcp(buffer, config, socket);
					String filename = config.get(ConfigName.RECEIVE_FILE_NAME);
					if (Utility.isEmpty(filename))
					{
						// 存到界面
						boolean puse = Utility.string2Bollean(config.get(ConfigName.PAUSE_RECEIVE));
						if (!puse)
						{
							System.out.println("update ui");
							UiUpdaer uiUpdaer = new UiUpdaer(textArea);
							uiUpdaer.update(log_content);
						}
					} else
					{
						// 存到文件
						Utility.saveToFile(filename, log_content);
					}

					// 调用一个方法 让socket断开连接时正常退出线程
					socket.getInputStream();
					// 减少CPU资源占用
					Thread.sleep(100);
				}
			} catch (Exception e)
			{
				// TODO: handle exception
				System.out.println("客户端已退出：" + socket.getInetAddress() + " P:" + socket.getPort());
				// 连接中断
				try
				{
					this.socket.close();
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				acceptedSockets.remove(this.socket);
				updateLable(acceptedSockets.size());
			}

		}
	}
}
