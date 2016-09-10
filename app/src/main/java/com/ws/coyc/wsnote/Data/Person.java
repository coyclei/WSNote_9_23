package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;

import com.ws.coyc.wsnote.Data.Table.PersonTable;
import com.ws.coyc.wsnote.SQLiteHelper.SQLiteManager;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;


/**
 * Created by coyc on 16-8-24.
 */

public class Person {

    public long id = -1;
    public String name = "";
    public String address1 = "";
    public String address2 = "";
    public String phone = "";

    ////////////////////////
    public int count = 0;
    public int all_prise_in = 0;
    public int all_prise_out = 0;

    public static String[] strings1 = {"_id", PersonTable.name,PersonTable.address1,PersonTable.address2,PersonTable.phone};


    public Person()
    {

    }

    public Person(Cursor cursor)
    {
        this.id = cursor.getInt(cursor.getColumnIndex("_id"));
        this.address1 = cursor.getString(cursor.getColumnIndex(PersonTable.address1));
        this.address2 = cursor.getString(cursor.getColumnIndex(PersonTable.address2));
        this.name = cursor.getString(cursor.getColumnIndex(PersonTable.name));
        this.phone = cursor.getString(cursor.getColumnIndex(PersonTable.phone));
        l.l("********************************************person");
        l.l("address1"+address1);
        l.l("address2"+address2);

        l.l("name"+name);
        l.l("phone"+phone);
    }

    public static void initPersonInSQL(Person person,Cursor cursor)
    {
        person.address1 = cursor.getString(cursor.getColumnIndex(PersonTable.address1));
        person.address2 = cursor.getString(cursor.getColumnIndex(PersonTable.address2));
        person.name = cursor.getString(cursor.getColumnIndex(PersonTable.name));
        person.phone = cursor.getString(cursor.getColumnIndex(PersonTable.phone));
    }



    public void insertNoWay()
    {
        update();
    }

    public void insertNoSameName() {

        //check the name is exist. if is exist do update else insert
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).serach(PersonTable.name,name);
        if(cursor.getCount() <= 0)
        {
            insertAnyway();
        }else
        {

            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            update();
        }
    }


    public void insertAnyway() {
        if(id == -1)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",name);
            contentValues.put("address1",address1);
            contentValues.put("address2",address2);

            contentValues.put("phone",phone);
            contentValues.put("src_photo","");

            id =  SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).insert(contentValues);
        }else
        {
            update();
        }
    }

    public void update() {


        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("address1",address1);
        contentValues.put("phone",phone);
        contentValues.put("src_photo","");
        SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).updateOneByItem("_id",id+"",contentValues);

    }

}
