package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;

import com.ws.coyc.wsnote.SQLiteHelper.SQLiteManager;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;

import java.util.Date;

/**
 * Created by coyc on 16-8-24.
 */

public class Info {


    public static final String name_id = "name_id";
    public static final String all_info = "all_info";
    public static final String single_in = "single_in";
    public static final String single_out = "single_out";
    public static final String count = "count";
    public static final String all_in = "all_in";
    public static final String all_out = "all_out";
    public static final String state = "state";
    public static final String state_fh = "state_fh";
    public static final String state_fk = "state_fk";
    public static final String date_start = "dateStart";
//    public static final String date_end = "date_end";
    public static final String src_img = "src_img";
    public static final String src_video = "src_video";
    public static final String src_audio = "src_audio";

    public static final String FH_STATE_FALSE = "未发货";
    public static final String FH_STATE_TRUE = "已发货";


    public static final String FK_STATE_FALSE = "未付款";
    public static final String FK_STATE_TRUE = "已付款";

    public static final String STATE_WAIT = "STATE_WAIT";
    public static final String STATE_ING = "STATE_ING";
    public static final String STATE_OVER = "STATE_OVER";

    public Person person = new Person();
    public long id = -1;
    public Date dateStart = new Date();
    public String goods = "";
    public boolean isChoose = false;
    public String image_url = "";


    public static String[] bill_strings = {"_id",name_id,all_info,single_in,single_out,
            count,all_in,all_out,state,state_fh,state_fk,date_start,src_img,src_video,src_audio};

//    public static String[] bill_strings_serach = {"_id",name_id,all_info, all_in,all_out,state_fh,state_fk};


    public void deleteInSQL()
    {
        SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).deleteOneByItem("_id",id+"");
    }

    public void deleteChoose()
    {
        if(isChoose)
        {
            deleteInSQL();
        }
    }

    public void insertInSQL()
    {
        if(id==-1)
        {
            person.insertNoWay();
            ContentValues contentValues = getContentValues();
            id = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).insert(contentValues);

        }else
        {
            update();
        }
    }

    public void insertInSQLAnyWay()
    {
        if(id==-1)
        {
            person.insertNoSameName();
            ContentValues contentValues = getContentValues();
            id = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).insert(contentValues);

        }else
        {
            update();
        }
    }

    public void update()
    {
        person.update();

        ContentValues contentValues = getContentValues();
        SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).updateOneByItem("_id",id+"",contentValues);
    }

    public void initBaseInfoInSQL(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex("_id"));
        this.dateStart = new Date(cursor.getLong(cursor.getColumnIndex(date_start)));
        this.person.id =  cursor.getInt(cursor.getColumnIndex(name_id));
        this.goods = cursor.getString(cursor.getColumnIndex(all_info));
        this.image_url = cursor.getString(cursor.getColumnIndex(src_img));
        l.l("************************************************************");
        l.l("initBaseInfoInSQL dateStart "+dateStart);
        l.l("initBaseInfoInSQL person.id "+person.id);
        l.l("initBaseInfoInSQL goods "+goods);
        l.l("initBaseInfoInSQL image_url "+image_url);
    }

    public ContentValues getContentValues() {



        return null;
    }

}
