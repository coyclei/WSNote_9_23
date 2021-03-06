package com.ws.coyc.wsnote.Data;

import android.content.Context;
import android.database.Cursor;
import com.ws.coyc.wsnote.Data.Table.BillTable;
import com.ws.coyc.wsnote.Data.Table.GoodsTable;
import com.ws.coyc.wsnote.Data.Table.PersonTable;
import com.ws.coyc.wsnote.Data.Table.SaleTable;
import com.ws.coyc.wsnote.Data.Table.WarehouseTable;
import com.ws.coyc.wsnote.SQLiteHelper.SQLiteManager;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.Utils.ComparatorPerson;
import com.ws.coyc.wsnote.Utils.DateUtils;
import java.util.ArrayList;
import java.util.Collections;
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

    public  ArrayList<BillInfoWait> infos_wait = new ArrayList<>();
    public  ArrayList<BillInfoIng> infos_ing = new ArrayList<>();
    public  ArrayList<BillInfoOver> infos_over = new ArrayList<>();

    public ArrayList<Person> persons = new ArrayList<>();
    public ArrayList<Goods> goodses = new ArrayList<>();

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

    private DataManager() {}

    // all data init
    public void init(Context context)
    {
        initSQL(context);

        //initData
        toCurrentDate();

        //initPerson
//        initPersonInfo();

        //initGoods
        goodses = goodsTable.getAllGoods();

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
        String[] strings = {BillTable.all_in,BillTable.all_out};
        for(int i = 0;i<size;i++)
        {
            Cursor cursor_bill =  SQLiteManager.getInstance().getTableByName(TABLE_BILL_NAME).serach(BillTable.name_id,strings,persons.get(i).id+"");
            cursor_bill.moveToFirst();
            int size_bill = cursor_bill.getCount();
            persons.get(i).count = size_bill;
            for(int k = 0;k<size_bill;k++)
            {
                int all_in = cursor_bill.getInt(cursor_bill.getColumnIndex(BillTable.all_in));
                int all_out = cursor_bill.getInt(cursor_bill.getColumnIndex(BillTable.all_out));
                persons.get(i).all_prise_in+=all_in;
                persons.get(i).all_prise_out+=all_out;
                cursor_bill.moveToNext();
            }
            cursor_bill.close();
        }
        ComparatorPerson comparatorPerson = new ComparatorPerson();
        Collections.sort(persons,comparatorPerson);
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
        startDate = DateUtils.getBeforNDay(date,7);
        endDate = DateUtils.getEndofZheDay(date);
        refreshInfoByDate(startDate,endDate);
    }

    public BillTable billTable;
    public PersonTable personTable;
    public GoodsTable goodsTable;
    public SaleTable saleTable;
    public WarehouseTable warehouseTable;
    private void initSQL(Context context) {
        //init table
        billTable = new BillTable();
        personTable = new PersonTable();
        goodsTable = new GoodsTable();
        saleTable = new SaleTable();
        warehouseTable = new WarehouseTable();

        //init SQL
        SQLiteManager.getInstance().init(context,DB_NAME);

        SQLiteManager.getInstance().registerTable(billTable);
        SQLiteManager.getInstance().registerTable(personTable);
        SQLiteManager.getInstance().registerTable(goodsTable);
        SQLiteManager.getInstance().registerTable(saleTable);
        SQLiteManager.getInstance().registerTable(warehouseTable);

        SQLiteManager.getInstance().open();//when the app close you should to close the SQL
//        SQLiteManager.getInstance().updateTable();
    }

    public void initInfo_wait()
    {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serach(BillTable.state, BillInfoWait.strings, BillInfo.STATE_WAIT);
        infos_wait = ConvertToWaitList(cursor);

        initPerson(infos_wait);
    }

    public void initInfo_ing()
    {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serach(BillTable.state, BillInfoIng.strings, BillInfo.STATE_ING);
        infos_ing = ConvertToIngList(cursor);

        initPerson(infos_ing);
    }

    public void initInfo_over()
    {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serach(BillTable.state, BillInfoOver.strings, BillInfo.STATE_OVER);
        infos_over = ConvertToOverList(cursor);
        initPerson(infos_over);
    }

    private void initPerson(ArrayList<? extends BillInfo> infos) {
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
                .serachBetweenAB_int(BillTable.date_start,startDate.getTime(),endDate.getTime(),BillTable.state, BillInfo.STATE_OVER);

        infos_over = ConvertToOverList(cursor);
        initPerson(infos_over);
    }

    private void refreshIngInfoByDate() {
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
                .serachBetweenAB_int(BillTable.date_start,startDate.getTime(),endDate.getTime(),BillTable.state, BillInfo.STATE_ING);

        l.l("refreshIngInfoByDate cursor size "+cursor.getCount());

        infos_ing = ConvertToIngList(cursor);

        initPerson(infos_ing);
    }

    private void refreshWaitInfoByDate() {

        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME)
                .serachBetweenAB_int(BillTable.date_start,startDate.getTime(),endDate.getTime(),BillTable.state, BillInfo.STATE_WAIT);
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


    private ArrayList<BillInfoWait> ConvertToWaitList(Cursor cursor) {
        ArrayList<BillInfoWait> msgs = new ArrayList<>();
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return msgs;
        }

        for (int i = 0; i < resultCounts; i++) {
            BillInfoWait temp = new BillInfoWait(cursor);

            msgs.add(temp);

            cursor.moveToNext();
        }
        return msgs;
    }



    private ArrayList<BillInfoIng> ConvertToIngList(Cursor cursor) {
        ArrayList<BillInfoIng> msgs = new ArrayList<>();
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return msgs;
        }

        for (int i = 0; i < resultCounts; i++) {
            BillInfoIng temp = new BillInfoIng(cursor);

            msgs.add(temp);

            cursor.moveToNext();
        }
        return msgs;
    }



    private ArrayList<BillInfoOver> ConvertToOverList(Cursor cursor) {
        ArrayList<BillInfoOver> msgs = new ArrayList<>();
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return msgs;
        }

        for (int i = 0; i < resultCounts; i++) {
            BillInfoOver temp = new BillInfoOver(cursor);

            msgs.add(temp);

            cursor.moveToNext();
        }
        return msgs;
    }



    public void addWaitInfo(BillInfoWait info)
    {
        infos_wait.add(info);
        info.insertInSQL();

    }

    public void addIngInfo(BillInfoIng info)
    {
        infos_ing.add(info);
        info.insertInSQL();
    }

    public void addOverInfo(BillInfoOver info)
    {
        infos_over.add(info);
        info.insertInSQL();
    }

    // add one new person
    public void addWaitInfoAnyWay(BillInfoWait info)
    {
        infos_wait.add(info);
        info.insertInSQLAnyWay();
    }

    public void addIngInfoAnyWay(BillInfoIng info)
    {
        infos_ing.add(info);
        info.insertInSQLAnyWay();
    }

    public void addOverInfoAnyWay(BillInfoOver info)
    {
        infos_over.add(info);
        info.insertInSQLAnyWay();
    }



    public void removeWaitInfo(BillInfoWait info)
    {
        infos_wait.remove(info);
        info.deleteInSQL();
    }

    public void removeIngInfo(BillInfoIng info)
    {
        infos_ing.remove(info);
        info.deleteInSQL();
    }

    public void removeOverInfo(BillInfoOver info)
    {
        infos_over.remove(info);
        info.deleteInSQL();
    }


    public void findPersonByKeyWord(final String key, final OnFindPerson onFindPerson)
    {

        ArrayList<Person> persons = new ArrayList<>();
        Cursor cursor = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_PERSON_NAME).mhSerach(PersonTable.name,Person.strings1,key);
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
            Cursor cursor_bill =  SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serachWithDate_Bill(BillTable.name_id,BillTable.bill_strings,name_id,startDate.getTime(),endDate.getTime());
            cursor_bill.moveToFirst();
            int size_bill = cursor_bill.getCount();
            for(int k = 0;k<size_bill;k++)
            {
                String state = cursor_bill.getString(cursor_bill.getColumnIndex(BillTable.state));

                if(state.equalsIgnoreCase(BillInfo.STATE_WAIT))
                {
                    infos_wait.add(new BillInfoWait(cursor_bill));
                }else if(state.equalsIgnoreCase(BillInfo.STATE_ING))
                {
                    infos_ing.add(new BillInfoIng(cursor_bill));
                }else if(state.equalsIgnoreCase(BillInfo.STATE_OVER))
                {
                    infos_over.add(new BillInfoOver(cursor_bill));
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
        Cursor cursor_bill = SQLiteManager.getInstance().getTableByName(DataManager.TABLE_BILL_NAME).serachBillByAnyKeyWithDate_Bill(BillTable.bill_strings,key,startDate.getTime(),endDate.getTime());
        cursor_bill.moveToFirst();
        int size_bill = cursor_bill.getCount();
        for(int i = 0;i<size_bill;i++)
        {
            String state = cursor_bill.getString(cursor_bill.getColumnIndex(BillTable.state));

            if(state.equalsIgnoreCase(BillInfo.STATE_WAIT))
            {
                infos_wait.add(new BillInfoWait(cursor_bill));
            }else if(state.equalsIgnoreCase(BillInfo.STATE_ING))
            {
                infos_ing.add(new BillInfoIng(cursor_bill));
            }else if(state.equalsIgnoreCase(BillInfo.STATE_OVER))
            {
                infos_over.add(new BillInfoOver(cursor_bill));
            }
            cursor_bill.moveToNext();
        }
        cursor_bill.close();
        initPerson(infos_wait);
        initPerson(infos_ing);
        initPerson(infos_over);

        onFindBill.onfind();
    }

    public void addGoods(Goods goods)
    {
        goodses.add(goods);
        goodsTable.insert(goods);
    }
    public void updateGoods(Goods goods)
    {
        goodsTable.update(goods);
    }

}
