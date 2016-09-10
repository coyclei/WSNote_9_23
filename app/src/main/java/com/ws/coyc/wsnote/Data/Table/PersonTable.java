package com.ws.coyc.wsnote.Data.Table;

import com.ws.coyc.wsnote.SQLiteHelper.Item;
import com.ws.coyc.wsnote.SQLiteHelper.Table;

/**
 * Created by coyc on 16-9-9.
 */

public class PersonTable extends Table{

    public static final String name = "name";
    public static final String address1 = "address1";
    public static final String address2 = "address2";
    public static final String phone = "phone";
    public static final String src_photo = "src_photo";

    public PersonTable()
    {
        init();
    }
    public void init()
    {
        table_name = "person";
        keyItem = "_id";

        items.add(new Item(name  , Item.item_type_text));
        items.add(new Item(address1  , Item.item_type_text));
        items.add(new Item(address2  , Item.item_type_text));
        items.add(new Item(phone  , Item.item_type_text));
        items.add(new Item(src_photo  , Item.item_type_text));
    }



}
