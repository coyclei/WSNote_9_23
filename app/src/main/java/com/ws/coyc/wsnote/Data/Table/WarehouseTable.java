package com.ws.coyc.wsnote.Data.Table;

import com.ws.coyc.wsnote.SQLiteHelper.Item;
import com.ws.coyc.wsnote.SQLiteHelper.Table;

/**
 * Created by coyc on 16-9-9.
 */

public class WarehouseTable extends Table{
    public static final String item_goods_id = "goods_id";
    public static final String item_count_now = "item_count_now";
    public static final String item_count_all_history = "item_count_all_history";
    public static final String item_more = "item_more";
    public WarehouseTable()
    {
        init();
    }
    public void init()
    {
        table_name = "warehouse_table";
        keyItem = "_id";

        items.add(new Item(item_goods_id,Item.item_type_text));
        items.add(new Item(item_count_now,Item.item_type_integer));
        items.add(new Item(item_count_all_history,Item.item_type_integer));
        items.add(new Item(item_more,Item.item_type_text));
    }

}
