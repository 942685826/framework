package com.yaxon.frameWork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建和版本管理
 *
 * @author guojiaping 2015-04-15 创建<br>
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    /**
     * 构造函数
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DBTable.DATABASE_NAME, null, DBTable.DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        int num = DBTable.TABLE_BASIC_CREATE.length;
        for (int i = 0; i < num; i++) {
            String dbTableName = DBTable.TABLE_BASIC_CREATE[i];
            db.execSQL(dbTableName);
        }
        num = DBTable.TABLE_WORK_CREATE.length;
        for (int i = 0; i < num; i++) {
            String dbTableName = DBTable.TABLE_WORK_CREATE[i];
            db.execSQL(dbTableName);
        }
        num = DBTable.TABLE_OTHER_CREATE.length;
        for (int i = 0; i < num; i++) {
            String dbTableName = DBTable.TABLE_OTHER_CREATE[i];
            db.execSQL(dbTableName);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}

