package com.nyanyaww.Protocol.Message;

import com.nyanyaww.Test.HexConvert;
import com.nyanyaww.TestData.AllSimulatorData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nyanyaww
 * @program modbus
 * @description 对请求的解析
 * @create 2019-06-02 01:07
 **/
public class MessageParser {
    private String request;
    private Map<String, Character> stringMap = new HashMap<>();

    public MessageParser(String request) {
        this.request = request;
        preHandle();
    }

    public void preHandle() {
        byte[] s = HexConvert.hexStringToBytes(request);
        String dehandleString = HexConvert.binaryToHexString(s);
        String[] sss = dehandleString.split(" ");

        String clientId = sss[0];
        String functionCode = sss[1];
        String startAddr = sss[2] + sss[3];
        String dataLength = sss[4] + sss[5];

        stringMap.put("clientId", (char) Integer.parseInt(clientId, 16));
        stringMap.put("functionCode", (char) Integer.parseInt(functionCode, 16));
        stringMap.put("startAddr", (char) Integer.parseInt(startAddr, 16));
        stringMap.put("dataLength", (char) Integer.parseInt(dataLength, 16));

    }

    public Map<String, Character> getStringMap() {
        return stringMap;
    }

    public static void main(String[] args) {
        AllSimulatorData allSimulatorData = new AllSimulatorData();
        MessageParser messageParser = new MessageParser("010100130015");
//        messageParser.preHandle();
        System.out.println(messageParser.getStringMap());
    }
}
