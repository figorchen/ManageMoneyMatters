package com.uuzz.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by troy on 2016/2/19.
 * 判断字符串工具类
 */
public class StringUtilTool {

    public final static String CHINESE = "[\u4e00-\u9fa5]*"; // 汉字
    public final static String DIGITAL = "\\d*"; // 数字
    public final static String LETTER = "\\w*"; // 字母

    /**
     * 是否特殊字符（除汉字、字母数字之外的算特殊字符）
     *
     * @param str
     * @return
     */
    public static boolean isSpecialChar(String str) {
        if (str != null && !"".equals(str)
                && (str.contains("_") || str.contains("——"))) {
            return true;
        }
        return !isMatches(str, CHINESE, DIGITAL, LETTER);
    }

    /**
     * 根据传递的正则表达式验证字符串
     *
     * @param str    待验证字符串
     * @param regexs 正则表达式数组,只要有一个匹配即返回true
     * @return true:通过验证 false:未通过验证
     */
    public static boolean isMatches(String str, String... regexs) {
        Pattern pattern;
        for (String regex : regexs) {
            pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);

            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是汉字
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        boolean flag = true;
        char temp[] = str.toCharArray();
        int count = temp.length;
        for (int i = 0; i < count; i++) {
            int v = temp[i];
            boolean result = (v >= 19968 && v <= 171941);
            if (!result) {
                flag = false;
                break;
            }
        }
        if (!isChineseChar(str)) {
            flag = false;
        }
        return flag;
    }

    private static boolean isChineseChar(String str) {
        return isMatches(str, CHINESE);
    }
}
