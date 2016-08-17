package com.uuzz.android.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public static final SimpleDateFormat DATE_INPUT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final SimpleDateFormat DEFAULT_FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat DEFAULT_FORMAT_YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");

    public static final SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DATE_FORMAT_MM = new SimpleDateFormat("MM");
    public static final SimpleDateFormat DATE_FORMAT_DD = new SimpleDateFormat("dd");
    public static final SimpleDateFormat DATE_FORMAT_HHmm = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat DATE_FORMAT_MMddHHmm = new SimpleDateFormat("MM-dd HH:mm");
    private static final long one_second = 1000;
    private static final long one_minute = one_second * 60;
    private static final long one_hour = one_minute * 60;
    private static final long one_day = one_hour * 24;
    private static final long one_second_s = 1;
    private static final long one_minute_s = one_second_s * 60;
    private static final long one_hour_s = one_minute_s * 60;
    private static final long one_day_s = one_hour_s * 24;

    /**
     * long time to string
     *
     * @param timeInMillis 传入0返回""
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        if(timeInMillis == 0) {
            return "";
        }
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_FORMAT_YYYYMMDDHHMMSS}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_FORMAT_YYYYMMDDHHMMSS);
    }

    /**
     * 获取当前时间并转为与服务器交互所用格式
     * @return
     */
    public static String getTime() {
        return getTime(System.currentTimeMillis(), DATE_INPUT_YYYYMMDDHHMMSS);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is
     * {@link #DEFAULT_FORMAT_YYYYMMDDHHMMSS}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String formatYYYYMMDDHHMMSS(String time) {
        try {
            return DEFAULT_FORMAT_YYYYMMDDHHMMSS.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatYYYYMMDD(String time) {
        try {
            return DATE_FORMAT_YYYYMMDD.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatMM(String time) {
        try {
            return DATE_FORMAT_MM.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDD(String time) {
        try {
            return DATE_FORMAT_DD.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param time
     * @return HH:mm
     */
    public static String formatHHmm(String time) {
        try {
            return DATE_FORMAT_HHmm.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
        }
        return "";
    }

    public static String formatYYYYMMDDHHMM(String time) {
        try {
            return DEFAULT_FORMAT_YYYYMMDDHHMM.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
        }
        return "";
    }

    public static String formatYYYY_MM_DD(String time) {
        try {
            return DEFAULT_FORMAT_YYYYMMDD.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
        }
        return "";
    }

    public static String formatMMddHHmm(String time) {

        try {
            return DATE_FORMAT_MMddHHmm.format(DATE_INPUT_YYYYMMDDHHMMSS.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param time
     * @return 刚刚，1分钟前，1小时前
     */
    public static String formatDate(long time) {
        if (time == 0) {
            return "";
        }
        long now = System.currentTimeMillis();
        long ext = now - time;

        if (ext >= one_day) {
            return getTime(time);
        }
        long m = ext / one_hour;
        if (m == 0) {
            m = ext / one_minute;
            if (m == 0) {
                return "刚刚";
            } else {
                return m + "分钟前";
            }
        } else {
            return m + "小时前";
        }
    }

    /**
     * @param longTime
     * @return 1天1小时，1小时5分，2分23秒
     */
    public static String formatTimeForIssue(String longTime) {

        return formatTime(longTime, "期次获取中");
    }

    public static String formatTimeEmpty(String longTime) {

        return formatTime(longTime, "");
    }

    public static String formatTime(String longTime, String errStr) {

        try {
            long time = Long.valueOf(longTime);
            long n = time / one_day_s;
            long left;
            String day, hour, minute, second;

            // 取天
            if (n > 0) {
                day = n + "天";
                left = time - n * one_day_s;

                n = left / one_hour_s;
                // 取小时
                if (n > 0) {
                    hour = n + "小时";
                    return day + hour;
                } else {
                    return day;
                }
            } else {
                // 不足一天
                n = time / one_hour_s;
                // 取小时
                if (n > 0) {
                    hour = n + "小时";

                    left = time - n * one_hour_s;

                    n = left / one_minute_s;
                    // 取分钟
                    if (n > 0) {
                        minute = n + "分钟";
                        return hour + minute;
                    } else {
                        return hour;
                    }

                } else {
                    // 不足一小时
                    n = time / one_minute_s;

                    if (n > 0) {
                        minute = n + "分钟";
                        second = (time - n * one_minute_s) + "秒";

                        return minute + second;
                    } else {
                        // 不足一分钟
                        return time + "秒";
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return errStr;
        }
    }

    // 20150101222222 => 2015-01-01
    public static String yyyymmddToNianyueri(String str) {
        if (str.length() < 8) {
            return str;
        }

        return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
    }

    // 20150101222222 => 2015年01月01日22时22分22秒
    public static String yyyymmddhhmmssToNianyuerishifenmiao(String str) {
        if (str.length() != 14) {
            return str;
        }

        return str.substring(0, 4) + "年" + str.substring(4, 6) + "月" + str.substring(6, 8) + "日" + str.substring(8, 10) + "时" + str.substring(10, 12) + "分" + str.substring(12, 14) + "秒";
    }

    // 20150101222222 => 2015-01-01 22:22:22
    public static String formatToWholeTime(String str) {
        if (str.length() != 14) {
            return str;
        }

        return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     * @param timeInMillis
     * @return
     */
    public static int getWeek(String timeInMillis) {
        // 再转换为时间
        Date date;
        try {
            date = DATE_INPUT_YYYYMMDDHHMMSS.parse(timeInMillis);
        } catch (ParseException e) {
            Log.e("TimeUtils", "parse time error", e);
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static String getWeekStr(String sdate){
        String str;
        switch (TimeUtil.getWeek(sdate)) {
            case 1:
                str = "周日";
                break;
            case 2:
                str = "周一";
                break;
            case 3:
                str = "周二";
                break;
            case 4:
                str = "周三";
                break;
            case 5:
                str = "周四";
                break;
            case 6:
                str = "周五";
                break;
            case 7:
                str = "周六";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * 描 述：用于开奖公告的奖期时间转化<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/6/1 注释 <br/>
     */
    public static String formatMMddHHmm2Week(String sdate){
        return TimeUtil.yyyymmddToNianyueri(sdate) + " (" + TimeUtil.getWeekStr(sdate) + ")";
    }

    public static String formatTimeForIssue2(String longTime) {

        return formatTime2(longTime, "期次获取中");
    }

    /**
     * *
     *
     * @param longTime
     * @param errStr
     * @return 1天2小时50分24秒=1:20:50:24
     */
    public static String formatTime2(String longTime, String errStr) {

        try {
            if (TextUtils.isEmpty(longTime)) {
                return errStr;
            }
            long time = Long.valueOf(longTime);
            long days = time / (60 * 60 * 24);
            long hours = (time % (60 * 60 * 24)) / (60 * 60);
            long minutes = (time % (60 * 60)) / (60);
            long seconds = (time % (60));
//        return days + " days " + hours + " hours " + minutes + " minutes "
//                + seconds + " seconds ";

            String _hours, _minutes, _seconds;


            if (hours < 10) {
                _hours = "0" + hours;
            } else {
                _hours = "" + hours;
            }

            if (minutes < 10) {
                _minutes = "0" + minutes;
            } else {
                _minutes = "" + minutes;
            }

            if (seconds < 10) {
                _seconds = "0" + seconds;
            } else {
                _seconds = "" + seconds;
            }

            if (days > 0) {
                return days + ":" + _hours + ":" + _minutes + ":"
                        + _seconds;
            } else {
                return _hours + ":" + _minutes + ":"
                        + _seconds;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return errStr;
        }
    }
}
