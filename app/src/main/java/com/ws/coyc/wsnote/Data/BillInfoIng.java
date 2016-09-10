package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.ws.coyc.wsnote.Data.Table.BillTable;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;

/**
 * Created by leipe on 2016/8/19.
 */
public class BillInfoIng extends BillInfo {

    public int all_in_money = 0;
    public String fh_state = FH_STATE_FALSE;
    public String fk_state = FK_STATE_FALSE;
    public static String [] strings;
    public BillInfoIng()
    {

        initStrings();
    }

    public BillInfoIng(Person person, String goods)
    {
        initStrings();
        this.person = person;
        this.goods = goods;
    }

    public BillInfoIng(Cursor cursor)
    {
        initStrings();
        initInfoIngInSQL(cursor);
    }

    private void initStrings()
    {
        strings = new String[8];
        strings[0] = "_id";
        strings[1] = BillTable.name_id;
        strings[2] = BillTable.all_info;
        strings[3] = BillTable.date_start;
        strings[4] = BillTable.all_in;
        strings[5] = BillTable.state_fh;
        strings[6] = BillTable.state_fk;
        strings[7] = BillTable.src_img;
    }

    @NonNull
    private void initInfoIngInSQL(Cursor cursor) {

        initBaseInfoInSQL(cursor);
        this.all_in_money = cursor.getInt(cursor.getColumnIndex(BillTable.all_in));
        this.fh_state = cursor.getString(cursor.getColumnIndex(BillTable.state_fh));
        this.fk_state = cursor.getString(cursor.getColumnIndex(BillTable.state_fk));
    }

    @NonNull
    public ContentValues getContentValues() {
        l.l("info ing getContentValues");
        ContentValues contentValues = new ContentValues();
        contentValues.put(BillTable.name_id,person.id);
        contentValues.put(BillTable.all_info,goods);
        contentValues.put(BillTable.single_in,"");
        contentValues.put(BillTable.single_out,"");
        contentValues.put(BillTable.count,"");
        contentValues.put(BillTable.all_in, all_in_money);
        contentValues.put(BillTable.all_out, "");
        contentValues.put(BillTable.state,STATE_ING);
        contentValues.put(BillTable.state_fh,fh_state);
        contentValues.put(BillTable.state_fk,fk_state);
        contentValues.put(BillTable.date_start, dateStart.getTime());
        contentValues.put(BillTable.date_end,"");
        contentValues.put(BillTable.src_img,image_url);
        contentValues.put(BillTable.sale_ids,"");
        contentValues.put(BillTable.src_audio,"");
        return contentValues;
    }




}
