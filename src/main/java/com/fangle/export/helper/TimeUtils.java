package com.fangle.export.helper;/*
 * @Author      : Nick
 * @Description :
 * @Date        : Create in 14:10 2018/6/13
 **/

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeUtils {
    /**
     *
     * @param ts
     * @return
     */
    public static int getTimeStamp(String ts) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (ts != null && !ts.equals("")) {
                long time = format.parse(ts).getTime() / 1000;
                return (int) time;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
