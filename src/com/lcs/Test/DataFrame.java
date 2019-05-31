package com.lcs.Test;

import java.util.*;

/**
 * @author nyanyaww
 * @program modbus
 * @description 定义数据帧的格式
 * @create 2019-05-31 17:19
 **/
public class DataFrame {
//
//    static class baseFrame {
//        private String addr;
//        private String functionCode;
//    }
//
//    static class Coil{
//        private
//    }


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

        public void initParser(){
            Queue<Integer> a= new Queue<Integer>() {
                @Override
                public boolean add(Integer integer) {
                    return false;
                }

                @Override
                public boolean offer(Integer integer) {
                    return false;
                }

                @Override
                public Integer remove() {
                    return null;
                }

                @Override
                public Integer poll() {
                    return null;
                }

                @Override
                public Integer element() {
                    return null;
                }

                @Override
                public Integer peek() {
                    return null;
                }

                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(Object o) {
                    return false;
                }

                @Override
                public Iterator<Integer> iterator() {
                    return null;
                }

                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @Override
                public <T> T[] toArray(T[] a) {
                    return null;
                }

                @Override
                public boolean remove(Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> c) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<? extends Integer> c) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> c) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> c) {
                    return false;
                }

                @Override
                public void clear() {

                }
            } ;
            System.out.println();
        }


        public int StringToHex(char a, char b) {
            int ans = (a - '0') * 10 + b - '0';
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

    }

}
