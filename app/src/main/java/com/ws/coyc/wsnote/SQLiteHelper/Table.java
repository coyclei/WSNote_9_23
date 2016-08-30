package com.ws.coyc.wsnote.SQLiteHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by coyc on 16-8-23.
 */

public class Table {
    /*
    member
     */
    //table name
    public String table_name;

    //table strings[]
    public ArrayList<Item> items = new ArrayList<>();

    //key
    public String keyItem;

    //db

    public Table(String table_name,String keyItem)
    {
        this.keyItem = keyItem;
        this.table_name = table_name;
    }

    public Table(String table_name,String keyItem,ArrayList<Item> items)
    {
        this.keyItem = keyItem;
        this.table_name = table_name;
        this.items = items;
    }

    /*
    fun
     */

    //create table

    //delete table

    //inserte item
    public long insert(ContentValues cv)
    {
        return SQLiteManager.getInstance().db.insert(table_name,null,cv);
    }

    //delete item
    public long deleteAllItem()
    {
        return SQLiteManager.getInstance().db.delete(table_name,null,null);
    }
    //
    public long deleteOneByItem(String item,String content)
    {
        String[] args = {String.valueOf(content)};
        return SQLiteManager.getInstance().db.delete(table_name,item+" =?",args);
    }

    //update item
    public long updateOneByItem(String item,String content,ContentValues contentData)
    {
        String[] args = {String.valueOf(content)};
        return SQLiteManager.getInstance().db.update(table_name,contentData, item+" =? ",args);
    }

    //serach by ..
    public Cursor getAllData()
    {
        int size = items.size();
        String[] strings = getStrings(size);
        return SQLiteManager.getInstance().db.query(table_name,strings,null,null,null,null,null);
    }
    public Cursor getDataWithItems(String [] items)
    {
        return SQLiteManager.getInstance().db.query(table_name,items,null,null,null,null,null);
    }


    public Cursor serach(String item,String content)
    {
        String[] coms = {String.valueOf(item),keyItem};
        String[] args = {String.valueOf(content)};

        return SQLiteManager.getInstance().db.query(table_name,coms,item+" =? ",args,null,null,null);
    }
    public Cursor serach(String item,String[] items,String content)
    {
        String[] args = {String.valueOf(content)};

        return SQLiteManager.getInstance().db.query(table_name,items,item+" =? ",args,null,null,null);
    }

    public Cursor serachWithDate_Bill(String item,String[] items,String content,long start,long end)
    {
        String[] args = {String.valueOf(content)};

        return SQLiteManager.getInstance().db.query(table_name,items,item+" =? ",args,null,null,null);
//        return SQLiteManager.getInstance().db.query(table_name,items,item+"=? and dateStart < "+start+" and dateStart > "+end,args,null,null,null);
    }


    public Cursor mhSerach(String item,String content)
    {
        String[] coms = {String.valueOf(item),keyItem};

        return SQLiteManager.getInstance().db.query(table_name, coms, item+"  LIKE ? ",new String[] { "%" + content + "%" }, null, null, null);
    }
    public Cursor mhSerach(String item,String[] coms,String content)
    {

        return SQLiteManager.getInstance().db.query(table_name, coms,item+"  LIKE ? ",new String[] { "%" + content + "%" }, null, null, null);
//        return SQLiteManager.getInstance().db.query(table_name, coms," phone = 18274833970 and "+ item+"  LIKE ? ",new String[] { "%" + content + "%" }, null, null, null);
//        return SQLiteManager.getInstance().db.query(table_name, coms," phone =? and "+ item+"  LIKE ? ",new String[] { "%" + content + "%" }, null, null, null);
//        return SQLiteManager.getInstance().db.query(table_name, coms," phone LIKE ? or address1 LIKE ? or "+item+"  LIKE ? ",new String[] { "%" + content + "%","%" + content + "%","%" + content + "%" }, null, null, null);
    }


    public Cursor serachPersonByAnyKey(String[] coms,String content)
    {
        return SQLiteManager.getInstance().db.query(table_name, coms," phone LIKE ? or address1 LIKE ? or name LIKE ? ",new String[] { "%" + content + "%","%" + content + "%","%" + content + "%" }, null, null, null);
    }

    public Cursor serachBillByAnyKey_Bill(String[] coms,String content)
    {
        return SQLiteManager.getInstance().db.query(table_name, coms," all_info LIKE ? or single_in LIKE ? or name LIKE ? ",new String[] { "%" + content + "%","%" + content + "%","%" + content + "%" }, null, null, null);
    }

    public Cursor serachBillByAnyKeyWithDate_Bill(String[] coms,String content,long start ,long end)
    {
        return SQLiteManager.getInstance().db.query(table_name, coms," all_info LIKE ? or state_fh LIKE ? or state_fk LIKE ? and dateStart < "+start+" and dateStart > "+end,new String[] { "%" + content + "%","%" + content + "%","%" + content + "%" }, null, null, null);
    }





    public Cursor mhSerach(String item,String content,String lim)
    {
        String[] coms = {String.valueOf(item),keyItem};

        return SQLiteManager.getInstance().db.query(table_name, coms, item+"  LIKE ? ",new String[] { "%" + content + "%" }, null, null, null,lim);
    }

    //you can only do it by id
    public Cursor serachWithOffset(int offset, int maxResult, String item, String content)
    {
        String[] coms = {String.valueOf(item),keyItem};
        return SQLiteManager.getInstance().db.rawQuery("select * from "+ table_name+ " order by "+item+" asc limit ?,?", new String[]{String.valueOf(offset), String.valueOf(maxResult)});
    }

    public Cursor serachBetweenAB_int(String item, int start, int end)
    {
        String[] coms = {item};
        return SQLiteManager.getInstance().db.rawQuery("select * from "+table_name+" where "+item+" > "+start+" and "+item +" < "+end,null);
    }
    public Cursor serachBetweenAB_int(String item, int start, int end,String withItem,String wItemContent)
    {
        String[] coms = {wItemContent};

        return SQLiteManager.getInstance().db.rawQuery("select * from "+table_name+" where "+item+" > "+start+" and "+item +" < "+end+" and "+withItem+" =? ",coms);
    }

    public Cursor serachBetweenAB_int(String item, long start, long end,String withItem,String wItemContent)
    {
        String[] coms = {wItemContent};

        return SQLiteManager.getInstance().db.rawQuery("select * from "+table_name+" where "+item+" > "+start+" and "+item +" < "+end+" and "+withItem+" =? ",coms);
    }

    public Cursor mhSerachBetweenAB_int_state(String item_int, long start, long end,String[] withItem,String wItemContent,String item_state,String state)
    {
        String[] conts = {String.valueOf(wItemContent)};
        String[] states = {String.valueOf(state),String.valueOf(wItemContent)};

//        return SQLiteManager.getInstance().db.rawQuery("select * from "+table_name+" where "+item_state +" =? "+state+" and "+item_int+" > "+start+" and "+item_int +" < "+end+" and "+withItem+" LIKE ? ",conts);
        return SQLiteManager.getInstance().db.rawQuery("select * from "+table_name+" where "+item_state +" =? "+" and "+item_int+" > "+start+" and "+item_int +" < "+end+" and "+"all_info LIKE ? ",states);

    }




    @NonNull
    private String[] getStrings(int size) {
        String[] strings = new String[size+1];
        strings[0] = keyItem;//do not forget it
        for(int i = 0;i<size;i++)
        {
            strings[i+1] = items.get(i).text;
        }
        return strings;
    }
}
