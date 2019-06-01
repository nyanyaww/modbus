package com.lcs.protocol;

import com.lcs.Test.FunctionCode;
import com.lcs.Test.HexConvert;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取离散量输入 功能码02
 * @create 2019-06-01 23:47
 **/
public class ReadDiscreteInputsRequest extends Request{
    public ReadDiscreteInputsRequest(char clientAddr, char startAddr, char dataLength) {
        super(clientAddr, startAddr, dataLength);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.READ_DISCRETE_INPUTS;
    }
    public static void main(String[] args) {
        char clientAddr = 0x01;
        char sAddr = 0x0013;
        char inffo = 0x0000;
        ReadDiscreteInputsRequest re = new ReadDiscreteInputsRequest(clientAddr, sAddr, inffo);
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
