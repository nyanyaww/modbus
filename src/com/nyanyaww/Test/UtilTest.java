package com.nyanyaww.Test;

/**
 * @author nyanyaww
 * @program modbus
 * @description 测试工具类
 * @create 2019-06-01 20:46
 **/


public class UtilTest {

    /**
     * 我们不考虑java的库本身给我们提供的方法
     * 实际上cs之间传输的是二进制数组才对
     * 可是二进制数组是十分难以阅读的
     * 我认为还是得把它转化成string型的来处理
     **/
    public static void main(String[] args) {
        byte[] s = HexConvert.hexStringToBytes("010100130013");
        System.out.println(HexConvert.binaryToHexString(s));
        String hex = HexConvert.hexToString("010100130013");
        for (int i = 0; i < hex.length(); i++) {
            System.out.print(Integer.valueOf(hex.charAt(i)));
            System.out.print(" ");
//            System.out.print(HexConvert.);
        }
    }
}
