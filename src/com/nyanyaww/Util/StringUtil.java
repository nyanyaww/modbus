package com.nyanyaww.Util;

import com.nyanyaww.Test.HexConvert;

import java.util.Vector;

/**
 * @author nyanyaww
 * @program modbus
 * @description 字符串的辅助类，用于分割字符
 * @create 2019-06-01 23:18
 **/
public class StringUtil {

    public static String charToStringIbit(char c) {
        String hexStr = Integer.toHexString(c);
        if (hexStr.length() == 1)
            return "0" + hexStr;
        return hexStr;
    }

    public static String charToStringIIbit(char c) {
        String hexStr = Integer.toHexString(c);
        switch (hexStr.length()) {
            case 1:
                return "000" + hexStr;
            case 2:
                return "00" + hexStr;
            case 3:
                return "0" + hexStr;
            case 4:
                return hexStr;
            case 0:
            default:
                return "0000";
        }
    }

    public static String binaryStringToHexString(String bString) {

        bString = bString.replace(" ", "");
        bString = bString.replace(" ", "");
        if (bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuilder tmp = new StringBuilder();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {

            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    public static char[] StringToCharX(String str) {
        int len = str.length();
        char[] cs = new char[len / 2];
        int j = 0;
        for (int i = 0; i < len; i += 2) {
            cs[j++] = (char)(str.charAt(i) <<8| str.charAt(i + 1));
        }
        return cs;
    }

    public static void main(String[] args) {
        char[] s = StringToCharX("10ff");
        System.out.println(Integer.valueOf(s[0]));
//        System.out.println(s[1]);
        String test = "123123";
        System.out.println(test.charAt(0) + "" + test.charAt(2));
//        System.out.println(HexConvert.hexStringToBytes("10ff")[1]);
//        System.out.println(HexConvert.hexStringToBytes("10ff")[2]);
//        System.out.println(HexConvert.hexStringToBytes("10ff")[3]);
//        System.out.println(HexConvert.hexStringToBytes("10ff")[4]);
    }
}
