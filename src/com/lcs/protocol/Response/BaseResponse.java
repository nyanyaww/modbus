package com.lcs.protocol.Response;

/**
 * @author nyanyaww
 * @program modbus
 * @description 基础的应答类
 * @create 2019-06-02 00:05
 **/
public class BaseResponse {
    private char clientId;
    private char[] data;

    public BaseResponse(char clientId, char[] data) {
        this.clientId = clientId;
        this.data = data;
    }
}
