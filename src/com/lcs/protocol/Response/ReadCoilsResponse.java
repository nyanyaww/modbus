package com.lcs.protocol.Response;

import com.lcs.code.FunctionCode;
import com.lcs.Util.StringUtil;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取线圈的应答 功能码01
 * @create 2019-06-02 00:19
 **/
public class ReadCoilsResponse extends BaseResponse {
    private char byteNumber;

    public ReadCoilsResponse(char clientId, char[] data) {
        super(clientId, data);
        byteNumber = (char) (data.length);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.READ_COILS;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.charToStringIbit(clientId));
        sb.append(StringUtil.charToStringIbit(getFunctionCode()));
        sb.append(StringUtil.charToStringIbit(byteNumber));
        for (char datum : data)
            sb.append(StringUtil.charToStringIbit(datum));
        return sb.toString();
    }

    public static void main(String[] args) {
        char[] data = {0xCD, 0x6B, 0x05};
        ReadCoilsResponse readCoilsResponse = new ReadCoilsResponse((char) 0x01, data);
        System.out.println(readCoilsResponse.toString());
    }

}
