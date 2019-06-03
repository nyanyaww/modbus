package com.nyanyaww.TestData;

import java.util.Random;

/**
 * @author nyanyaww
 * @program modbus
 * @description 模拟生成保持寄存器的值
 * @create 2019-06-02 14:01
 **/
public class HoldingRegisterData {
    private char[] holdingRegisterNo = new char[125];

    public HoldingRegisterData() {
        init();
    }

    public void init() {
        Random random = new Random();
        for (int i = 0; i < 125; i++) {
            holdingRegisterNo[i] = (char) random.nextInt(65535);
//            System.out.println(Integer.valueOf(holdingRegisterNo[i]));
        }

    }

    public char[] getHoldingRegisterNo() {
        return holdingRegisterNo;
    }

    public static void main(String[] args) {
        HoldingRegisterData holdingRegisterData = new HoldingRegisterData();

    }
}
