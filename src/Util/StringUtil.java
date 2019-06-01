package Util;

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
}
