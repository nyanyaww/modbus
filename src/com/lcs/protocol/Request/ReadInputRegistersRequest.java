package com.lcs.protocol.Request;

import com.lcs.Test.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取输入寄存器 功能码04
 * @create 2019-06-01 23:49
 **/
public class ReadInputRegistersRequest extends BaseRequest {

    public ReadInputRegistersRequest(char clientId, char startAddr, char dataLength) {
        super(clientId, startAddr, dataLength);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.READ_INPUT_REGISTERS;
    }
}
