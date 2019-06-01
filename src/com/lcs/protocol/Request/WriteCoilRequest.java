package com.lcs.protocol.Request;

import com.lcs.Test.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 写单个线圈 功能码05
 * @create 2019-06-01 23:49
 **/
public class WriteCoilRequest extends BaseRequest {
    public WriteCoilRequest(char clientAddr, char startAddr, char dataLength) {
        super(clientAddr, startAddr, dataLength);
    }

    @Override
    public char getFunctionCode() {
        return FunctionCode.WRITE_COIL;
    }
}
