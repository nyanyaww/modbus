package com.lcs.protocol.Response;

import com.lcs.Test.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取输入寄存器 功能码04
 * @create 2019-06-02 00:50
 **/
public class ReadInputRegister extends ReadHoldingRegisterResponse{
    public ReadInputRegister(char clientId, char[] data) {
        super(clientId, data);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.READ_INPUT_REGISTERS;
    }
}
