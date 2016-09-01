package com.ws.coyc.wsnote.Data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.ws.coyc.wsnote.SQLiteHelper.Item;
import com.ws.coyc.wsnote.SQLiteHelper.SQLiteManager;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by coyc on 16-8-24.
 */
public class DataManager {

    public static final String DB_NAME = "wshelper.db";
    public static final String TABLE_PERSON_NAME = "person";
    public static final String TABLE_BILL_NAME = "bill";

    public Date startDate;
    public Date endDate;


    public  ArrayList<InfoWait> infos_wait = new ArrayList<>();
    public  ArrayList<InfoIng> infos_ing = new ArrayList<>();
    public  ArrayList<InfoOver> infos_over = new ArrayList<>();

    public ArrayList<Person> persons = new ArrayList<>();

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {

        if(ourInstance == null)
        {
            synchronized (DataManager.class)
            {
                if(ourInstance == null)
                {
                    ourInstance = new DataManager();
                }
            }
        }
        return ourInstance;
    }

    private DataManager() {
    }

    // all data init
    public void init(Context context)
    {
        initSQL(context);

        //initData
        toCurrentDate();

        //initPerson
//        initPersonInfo();

    }

    public void initPersonInfo() {
        persons.clear();
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).getDataWithItems(Person.strings1);
        cursor.moveToFirst();
        int size_c = cursor.getCount();
        for(int i = 0;i<size_c;i++)
        {
            persons.add(new Person(cursor));
            cursor.moveToNext();
        }


        int size = persons.size();
        String[] strings = {Info.all_in,Info.all_out};
        for(int i = 0;i<size;i++)
        {
            Cursor cursor_bill =  SQLiteManager.getInstance().getTableByName(TABLE_BILL_NAME).serach(Info.name_id,strings,persons.get(i).id+"");
            cursor_bill.moveToFirst();
            int size_bill = cursor_bill.getCount();
            persons.get(i).count = size_bill;
            for(int k = 0;k<size_bill;k++)
            {
                int all_in = cursor_bill.getInt(cursor_bill.getColumnIndex(Info.all_in));
                int all_out = cursor_bill.getInt(cursor_bill.getColumnIndex(Info.all_out));
                persons.get(i).all_prise_in+=all_in;
                persons.get(i).all_prise_out+=all_out;
                cursor_bill.moveToNext();
            }
            cursor_bill.close();
        }
    }

    public int getPersonSize()
    {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).getDataWithItems(Person.strings1);
        int size = cursor.getCount();
        cursor.close();
        return size;
    }

    public void toCurrentDate() {
        Date date = new Date();
        startDate = DateUtils.getBefor30Start(date);
        endDate = DateUtils.getNextDay(date);
        refreshInfoByDate(startDate,endDate);
    }

    private void initSQL(Context context) {
        //init SQL
        SQLiteManager.getInstance().init(context,DB_NAME);

        ArrayList<Item> items_bill = initBillTableItems();
        SQLiteManager.getInstance().registerTable(TABLE_BILL_NAME,items_bill);

        ArrayList<Item> items_person = initPersonTableItems();
        SQLiteManager.getInstance().registerTable(TABLE_PERSON_NAME,items_person);

        SQLiteManager.getInstance().open();//when the app close you should to close the SQL
    }

    public void initInfo_wait()
    {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serach(Info.state,InfoWait.strings,Info.STATE_WAIT);
        infos_wait = ConvertToWaitList(cursor);

        initPerson(infos_wait);
    }

    public void initInfo_ing()
    {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serach(Info.state,InfoIng.strings,Info.STATE_ING);
        infos_ing = ConvertToIngList(cursor);

        initPerson(infos_ing);
    }

    public void initInfo_over()
    {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serach(Info.state,InfoOver.strings,Info.STATE_OVER);
        infos_over = ConvertToOverList(cursor);
        initPerson(infos_over);
    }

    private void initPerson(ArrayList<? extends Info> infos) {
        int size = infos.size();

        for(int i = 0;i<size;i++)
        {
            l.l("init person"+infos.get(0).getClass().getName());
            initPersonById(infos.get(i).person);
        }
    }



    public void refreshInfoByDate(Date startDate ,Date endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        refreshWaitInfoByDate();
        refreshIngInfoByDate();
        refreshOverInfoByDate();
    }

    public void refreshInfoByDate()
    {
        refreshWaitInfoByDate();
        refreshIngInfoByDate();
        refreshOverInfoByDate();
    }

    private void refreshOverInfoByDate() {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
                .serachBetweenAB_int(Info.date_start,startDate.getTime(),endDate.getTime(),Info.state,Info.STATE_OVER);

        infos_over = ConvertToOverList(cursor);
        initPerson(infos_over);
    }

    private void refreshIngInfoByDate() {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
                .serachBetweenAB_int(Info.date_start,startDate.getTime(),endDate.getTime(),Info.state,Info.STATE_ING);

        l.l("refreshIngInfoByDate cursor size "+cursor.getCount());

        infos_ing = ConvertToIngList(cursor);

        initPerson(infos_ing);
    }

    private void refreshWaitInfoByDate() {

        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
                .serachBetweenAB_int(Info.date_start,startDate.getTime(),endDate.getTime(),Info.state,Info.STATE_WAIT);
        infos_wait = ConvertToWaitList(cursor);
        initPerson(infos_wait);
    }

    private void initPersonById(Person person) {


        Cursor cursor_p =  SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).serach("_id",Person.strings1,person.id+"");
        if(cursor_p.getCount() <=0)
        {
            return;
        }
        cursor_p.moveToFirst();
        Person.initPersonInSQL(person,cursor_p);

    }


    private ArrayList<InfoWait> ConvertToWaitList(Cursor cursor) {
        ArrayList<InfoWait> msgs = new ArrayList<>();
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return msgs;
        }

        for (int i = 0; i < resultCounts; i++) {
            InfoWait temp = new InfoWait(cursor);

            msgs.add(temp);

            cursor.moveToNext();
        }
        return msgs;
    }



    private ArrayList<InfoIng> ConvertToIngList(Cursor cursor) {
        ArrayList<InfoIng> msgs = new ArrayList<>();
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return msgs;
        }

        for (int i = 0; i < resultCounts; i++) {
            InfoIng temp = new InfoIng(cursor);

            msgs.add(temp);

            cursor.moveToNext();
        }
        return msgs;
    }



    private ArrayList<InfoOver> ConvertToOverList(Cursor cursor) {
        ArrayList<InfoOver> msgs = new ArrayList<>();
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return msgs;
        }

        for (int i = 0; i < resultCounts; i++) {
            InfoOver temp = new InfoOver(cursor);

            msgs.add(temp);

            cursor.moveToNext();
        }
        return msgs;
    }



    public void addWaitInfo(InfoWait info)
    {
        infos_wait.add(info);
        info.insertInSQL();

    }

    public void addIngInfo(InfoIng info)
    {
        infos_ing.add(info);
        info.insertInSQL();
    }

    public void addOverInfo(InfoOver info)
    {
        infos_over.add(info);
        info.insertInSQL();
    }

    // add one new person
    public void addWaitInfoAnyWay(InfoWait info)
    {
        infos_wait.add(info);
        info.insertInSQLAnyWay();
    }

    public void addIngInfoAnyWay(InfoIng info)
    {
        infos_ing.add(info);
        info.insertInSQLAnyWay();
    }

    public void addOverInfoAnyWay(InfoOver info)
    {
        infos_over.add(info);
        info.insertInSQLAnyWay();
    }



    public void removeWaitInfo(InfoWait info)
    {
        infos_wait.remove(info);
        info.deleteInSQL();
    }

    public void removeIngInfo(InfoIng info)
    {
        infos_ing.remove(info);
        info.deleteInSQL();
    }

    public void removeOverInfo(InfoOver info)
    {
        infos_over.remove(info);
        info.deleteInSQL();
    }






    public static final String name = "name";
    public static final String address1 = "address1";
    public static final String address2 = "address2";
    public static final String phone = "phone";
    public static final String src_photo = "src_photo";
    @NonNull
    private ArrayList<Item> initPersonTableItems() {
        ArrayList<Item> items_person = new ArrayList<>();

        items_person.add(new Item(name  , Item.item_type_text));
        items_person.add(new Item(address1  , Item.item_type_text));
        items_person.add(new Item(address2  , Item.item_type_text));
        items_person.add(new Item(phone  , Item.item_type_text));
        items_person.add(new Item(src_photo  , Item.item_type_text));

        return items_person;
    }



    @NonNull
    private ArrayList<Item> initBillTableItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(Info.name_id,Item.item_type_integer));
        items.add(new Item(Info.all_info,Item.item_type_text));
        items.add(new Item(Info.single_in,Item.item_type_integer));
        items.add(new Item(Info.single_out,Item.item_type_integer));
        items.add(new Item(Info.count,Item.item_type_integer));
        items.add(new Item(Info.all_in,Item.item_type_integer));
        items.add(new Item(Info.all_out,Item.item_type_integer));
        items.add(new Item(Info.state,Item.item_type_text));
        items.add(new Item(Info.state_fh,Item.item_type_text));
        items.add(new Item(Info.state_fk,Item.item_type_text));
        items.add(new Item(Info.date_start,Item.item_type_long));
//        items.add(new Item(Info.date_end,Item.item_type_long));
        items.add(new Item(Info.src_img,Item.item_type_text));
        items.add(new Item(Info.src_video,Item.item_type_text));
        items.add(new Item(Info.src_audio,Item.item_type_text));
        return items;
    }


    public void findPersonByKeyWord(final String key, final OnFindPerson onFindPerson)
    {

        ArrayList<Person> persons = new ArrayList<>();
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).mhSerach(DataManager.name,Person.strings1,key);
        if(cursor == null)
        {
            l.l("findPersonByKeyWord  cursor == null");
            onFindPerson.onfind(persons);
            return;
        }
        cursor.moveToFirst();
        int size = cursor.getCount();
        for(int i = 0;i<size;i++)
        {
            persons.add(new Person(cursor));
            cursor.moveToNext();
        }
        onFindPerson.onfind(persons);
    }

    public void findBillByKeyWord(String key,OnFindBill onFindBill)
    {
//        Cursor cursor_wait = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
//                .mhSerachBetweenAB_int_state(Info.date_start,startDate.getTime(),endDate.getTime(),Info.bill_strings,key,Info.state,Info.STATE_WAIT);
//        infos_wait = ConvertToWaitList(cursor_wait);
//        initPerson(infos_wait);
//
//        Cursor cursor_ing = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
//                .mhSerachBetweenAB_int_state(Info.date_start,startDate.getTime(),endDate.getTime(),Info.bill_strings,key,Info.state,Info.STATE_ING);
//        infos_ing = ConvertToIngList(cursor_ing);
//        initPerson(infos_ing);
//
//
//        Cursor cursor_over = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
//                .mhSerachBetweenAB_int_state(Info.date_start,startDate.getTime(),endDate.getTime(),Info.bill_strings,key,Info.state,Info.STATE_OVER);
//        infos_over = ConvertToOverList(cursor_over);
//        initPerson(infos_over);





        infos_wait.clear();
        infos_over.clear();
        infos_ing.clear();
        /**
         * find in person
         */
        Cursor cursor_person =  SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).serachPersonByAnyKey(Person.strings1,key);
        /**
         * find in bill with person id
         */
        cursor_person.moveToFirst();
        int size_person = cursor_person.getCount();
        l.l("findBillByKeyWord cursor_person size "+size_person);
        for(int i = 0;i<size_person;i++)
        {
            String name_id = cursor_person.getString(cursor_person.getColumnIndex("_id"));
            Cursor cursor_bill =  SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serachWithDate_Bill(Info.name_id,Info.bill_strings,name_id,startDate.getTime(),endDate.getTime());
            cursor_bill.moveToFirst();
            int size_bill = cursor_bill.getCount();
            for(int k = 0;k<size_bill;k++)
            {
                String state = cursor_bill.getString(cursor_bill.getColumnIndex(Info.state));

                if(state.equalsIgnoreCase(Info.STATE_WAIT))
                {
                    infos_wait.add(new InfoWait(cursor_bill));
                }else if(state.equalsIgnoreCase(Info.STATE_ING))
                {
                    infos_ing.add(new InfoIng(cursor_bill));
                }else if(state.equalsIgnoreCase(Info.STATE_OVER))
                {
                    infos_over.add(new InfoOver(cursor_bill));
                }
                cursor_bill.moveToNext();
            }

            cursor_bill.close();
            cursor_person.moveToNext();
        }
        cursor_person.close();
        /**
         * find in bill
         */
        Cursor cursor_bill = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serachBillByAnyKeyWithDate_Bill(Info.bill_strings,key,startDate.getTime(),endDate.getTime());
        cursor_bill.moveToFirst();
        int size_bill = cursor_bill.getCount();
        for(int i = 0;i<size_bill;i++)
        {
            String state = cursor_bill.getString(cursor_bill.getColumnIndex(Info.state));

            if(state.equalsIgnoreCase(Info.STATE_WAIT))
            {
                infos_wait.add(new InfoWait(cursor_bill));
            }else if(state.equalsIgnoreCase(Info.STATE_ING))
            {
                infos_ing.add(new InfoIng(cursor_bill));
            }else if(state.equalsIgnoreCase(Info.STATE_OVER))
            {
                infos_over.add(new InfoOver(cursor_bill));
            }
            cursor_bill.moveToNext();
        }
        cursor_bill.close();
        initPerson(infos_wait);
        initPerson(infos_ing);
        initPerson(infos_over);

        onFindBill.onfind();
    }







}
