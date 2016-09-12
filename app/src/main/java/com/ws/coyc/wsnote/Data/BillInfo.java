package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;

import com.ws.coyc.wsnote.Data.Table.BillTable;
import com.ws.coyc.wsnote.SQLiteHelper.SQLiteManager;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by coyc on 16-8-24.
 */

public class BillInfo {


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
    public ArrayList<Goods> goodses = new ArrayList<>();


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
        this.dateStart = new Date(cursor.getLong(cursor.getColumnIndex(BillTable.date_start)));
        this.person.id =  cursor.getInt(cursor.getColumnIndex(BillTable.name_id));
        this.goods = cursor.getString(cursor.getColumnIndex(BillTable.all_info));
        this.image_url = cursor.getString(cursor.getColumnIndex(BillTable.src_img));
        l.l("************************************************************");
        l.l("initBaseInfoInSQL dateStart "+dateStart);
        l.l("initBaseInfoInSQL person.id "+person.id);
        l.l("initBaseInfoInSQL goods "+goods);
        l.l("initBaseInfoInSQL image_path "+image_url);
    }

    public ContentValues getContentValues() {

        return null;
    }

}
