package com.lcs.protocol.Message;

import com.lcs.Test.HexConvert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author nyanyaww
 * @program modbus
 * @description 对请求的解析
 * @create 2019-06-02 01:07
 **/
public class MessageParser {
    private String request;
    private String clientId;
    private String functionCode;
    private String startAddr;
    private String dataLength;
    private Map<String, Character> stringMap = new HashMap<>();

    public MessageParser(String request) {
        this.request = request;
    }

    public void preHandle() {
        byte[] s = HexConvert.hexStringToBytes(request);
        String dehandleString = HexConvert.binaryToHexString(s);
        String[] sss = dehandleString.split(" ");

        clientId = sss[0];
        functionCode = sss[1];
        startAddr = sss[2] + sss[3];
        dataLength = sss[4] + sss[5];

        char temp = 11;
        stringMap.put("clientId", temp);
        System.out.println(stringMap.get("clientId"));
//        stringMap.put("functionCode", (char) Integer.parseInt(functionCode, 16));
//        stringMap.put("startAddr", (char) Integer.parseInt(startAddr, 16));
//        stringMap.put("dataLength", (char) Integer.parseInt(dataLength, 16));

    }

    public Map<String, Character> getStringMap() {
        return stringMap;
    }

    public static void main(String[] args) {
        MessageParser messageParser = new MessageParser("010100130015");
        messageParser.preHandle();
        MessageMap messageMap = new MessageMap();
//        messageMap.message = messageParser.getStringMap();
        System.out.println(messageParser.getStringMap());
    }
}
