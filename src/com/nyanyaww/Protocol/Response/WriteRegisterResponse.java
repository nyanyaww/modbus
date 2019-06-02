package com.nyanyaww.Protocol.Response;

import com.nyanyaww.code.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 写入寄存器应答 功能码06
 * @create 2019-06-02 01:00
 **/
public class WriteRegisterResponse extends WriteCoilResponse {
    public WriteRegisterResponse(char clientId) {
        super(clientId);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.WRITE_REGISTER;
    }
}
