// test3.java 7/23/97 - JAVA program to read registers via gateway
// compile as
// javac test3.java
// run as
// java test3 aswales1.modicon.com 1 2 3

package com.lcs.Test;

import java.io.*;
import java.net.*;
import java.util.*;

class test3 {

    public static void main(String[] args) {
//        Byte addr = 0x1F;
//        Byte functionCode = 0x08;
//        Byte[] registerStartAddr = {0x00, 0x04};
//        Byte[] registerNumbers = {0x00, 0x02};
//
//        Vector<Byte> HHH = new Vector<>();
//        {
//            HHH.add(addr);
//            HHH.add(functionCode);
//
//            HHH.add(registerStartAddr[0]);
//            HHH.add(registerStartAddr[1]);
//
//            HHH.add(registerNumbers[0]);
//            HHH.add(registerNumbers[1]);
//        }
//
//        System.out.println(byteToHex(HHH.get(0)));
        byte a =89;
        String
        System.out.println(a & 0xff);
        if()
        System.out.println((a & 0xff) - 256);

    }

    public static String byteToHex(byte bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");

        strHex = Integer.toHexString(bytes & 0xFF);
        sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0

        return sb.toString().trim();
    }
}