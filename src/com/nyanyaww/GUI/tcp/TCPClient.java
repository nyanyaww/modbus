package com.nyanyaww.GUI.tcp;

import com.nyanyaww.GUI.controller.ConfigName;
import javafx.scene.control.TextArea;
import com.nyanyaww.GUI.util.ThreadPoll;
import com.nyanyaww.GUI.util.UiUpdaer;
import com.nyanyaww.GUI.util.Utility;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.Map;

public class TCPClient
{

	public static Socket socket = null;
	private static Map<String, String> config = null;

	private static TextArea textArea = null;

	public static void createSocket(String ip, String port, Map<String, String> config, TextArea textArea)
			throws Exception
	{

		TCPClient.textArea = textArea;
		TCPClient.config = config;
		socket = new Socket(ip, Integer.valueOf(port));
		ThreadPoll.execute(() -> {
			try
			{
				BufferedReader reader = Utility.ins2BufferedReader(socket.getInputStream());
				while (!socket.isClosed())
				{
					StringBuffer buffer = new StringBuffer();
					buffer.append(reader.readLine());
					if (Utility.isEmpty(buffer.toString()))
						continue;
					String log_content = Utility.processStringTcp(buffer, config, socket);
					String filename = config.get(ConfigName.RECEIVE_FILE_NAME);
					if (Utility.isEmpty(filename))
					{
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
				}
			} catch (Exception e)
			{
				// TODO: handle exception
				Utility.alertBox("发生错误 "+e.getMessage());
			}

		});

	}

}
