package com.hongri.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import static com.hongri.sqlite.dao.DaoConst.ARTICLE_INFO;
import static com.hongri.sqlite.dao.DaoConst.GENDER;
import static com.hongri.sqlite.dao.DaoConst.NAME;
import static com.hongri.sqlite.dao.DaoConst.PERSON_ID;
import static com.hongri.sqlite.dao.DaoConst.TABLE_NAME;
import static com.hongri.sqlite.dao.DaoConst.TABLE_NAME_NEW;

/**
 * @author zhongyao
 * @date 2019/4/16
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private String sql = "CREATE TABLE if not exists " + TABLE_NAME + "(_id integer primary key autoincrement,"
        + PERSON_ID + " text," + NAME + " text," + GENDER + " text," + ARTICLE_INFO + " BLOB" + ")";

    private String sqlNew = "CREATE TABLE if not exists " + TABLE_NAME_NEW + "(_id integer primary key autoincrement,"
        + PERSON_ID + " text," + NAME + " text," + GENDER + " text," + ARTICLE_INFO + " BLOB" + ")";

    //private String SQL_TEMP_TABLE_CREATE = "ALERT TABLE " + TABLE_NAME + "RENAME TO " + TABLE_NAME_TEMP;

    public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(sql);
        db.execSQL(sqlNew);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //使用for循环是为了支持跨版本升级的情况
        for (int i = oldVersion; i <= newVersion; i++) {
            switch (i) {
                case 2:
                    //db.execSQL(SQL_TEMP_TABLE_CREATE);
                    break;
                case 3:

                    break;
                default:
                    break;
            }
        }
    }
}
