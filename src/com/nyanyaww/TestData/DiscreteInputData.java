package com.nyanyaww.TestData;

import java.util.Random;

/**
 * @author nyanyaww
 * @program modbus
 * @description 离散量输入寄存器的模拟数据
 * @create 2019-06-02 13:57
 **/
public class DiscreteInputData {
    private byte[] discreteInputNo = new byte[2000];

    public DiscreteInputData() {
        init();
    }

    public void init() {
        Random random = new Random();
        for (int i = 0; i < 2000; i++) {
            discreteInputNo[i] = (byte) (random.nextBoolean() ? 1 : 0);
//            System.out.println(discreteInputNo[i]);
        }

    }

    public byte[] getDiscreteInputNo() {
        return discreteInputNo;
    }

    public static void main(String[] args){
        DiscreteInputData discreteInputData = new DiscreteInputData();
    }
}
