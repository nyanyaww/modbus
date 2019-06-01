package com.lcs.protocol.Request;

import com.lcs.Test.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取输入寄存器 功能码04
 * @create 2019-06-01 23:49
 **/
public class ReadInputRegistersBaseRequest extends BaseRequest {

    public ReadInputRegistersBaseRequest(char clientAddr, char startAddr, char dataLength) {
        super(clientAddr, startAddr, dataLength);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.READ_INPUT_REGISTERS;
    }
}
