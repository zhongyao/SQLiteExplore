package com.hongri.sqlite.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.hongri.sqlite.bean.ArticleInfo;
import com.hongri.sqlite.bean.PersonInfo;

import static com.hongri.sqlite.dao.DaoConst.ARTICLE_INFO;
import static com.hongri.sqlite.dao.DaoConst.DB_NAME;
import static com.hongri.sqlite.dao.DaoConst.GENDER;
import static com.hongri.sqlite.dao.DaoConst.NAME;
import static com.hongri.sqlite.dao.DaoConst.PERSON_ID;
import static com.hongri.sqlite.dao.DaoConst.TABLE_NAME;
import static com.hongri.sqlite.dao.DaoConst.TABLE_NAME_NEW;
import static com.hongri.sqlite.dao.DaoConst.VERSION;

/**
 * @author zhongyao
 * @date 2019/4/16
 */

public class DaoServiceImpl implements IDaoService {

    private MySQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;
    private String saveSql = "";
    private String saveBlobSql = "";

    public DaoServiceImpl(Context context) {
        initDao(context);
    }

    private void initDao(Context context) {
        mOpenHelper = new MySQLiteOpenHelper(context, DB_NAME, null, VERSION);
    }

    @Override
    public void save(String personId, String name, String gender, DataCallback<Boolean> callback) {
        mDatabase = mOpenHelper.getWritableDatabase();

        try {
            //ContentValues values = new ContentValues();
            //values.put(PERSON_ID, personId);
            //values.put(NAME, name);
            //values.put(GENDER, gender);
            //long result = mDatabase.insert(TABLE_NAME, null, values);
            saveSql = "INSERT INTO " + TABLE_NAME + " (" + PERSON_ID + ", " + NAME + ", " + GENDER + ") " + " values ("
                + "'" + personId + "', " + "'" + name + "', " + "'" + gender + "'" + " )";
            mDatabase.execSQL(saveSql);
            callback.onDataCallback(true);

        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataCallback(false);
        }
    }

    @Override
    public void saveBlob(String personId, ArticleInfo blobInfo, DataCallback<Boolean> callback) {
        boolean ret;
        mDatabase = mOpenHelper.getWritableDatabase();
        String name = "yao";
        String gender = "female";
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(blobInfo);
            outputStream.flush();

            data = byteArrayOutputStream.toByteArray();
            outputStream.close();
            byteArrayOutputStream.close();

            /**
             * 方法一（可行）
             * BEGIN
             */
            ContentValues values = new ContentValues();
            values.put(PERSON_ID, "33");
            values.put(NAME, "yao");
            values.put(GENDER, "female");
            values.put(ARTICLE_INFO, data);
            long result = mDatabase.insert(TABLE_NAME, null, values);
            if (result != -1) {
                ret = true;
            } else {
                ret = false;
            }
            /**
             * END
             */

            /**
             * 方法二（待验证）
             * BEGIN
             */
            saveBlobSql = "INSERT INTO " + TABLE_NAME + " (" + PERSON_ID + ", " + NAME + ", " + GENDER + ", "
                + ARTICLE_INFO
                + ") " + " values ("
                + "'" + personId + "', " + "'" + name + "', " + "'" + gender + "', " + "'" + data + "'" + " )";
            mDatabase.execSQL(saveBlobSql);
            ret = true;
            /**
             * END
             */

            callback.onDataCallback(ret);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onDataCallback(false);
        }

    }

    @Override
    public void query(String personId, DataCallback<PersonInfo> callback) {
        mDatabase = mOpenHelper.getReadableDatabase();

        Cursor cursor = null;
        PersonInfo personInfo = null;
        try {
            cursor = mDatabase.query(TABLE_NAME, new String[] {PERSON_ID, NAME, GENDER}, PERSON_ID + " =?",
                new String[] {personId},
                null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                personInfo = new PersonInfo();
                personInfo.setName(cursor.getString(cursor.getColumnIndex(PERSON_ID)));
                personInfo.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                personInfo.setGender(cursor.getString(cursor.getColumnIndex(GENDER)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callback.onDataCallback(personInfo);
            cursor.close();
        }
    }

    @Override
    public void queryBlob(String personId, DataCallback<ArticleInfo> callback) {
        mDatabase = mOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        ArticleInfo articleInfo = null;
        byte[] data;
        try {
            String querySql = "SELECT * FROM " + TABLE_NAME + " WHERE " + PERSON_ID + " = ?";
            cursor = mDatabase.rawQuery(querySql,
                new String[] {personId});
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                data = cursor.getBlob(cursor.getColumnIndex(ARTICLE_INFO));

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                articleInfo = (ArticleInfo)objectInputStream.readObject();

                byteArrayInputStream.close();
                objectInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callback.onDataCallback(articleInfo);
            cursor.close();
        }
    }

    /**
     * 数据库迁移
     */
    @Override
    public void upgradeDB() {
        //查询数据库中的所有表
        List<String> tables = queryAllTables(mDatabase);
        //
        for (String tableName : tables) {
            if ("android_metadata".equals(tableName) || "sqlite_sequence".equals(tableName)) {
                continue;
            }
            //创建新表
            mOpenHelper.onCreate(mDatabase);
            //旧表数据迁移至新表
            migrateTableData(mDatabase, TABLE_NAME, TABLE_NAME_NEW);
            //删除旧表
            dropOldTable(mDatabase, TABLE_NAME);
        }
    }

    private void dropOldTable(SQLiteDatabase mDatabase, String oldTableName) {
        String sql = "DROP TABLE IF EXISTS " + oldTableName;
        mDatabase.execSQL(sql);
    }

    private void migrateTableData(SQLiteDatabase mDatabase, String oldTableName, String newTableName) {
        String columns = TextUtils.join(",", queryColumns(mDatabase, oldTableName));
        String sql = "INSERT INTO " + newTableName + " (" + columns + ") SELECT " + columns + " FROM " + oldTableName;
        mDatabase.execSQL(sql);
    }

    private String[] queryColumns(SQLiteDatabase mDatabase, String oldTableName) {
        String sql = "SELECT * FROM " + oldTableName;
        Cursor cursor = mDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        cursor.close();
        return columnNames;
    }

    private List<String> queryAllTables(SQLiteDatabase mDatabase) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM " + DB_NAME + " WHERE type='table' ORDER BY name";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;
    }

    @Override
    public void onUnregister() {

    }
}
