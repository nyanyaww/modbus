package com.lcs.protocol.Request;

import com.lcs.Test.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 写寄存器 功能码06
 * @create 2019-06-01 23:50
 **/
public class WriteRegisterRequest extends BaseRequest {

    public WriteRegisterRequest(char clientAddr, char startAddr, char dataLength) {
        super(clientAddr, startAddr, dataLength);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.WRITE_REGISTER;
    }
}
