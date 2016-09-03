package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.Utils.DateUtils;
import java.util.Date;

/**
 * Created by leipe on 2016/8/19.
 */
public class InfoOver extends Info{

//    public Date end_date= new Date();
    public int all_in_money = 0;
    public int all_out_money = 0;

    public static String [] strings;

    public InfoOver()
    {
        initStrings();
    }

    public InfoOver(Person person, String goods)
    {
        initStrings();
        this.person = person;
        this.goods = goods;
    }

    public InfoOver(Cursor cursor)
    {
        initStrings();
        initInfoOverInSQL(cursor);
    }

    private void initStrings()
    {
        strings = new String[8];
        strings[0] = "_id";
        strings[1] = name_id;
        strings[2] = all_info;
        strings[3] = date_start;
        strings[4] = all_in;
        strings[5] = all_out;
//        strings[6] = date_end;
        strings[7] = src_img;
    }


    @NonNull
    private void initInfoOverInSQL(Cursor cursor) {
        initBaseInfoInSQL(cursor);
//        this.end_date = new Date(cursor.getLong(cursor.getColumnIndex(date_end)));
        this.all_in_money = cursor.getInt(cursor.getColumnIndex(all_in));
        this.all_out_money = cursor.getInt(cursor.getColumnIndex(all_out));
    }




    @NonNull
    public ContentValues getContentValues() {
        l.l("info over getContentValues");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name_id",person.id);
        contentValues.put("all_info",goods);
        contentValues.put("single_in","");
        contentValues.put("single_out","");
        contentValues.put("count","");
        contentValues.put("all_in", all_in_money);
        contentValues.put("all_out", all_out_money);
        contentValues.put("state",STATE_OVER);
        contentValues.put("state_fh","");
        contentValues.put("state_fk","");
        contentValues.put("dateStart", dateStart.getTime());
        contentValues.put("date_end","");
        contentValues.put("src_img",image_url);
        contentValues.put("src_video","");
        contentValues.put("src_audio","");
        return contentValues;
    }

}
