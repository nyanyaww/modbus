package com.lcs.protocol.Response;

import com.lcs.Test.FunctionCode;
import com.lcs.Util.StringUtil;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取保持寄存器应答 功能码03
 * @create 2019-06-02 00:45
 **/
public class ReadHoldingRegisterResponse extends BaseResponse {
    private char byteNumber;

    public ReadHoldingRegisterResponse(char clientId, char[] data) {
        super(clientId, data);
        byteNumber = (char) (data.length * 2);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.READ_HOLDING_REGISTERS;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.charToStringIbit(clientId));
        sb.append(StringUtil.charToStringIbit(getFunctionCode()));
        sb.append(StringUtil.charToStringIbit(byteNumber));
        for (char datum : data)
            sb.append(StringUtil.charToStringIIbit(datum));
        return sb.toString();
    }

    public static void main(String[] args) {
        char[] data = {0x022b, 0x0000, 0x0064};
        ReadHoldingRegisterResponse readCoilsResponse = new ReadHoldingRegisterResponse((char) 0x01, data);
        System.out.println(readCoilsResponse.toString());
    }
}
