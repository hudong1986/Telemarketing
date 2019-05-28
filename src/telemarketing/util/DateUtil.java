/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telemarketing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;


/**
 * 鏃ユ湡Util绫�
 *
 * @author calvin
 */
public class DateUtil {

    private static String defaultDatePattern = "yyyy-MM-dd ";

    /**
     * 鑾峰緱榛樿鐨� date pattern
     */
    public static String getDatePattern() {
        return defaultDatePattern;
    }

    /**
     * 杩斿洖棰勮Format鐨勫綋鍓嶆棩鏈熷瓧绗︿覆
     */
    public static String getToday() {
        Date today = new Date();
        return format(today);
    }

    /**
     * 浣跨敤棰勮Format鏍煎紡鍖朌ate鎴愬瓧绗︿覆
     */
    public static String format(Date date) {
        return date == null ? " " : format(date, getDatePattern());
    }

    /**
     * 浣跨敤鍙傛暟Format鏍煎紡鍖朌ate鎴愬瓧绗︿覆
     */
    public static String format(Date date, String pattern) {
        return date == null ? " " : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 浣跨敤棰勮鏍煎紡灏嗗瓧绗︿覆杞负Date
     */
    public static Date parse(String strDate) throws ParseException {
        return StringUtils.isBlank(strDate) ? null : parse(strDate,
                getDatePattern());
    }

    /**
     * 浣跨敤鍙傛暟Format灏嗗瓧绗︿覆杞负Date
     * @param strDate
     * @param pattern
     * @return 
     * @throws java.text.ParseException
     */
    public static Date parse(String strDate, String pattern)
            throws ParseException {
        return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
                pattern).parse(strDate);
    }

    /**
     * 鍦ㄦ棩鏈熶笂澧炲姞鏁颁釜鏁存湀
     * @param date
     * @param n
     * @return 
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }
    
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }
    
    public static Date addSeconds(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, n);
        return cal.getTime();
    }
    

    public static String getLastDayOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        // 骞�  
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        // 鏈堬紝鍥犱负Calendar閲岀殑鏈堟槸浠庡紑濮嬶紝鎵�浠ヨ-  
        // cal.set(Calendar.MONTH, Integer.parseInt(month) - );  
        // 鏃ワ紝璁句负涓�鍙�  
        cal.set(Calendar.DATE,1);
        // 鏈堜唤鍔犱竴锛屽緱鍒颁笅涓湀鐨勪竴鍙�  
        cal.add(Calendar.MONTH,1);
        // 涓嬩竴涓湀鍑忎竴涓烘湰鏈堟渶鍚庝竴澶�  
        cal.add(Calendar.DATE, -1);
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 鑾峰緱鏈堟湯鏄嚑鍙�  
    }

    public static Date getDate(String year, String month, String day)
            throws ParseException {
        String result = year + "- "  + (month.length() == 1 ? (" " + month) : month) + "- "+ (day.length() == 1 ? (" " + day) : day);
        return parse(result);
    }
}
