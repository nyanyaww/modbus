package com.nyanyaww.Protocol.Response;

import com.nyanyaww.code.FunctionCode;

/**
 * @author nyanyaww
 * @program modbus
 * @description 基础的应答类
 * @create 2019-06-02 00:05
 **/
public class BaseResponse {
    char clientId;
    char[] data;

    public BaseResponse(char clientId) {
        this.clientId = clientId;
    }

    public BaseResponse(char clientId, char[] data) {
        this.clientId = clientId;
        this.data = data;
    }

    public char getFunctionCode() {
        return FunctionCode.READ_COILS;
    }
}
