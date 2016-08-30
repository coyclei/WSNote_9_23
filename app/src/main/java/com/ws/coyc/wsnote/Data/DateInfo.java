package com.ws.coyc.wsnote.Data;

/**
 * Created by coyc on 16-8-25.
 */

public class DateInfo {
    public int date = 0;
    public int bill_size = 0;

    public DateInfo(int date)
    {
        this.date = date;
    }

    public DateInfo(int date,int size)
    {
        this.date = date;
        this.bill_size = size;
    }

}
