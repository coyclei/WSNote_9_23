package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.Utils.DateUtils;

/**
 * Created by leipe on 2016/8/19.
 */
public class InfoIng extends Info{

    public int all_in_money = 0;
    public String fh_state = FH_STATE_FALSE;
    public String fk_state = FK_STATE_FALSE;
    public static String [] strings;
    public InfoIng()
    {

        initStrings();
    }

    public InfoIng(Person person, String goods)
    {
        initStrings();
        this.person = person;
        this.goods = goods;
    }

    public InfoIng(Cursor cursor)
    {
        initStrings();
        initInfoIngInSQL(cursor);
    }

    private void initStrings()
    {
        strings = new String[8];
        strings[0] = "_id";
        strings[1] = name_id;
        strings[2] = all_info;
        strings[3] = date_start;
        strings[4] = all_in;
        strings[5] = state_fh;
        strings[6] = state_fk;
        strings[7] = src_img;
    }

    @NonNull
    private void initInfoIngInSQL(Cursor cursor) {

        initBaseInfoInSQL(cursor);
        this.all_in_money = cursor.getInt(cursor.getColumnIndex(all_in));
        this.fh_state = cursor.getString(cursor.getColumnIndex(state_fh));
        this.fk_state = cursor.getString(cursor.getColumnIndex(state_fk));
    }

    @NonNull
    public ContentValues getContentValues() {
        l.l("info ing getContentValues");
        ContentValues contentValues = new ContentValues();
        contentValues.put(name_id,person.id);
        contentValues.put(all_info,goods);
        contentValues.put(single_in,"");
        contentValues.put(single_out,"");
        contentValues.put(count,"");
        contentValues.put(all_in, all_in_money);
        contentValues.put(all_out, "");
        contentValues.put(state,STATE_ING);
        contentValues.put(state_fh,fh_state);
        contentValues.put(state_fk,fk_state);
        contentValues.put(date_start, dateStart.getTime());
//        contentValues.put(date_end,dateStart.getTime());
        contentValues.put("date_end","");
        contentValues.put(src_img,image_url);
        contentValues.put(src_video,"");
        contentValues.put(src_audio,"");
        return contentValues;
    }




}
