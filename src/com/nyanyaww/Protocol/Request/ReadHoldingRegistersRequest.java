package com.nyanyaww.Protocol.Request;

import com.nyanyaww.code.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 读取保持寄存器 功能码03
 * @create 2019-06-01 23:48
 **/
public class ReadHoldingRegistersRequest extends BaseRequest {
    public ReadHoldingRegistersRequest(char clientId, char startAddr, char dataLength) {
        super(clientId, startAddr, dataLength);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.READ_HOLDING_REGISTERS;
    }
}
