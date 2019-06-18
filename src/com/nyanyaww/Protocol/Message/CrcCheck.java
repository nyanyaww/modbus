package com.nyanyaww.Protocol.Message;

/**
 * @author nyanyaww
 * @program modbus
 * @description crc校验
 * @create 2019-06-18 15:33
 **/
public class CrcCheck {

    public static String crc16(String gprsstr) {
        try {
            int crc;
            int strlength, r;
            byte sbit;
            int tc;
            strlength = gprsstr.length();
            byte[] data = gprsstr.getBytes();
            crc = 0x0000FFFF;
            for (int i = 0; i < strlength; i++) {
                tc = (int) (crc >>> 8);
                crc = (int) (tc ^ data[i]);
                for (r = 0; r < 8; r++) {
                    sbit = (byte) (crc & 0x00000001);
                    crc >>>= 1;
                    if (sbit != 0)
                        crc ^= 0x0000A001;
                }
            }
            return Integer.toHexString(crc);
        } catch (Exception ex) {
            return "";
        }
    }
}
