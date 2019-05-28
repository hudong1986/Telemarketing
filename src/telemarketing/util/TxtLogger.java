/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telemarketing.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author HCD
 */
public class TxtLogger {

    private static String RootDirString = "D:/TelemarketingLog";

    static {
    	try{
    		 if(!new File("D:/").exists()){
    			 RootDirString = "E:/TelemarketingLog";
    		 }
    		
    		 String os_name = SystemUtils.OS_NAME.toLowerCase();
    		 if(os_name!=null && !os_name.contains("windows"))
    		 {
    			 
    			 RootDirString="/usr/TelemarketingLog/";
    		 }
    	}
    	catch(Exception ex){
    		
    	}
    }
    
    public static void SetRootDir(String rootString) {
        TxtLogger.RootDirString += rootString;
    }

    public static void log(Throwable ex, LogFileCreateType logFileCreateType, String customDir) {
         StringBuffer sb= new StringBuffer();
         for(StackTraceElement element : ex.getStackTrace()){
             sb.append(element.toString()+"\r\n"); 
         } 
       
        TxtLogger.log(ex.getMessage()+" "+sb.toString(), LogTye.ERROR, logFileCreateType, customDir);
    }

    /**
     * 添加文本日志
     *
     * @param log
     * @param logTye
     * @param logFileCreateType
     * @param customDir
     */
    synchronized public static void log(String log, LogTye logTye, LogFileCreateType logFileCreateType, String customDir) {
        try {
            String path = RootDirString;
            String filePath = "";
            if (customDir == null || customDir.length() <= 0) {
            } else {
                path += "/" + customDir;
            }

            path += "/" + logTye.name();
            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int date = c.get(Calendar.DATE);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);
            if (logFileCreateType == LogFileCreateType.OneFileEveryDay) {
                path += "/" + year + "/" + month;
                filePath = path + "/" + date + ".log";
            } else if (logFileCreateType == LogFileCreateType.OneFileAnHour) {
                path += "/" + year + "/" + month + "/" + date;
                filePath = path + "/" + hour + ".log";
            } else if (logFileCreateType == LogFileCreateType.OneFilePerTenMinutes) {
                path += "/" + year + "/" + month + "/" + date + "/" + hour;
                filePath = path + "/" + WhichTenMinutes(minute) + ".log";
            }

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            AppendFile.method1(filePath, dateFormat.format(now) + ":" + log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String WhichTenMinutes(int minute) {
        return String.valueOf(minute / 10 + 1);
    }

    public enum LogTye {
        Debug, INFO, ERROR;
    }

    public enum LogFileCreateType {
        OneFileEveryDay,
        OneFileAnHour,
        OneFilePerTenMinutes,
    }
}
