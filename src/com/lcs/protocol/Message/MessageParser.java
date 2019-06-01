package com.lcs.protocol.Message;

import com.lcs.Test.HexConvert;

import java.util.Hashtable;

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

    public MessageParser(String request) {
        this.request = request;
    }

    public void preHandle() {
        byte[] s = HexConvert.hexStringToBytes(request);
        String dehandleString = HexConvert.binaryToHexString(s);
        String[] sss = dehandleString.split(" ");
        System.out.println("数据的解包，16进制转化");
        System.out.println(dehandleString);


        clientId = sss[0];
        functionCode = sss[1];
        startAddr = sss[2] + sss[3];
        dataLength = sss[4] + sss[5];

        System.out.println(clientId);
        System.out.println(functionCode);
        System.out.println(startAddr);
        System.out.println(dataLength);
    }

    public static void main(String[] args) {
        MessageParser messageParser = new MessageParser("010100130015");
        messageParser.preHandle();
    }
}
