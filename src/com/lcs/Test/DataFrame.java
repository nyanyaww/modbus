package com.lcs.Test;

/**
 * @author nyanyaww
 * @program modbus
 * @description 定义数据帧的格式
 * @create 2019-05-31 17:19
 **/
public class DataFrame {
    private char addr;
    private char functionCode;
    private char[] registerStartAddr = new char[2];
    private char[] registerNumber = new char[2];

    public static void main(String[] args){

    }

}
