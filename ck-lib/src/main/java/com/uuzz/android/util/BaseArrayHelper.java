/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: BaseArrayHelper.java <br/>
 * <p/>
 * Created by 谌珂 on 2015/12/30.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: BaseArrayHelper <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2015/12/30 <br/>
 * @author 谌珂 <br/>
 */
public class BaseArrayHelper {
    /**
     * 描 述：int数组转Integer数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的int[]
     * @return Integer[]
     */
    public static Integer[] getIntegerArray(int[] original){
        Integer[] array = new Integer[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = Integer.valueOf(original[i]);
        }
        return array;
    }

    /**
     * 描 述：Integer数组转int数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Integer[]
     * @return int[]
     */
    public static int[] getIntArray(Integer[] original){
        int[] array = new int[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：short数组转Short数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的short[]
     * @return Short[]
     */
    public static Short[] getShortArray(short[] original){
        Short[] array = new Short[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：Short数组转short数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Short[]
     * @return short[]
     */
    public static short[] getShortArray(Short[] original){
        short[] array = new short[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：double数组转Double数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的double[]
     * @return Double[]
     */
    public static Double[] getDoubleArray(double[] original){
        Double[] array = new Double[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：Double数组转double数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Double[]
     * @return double[]
     */
    public static double[] getDoubleArray(Double[] original){
        double[] array = new double[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：float数组转Float数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的float[]
     * @return Float[]
     */
    public static Float[] getFloatArray(float[] original){
        Float[] array = new Float[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：Float数组转float数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Float[]
     * @return float[]
     */
    public static float[] getFloatArray(Float[] original){
        float[] array = new float[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：boolean数组转Boolean数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的boolean[]
     * @return Boolean[]
     */
    public static Boolean[] getBooleanArray(boolean[] original){
        Boolean[] array = new Boolean[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：Boolean数组转boolean数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Boolean[]
     * @return boolean[]
     */
    public static boolean[] getBooleanArray(Boolean[] original){
        boolean[] array = new boolean[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：long数组转Long数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的long[]
     * @return Long[]
     */
    public static Long[] getLongArray(long[] original){
        Long[] array = new Long[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：Long数组转long数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Long[]
     * @return long[]
     */
    public static long[] getLongArray(Long[] original){
        long[] array = new long[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：byte数组转Byte数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的byte[]
     * @return Byte[]
     */
    public static Byte[] getByteArray(byte[] original){
        Byte[] array = new Byte[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：Byte数组转byte数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Byte[]
     * @return byte[]
     */
    public static byte[] getByteArray(Byte[] original){
        byte[] array = new byte[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：char数组转Character数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的char[]
     * @return Character[]
     */
    public static Character[] getCharacterArray(char[] original){
        Character[] array = new Character[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }

    /**
     * 描 述：Character数组转char数组<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     * @param original 需要转化的Character[]
     * @return char[]
     */
    public static char[] getCharacterArray(Character[] original){
        char[] array = new char[original.length];
        for (int i = 0; i < original.length; i++) {
            array[i] = original[i];
        }
        return array;
    }
}
