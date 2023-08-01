package com.wyf.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtil {


    //格式化为
    public static DateTimeFormatter DTF_TO_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatTimestamp(long millis,DateTimeFormatter dtf){

        if(millis>0){
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.of("+8"));
            return dateTime.format(dtf);
        }else{
          throw new RuntimeException("时间戳不得为负数");
        }
    }

    public static void main(String[] args) {
        String s = DateUtil.formatTimestamp(System.currentTimeMillis(), DTF_TO_TIME);
        System.out.println(s);
    }

}
