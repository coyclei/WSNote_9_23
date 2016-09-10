package com.ws.coyc.wsnote.Data.Table;

import com.ws.coyc.wsnote.SQLiteHelper.Item;
import com.ws.coyc.wsnote.SQLiteHelper.Table;

/**
 * Created by coyc on 16-9-9.
 */

public class SaleTable extends Table{
    public static final String item_goods_id = "goods_id";
    public static final String item_money_in_final = "item_money_in_final";
    public static final String item_money_out_final = "item_count_now";
    public static final String item_more = "item_more";
    public static final String item_sale_date = "item_sale_date";

    public SaleTable()
    {
        init();
    }
    public void init()
    {
        table_name = "sale_table";
        keyItem = "_id";

        items.add(new Item(item_goods_id,Item.item_type_text));
        items.add(new Item(item_money_in_final,Item.item_type_integer));
        items.add(new Item(item_money_out_final,Item.item_type_integer));
        items.add(new Item(item_sale_date,Item.item_type_text));
        items.add(new Item(item_more,Item.item_type_text));
    }

    

}
