package com.ws.coyc.wsnote.Utils;

import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jin 2014�?5�?28�? 上午9:34:31
 */

public class DateUtils {
    /**
     * 获取当前时间--如：2012-11-06 12:12:10
     */
    public static String getCurrentDate() {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }

    /**
     * 获取指定格式的GPS时间
     *
     * @param date
     * @return
     */
    public static String getGPSDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 返回时间对象;<br/>
     * format为时间格式如("yyyy/MM/dd")<br/>
     * 返回null表示出错�?
     */
    public static Date getDate(String time, String format) {
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 毫秒换成几天前几小时几分�?
     */
    public static String periodToString(Long millisecond) {
        String str = "";
        long day = millisecond / 86400000;
        long hour = (millisecond % 86400000) / 3600000;
        long minute = (millisecond % 86400000 % 3600000) / 60000;
        if (day > 0) {
            str = String.valueOf(day) + "天前";
        }
        if (hour > 0) {
            str += String.valueOf(hour) + "小时";
        }
        if (minute > 0) {
            str += String.valueOf(minute) + "分钟";
        }
        return str;
    }

    /**
     * 计算几天�?;<br/>
     * 传入�?始时�?(比如"2012/11/06对应format�?"yyyy/MM/dd";<br/>
     * 如果返回-1表示格式错误
     */
    public static int getIntervalDays(String beginTime, String format) {
        int dayNum = 0;
        try {
            Date start = getDate(beginTime, format);
            Date now = new Date();
            long res = now.getTime() - start.getTime();
            dayNum = (int) (((res / 1000) / 3600) / 24);
            System.out.println(dayNum + "天前");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return dayNum;
    }

    /**
     * 计算几天�?;<br/>
     * 传入�?始时�?(格式�?2012-11-06 12:12:10)<br/>
     * 如果返回-1表示格式错误
     */
    public static int getIntervalDays(String beginTime) {
        return getIntervalDays(beginTime, "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 返回当前日期xxxx年x月xx�? 星期x
     *
     * @return
     */
    public static String getDate() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = {"星期�?", "星期�?", "星期�?", "星期�?", "星期�?",
                "星期�?", "星期�?"};
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        String mDate = c.get(Calendar.YEAR) + "�?" + c.get(Calendar.MONTH)
                + "�?" + c.get(Calendar.DATE) + "�?  " + weekDays[w];
        return mDate;
    }

    public static int getYear(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.YEAR);
    }

    public static int getMouth(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.MONTH)+1;
    }

    public static int getDay(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.DAY_OF_MONTH)-1;
    }
    public static int getCurrentYear()
    {
        Date data = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(data);

        return c.get(Calendar.YEAR);
    }
    public static int getCurrentMouth()
    {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.MONTH);
    }

    public static int CountDays(int m,int y) {
        if(m==1||m==3||m==5||m==7||m==8||m==10||m==12)
        {
            return 31;
        }else
        {
            if(m ==2)
            {
                if(y%4 == 0)
                {
                    return 29;
                }else
                {
                    return 28;
                }
            }else
            {
                return 30;
            }
        }
    }



    public static Date getCurrentMouthStart()
    {
        Date date = new Date();

        return ConverToDate_S(getYear(date)+"-"+getMouth(date)+"-01 00:00:00");
    }

    public static Date getCurrentMouthEnd()
    {
        Date date = new Date();
        int y = getYear(date);
        int m = getMouth(date);
        l.l("......year .."+y);
        l.l("......mouth .."+m);
        return ConverToDate_S(y+"-"+m+"-"+CountDays(m,y)+" 23:59:59");
    }

    public static Date getMouthStart(Date date)
    {
        String a = getYear(date)+"-"+getMouth(date)+"-01 00:00:00";
        System.out.println("getMouthStart  "+a);
        return ConverToDate_S(a);
    }

    public static Date getMouthEnd(Date date)
    {
        int y = getYear(date);
        int m = getMouth(date);
        System.out.println("......year .."+y);
        System.out.println("......mouth .."+m);
        String a = y+"-"+m+"-"+CountDays(m,y)+" 23:59:59";
        System.out.println("getMouthEnd  "+a);
//        l.l(");
//        l.l("......mouth .."+m);
        return ConverToDate_S(a);
    }

    public static String getWeek(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四",
                "星期五", "星期六"};
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 返回当前x月xx�? 星期x
     *
     * @return
     */
    public static String getDateNew() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = {"星期�?", "星期�?", "星期�?", "星期�?", "星期�?",
                "星期�?", "星期�?"};
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        String mDate = c.get(Calendar.MONTH) + 1 + "�?" + c.get(Calendar.DATE)
                + "�?  ";
        return mDate;
    }

    /**
     * 返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 日期相加
     *
     * @param date 日期
     * @param day  天数
     * @return 返回相加后的日期
     */
    public static Date addDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期相减
     *
     * @param date  日期
     * @param date1 日期
     * @return 返回相减后的日期
     */
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 计算当前是什么星�? <br/>
     * 返回�?"天蝎�?"
     */
    public static String xingzuo(Date s) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(s);
        String xingzuo = "�?";
        int day = cal.get(Calendar.DAY_OF_YEAR);
        if ((cal.get(Calendar.YEAR) % 4 == 0)
                && (cal.get(Calendar.YEAR) % 100 != 0)
                || (cal.get(Calendar.YEAR) % 400 == 0)) {
            if ((day >= 1 && day <= 19) || (day >= 357 && day <= 366)) {
                xingzuo = "魔蝎�?";
            } else if (day >= 20 && day <= 49) {
                xingzuo = "水瓶�?";
            } else if (day >= 50 && day <= 80) {
                xingzuo = "双鱼�?";
            } else if (day >= 81 && day <= 110) {
                xingzuo = "白羊�?";
            } else if (day >= 111 && day <= 141) {
                xingzuo = "金牛�?";
            } else if (day >= 142 && day <= 173) {
                xingzuo = "双子�?";
            } else if (day >= 174 && day <= 203) {
                xingzuo = "巨蟹�?";
            } else if (day >= 204 && day <= 235) {
                xingzuo = "狮子�?";
            } else if (day >= 236 && day <= 266) {
                xingzuo = "处女�?";
            } else if (day >= 267 && day <= 296) {
                xingzuo = "天秤�?";
            } else if (day >= 297 && day <= 326) {
                xingzuo = "天蝎�?";
            } else if (day >= 327 && day <= 356) {
                xingzuo = "射手�?";
            }
        } else {
            if ((day >= 1 && day <= 19) || (day >= 357 && day <= 366)) {
                xingzuo = "魔蝎�?";
            } else if (day >= 20 && day <= 48) {
                xingzuo = "水瓶�?";
            } else if (day >= 49 && day <= 79) {
                xingzuo = "双鱼�?";
            } else if (day >= 80 && day <= 109) {
                xingzuo = "白羊�?";
            } else if (day >= 110 && day <= 140) {
                xingzuo = "金牛�?";
            } else if (day >= 141 && day <= 172) {
                xingzuo = "双子�?";
            } else if (day >= 173 && day <= 202) {
                xingzuo = "巨蟹�?";
            } else if (day >= 203 && day <= 234) {
                xingzuo = "狮子�?";
            } else if (day >= 235 && day <= 265) {
                xingzuo = "处女�?";
            } else if (day >= 266 && day <= 295) {
                xingzuo = "天秤�?";
            } else if (day >= 296 && day <= 325) {
                xingzuo = "天蝎�?";
            } else if (day >= 326 && day <= 355) {
                xingzuo = "射手�?";
            }
        }
        return xingzuo;
    }

    public static Date ConverToDate_S(String strDate) {
        Date result = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            result = (Date) df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new Date();
        }
        return result;
    }

    /**
     * ���ʱ�׼��ʽ
     *
     * @param date
     * @return
     */
    public static String ConverToString_S(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(date);
    }


    /**
     * yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    public static String ConverToString_YMDHM(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return df.format(date);
    }


    public static Date ConverToDate_YMDHM(String strDate) {
        Date result = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            result = (Date) df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new Date();
        }
        return result;
    }


    public static String ConverToString_YMDH(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");

        return df.format(date);
    }

    public static String ConverToString_MDHM(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("M-d HH:mm");

        return df.format(date);
    }

    public static String ConverToString_YMD(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }


    public static Date ConverToDateYMD(String strDate) {
        Date result = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            result = (Date) df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new Date();
        }
        return result;
    }


    public static Date ConverToDateHM(String strDate) {
        Date result = null;
        DateFormat df = new SimpleDateFormat("HH:mm");
        try {
            result = (Date) df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new Date();
        }
        return result;
    }

    /**
     * HH:MM
     *
     * @param date
     * @return
     */
    public static String ConverToString_HM(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("HH:mm");

        return df.format(date);
    }

    public static String ConverToString_YMD2(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        return df.format(date);
    }

    /**
     * MM��dd��
     *
     * @param date
     * @return
     */
    public static String ConverToString_MD(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("MM月dd号");

        return df.format(date);
    }

    public static String ConverToString_MD2(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("M/d");

        return df.format(date);
    }

}
