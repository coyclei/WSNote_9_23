package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;

import com.ws.coyc.wsnote.Data.Table.GoodsTable;
import com.ws.coyc.wsnote.Data.Table.PersonTable;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;

/**
 * Created by coyc on 16-9-10.
 */

public class Goods {

    public long id = -1;
    public String name = "";
    public String info = "";
    public String image_url_local = "";
    public String image_url_net = "";
    public int money_in_temp = 0;
    public int money_out_temp = 0;
    public int count = 0;
    public String more = "";

    public Goods()
    {

    }

    public Goods(String name,String info,int money_in_temp,int money_out_temp)
    {
        this.name = name;
        this.info = info;
        this.money_in_temp = money_in_temp;
        this.money_out_temp = money_out_temp;
    }
    public Goods(Cursor cursor)
    {
        this.id = cursor.getLong(cursor.getColumnIndex("_id"));
        this.name = cursor.getString(cursor.getColumnIndex(GoodsTable.item_name));
        this.info = cursor.getString(cursor.getColumnIndex(GoodsTable.item_info));
        this.image_url_local = cursor.getString(cursor.getColumnIndex(GoodsTable.item_image_url_local));
        this.image_url_net = cursor.getString(cursor.getColumnIndex(GoodsTable.item_image_url_net));
        this.money_in_temp = cursor.getInt(cursor.getColumnIndex(GoodsTable.item_money_in_temp));
        this.money_out_temp = cursor.getInt(cursor.getColumnIndex(GoodsTable.item_money_out_temp));
        this.count = cursor.getInt(cursor.getColumnIndex(GoodsTable.item_count));
        this.more = cursor.getString(cursor.getColumnIndex(GoodsTable.item_more));
    }


    public ContentValues getContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(GoodsTable.item_name,name);
        values.put(GoodsTable.item_info,info);
        values.put(GoodsTable.item_image_url_local,image_url_local);
        values.put(GoodsTable.item_image_url_net,image_url_net);
        values.put(GoodsTable.item_money_in_temp,money_in_temp);
        values.put(GoodsTable.item_money_out_temp,money_out_temp);
        values.put(GoodsTable.item_more,more);
        values.put(GoodsTable.item_count,count);
        return values;
    }

}
