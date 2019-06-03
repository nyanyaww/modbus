package com.nyanyaww.code;

/**
 * @author nyanyaww
 * @program modbus
 * @description 功能码的表示 使程序可读性变好
 * @create 2019-05-31 00:20
 **/
public class FunctionCode {
    public static final byte READ_COILS = 1;
    public static final byte READ_DISCRETE_INPUTS = 2;
    public static final byte READ_HOLDING_REGISTERS = 3;
    public static final byte READ_INPUT_REGISTERS = 4;
    public static final byte WRITE_COIL = 5;
    public static final byte WRITE_REGISTER = 6;

    public static String toString(byte code) {
        return Integer.toString(code & 0xff);
    }
}
