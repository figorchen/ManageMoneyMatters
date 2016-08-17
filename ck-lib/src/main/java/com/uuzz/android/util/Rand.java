package com.uuzz.android.util;

import java.util.Date;
import java.util.Random;

public class Rand {
    private static Random random = new Random(new Date().getTime());

    public static long getLong() {
        long tmp = random.nextLong();
        return tmp < 0 ? 0 - tmp : tmp;
    }

    public static int getInt() {
        int tmp = random.nextInt();
        return tmp < 0 ? 0 - tmp : tmp;
    }

    public static int getInt(int n) {
        return random.nextInt(n);
    }

    public static String getRandomNumString(int weishu) {
        String rtn = "";

        for (int i = 0; i < weishu; i++) {
            rtn += (getInt(10) + "");
        }

        return rtn;
    }

    public static int getVerifyCode() {
        return 100000 + random.nextInt(900000);
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}

