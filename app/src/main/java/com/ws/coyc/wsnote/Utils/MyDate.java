package com.ws.coyc.wsnote.Utils;

import java.util.Date;

/**
 * Created by coyc on 16-9-1.
 */

public class MyDate {

    public Date date;
    public String day;

    public MyDate()
    {

    }
    public MyDate(String s)
    {
        day = s;
    }
    public MyDate(Date date,String s)
    {
        this.date = date;
        day = s;
    }
}
