package com.nft.common.Utils;

import com.nft.common.Constants;
import scala.collection.parallel.mutable.ParHashSet;

import javax.xml.transform.Source;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author: 戏人看戏
* @Date: 2023/12/28 19:34
* @Description: 用于时间转化的工具类
*/
public class TimeUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 时间戳转date
     */
    public static Date timestampToDate(String ts) throws ParseException {
        long l = Long.parseLong(ts);
        Date date = new Date(l);
        return date;
    }

    /**
     * 获取当前时间工具
     */
    public static Date getCurrent(){
        try {
            String format = DATE_FORMAT.format(new Date());
            Date parse = DATE_FORMAT.parse(format);
            return parse;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public static String Date2Str(Date time){
        try {
            String format = DATE_FORMAT.format(time);
            return format;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}
