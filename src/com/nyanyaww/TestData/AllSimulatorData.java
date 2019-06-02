package com.nyanyaww.TestData;

/**
 * @author nyanyaww
 * @program modbus
 * @description 提供api调用生成所有我们需要的模拟数据
 * @create 2019-06-02 14:10
 **/
public class AllSimulatorData {
    private byte[] coilData;
    private byte[] discreteInputData;
    private char[] holdingRegisterData;
    private char[] inputRegisterData;

    public AllSimulatorData() {
        CoilsData coilsData = new CoilsData();
        DiscreteInputData discreteInputData = new DiscreteInputData();
        HoldingRegisterData holdingRegisterData = new HoldingRegisterData();
        InputRegisterData inputRegisterData = new InputRegisterData();
        this.coilData = coilsData.getCoilNo();
        this.discreteInputData = discreteInputData.getDiscreteInputNo();
        this.holdingRegisterData = holdingRegisterData.getHoldingRegisterNo();
        this.inputRegisterData = inputRegisterData.getInputRegister();
    }

    public byte[] getCoilData() {
        return coilData;
    }

    public byte[] getDiscreteInputData() {
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
        for (int i = 0; i < 2000; i++) {
            System.out.print(allSimulatorData.getCoilData()[i]);
            System.out.print(" ");
            System.out.print(allSimulatorData.getDiscreteInputData()[i]);
            System.out.println();
        }
        for (int i = 0; i < 125; i++) {
            System.out.print(allSimulatorData.getHoldingRegisterData()[i]);
            System.out.print(" ");
            System.out.print(allSimulatorData.getInputRegisterData()[i]);
            System.out.println();
        }
    }
}
