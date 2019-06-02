package com.nyanyaww.Protocol.Message;

import com.nyanyaww.Protocol.Response.ReadCoilsResponse;
import com.nyanyaww.Protocol.Response.ReadHoldingRegisterResponse;
import com.nyanyaww.TestData.AllSimulatorData;
import com.nyanyaww.code.FunctionCode;
import com.nyanyaww.Protocol.Request.WriteCoilRequest;
import com.nyanyaww.Protocol.Request.WriteRegisterRequest;

import java.util.Map;

/**
 * @author nyanyaww
 * @program modbus
 * @description 对解包之后的处理，这个类是提供给下位机的
 * @create 2019-06-02 01:09
 **/
public class MessageHandle {
    private Map<String, char[]> clientData;
    private char clientId;          // 从机地址
    private char functionCode;      // 功能码
    private char startAddr;         // 起始地址
    private char dataLength;        // 数据长度


    public MessageHandle(char clientId, char functionCode, char startAddr, char dataLength, Map<String, char[]> clientData) {
        this.clientId = clientId;
        this.functionCode = functionCode;
        this.startAddr = startAddr;
        this.dataLength = dataLength;
        this.clientData = clientData;
    }

    public void init() {
        switch (functionCode) {
            case FunctionCode.READ_COILS:
                //TODO:这里其实不对 实际上是8bit的8个线圈值，而这里的16bit数据只有表示了一个值
                char[] coilsData = {clientData.get("线圈")[0]};
                ReadCoilsResponse readCoilsResponse = new ReadCoilsResponse(clientId,
                        coilsData);
                System.out.println(readCoilsResponse.toString());
                break;
            case FunctionCode.READ_DISCRETE_INPUTS:
                System.out.println("0x02");
                break;
            case FunctionCode.READ_HOLDING_REGISTERS:
                char[] holdRegister = {clientData.get("保持寄存器")[0]};
                ReadHoldingRegisterResponse readHoldingRegisterResponse =
                        new ReadHoldingRegisterResponse(clientId, holdRegister);
                System.out.println(readHoldingRegisterResponse.toString());
                break;
            case FunctionCode.READ_INPUT_REGISTERS:
                System.out.println("0x04");
                break;
            case FunctionCode.WRITE_COIL:
                WriteCoilRequest writeCoilRequest = new WriteCoilRequest(clientId, startAddr, dataLength);
                System.out.println(writeCoilRequest.toString());
                break;
            case FunctionCode.WRITE_REGISTER:
                WriteRegisterRequest writeRegisterRequest = new WriteRegisterRequest(clientId, startAddr, dataLength);
                System.out.println(writeRegisterRequest.toString());
                break;
            default:
                break;
        }
    }

    public void showData() {
        for (int i = 0; i < 2000; i++) {
            System.out.print(Integer.valueOf(clientData.get("线圈")[i]));
            System.out.print(" ");
            System.out.print(Integer.valueOf(clientData.get("离散量输入")[i]));
            System.out.println();
        }
        for (int i = 0; i < 125; i++) {
            System.out.print(Integer.valueOf(clientData.get("保持寄存器")[i]));
            System.out.print(" ");
            System.out.print(clientData.get("输入寄存器")[i]);
            System.out.println();
        }
    }


    public static void main(String[] args) {
        AllSimulatorData allSimulatorData = new AllSimulatorData();
        Map<String, char[]> clientData = allSimulatorData.getClientData();

        MessageParser messageParser = new MessageParser("010300130013");
        Map<String, Character> map = messageParser.getStringMap();
        MessageHandle messageHandle = new MessageHandle(map.get("从机地址"), map.get("功能码"),
                map.get("起始地址"), map.get("请求长度"), clientData);
        messageHandle.init();
//        messageHandle.showData();
    }
}
