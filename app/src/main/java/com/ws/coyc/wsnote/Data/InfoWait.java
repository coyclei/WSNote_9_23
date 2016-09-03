package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.Utils.DateUtils;

/**
 * Created by leipe on 2016/8/19.
 */
public class InfoWait extends Info {

    public static String [] strings;

    public InfoWait()
    {
        initString();
    }

    public InfoWait(Person person, String goods)
    {
        initString();
        this.person = person;
        this.goods = goods;
    }

    public InfoWait(Cursor cursor)
    {
        initString();
        initInfoWaitInSQL(cursor);
    }

    private void initString()
    {
        strings = new String[5];
        strings[0] = "_id";
        strings[1] = name_id;
        strings[2] = all_info;
        strings[3] = date_start;
        strings[4] = src_img;
    }

    @NonNull
    private void initInfoWaitInSQL(Cursor cursor) {
        initBaseInfoInSQL(cursor);
    }

    @NonNull
    public ContentValues getContentValues() {
        l.l("info wait getContentValues");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name_id", person.id);
        contentValues.put("all_info", goods);
        contentValues.put("single_in", "");
        contentValues.put("single_out", "");
        contentValues.put("count", "");
        contentValues.put("all_in", "");
        contentValues.put("all_out", "");
        contentValues.put("state", STATE_WAIT);
        contentValues.put("state_fh", "");
        contentValues.put("state_fk", "");
        contentValues.put("dateStart", dateStart.getTime());
//        contentValues.put("date_end", dateStart.getTime());
        contentValues.put("date_end","");
        l.l("image_url....................."+image_url);
        contentValues.put("src_img", image_url);
        contentValues.put("src_video", "");
        contentValues.put("src_audio", "");
        contentValues.put("date_end", "");
        return contentValues;
    }

}
