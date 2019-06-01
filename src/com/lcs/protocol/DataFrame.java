package com.lcs.protocol;

import Util.StringUtil;
import com.lcs.Test.HexConvert;

import java.util.*;

/**
 * @author nyanyaww
 * @program modbus
 * @description 定义数据帧的格式
 * @create 2019-05-31 17:19
 **/
public class DataFrame {

    private char clientAddr;        // 从机地址
    private char functionCode;      // 功能码
    private char startAddr;         // 起始地址
    private char[] dataInfo;

    public DataFrame(char clientAddr, char functionCode, char startAddr, char[] dataInfo) {
        this.clientAddr = clientAddr;
        this.functionCode = functionCode;
        this.startAddr = startAddr;
        this.dataInfo = dataInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.charToStringIbit(clientAddr));
        sb.append(StringUtil.charToStringIbit(functionCode));
        sb.append(StringUtil.charToStringIIbit(startAddr));
        for (char c : dataInfo) {
            sb.append(StringUtil.charToStringIIbit(c));
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        char clientAddr = 0x01;
        char functionCode = 0x01;
        char sAddr = 0x00ff;
        char[] inffo = {0x1100, 0x1102};
        DataFrame dataFrame = new DataFrame(clientAddr, functionCode, sAddr, inffo);
        System.out.println("数据的封装");
        System.out.println(dataFrame.toString());

        byte[] s = HexConvert.hexStringToBytes(dataFrame.toString());
        String dehandleString = HexConvert.binaryToHexString(s);
        String[] sss = dehandleString.split(" ");
        System.out.println("数据的解包，16进制转化");
        System.out.println(dehandleString);

        System.out.println("分离出单个8bit数据");
        for (int i = 0; i < sss.length; i++)
            System.out.printf("%d,%s\n", i, sss[i]);
    }

}
