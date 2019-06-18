package com.nyanyaww.Protocol.Message;

import com.nyanyaww.TestData.AllSimulatorData;

import java.util.Map;

/**
 * @author nyanyaww
 * @program modbus
 * @description
 * @create 2019-06-19 02:23
 **/
public class ClientResponse {
    // 获取仿真数据
    static private AllSimulatorData allSimulatorData = new AllSimulatorData();
    static private Map<String, char[]> clientData = allSimulatorData.getClientData();

    public ClientResponse() {
    }

    static public String send(String receive) {
        StringBuilder stringBuilder = new StringBuilder();
        MessageParser messageParser = new MessageParser();
        messageParser.setRequest(receive);
        Map<String, Character> map = messageParser.getStringMap();
        // 数据处理
        MessageHandle messageHandle = new MessageHandle(map.get("从机地址"), map.get("功能码"),
                map.get("起始地址"), map.get("请求长度"), clientData);
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(messageHandle.packResponse());
        stringBuilder.append(CrcCheck.crc16(messageHandle.packResponse()));
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(send("010100130013"));
        System.out.println(send("010100130013"));
    }
}
