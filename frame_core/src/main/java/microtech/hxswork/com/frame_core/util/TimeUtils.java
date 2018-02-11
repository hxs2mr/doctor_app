package microtech.hxswork.com.frame_core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by wxg on 2017/8/23
 */
public class TimeUtils {
    public final static int MINUTE_SECS = 60;
    public final static int HOUR_SECS = MINUTE_SECS * 60;
    public final static int DAY_SECS = 24 * HOUR_SECS;
    public final static int WEEK_SECS = 7 * DAY_SECS;
    public final static int MONTH_SECS = 30 * DAY_SECS;


    public static long currTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static long currDayZeroTimestamp() {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return c.getTimeInMillis() / 1000;
    }

    public static String dayFormat(long timestamp) {
        Date d = new Date(timestamp * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        return formatter.format(d);
    }

    public static long lastDayWithYear(int year) {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        c.set(year, 11, 31, 23, 59, 59);
        return c.getTimeInMillis() / 1000;
    }

    public static int yearFromTimestamp(long timestamp) {
        Date d = new Date(timestamp * 1000);
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        return c.get(Calendar.YEAR);
    }

    public static String currTimeSN() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dayS = formatter.format(now);
        long mils = System.currentTimeMillis() - currDayZeroTimestamp() * 1000;
        return String.format("%s%010d", dayS, mils);
    }

    public static long getMinutes(long timestamp) {
        return (timestamp - currTimestamp()) / 60;
    }

    public static int currYear() {
        return yearFromTimestamp(currTimestamp());
    }
    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串

     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     z
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */

    public static String date2TimeStamp1(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static String timeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);
        return t;
    }
    /*
       * 将时间转换为时间戳
       */
    public static String dateToStamp(String s,String format) {
        String res;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = simpleDateFormat.parse(s);
            long ts = date.getTime()/1000;
            res = String.valueOf(ts);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s,String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(s)*1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

        /**
         * 传入日期字符串返回对应时间戳
         */
    public static long timestampFromDateStr(String dateStr) {
        if (dateStr == null || "".equals(dateStr.trim())) {
            return 0L;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr).getTime() / 1000;
        } catch (ParseException e) {
            System.err.println("日期格式错误");
            return 0L;
        }
    }
    /**
     * 传入日期字符串返回对应时间戳
     */
    public static long timestampFromDateStr(String dateStr,String formt) {
        if (dateStr == null || "".equals(dateStr.trim())) {
            return 0L;
        }
        try {
            return new SimpleDateFormat(formt).parse(dateStr).getTime() / 1000;
        } catch (ParseException e) {
            System.err.println("日期格式错误");
            return 0L;
        }
    }


    public static void main(String[] args) {
        //82540800 82540800
        System.err.println(TimeUtils.timestampFromDateStr("1967-05-22 23:59:00","yyyy-MM-dd HH:mm:ss"));
//        System.err.println(stampToDate("-82540800","yyyy-MM-dd"));
    }
}
