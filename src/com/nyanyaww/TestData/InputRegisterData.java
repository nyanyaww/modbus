package com.nyanyaww.TestData;

import java.util.Date;
import java.util.Random;

/**
 * @author nyanyaww
 * @program modbus
 * @description 模拟生成输入寄存器的值
 * @create 2019-06-02 14:07
 **/
public class InputRegisterData {
    private char[] inputRegister = new char[125];

    public InputRegisterData() {
        init();
    }

    public void init() {
        Random random = new Random();
        for (int i = 0; i < 125; i++) {
            inputRegister[i] = (char) random.nextInt(65535);
//            System.out.println(Integer.valueOf(inputRegister[i]));
        }
    }

    public char[] getInputRegister() {
        return inputRegister;
    }

    public static void main(String[] args) {
        InputRegisterData holdingRegisterData = new InputRegisterData();

    }
}
