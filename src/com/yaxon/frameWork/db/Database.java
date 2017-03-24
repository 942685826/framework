/**
 * Copyright (C) 2013 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.yaxon.frameWork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.yaxon.frameWork.debug.LogUtils;

/**
 * 数据库类
 *
 * @author guojiaping
 * @version 2015-04-15 创建<br>
 */
public class Database {
    private static String tag = Database.class.getSimpleName();
    private static SQLiteDatabase mSQLiteDatabase = null;
    private static Database mInstance = null;
    private DatabaseHelper mDatabaseHelper = null;


    /**
     * 构造函数
     *
     * @param context
     */
    private Database(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * 获取数据库实例
     *
     * @return
     */
    public static Database getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Database(context);
        }
        return mInstance;
    }

    /**
     * 打开SQLite数据库,数据库不存在则自动创建
     *
     * @return
     */
    public SQLiteDatabase openSQLiteDatabase() {
        if (mSQLiteDatabase == null || !mSQLiteDatabase.isOpen()) {
            LogUtils.v(tag, "open SQLiteDatabase");
            try {
                mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
                //mSQLiteDatabase=mDatabaseHelper.openOrCreateDatabase("Test",MODE_PRIVATE,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mSQLiteDatabase;
    }

    /**
     * 关闭数据库
     */
    public void closeSQLiteDatabase() {
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
            mDatabaseHelper = null;
        }

        /**
         * 释放页面缓存
         */
        SQLiteDatabase.releaseMemory();
    }


}
