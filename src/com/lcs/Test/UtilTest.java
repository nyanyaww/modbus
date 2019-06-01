package com.lcs.Test;

/**
 * @author nyanyaww
 * @program modbus
 * @description 测试工具类
 * @create 2019-06-01 20:46
 **/


public class UtilTest {

    public static void main(String[] args) {
        String s = HexConvert.stringToHex("11019019");
        System.out.println(s);
        String hex = HexConvert.hexToString("010100130013");
        for (int i = 0; i < hex.length(); i++) {
            System.out.print ((int) hex.charAt(i));
            System.out.print(" ");
        }
    }
}
