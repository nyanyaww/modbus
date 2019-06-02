package com.nyanyaww.Test;
//
///**
// * @author nyanyaww
// * @program modbus
// * @description 平时的测试类
// * @create 2019-06-02 22:36
// **/
//public class Test {
//    char a = 0;
//    char b = 1;
//    char ans;
//
//    public int binary(char a[]) {
//        int ans = 0;
//        for (int i = 0; i < 8; i++) {
//            ans += Math.pow(10, 8-i)*a[i];
//        }
//        return ans;
//    }
//
//    public static void main(String[] args) {
//        char[] a = {1, 0, 0, 1, 0, 1, 0, 1};
//        Test test = new Test();
//        int ab = test.binary(a);
//        System.out.println(Integer.parseInt("149",16));
//    }
//}

import java.util.Scanner;

public class Test {

    public static String hexString2binaryString(String bString) {

        bString = bString.replace(" ", "");//去掉直接从word表格内复制出来的空格
        bString = bString.replace(" ", "");//去掉英文空格
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {

            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));

        }
        return tmp.toString();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//      Scanner scanner = new Scanner(System.in);
//      String [] hexString = new String [52];
//      for(int i = 0 ; i <52 ;i++){
//          String s = scanner.nextLine();
//          hexString[i] = hexString2binaryString(s);
//      }
//      for(int i = 0;i < 52 ;i++){
//          System.out.println(hexString[i]);
//      }
        System.out.println(hexString2binaryString("10010101"));
    }

}