package com.ws.coyc.wsnote.Data;

import android.content.ContentValues;
import android.database.Cursor;

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

    public static String[] strings1 = {"_id",DataManager.name,DataManager.address1,DataManager.address2,DataManager.phone};


    public Person()
    {
    }

    public Person(Cursor cursor)
    {

        this.address1 = cursor.getString(cursor.getColumnIndex(DataManager.address1));
        this.address2 = cursor.getString(cursor.getColumnIndex(DataManager.address2));
        this.name = cursor.getString(cursor.getColumnIndex(DataManager.name));
        this.phone = cursor.getString(cursor.getColumnIndex(DataManager.phone));
        l.l("********************************************person");
        l.l("address1"+address1);
        l.l("address2"+address2);
        l.l("name"+name);
        l.l("phone"+phone);
    }

    public static void initPersonInSQL(Person person,Cursor cursor)
    {
        person.address1 = cursor.getString(cursor.getColumnIndex(DataManager.address1));
        person.address2 = cursor.getString(cursor.getColumnIndex(DataManager.address2));
        person.name = cursor.getString(cursor.getColumnIndex(DataManager.name));
        person.phone = cursor.getString(cursor.getColumnIndex(DataManager.phone));
    }



    public void insertNoWay()
    {
        update();
    }

    public void insertNoSameName() {

        //check the name is exist. if is exist do update else insert
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).serach(DataManager.name,name);
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
