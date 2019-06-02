package com.nyanyaww.TestData;

import java.util.Random;

/**
 * @author nyanyaww
 * @program modbus
 * @description 线圈的模拟数据
 * @create 2019-06-02 13:46
 **/
public class CoilsData {

    private char[] coilNo = new char[2000];

    public CoilsData() {
        init();
    }

    public void init() {
        Random random = new Random();
        for (int i = 0; i < 2000; i++) {
            coilNo[i] = (char) (random.nextBoolean() ? 1 : 0);
//            System.out.println(coilNo[i]);
        }

    }

    public char[] getCoilNo() {
        return coilNo;
    }

    public static void main(String[] args) {
        CoilsData coilsData = new CoilsData();
    }
}
