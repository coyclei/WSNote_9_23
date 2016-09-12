package com.ws.coyc.wsnote.SQLiteHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by coyc on 16-8-23.
 * before use the SDK you should do SQLiteManager.init();
 * then you can create table by SQLiteManager.crateTable();
 */

public class SQLiteManager {


    /*
    member
     */
    // SQLite
    public SQLiteDatabase db;
    private Context context;
    private DBOpenHelper dbOpenHelper;
    private class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            int size = tables.size();
            l.l("SQL onCreate ");
            for(int i = 0;i<size;i++)
            {
                String sql = getTableCreateSQLString(tables.get(i));
                l.l("SQL onCreate "+sql);
                _db.execSQL(sql);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion,int _newVersion) {
            int size = tables.size();
            for(int i = 0;i<size;i++)
            {
                String sql = getTableCreateSQLString(tables.get(i));
                _db.execSQL(sql);
            }
        }// end for private static class DBOpenHelper extends SQLiteOpenHelper
    }

    // DB_name
    private String DB_NAME;

    // DB_version
    private int DB_VERSION = 2;

    //ArrayList <Table>
    private ArrayList<Table> tables = new ArrayList<>();

    /*
    fun
     */
    //constrat
    public SQLiteManager()
    {

    }

    //init
    public void init(Context context,String DB_name)
    {
        tables.clear();
        this.context = context;
        this.DB_NAME = DB_name;
    }

    private static SQLiteManager instance = null;

    public static SQLiteManager getInstance()
    {
        if(instance == null)
        {
            synchronized (SQLiteManager.class)
            {
                if(instance == null)
                {
                    instance = new SQLiteManager();
                }
            }
        }
        return instance;
    }

    public void updateTable()
    {
        dbOpenHelper.onUpgrade(db,DB_VERSION,DB_VERSION);
    }


    //open
    public void open() {

        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);

        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            exceptionHandler();
        }
        db.beginTransaction();
    }
    /**
     * 数据库文件损坏删除异常处理
     */
    private void exceptionHandler() {
        if(db == null)
        {
            return;
        }
        File file = new File(db.getPath());
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    open();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //close
    public void close() {

        db.setTransactionSuccessful();
        db.endTransaction();
        if (db != null) {
            db.close();
            db = null;
            l.l("close db!= null");
        }else
        {
            l.l("close db = null");
        }
    }




    //create table
    public void registerTable(String table_name, ArrayList<Item> items)
    {
        Table table = new Table(table_name,"_id",items);

        tables.add(table);
    }
    public void registerTable(Table table)
    {
        tables.add(table);
    }

    // get table create sql by items
    private static String getTableCreateSQLString(Table table) {
        String sql = "create table "+table.table_name;

        sql += "("+table.keyItem+" integer primary key autoincrement";

        int size = table.items.size();

        for(int i = 0;i<size;i++)
        {
            sql += ",";
            if(table.items.get(i).type.equals(Item.item_type_integer))
            {
                sql += table.items.get(i).text+" integer not null";
            }else if(table.items.get(i).type.equals(Item.item_type_text))
            {
                sql += table.items.get(i).text+" text not null";
            }else if(table.items.get(i).type.equals(Item.item_type_boolen))
            {
                sql += table.items.get(i).text+" bool not null";
            }else if(table.items.get(i).type.equals(Item.item_type_long))
            {
                sql += table.items.get(i).text+" long not null";
            }
            // TODO: 16-8-23 add other data type



        }
        sql += ");";
        l.l("getTableCreateSQLString"+sql);
        return sql;
    }

    public Table getTableByName(String tableName)
    {
        int size = tables.size();
        if(size == 0)
        {
            return null;
        }else
        {
            for(int i = 0;i<size;i++)
            {
                if(tableName.equalsIgnoreCase(tables.get(i).table_name))
                {
                    return tables.get(i);
                }
            }
        }
        return null;
    }

}
