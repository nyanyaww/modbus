package com.nyanyaww.Protocol.Message;

import com.nyanyaww.Protocol.Response.ReadCoilsResponse;
import com.nyanyaww.Protocol.Response.ReadDiscreteInputResponse;
import com.nyanyaww.Protocol.Response.ReadHoldingRegisterResponse;
import com.nyanyaww.Protocol.Response.ReadInputRegister;
import com.nyanyaww.TestData.AllSimulatorData;
import com.nyanyaww.Util.StringUtil;
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
    static private char[] allCoilsData;
    static private char[] allDiscreteInputData;
    static private char[] allHoldRegisterData;
    static private char[] allInputRegisterData;


    public MessageHandle(char clientId, char functionCode, char startAddr, char dataLength, Map<String, char[]> clientData) {
        this.clientId = clientId;
        this.functionCode = functionCode;
        this.startAddr = startAddr;
        this.dataLength = dataLength;
        this.clientData = clientData;
        allCoilsData = clientData.get("线圈");
        allDiscreteInputData = clientData.get("离散量输入");
        allHoldRegisterData = clientData.get("保持寄存器");
        allInputRegisterData = clientData.get("输入寄存器");
    }

    public String packResponse() {
        switch (functionCode) {
            // 完成
            case FunctionCode.READ_COILS:
                // 先得到线圈对应的二进制的字符串
                String coilsData = getCoilsData(startAddr, startAddr + dataLength);
                String hexData = StringUtil.binaryStringToHexString(coilsData);
                char[] ans = StringUtil.StringToCharX(hexData);
                ReadCoilsResponse readCoilsResponse = new ReadCoilsResponse(clientId,
                        ans);
                return readCoilsResponse.toString();
            // 完成
            case FunctionCode.READ_DISCRETE_INPUTS:
                // 先得到线圈对应的二进制的字符串
                String discreteInputsData = getDiscreteInputsData(startAddr, startAddr + dataLength);
                String discreteInputsHexData = StringUtil.binaryStringToHexString(discreteInputsData);
                assert discreteInputsHexData != null;
                char[] ans1 = StringUtil.StringToCharX(discreteInputsHexData);
                ReadDiscreteInputResponse readDiscreteInputResponse = new ReadDiscreteInputResponse(clientId,
                        ans1);
                return readDiscreteInputResponse.toString();

            case FunctionCode.READ_HOLDING_REGISTERS:
                char[] holdRegister = getHoldRegisterData(startAddr, startAddr + dataLength);
                ReadHoldingRegisterResponse readHoldingRegisterResponse =
                        new ReadHoldingRegisterResponse(clientId, holdRegister);
                return readHoldingRegisterResponse.toString();

            case FunctionCode.READ_INPUT_REGISTERS:
                char[] inputRegister = getInputRegisterData(startAddr, startAddr + dataLength);
                ReadInputRegister readInputRegister =
                        new ReadInputRegister(clientId, inputRegister);
                return readInputRegister.toString();

            // 完成
            case FunctionCode.WRITE_COIL:
                WriteCoilRequest writeCoilRequest = new WriteCoilRequest(clientId, startAddr, dataLength);
                return writeCoilRequest.toString();
            // 完成
            case FunctionCode.WRITE_REGISTER:
                WriteRegisterRequest writeRegisterRequest = new WriteRegisterRequest(clientId, startAddr, dataLength);
                return writeRegisterRequest.toString();
            default:
                return "NULL";
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

    // 获取仿真数据
    // 获取线圈数据
    public String getCoilsData(int from, int to) {
        int length = to - from;
        int charLength = length % 8;
        if (charLength == 0)
            charLength = 8;
        StringBuilder sb = new StringBuilder();
        char[] returnData = new char[length];
        for (int i = 0; i < length; i++) {
            returnData[i] = allCoilsData[from + i];
            sb.append(Integer.valueOf(returnData[i]));
        }
        for (int i = length; i < length + 8 - charLength; i++) {
            sb.append(0);
        }
        return sb.toString();
    }

    // 获取离散输入数据
    public String getDiscreteInputsData(int from, int to) {
        int length = to - from;
        int charLength = length % 8;
        if (charLength == 0)
            charLength = 8;
        StringBuilder sb = new StringBuilder();
        char[] returnData = new char[length];
        for (int i = 0; i < length; i++) {
            returnData[i] = allDiscreteInputData[from + i];
            sb.append(Integer.valueOf(returnData[i]));
        }
        for (int i = length; i < length + 8 - charLength; i++) {
            sb.append(0);
        }
        return sb.toString();
    }

    // 获取保持寄存器数据
    public char[] getHoldRegisterData(int from, int to) {
        int length = to - from;
        char[] returnData = new char[length];
        for (int i = 0; i < length; i++) {
            returnData[i] = allHoldRegisterData[from + i];
        }
        return returnData;
    }

    // 获取输入寄存器数据
    public char[] getInputRegisterData(int from, int to) {
        int length = to - from;
        char[] returnData = new char[length];
        for (int i = 0; i < length; i++) {
            returnData[i] = allInputRegisterData[from + i];
        }
        return returnData;
    }

    public static void main(String[] args) {
        // 获取仿真数据
        AllSimulatorData allSimulatorData = new AllSimulatorData();
        Map<String, char[]> clientData = allSimulatorData.getClientData();

        // 6种功能码的测试
        String[] testSend = {
                "010100130013",
                "010200130013",
                "010300130003",
                "010400130003",
                "010500130013",
                "010600130013",
        };

        // 上位机请求解析
        // 发送数据
        MessageParser messageParser = new MessageParser();
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < testSend.length; i++) {
            System.out.println("测试" + (i + 1));
            sb.delete(0, sb.length());
            sb.append(testSend[i]);
            sb.append(CrcCheck.crc16(testSend[i]));
            messageParser.setRequest(sb.toString());
            System.out.println("发送 " + sb.toString());
            // 数据解析为字典
            Map<String, Character> map = messageParser.getStringMap();
            // 数据处理
            MessageHandle messageHandle = new MessageHandle(map.get("从机地址"), map.get("功能码"),
                    map.get("起始地址"), map.get("请求长度"), clientData);
            stringBuilder.delete(0,stringBuilder.length());
            stringBuilder.append(messageHandle.packResponse());
            stringBuilder.append(CrcCheck.crc16(messageHandle.packResponse()));
            System.out.println("应答 " + stringBuilder.toString());
        }
    }
}
