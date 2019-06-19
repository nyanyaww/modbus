/**
 * Sample Skeleton for 'NetAssistView.fxml' Controller Class
 */

package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import core.encoding.EncodingChange;
import core.tcp.TCPClient;
import core.tcp.TCPServer;
import core.udp.UdpSocket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.media.VideoTrack;
import javafx.stage.FileChooser;
import util.UiUpdaer;
import util.Utility;

public class NetAssistController
{

	
	public static long send_bytes = 0;
	// 主要socket
	private Socket tcp_client_socket = null;
	// 服务器socket
	private ServerSocket tcp_server_socket = null;

	// udp socket
	private DatagramSocket udp_socket = null;

	// 本界面的所有配置都会存在这里 true false 类型会被保存成字符串形式
	private Map<String, String> config = new HashMap<String, String>();

	// 类型为 TCpServer 要保存所有的 accepted sockets 方便发送消息
	private Set<Socket> acceptedSockets = null;
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML
	private ComboBox<String> combobox_type_of_internet;

	@FXML
	private ComboBox<String> combobox_local_ip_address;
	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="label_netType"
	private Label label_netType; // Value injected by FXMLLoader

	@FXML // fx:id="label_port"
	private Label label_port; // Value injected by FXMLLoader

	@FXML // fx:id="textfield_port"
	private TextField textfield_port; // Value injected by FXMLLoader

	@FXML // fx:id="textfield_ipAddress"
	private TextField textfield_ipAddress; // Value injected by FXMLLoader

	@FXML // fx:id="radioButton_UDP"
	private RadioButton radioButton_UDP; // Value injected by FXMLLoader

	@FXML // fx:id="label_ipAddress"
	private Label label_ipAddress; // Value injected by FXMLLoader

	@FXML // fx:id="radioButton_tcpClient"
	private RadioButton radioButton_tcpClient; // Value injected by FXMLLoader

	@FXML // fx:id="radioButton_tcpServer"
	private RadioButton radioButton_tcpServer; // Value injected by FXMLLoader
	@FXML
	private TextArea textarea_send_message;

	// 日志显示
	@FXML
	private TextArea textarea_log;

	@FXML
	private HBox hbox_des_host;

	@FXML
	private TextField textfield_des_host;

	@FXML
	private RadioButton radiobutton_ascii;

	@FXML
	private RadioButton radiobutton_hex;

	@FXML
	private Button button_connect;

	@FXML
	private RadioButton radiobutton_send_ascii;

	@FXML
	private RadioButton radiobutton_send_hex;

	@FXML
	private Button button_send;
	
	@FXML
	private Label lable_send_bytes;
	
	@FXML
	private Label lable_connect_nums;

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize()
	{
		assert label_netType != null : "fx:id=\"label_netType\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		assert label_port != null : "fx:id=\"label_port\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		assert textfield_port != null : "fx:id=\"textfield_port\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		assert textfield_ipAddress != null : "fx:id=\"textfield_ipAddress\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		assert radioButton_UDP != null : "fx:id=\"radioButton_UDP\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		assert label_ipAddress != null : "fx:id=\"label_ipAddress\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		assert radioButton_tcpClient != null : "fx:id=\"radioButton_tcpClient\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		assert radioButton_tcpServer != null : "fx:id=\"radioButton_tcpServer\" was not injected: check your FXML file 'NetAssistView.fxml'.";
		// 获取本地所有网卡ip,动态添加到选择框中
		label_ipAddress.setText("本地主机名称");
		ObservableList<String> options = FXCollections.observableArrayList(Utility.getNetworkAddress());
		combobox_local_ip_address.setItems(options);
		combobox_local_ip_address.getSelectionModel().select(0);
		textarea_send_message.setText("test message");
		config.put(ConfigName.RECEIVE_IS_ASCII, "true");
		config.put(ConfigName.SEND_ASCII, "true");
		textfield_cycle_t.setText("1000");
	}

	// 连接成功标注，连接成功后，将此变量 值为 true
	private boolean isConnectSuccess = false;

	public void doConnect(ActionEvent event)
	{

		if (!isConnectSuccess)
		{

			connectSocket();
		} else
		{
			disconnectSocket();
		}
	}

	private void connectSocket()
	{
		String internet_type = combobox_type_of_internet.getValue();
		String local_address = combobox_local_ip_address.getValue();
		String port = textfield_port.getText();
		config.put(ConfigName.INTERNET_TYPE, internet_type);
		config.put(ConfigName.LOCAL_IP, local_address);
		config.put(ConfigName.LOCAL_PORT, port);
		System.out.println("类型为" + internet_type);
		System.out.println("ip地址为" + local_address + ":" + port);
		System.out.println("开始连接");
		// 下面写连接的逻辑
		openSocket();
		// 上面写连接的逻辑
		// 模拟连接成功

		// 连接成功 并且 连接选项为udp
		if (isConnectSuccess && combobox_type_of_internet.getValue().equals("UDP"))
		{
			hbox_des_host.setDisable(false);
		}
		if(isConnectSuccess)
			button_connect.setText("断开");
	}

	private void openSocket()
	{
		switch (config.get(ConfigName.INTERNET_TYPE))
		{
		case "TCP Server":
			openTcpServer();
			break;
		case "TCP Client":
			openTcpClient();
			break;
		default:
			openUdp();
			break;
		}
	}

	private void openTcpServer()
	{
		try
		{
			System.out.println("open tcp server socket");
			TCPServer.createTcpServer(config.get(ConfigName.LOCAL_IP), config.get(ConfigName.LOCAL_PORT), config,
					textarea_log,lable_connect_nums);
			
			tcp_server_socket = TCPServer.serverSocket;
			acceptedSockets = TCPServer.acceptedSockets;
			isConnectSuccess = true;
		} catch (Exception e)
		{
			Utility.alertBox("服务器开启失败"+e.getMessage());
		}
		
	}
	//TODO
	private void openTcpClient()
	{
		try
		{
			System.out.println("open tcp client socket");
			TCPClient.createSocket(config.get(ConfigName.LOCAL_IP), config.get(ConfigName.LOCAL_PORT), config,
					textarea_log);
			tcp_client_socket = TCPClient.socket;
			isConnectSuccess = true;
		} catch (Exception e)
		{
			// TODO: handle exception
			Utility.alertBox("连接失败："+e.getMessage());
		}
		
		
	}
	//TODO
	private void openUdp()
	{
		try
		{
			System.out.println("open udp socket");
			UdpSocket.createUdpSocket(config.get(ConfigName.LOCAL_IP), config.get(ConfigName.LOCAL_PORT), textarea_log, config);
			udp_socket = UdpSocket.socket;
			isConnectSuccess = true;
		} catch (Exception e)
		{
			// TODO: handle exception
			Utility.alertBox("打开udp失败："+e.getMessage());
		}
	}

	private void disconnectSocket()
	{
		System.out.println("断开连接");
		try
		{
			isConnectSuccess = false;
			button_connect.setText("连接");
			hbox_des_host.setDisable(true);
			switch (combobox_type_of_internet.getValue())
			{
			case "TCP Server":
				tcp_server_socket.close();
				tcp_server_socket = null;
				break;
			case "TCP Client":
				tcp_client_socket.close();
				tcp_client_socket = null;
				break;
			default:
				udp_socket.close();
				udp_socket = null;
				break;
			}
			// 正在循环发送 ，突然断开，则关闭发送线程。
			if (isSending)
			{
				button_send.setText("循环发送");
				isSending = false;
			}
			send_bytes = 0L;
			UiUpdaer uiUpdaer = new UiUpdaer(lable_send_bytes);
			uiUpdaer.resetAndUpdate("已发送："+send_bytes+"个字节");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			Utility.alertBox(e.getMessage());
			e.printStackTrace();
		}
	}

	boolean isSending = false;

	// 点击发送按钮的动作
	public void doSendMessage(ActionEvent event)
	{

		String message = textarea_send_message.getText();
		System.out.println("将要发送" + message);
		System.out.println("开始发送");
		boolean isCycle = Utility.string2Bollean(config.get(ConfigName.SEND_IS_CYCLE));
		if(tcp_client_socket == null && tcp_server_socket == null && udp_socket == null) {
			// 三个都未连接 此时不能发送
			Utility.alertBox("你还没有连接呢");
			return;
		}
		if (isCycle)
		{
			// 循环发送
			if (isSending)
			{
				// 正在循环发送
				isSending = false;

				// 关闭循环发送
				button_send.setText("循环发送");
			} else
			{
				// 未在循环发送

				// 开启循环发送

				String delay = textfield_cycle_t.getText();
				if (Utility.isEmpty(delay))
				{
					Utility.alertBox("延迟周期为空，重新输入");
					return;
				}
				isSending = true;
				int T = Integer.valueOf(delay);
				Thread thread = new Thread(() -> {
					try
					{
						while (isSending)
						{
							sendMessage(message);
							Thread.sleep(T);
						}
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				});
				thread.start();
				button_send.setText("结束");
			}
		} else
		{
			// 单次发送
			sendMessage(message);
		}
		
	}
	// 发送消息
	private void sendMessage(String message)
	{
		if (Utility.isEmpty(message))
		{
			Utility.alertBox("发送消息为空");
		}
		switch (combobox_type_of_internet.getValue())
		{
		case "TCP Server":
			tcpServerSend(message);
			break;
		case "TCP Client":
			tcpClientSend(message);
			break;
		default:
			udpSend(message);
			break;
		}
		send_bytes+=message.getBytes().length;
		UiUpdaer uiUpdaer = new UiUpdaer(lable_send_bytes);
		uiUpdaer.resetAndUpdate("已发送："+send_bytes+"个字节");
	}
	// tcpserver 发送消息
	private void tcpServerSend(String message)
	{
		if(acceptedSockets==null || acceptedSockets.size() == 0)
			Utility.alertBox("还没有客户端连接服务器");
		for(Socket socket : acceptedSockets) {
			boolean send_hex = Utility.string2Bollean(config.get(ConfigName.SEND_HEX));
			if(send_hex)
				message = EncodingChange.str2Hex(message);
			Utility.sendMessageBySocket(message, socket,config,textarea_log);
		}
	}

	private void tcpClientSend(String message)
	{
		if(tcp_client_socket == null)
			Utility.alertBox("发没有打开tcp client");
		boolean send_hex = Utility.string2Bollean(config.get(ConfigName.SEND_HEX));
		if(send_hex)
			message = EncodingChange.str2Hex(message);
		Utility.sendMessageBySocket(message, tcp_client_socket,config,textarea_log);
	}
	
	private void udpSend(String message)
	{
		if(udp_socket == null)
			Utility.alertBox("没有打开udp");
		config.put(ConfigName.SEND_IP,textfield_des_host.getText());
		boolean send_hex = Utility.string2Bollean(config.get(ConfigName.SEND_HEX));
		if(send_hex)
			message = EncodingChange.str2Hex(message);
		UdpSocket.sendPacket(udp_socket, message);
	}

	// 清理发送框数据
	public void doClearBottom(ActionEvent event)
	{
		textarea_send_message.clear();
		System.out.println("清理发送框数据");
	}

	// 清理日志框数据
	public void doClearTop(ActionEvent event)
	{
		textarea_log.clear();
		System.out.println("清理日志框数据");
	}

	// 清理目标主机输入框
	public void doClearDesHost(ActionEvent event)
	{
		textfield_des_host.clear();
		System.out.println("清理目标主机输入框");
	}

	// 两个radiobutton的事件函数 接收设置
	public void onAsciiClick(ActionEvent event)
	{
		radiobutton_hex.setSelected(false);
		config.put(ConfigName.RECEIVE_IS_ASCII, "true");
		config.put(ConfigName.RECEIVE_IS_HEX, "false");
	}
	// hex button 回调
	public void onHexClick(ActionEvent event)
	{
		radiobutton_ascii.setSelected(false);
		config.put(ConfigName.RECEIVE_IS_ASCII, "false");
		config.put(ConfigName.RECEIVE_IS_HEX, "true");
	}

	// 两个radiobutton的事件函数 发送设置
	public void onSendAscii(ActionEvent event)
	{
		radiobutton_send_hex.setSelected(false);
		config.put(ConfigName.SEND_ASCII, "true");
		config.put(ConfigName.SEND_HEX, "false");
	}

	public void onSendHex(ActionEvent event)
	{
		radiobutton_send_ascii.setSelected(false);
		config.put(ConfigName.SEND_ASCII, "false");
		config.put(ConfigName.SEND_HEX, "true");
	}

	@FXML
	private CheckBox checkbox_log_mode;

	public void changeLogMode(ActionEvent event)
	{
		boolean selected = checkbox_log_mode.isSelected();
		config.put(ConfigName.RECEIVE_LOG_MODE, String.valueOf(selected));
		System.out.println(ConfigName.RECEIVE_LOG_MODE + ":" + selected);
	}

	@FXML
	private CheckBox checkbox_auto_enter;

	public void changeAutoEnter(ActionEvent event)
	{
		boolean selected = checkbox_auto_enter.isSelected();
		config.put(ConfigName.RECEIVE_AUTO_ENTER, String.valueOf(selected));
		System.out.println(ConfigName.RECEIVE_AUTO_ENTER + ":" + selected);
	}

	@FXML
	private CheckBox checkbox_save_file;

	public void getFileName(ActionEvent event)
	{

		boolean selected = checkbox_save_file.isSelected();
		if (selected)
		{
			// 这里写打开对话框 获得文件路径
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("选择储存接收内容的文件");
			File file = fileChooser.showOpenDialog(null);
			//
			textarea_log.setText("接收转向至文件\r\n" + file.getAbsolutePath());
			textarea_log.setDisable(true);
			config.put(ConfigName.RECEIVE_FILE_NAME, file.getAbsolutePath());
			System.out.println(ConfigName.RECEIVE_FILE_NAME + ":" + config.get(ConfigName.RECEIVE_FILE_NAME));
		} else
		{
			textarea_log.setDisable(false);
			textarea_log.setText("");
			config.put(ConfigName.RECEIVE_FILE_NAME, null);
		}

	}

	@FXML
	private CheckBox checkbox_pause_disply;

	public void puseDisplay(ActionEvent event)
	{
		boolean selected = checkbox_pause_disply.isSelected();
		config.put(ConfigName.PAUSE_RECEIVE, String.valueOf(selected));
		System.out.println(ConfigName.PAUSE_RECEIVE + ":" + selected);
		textarea_log.setDisable(selected);
	}

	// 发送配置
	@FXML
	private CheckBox checkbox_auto_parse;

	public void onChangeAtuoParse(ActionEvent event)
	{
		boolean selected = checkbox_auto_parse.isSelected();
		config.put(ConfigName.SEND_AUTO_PARSE, String.valueOf(selected));
		System.out.println(ConfigName.SEND_AUTO_PARSE + ":" + selected);
	}

	@FXML
	private CheckBox checkbox_send_file_name;

	public void onGetFileNameSend(ActionEvent event) throws FileNotFoundException
	{

		boolean selected = checkbox_send_file_name.isSelected();
		if (selected)
		{
			// 此处写获得文件路径
			FileChooser fileChooser = new FileChooser();
			
			fileChooser.setTitle("打开发送文件");
			File file = fileChooser.showOpenDialog(null);
			String file_content = Utility.readFromIns(new FileInputStream(file));
			textarea_send_message.setText(file_content);
			config.put(ConfigName.SEND_FILE_NAME, file.getAbsolutePath());
			System.out.println(ConfigName.SEND_FILE_NAME + ":" + config.get(ConfigName.SEND_FILE_NAME));
		} else
		{
			config.put(ConfigName.SEND_FILE_NAME, null);
		}

	}

	@FXML
	private CheckBox checkbox_send_auto_enter;

	public void onChangeAutoEnterSend(ActionEvent event)
	{
		boolean selected = checkbox_send_auto_enter.isSelected();
		config.put(ConfigName.SEND_AT_AUTO_ENTER, String.valueOf(selected));
		System.out.println(ConfigName.SEND_AT_AUTO_ENTER + ":" + selected);
	}

	@FXML
	private CheckBox checkbox_is_cycle;
	@FXML
	private TextField textfield_cycle_t;
	// 循环发送回调
	public void onChangeToCycle(ActionEvent event)
	{
		boolean selected = checkbox_is_cycle.isSelected();
		config.put(ConfigName.SEND_IS_CYCLE, String.valueOf(selected));
		System.out.println(ConfigName.SEND_IS_CYCLE + ":" + selected);
		if (selected)
			button_send.setText("循环发送");
		else
			button_send.setText("发送");

	}
	// 网络类型选择框回调
	public void onInternetTypeChange(ActionEvent event)
	{
		String value = combobox_type_of_internet.getValue();
		if (value.equals("TCP Client"))
		{
			label_ipAddress.setText("远程主机名称");
			button_connect.setText("连接");
			ObservableList<String> options = FXCollections.observableArrayList();
			options.add("127.0.0.1");
			combobox_local_ip_address.setItems(options);
			combobox_local_ip_address.setEditable(true);
			combobox_local_ip_address.getSelectionModel().select(0);
		} else
		{
			button_connect.setText("打开");
			label_ipAddress.setText("本地主机名称");
			ObservableList<String> options = FXCollections.observableArrayList(Utility.getNetworkAddress());
			combobox_local_ip_address.setItems(options);
			combobox_local_ip_address.getSelectionModel().select(0);
			textarea_send_message.setText("test message");
		}
		if(value.equals("TCP Server")) {
			lable_connect_nums.setDisable(false);
		}else {
			lable_connect_nums.setDisable(true);
		}
	}

}
