package com.lcs.protocol.Request;

import com.lcs.Util.StringUtil;
import com.lcs.Test.FunctionCode;
import com.lcs.Test.HexConvert;

/**
 * @author nyanyaww
 * @program modbus
 * @description 请求的封装
 * @create 2019-06-01 23:28
 **/
public class BaseRequest {
    private char clientId;        // 从机地址
    private char startAddr;         // 起始地址
    private char dataLength;        // 数据长度

    public BaseRequest(char clientId, char startAddr, char dataLength) {
        this.clientId = clientId;
        this.startAddr = startAddr;
        this.dataLength = dataLength;
    }

    public char getFunctionCode() {
        return FunctionCode.READ_COILS;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.charToStringIbit(clientId));
        sb.append(StringUtil.charToStringIbit(getFunctionCode()));
        sb.append(StringUtil.charToStringIIbit(startAddr));
        sb.append(StringUtil.charToStringIIbit(dataLength));
        return sb.toString();
    }

    public static void main(String[] args) {
        char clientAddr = 0x01;
        char functionCode = 0x01;
        char sAddr = 0x0013;
        char inffo = 0x0000;
        BaseRequest re = new BaseRequest(clientAddr, sAddr, inffo);
        System.out.println("数据的封装");
        System.out.println(re.toString());

        byte[] s = HexConvert.hexStringToBytes(re.toString());
        String dehandleString = HexConvert.binaryToHexString(s);
        String[] sss = dehandleString.split(" ");
        System.out.println("数据的解包，16进制转化");
        System.out.println(dehandleString);

        System.out.println("分离出单个8bit数据");
        for (int i = 0; i < sss.length; i++)
            System.out.printf("%d,%s\n", i, sss[i]);
    }
}
