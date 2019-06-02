package com.nyanyaww.Protocol.Request;

import com.nyanyaww.code.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 写单个线圈 功能码05
 * @create 2019-06-01 23:49
 **/
public class WriteCoilRequest extends BaseRequest {
    public WriteCoilRequest(char clientId, char startAddr, char dataLength) {
        super(clientId, startAddr, dataLength);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.WRITE_COIL;
    }
}
