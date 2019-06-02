package com.nyanyaww.TestData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nyanyaww
 * @program modbus
 * @description 提供api调用生成所有我们需要的模拟数据
 * @create 2019-06-02 14:10
 **/
public class AllSimulatorData {
    private char[] coilData;
    private char[] discreteInputData;
    private char[] holdingRegisterData;
    private char[] inputRegisterData;
    private Map<String, char[]> clientData = new HashMap<>();

    public AllSimulatorData() {
        CoilsData coilsData = new CoilsData();
        DiscreteInputData discreteInputData = new DiscreteInputData();
        HoldingRegisterData holdingRegisterData = new HoldingRegisterData();
        InputRegisterData inputRegisterData = new InputRegisterData();
        this.coilData = coilsData.getCoilNo();
        this.discreteInputData = discreteInputData.getDiscreteInputNo();
        this.holdingRegisterData = holdingRegisterData.getHoldingRegisterNo();
        this.inputRegisterData = inputRegisterData.getInputRegister();
        clientData.put("线圈", this.coilData);
        clientData.put("离散量输入", this.discreteInputData);
        clientData.put("保持寄存器", this.holdingRegisterData);
        clientData.put("输入寄存器", this.inputRegisterData);

    }

    public Map<String, char[]> getClientData() {
        return clientData;
    }

    public char[] getCoilData() {
        return coilData;
    }

    public char[] getDiscreteInputData() {
        return discreteInputData;
    }

    public char[] getHoldingRegisterData() {
        return holdingRegisterData;
    }

    public char[] getInputRegisterData() {
        return inputRegisterData;
    }

    public static void main(String[] args) {
        AllSimulatorData allSimulatorData = new AllSimulatorData();
        Map<String,char[]> ans = allSimulatorData.getClientData();
        for (int i = 0; i < 2000; i++) {
            System.out.print(Integer.valueOf(ans.get("线圈")[i]));
            System.out.print(" ");
            System.out.print(Integer.valueOf(ans.get("离散量输入")[i]));
            System.out.println();
        }
        for (int i = 0; i < 125; i++) {
            System.out.print(Integer.valueOf(ans.get("保持寄存器")[i]));
            System.out.print(" ");
            System.out.print(ans.get("输入寄存器")[i]);
            System.out.println();
        }
    }
}
