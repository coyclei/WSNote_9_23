package com.ws.coyc.wsnote.SQLiteHelper;

/**
 * Created by coyc on 16-8-23.
 */

public class Item {


    public static final String item_type_integer = "item_type_integer";
    public static final String item_type_text = "item_type_text";
    public static final String item_type_long = "item_type_long";
    public static final String item_type_boolen = "item_type_boolen";

    public String text = "";
    public String type = "";

    public Item(String text,String type)
    {
        this.text = text;
        this.type = type;
    }
    public Item()
    {

    }
}
