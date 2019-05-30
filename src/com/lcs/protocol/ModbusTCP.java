package com.lcs.protocol;

/**
 * @author nyanyaww
 * @program modbus
 * @description 这是是实现了modbus的tcp协议，但是请注意，
 * 这只是作业中使用的所谓的modbustcp协议，实际的tcp不需要crc校验
 * @create 2019-05-30 23:41
 **/
public class ModbusTCP {
    private char addr;
    public ModbusTCP()
    {
        addr = 0x01;
    }

    public void setAddr(char addr1) {
        addr = addr1;
    }

    char getAddr() {
        return addr;
    }


    public static void main(String[] args) {
        ModbusTCP modbusTCP = new ModbusTCP();
        modbusTCP.setAddr((char) 0x01);
    }
}
