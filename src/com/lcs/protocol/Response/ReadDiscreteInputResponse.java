package com.lcs.protocol.Response;

import com.lcs.Test.FunctionCode;
import com.lcs.Util.StringUtil;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取离散量输入应答 功能码02
 * @create 2019-06-02 00:43
 **/
public class ReadDiscreteInputResponse extends ReadCoilsResponse {


    public ReadDiscreteInputResponse(char clientId, char[] data) {
        super(clientId, data);
    }

    public static void main(String[] args) {
        char[] data = {0xCD, 0x6B, 0x05};
        ReadDiscreteInputResponse readCoilsResponse = new ReadDiscreteInputResponse((char) 0x01, data);
        System.out.println(readCoilsResponse.toString());
    }
}
