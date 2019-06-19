package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.ConfigName;
import core.encoding.EncodingChange;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;

public class Utility
{

	public static boolean checkIp(String ipAddress) {  
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";   
        Pattern pattern = Pattern.compile(ip);  
        Matcher matcher = pattern.matcher(ipAddress);  
        return matcher.matches();  
    }
	
	public static void sendMessageBySocket(String message, Socket socket, Map<String, String> config,
			TextArea textarea_log)
	{
		try
		{
			PrintWriter writer = os2pPrintWriter(socket.getOutputStream());
			writer.println(message);
			writer.flush();
			afterSendMessage(message, socket.getRemoteSocketAddress().toString(), config, textarea_log);
			// 不能关掉这个流 切记！
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			Utility.alertBox(e.getMessage());
		}
	}

	// 发送消息之后的后续动作
	public static void afterSendMessage(String message, String ip, Map<String, String> config, TextArea textarea_log)
	{
		boolean log_mode = Utility.string2Bollean(config.get(ConfigName.RECEIVE_LOG_MODE));
		// file name is empty ?
		boolean f = Utility.isEmpty(config.get(ConfigName.RECEIVE_FILE_NAME));

		// 暂停显示？
		boolean pause = Utility.string2Bollean(config.get(ConfigName.PAUSE_RECEIVE));
		//
		if (log_mode && f && !pause)
		{
			// 日志模式 并且不存到文件 并且不是暂停显示 则显示发送消息
			String time_str = "[" + Utility.getCurTime() + "] " + "send to:" + ip +"send bytes:"+message.getBytes().length+ "\r\n";
			message = time_str + message;
			UiUpdaer uiUpdaer = new UiUpdaer(textarea_log);
			uiUpdaer.update(message);
		}
		// 非日志模式 不显示
	}

	// 打开一个消息弹窗提示 在别的线程也可以调用
	public static void alertBox(String alllertMessage)
	{

		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText("");
			alert.setContentText("发生错误，提示信息为->"+alllertMessage);
			alert.showAndWait();
		});

	}

	// 处理收到的tcp字符串
	public static String processStringTcp(StringBuffer buffer, Map<String, String> config, Socket socket)
	{
		System.out.println("收到消息" + buffer.toString());

		boolean log_mode = Utility.string2Bollean(config.get(ConfigName.RECEIVE_LOG_MODE));

		String time_str = "";
		if (log_mode)
		{
			int bytes = buffer.toString().getBytes().length;
			time_str = "[" + Utility.getCurTime() + "] " + "receive from:" + socket.getRemoteSocketAddress() +"receive bytes:"+bytes+ "\r\n";
		}
		boolean is_hex = Utility.string2Bollean(config.get(ConfigName.RECEIVE_IS_HEX));
		boolean is_ascii = Utility.string2Bollean(config.get(ConfigName.RECEIVE_IS_ASCII));
		// 最终的文本
		String log_content = "";
		if (is_hex)
		{
			log_content = time_str + EncodingChange.str2Hex(buffer.toString());
		} else if (is_ascii)
		{
			log_content = time_str + buffer.toString();
		}

		boolean auot_enter = Utility.string2Bollean(config.get(ConfigName.RECEIVE_AUTO_ENTER));
		if (auot_enter)
			log_content += "\r\n";
		System.out.println(log_content);
		return log_content;
	}

	// 处理收到的udp包
	public static String processStringUdp(StringBuffer buffer, Map<String, String> config, DatagramPacket packet)
	{
		System.out.println("收到消息" + buffer.toString());

		boolean log_mode = Utility.string2Bollean(config.get(ConfigName.RECEIVE_LOG_MODE));

		String time_str = "";
		if (log_mode)
		{
			int bytes = buffer.toString().getBytes().length;
			time_str = "[" + Utility.getCurTime() + "] " + "receive from:" + packet.getAddress() + ":"
					+ packet.getPort() +"receive bytes:"+bytes+ "\r\n";
		}
		boolean is_hex = Utility.string2Bollean(config.get(ConfigName.RECEIVE_IS_HEX));
		boolean is_ascii = Utility.string2Bollean(config.get(ConfigName.RECEIVE_IS_ASCII));
		// 最终的文本
		String log_content = "";
		if (is_hex)
		{
			log_content = time_str + EncodingChange.str2Hex(buffer.toString());
		} else if (is_ascii)
		{
			log_content = time_str + buffer.toString();
		}

		boolean auot_enter = Utility.string2Bollean(config.get(ConfigName.RECEIVE_AUTO_ENTER));
		if (auot_enter)
			log_content += "\r\n";
		System.out.println(log_content);
		return log_content;
	}

	public static String readFromIns(InputStream ins)
	{
		ByteArrayOutputStream baos = null;
		try
		{
			byte[] buffer = new byte[1024];
			baos = new ByteArrayOutputStream();
			int len = -1;
			while ((len = ins.read(buffer)) != -1)
			{
				baos.write(buffer, 0, len);
			}
			String string = baos.toString("utf-8");
			return string;
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try
			{
				ins.close();
				baos.close();
			} catch (Exception e2)
			{
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return "";
	}

	public static void saveToFile(String filename, String content)
	{
		System.out.println("写入内容为" + content);
		File file = new File(filename);
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		try
		{
			if (!file.exists())
			{
				boolean hasFile = file.createNewFile();
				if (hasFile)
				{
					System.out.println("file not exists, create new file");
				}
				fos = new FileOutputStream(file);
			} else
			{
				System.out.println("file exists");
				fos = new FileOutputStream(file, true);
			}

			osw = new OutputStreamWriter(fos, "utf-8");
			osw.write(content); // 写入内容
			osw.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{ // 关闭流
			try
			{
				if (osw != null)
				{
					osw.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				if (fos != null)
				{
					fos.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static String getCurTime()
	{
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return simpleDateFormat.format(date);
	}

	public static boolean string2Bollean(String string)
	{
		if (isEmpty(string))
			return false;
		return Boolean.valueOf(string);
	}

	public static boolean isEmpty(String string)
	{
		if (string == null || string.equals(""))
			return true;
		return false;
	}

	public static BufferedReader ins2BufferedReader(InputStream stream)
	{
		return new BufferedReader(new InputStreamReader(stream));
	}

	public static PrintWriter os2pPrintWriter(OutputStream stream)
	{
		return new PrintWriter(stream);
	}

	// 获得本地所有网卡的ip
	public static List<String> getNetworkAddress()
	{
		List<String> result = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces;
		try
		{
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			while (netInterfaces.hasMoreElements())
			{
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements())
				{
					ip = addresses.nextElement();
					if (ip.getHostAddress().indexOf(':') == -1)
					{
						result.add(ip.getHostAddress());
					}
				}
			}

			return result;
		} catch (Exception e)
		{
			return null;
		}
	}

}
