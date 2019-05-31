package com.lcs.Test;

import java.util.Arrays;
import java.util.Vector;

/**
 * @author nyanyaww
 * @program modbus
 * @description 定义数据帧的格式
 * @create 2019-05-31 17:19
 **/
public class DataFrame {

    static class Request {
        private char addr;
        private char functionCode;
        private char[] coilStartAddr = new char[2];
        private char[] coilNumber = new char[2];
        private StringBuilder RequestTest = new StringBuilder();

        public Request(char addr, char functionCode, char[] coilStartAddr, char[] coilNumber) {
            this.addr = addr;
            this.functionCode = functionCode;
            this.coilStartAddr = coilStartAddr;
            this.coilNumber = coilNumber;


            RequestTest.append(charToString(addr));
//            RequestTest.append(" ");
            RequestTest.append(charToString(functionCode));
//            RequestTest.append(" ");
            RequestTest.append(charToString(coilStartAddr[0]));
//            RequestTest.append(" ");
            RequestTest.append(charToString(coilStartAddr[1]));
//            RequestTest.append(" ");
            RequestTest.append(charToString(coilNumber[0]));
//            RequestTest.append(" ");
            RequestTest.append(charToString(coilNumber[1]));

//            System.out.println(RequestTest);

        }


        public String getRequestTest() {
            return RequestTest.toString();
        }

        public String charToString(char c) {
            String hexStr = Integer.toHexString(c);
            if (hexStr.length() == 1)
                return "0" + hexStr;
            return hexStr;
        }
    }

    static class Response {
        private char addr;
        private char functionCode;
        private char btyes;
        private char[] coilStatus;
        private StringBuilder Response = new StringBuilder();
        private String request;

        public Response(String request) {
            this.request = request;
        }

        public String ans() {
            int i = 0;
            for (i = 0; i < request.length(); i += 2) {
                System.out.print(request.charAt(i));
                System.out.println(request.charAt(i + 1));
            }
            return request;
        }

        public void test() {
            System.out.println(StringToHex("13"));
        }


        public int StringToHex(String s) {
            int ans = (s.charAt(0) - '0') * 10 + s.charAt(1) - '0';
            return ans;
        }
    }


    public static void main(String[] args) {
        char addr = 0x01;
        char functionCode = 0x01;
        char[] coilStartAddr = {0x00, 0x13};
        char[] coilNumber = {0x00, 0x13};
        Request request = new Request(addr, functionCode, coilStartAddr, coilNumber);
//        System.out.println(request.getRequestTest());

        Response response = new Response(request.getRequestTest());
        response.test();

    }

}
