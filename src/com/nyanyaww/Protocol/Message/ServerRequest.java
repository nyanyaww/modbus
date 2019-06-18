package com.nyanyaww.Protocol.Message;

/**
 * @author nyanyaww
 * @program modbus
 * @description 封装
 * @create 2019-06-19 02:23
 **/
public class ServerRequest {

    public String send(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append(CrcCheck.crc16(message));
        return sb.toString();
    }

    public static void main(String[] args) {
        // 6种功能码的测试
        String[] testSend = {
                "010100130013",
                "010200130013",
                "010300130003",
                "010400130003",
                "010500130013",
                "010600130013",
        };
        ServerRequest serverRequest = new ServerRequest();
        for (int i = 0; i < 6; i++) {
            System.out.println(serverRequest.send(testSend[i]));
        }
    }
}
