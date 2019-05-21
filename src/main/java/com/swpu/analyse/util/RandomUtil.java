package com.swpu.analyse.util;


import java.util.Random;

/**
 * @author cyg
 * @date 18-7-26 下午9:20
 **/
public class RandomUtil {

    /**
     * 功能描述: <br>
     * 〈生成 length 长的随机数〉
     *
     * @param
     * @return
     * @author cyg
     * @date 18-9-18 下午4:16
     */
    public static String getRandomInteger(int length) {
        String base = "0123456789abcdefghijkmlmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 在指定范围内生成随机数
     **/
    public static String getRandomInteger(String base, int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


}
