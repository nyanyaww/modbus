package com.nyanyaww.Util;

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

        bString = bString.replace(" ", "");//去掉直接从word表格内复制出来的空格
        bString = bString.replace(" ", "");//去掉英文空格
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
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
}
