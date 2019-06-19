package core.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Scanner;

import com.nyanyaww.Protocol.Message.ClientResponse;
import controller.ConfigName;
import javafx.scene.control.TextArea;
import util.ThreadPoll;
import util.UiUpdaer;
import util.Utility;

public class TCPClient {

    public static Socket socket = null;
    private static Map<String, String> config = null;

    private static TextArea textArea = null;

    public static void createSocket(String ip, String port, Map<String, String> config, TextArea textArea)
            throws Exception {

        TCPClient.textArea = textArea;
        TCPClient.config = config;
        socket = new Socket(ip, Integer.valueOf(port));
        ThreadPoll.execute(() -> {
            try {
                BufferedReader reader = Utility.ins2BufferedReader(socket.getInputStream());
                while (!socket.isClosed()) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(reader.readLine());
                    if (Utility.isEmpty(buffer.toString()))
                        continue;
                    String log_content = Utility.processStringTcp(buffer, config, socket);
                    Utility.sendMessageBySocket(ClientResponse.send(log_content), socket, config, textArea);
                    String filename = config.get(ConfigName.RECEIVE_FILE_NAME);
                    if (Utility.isEmpty(filename)) {
                        boolean puse = Utility.string2Bollean(config.get(ConfigName.PAUSE_RECEIVE));
                        if (!puse) {
                            System.out.println("update ui");
                            UiUpdaer uiUpdaer = new UiUpdaer(textArea);
                            uiUpdaer.update(log_content);
                        }
                    } else {
                        // 存到文件
                        Utility.saveToFile(filename, log_content);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                Utility.alertBox("发生错误 " + e.getMessage());
            }

        });

    }

}
