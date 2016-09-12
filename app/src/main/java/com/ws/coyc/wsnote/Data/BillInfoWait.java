package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.ws.coyc.wsnote.Data.Table.BillTable;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;

/**
 * Created by leipe on 2016/8/19.
 */
public class BillInfoWait extends BillInfo {

    public static String [] strings;

    public BillInfoWait()
    {
        initString();
    }

    public BillInfoWait(Person person, String goods)
    {
        initString();
        this.person = person;
        this.goods = goods;
    }

    public BillInfoWait(Cursor cursor)
    {
        initString();
        initInfoWaitInSQL(cursor);
    }

    private void initString()
    {
        strings = new String[5];
        strings[0] = "_id";
        strings[1] = BillTable.name_id;
        strings[2] = BillTable.all_info;
        strings[3] = BillTable.date_start;
        strings[4] = BillTable.src_img;
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
        l.l("image_path....................."+image_url);
        contentValues.put("src_img", image_url);
        contentValues.put("sale_ids", "");
        contentValues.put("src_audio", "");
        contentValues.put("date_end", "");
        return contentValues;
    }

}
