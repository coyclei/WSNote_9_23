package com.ws.coyc.wsnote.Data.Table;

import android.content.ContentValues;
import android.database.Cursor;

import com.ws.coyc.wsnote.Data.Goods;
import com.ws.coyc.wsnote.Data.Person;
import com.ws.coyc.wsnote.SQLiteHelper.Item;
import com.ws.coyc.wsnote.SQLiteHelper.Table;

import java.util.ArrayList;

/**
 * Created by coyc on 16-9-9.
 */

public class GoodsTable extends Table{

    public static final String item_name = "name";
    public static final String item_info = "info";
    public static final String item_image_url_local = "image_url_local";
    public static final String item_image_url_net = "image_url_net";
    public static final String item_money_in_temp = "money_in_temp";
    public static final String item_money_out_temp = "money_out_temp";
    public static final String item_more = "item_more";
    public static final String item_count = "item_count";

    public GoodsTable()
    {
        init();
    }
    private void init()
    {
        table_name = "goods_table";
        keyItem = "_id";

        items.add(new Item(item_name ,Item.item_type_text));
        items.add(new Item(item_info  ,Item.item_type_text));
        items.add(new Item(item_image_url_local  ,Item.item_type_text));
        items.add(new Item(item_image_url_net  ,Item.item_type_text));
        items.add(new Item(item_money_in_temp  ,Item.item_type_integer));
        items.add(new Item(item_money_out_temp  ,Item.item_type_integer));
        items.add(new Item(item_count  ,Item.item_type_integer));
        items.add(new Item(item_more  ,Item.item_type_text));
    }

    public void insert(Goods goods)
    {
        goods.id = insert(goods.getContentValues());
    }

    public ArrayList<Goods> getAllGoods()
    {
        ArrayList<Goods> goodses = new ArrayList<>();
        Cursor cursor = getAllData();
        cursor.moveToFirst();
        int size_c = cursor.getCount();
        for(int i = 0;i<size_c;i++)
        {
            goodses.add(new Goods(cursor));
            cursor.moveToNext();
        }
        return goodses;
    }

    public void update(Goods goods)
    {
        updateOneByItem("_id",goods.id+"",goods.getContentValues());
    }





}
