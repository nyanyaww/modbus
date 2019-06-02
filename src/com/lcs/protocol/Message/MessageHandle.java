package com.lcs.protocol.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nyanyaww
 * @program modbus
 * @description 对解包之后的处理
 * @create 2019-06-02 01:09
 **/
public class MessageHandle {
    private char clientId;          // 从机地址
    private char functionCode;      // 功能码
    private char startAddr;         // 起始地址
    private char dataLength;        // 数据长度

    public MessageHandle(char clientId, char functionCode, char startAddr, char dataLength) {
        this.clientId = clientId;
        this.functionCode = functionCode;
        this.startAddr = startAddr;
        this.dataLength = dataLength;
    }

    public void init() {
        switch (functionCode) {
            case 0x01:
                System.out.println("0x01");
                break;
            case 0x02:
                System.out.println("0x02");
                break;
            case 0x03:
                System.out.println("0x03");
                break;
            case 0x04:
                System.out.println("0x04");
                break;
            case 0x05:
                System.out.println("0x05");
                break;
            case 0x06:
                System.out.println("0x06");
                break;
            default:
                break;
        }
    }


    public static void main(String[] args) {
        MessageParser messageParser = new MessageParser("010100130015");
        Map<String, Character> map = messageParser.getStringMap();
        MessageHandle messageHandle = new MessageHandle(map.get("clientId"), map.get("functionCode"),
                map.get("startAddr"), map.get("dataLength"));
        messageHandle.init();
    }
}
