package com.ws.coyc.wsnote.Data.Table;

import com.ws.coyc.wsnote.SQLiteHelper.Item;
import com.ws.coyc.wsnote.SQLiteHelper.Table;

/**
 * Created by coyc on 16-9-9.
 */

public class BillTable extends Table{

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
    public static final String date_end = "date_end";
    public static final String src_img = "src_img";
    public static final String sale_ids = "src_video";
    public static final String src_audio = "src_audio";

    public static String[] bill_strings = {"_id",name_id,all_info,single_in,single_out,
            count,all_in,all_out,state,state_fh,state_fk,date_start,src_img, sale_ids,src_audio,date_end};


    public BillTable()
    {
        init();
    }
    public void init()
    {
        table_name = "bill";
        keyItem = "_id";

        items.add(new Item(name_id,Item.item_type_integer));
        items.add(new Item(all_info,Item.item_type_text));
        items.add(new Item(single_in,Item.item_type_integer));
        items.add(new Item(single_out,Item.item_type_integer));
        items.add(new Item(count,Item.item_type_integer));
        items.add(new Item(all_in,Item.item_type_integer));
        items.add(new Item(all_out,Item.item_type_integer));
        items.add(new Item(state,Item.item_type_text));
        items.add(new Item(state_fh,Item.item_type_text));
        items.add(new Item(state_fk,Item.item_type_text));
        items.add(new Item(date_start,Item.item_type_long));
        items.add(new Item(date_end,Item.item_type_long));
        items.add(new Item(src_img,Item.item_type_text));
        items.add(new Item(sale_ids,Item.item_type_text));
        items.add(new Item(src_audio,Item.item_type_text));
    }

}
