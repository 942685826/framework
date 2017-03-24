/**
 * Copyright (C) 2013 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.yaxon.frameWork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.yaxon.frameWork.debug.LogUtils;

/**
 * ���ݿ���
 *
 * @author guojiaping
 * @version 2015-04-15 ����<br>
 */
public class Database {
    private static String tag = Database.class.getSimpleName();
    private static SQLiteDatabase mSQLiteDatabase = null;
    private static Database mInstance = null;
    private DatabaseHelper mDatabaseHelper = null;


    /**
     * ���캯��
     *
     * @param context
     */
    private Database(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * ��ȡ���ݿ�ʵ��
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
     * ��SQLite���ݿ�,���ݿⲻ�������Զ�����
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
     * �ر����ݿ�
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
         * �ͷ�ҳ�滺��
         */
        SQLiteDatabase.releaseMemory();
    }


}
