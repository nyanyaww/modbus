package udp;

import controller.ConfigName;
import javafx.scene.control.TextArea;
import util.ThreadPoll;
import util.UiUpdaer;
import util.Utility;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;

public class UdpSocket
{
	private static TextArea textArea;

	private static Map<String, String> config;

	public static DatagramSocket socket = null;

	public static void createUdpSocket(String ip, String port, TextArea textArea, Map<String, String> config)
			throws SocketException
	{
		UdpSocket.textArea = textArea;
		UdpSocket.config = config;

		InetSocketAddress address = new InetSocketAddress(ip, Integer.valueOf(port));
		socket = new DatagramSocket(address);

		ThreadPoll.execute(() -> {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try
			{
				while (!socket.isClosed())
				{
					socket.receive(packet);
					String info = new String(data, 0, packet.getLength(), "utf-8");
					StringBuffer buffer = new StringBuffer();
					buffer.append(info);
					String log_content = Utility.processStringUdp(buffer, config, packet);
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
					Thread.sleep(200);
				}
			} catch (Exception e)
			{
				// TODO: handle exception
				Utility.alertBox(e.getMessage());
				e.printStackTrace();
			}

		});

	}

	public static void sendPacket(DatagramSocket socket, String content)
	{
		// 得到目标ip
		try
		{
			String string = config.get(ConfigName.SEND_IP);
			if (Utility.isEmpty(string))
			{
				throw new Exception("发送失败，Ip输入为空");
			}
			int index = string.indexOf(':');
			String ip = string.substring(0, index);
			String port = string.substring(index + 1, string.length());
			if (!Utility.checkIp(ip))
				throw new Exception("ip不合法");
			System.out.println("udp将要发送给" + ip + ":" + port);
			InetSocketAddress des = new InetSocketAddress(ip, Integer.valueOf(port));

			byte[] buffer = content.getBytes("utf-8");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, des);
			socket.send(packet);
			// 发送过后 决定是否显示
			Utility.afterSendMessage(content, string, config, textArea);
		} catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
			Utility.alertBox(e.getMessage());
		}

	}

}
