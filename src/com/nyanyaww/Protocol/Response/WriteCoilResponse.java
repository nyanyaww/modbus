package com.nyanyaww.Protocol.Response;

import com.nyanyaww.Util.StringUtil;

/**
 * @author nyanyaww
 * @program modbus
 * @description 写入线圈应答 功能码05
 * @create 2019-06-02 00:54
 **/
public class WriteCoilResponse extends BaseResponse {
    private char startAddr;         // 起始地址
    private char dataLength;        // 数据长度

    public WriteCoilResponse(char clientId) {
        super(clientId);
    }

    public void readResponse(char startAddr, char dataLength) {
        this.startAddr = startAddr;
        this.dataLength = dataLength;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.charToStringIbit(clientId));
        sb.append(StringUtil.charToStringIbit(getFunctionCode()));
        sb.append(StringUtil.charToStringIIbit(startAddr));
        sb.append(StringUtil.charToStringIIbit(dataLength));
        return sb.toString();
    }
}
