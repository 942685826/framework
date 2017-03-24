package com.yaxon.frameWork.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 数据库操作工具类
 *
 * @author guojiaping 2015-04-15 创建<br>
 */
public class DBUtils {
    private static DBUtils mInstance = null;
    private LinkedList<DBDataChangedListener> mUsedLists = new LinkedList<DBDataChangedListener>();
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * 数据库构造 行数
     */
    private DBUtils(Context context) {
        super();
        // TODO Auto-generated constructor stub
        mSQLiteDatabase = Database.getInstance(context).openSQLiteDatabase();
    }

    /**
     * 获取数据库操作实例
     *
     * @return
     */
    public static DBUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBUtils(context);
        }
        return mInstance;
    }

    /**
     * 开始事务 用于多条数据插入前调用 目前区域、门店、商品三条协议使用
     */
    public void beginTransaction() {
        mSQLiteDatabase.beginTransaction();
    }

    /**
     * 结束事务 用于多条插入语句结束后 调用 此时一起提交数据
     */
    public void endTransaction() {
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    /**
     * 删除数据库
     *
     * @param databaseName : 数据库名
     * @return
     */
    public void deleteDataBase(String databaseName) {
        this.deleteDataBase(databaseName);
    }

    /**
     * 删除一个数据库表
     *
     * @param tableName : 表名
     * @return
     */
    public void deleteTable(String tableName) {
        mSQLiteDatabase.execSQL("DROP TABLE " + tableName);
    }

    /**
     * 清空一个数据库表内容
     *
     * @param tableName ：表名
     * @return
     */
    public void clearTable(String tableName) {
        mSQLiteDatabase.execSQL("delete from  " + tableName);
    }

    /**
     * 更新数据
     *
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public void updateTable(String table, ContentValues cv, String whereClause, String[] whereArgs) {
        mSQLiteDatabase.update(table, cv, whereClause, whereArgs);
    }

    /**
     * 根据整型数值匹配更新表
     *
     * @param table     :数据库表名
     * @param cv        :内容
     * @param condition :条件
     * @param value     :整型数值
     */
    public void updateTable(String table, ContentValues cv, String condition, Integer value) {
        mSQLiteDatabase.update(table, cv, condition + "= ?", new String[]{Integer.toString(value)});
    }

    /**
     * 根据整型数值匹配更新表
     *
     * @param table     :数据库表名
     * @param cv        :内容
     * @param condition :条件
     * @param value     :字符串
     */
    public int updateTable(String table, ContentValues cv, String condition, String value) {
        return mSQLiteDatabase.update(table, cv, condition + " = ?", new String[]{value});
    }

    /**
     * 根据整型和字符串 数值匹配更新表
     *
     * @param table      :数据库表名
     * @param cv         :内容
     * @param condition  :条件
     * @param value      :整型数值
     * @param condition2 :条件2
     * @param value2     :字符串
     */
    public void updateTable(String table, ContentValues cv,
                            String condition, Integer value, String condition2, String value2) {
        mSQLiteDatabase.update(table, cv, condition + " = ? " + " and " + condition2 + " = ?",
                new String[]{Integer.toString(value), value2});
    }

    /**
     * 根据两个字符串 数值匹配更新表
     *
     * @param table      :数据库表名
     * @param cv         :内容
     * @param condition  :条件
     * @param value      :整型数值
     * @param condition2 :条件2
     * @param value2     :字符串
     */
    public void updateTable(String table, ContentValues cv,
                            String condition, String value, String condition2, String value2) {
        mSQLiteDatabase.update(table, cv, condition + " = ? " + " and " + condition2 + " = ?",
                new String[]{value, value2});
    }

    /**
     * 根据两个整型数值匹配更新表
     *
     * @param table      :数据库表名
     * @param cv         :内容
     * @param value      :整型数值
     * @param condition2 :条件2
     * @param value2     :整型数值
     */
    public void updateTable(String table, ContentValues cv,
                            String condition1, Integer value, String condition2, Integer value2) {
        mSQLiteDatabase.update(table, cv, condition1 + " = ?" + " and "
                + condition2 + " = ?", new String[]{Integer.toString(value), Integer.toString(value2)});

    }

    /**
     * 根据三个整型数值匹配更新表
     *
     * @param table      :数据库表名
     * @param cv         :内容
     * @param condition1 :条件1
     * @param value1     :整型数值1
     * @param condition2 :条件2
     * @param value2     :整型数值2
     * @param condition3 :条件3
     * @param value3     :整型数值3
     */
    public void updateTable(String table, ContentValues cv,
                            String condition1, Integer value1, String condition2, Integer value2, String condition3, Integer value3) {
        mSQLiteDatabase.update(table, cv,
                condition1 + " = ? " + " and "
                        + condition2 + " = ? " + " and "
                        + condition3 + " = ?",
                new String[]{Integer.toString(value1), Integer.toString(value2), Integer.toString(value3)});
    }

    /**
     * 根据三个整型数值匹配更新表
     *
     * @param table      :数据库表名
     * @param cv         :内容
     * @param condition1 :条件1
     * @param value1     :字符串1
     * @param condition2 :条件2
     * @param value2     :整型数值2
     * @param condition3 :条件3
     * @param value3     :整型数值3
     */
    public void updateTable(String table, ContentValues cv,
                            String condition1, String value1, String condition2, Integer value2, String condition3, Integer value3) {
        mSQLiteDatabase.update(table, cv,
                condition1 + " = ? " + " and "
                        + condition2 + " = ? " + " and "
                        + condition3 + " = ?",
                new String[]{value1, Integer.toString(value2), Integer.toString(value3)});
    }

    /**
     * 根据三个整型数值匹配更新表
     *
     * @param table      :数据库表名
     * @param cv         :内容
     * @param condition1 :条件1
     * @param value1     :字符串1
     * @param condition2 :条件2
     * @param value2     :字符串2
     * @param condition3 :条件3
     * @param value3     :整型数值3
     */
    public void updateTable(String table, ContentValues cv,
                            String condition1, String value1, String condition2, String value2, String condition3, Integer value3) {
        mSQLiteDatabase.update(table, cv,
                condition1 + " = ? " + " and "
                        + condition2 + " = ? " + " and "
                        + condition3 + " = ?",
                new String[]{value1, value2, Integer.toString(value3)});
    }

    /**
     * 根据条件字符串匹配删除表中的数据
     *
     * @param table     表名
     * @param condition 条件字符串
     */
    public void DeleteData(String table, String condition) {
        // mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " +
        // condition);
        mSQLiteDatabase.delete(table, condition, null);
    }

    /**
     * 删除数据
     *
     * @param table       表名
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public void delete(String table, String whereClause, String[] whereArgs) {

        mSQLiteDatabase.delete(table, whereClause, whereArgs);
    }

    /**
     * 根据整型数值匹配删除表中某项
     *
     * @param table     表名
     * @param condition 列名
     * @param value     数值
     * @return
     */
    public void DeleteDataByCondition(String table,
                                      String condition, Integer value) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + "="
                + Integer.toString(value));
    }

    /**
     * 根据整型数值匹配删除表中某项
     *
     * @param table      表名
     * @param condition  列名
     * @param value      数值
     * @param condition2 列名
     * @param value2     数值
     * @return
     */
    public void DeleteDataBy2Condition(String table,
                                       String condition, Integer value, String condition2, Integer value2) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = "
                + Integer.toString(value) + " and " + condition2 + " = " + value2);
    }

    /**
     * 根据整型数值匹配删除表中某项
     *
     * @param table      表名
     * @param condition  列名
     * @param value      数值
     * @param condition2 列名
     * @param value2     数值
     * @param condition3 列名
     * @param value3     字符串
     * @return
     */
    public void DeleteDataBy3Condition(String table,
                                       String condition, Integer value, String condition2, Integer value2, String condition3, String value3) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = "
                + Integer.toString(value) + " and " + condition2 + " = " + value2
                + " and " + condition3 + "= '" + value3 + "'");
    }

    /**
     * 从表中删除不符合条件（字符串）的数据
     *
     * @param table     表名
     * @param condition 列名
     * @param str       字符串
     * @return
     */
    public void DeleteDataByNotLikeCondition(String table,
                                             String condition, String str) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " not like \'%"
                + str + "%\'");
    }

    /**
     * 根据整型数组匹配删除表中项,只要数字在该数组范围内即删除
     *
     * @param table     表名
     * @param condition 列名
     * @param scale     整形数组
     * @return
     */
    public void DeleteDataByCondition(String table,
                                      String condition, int[] scale) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " in ("
                + Arrays.toString(scale).substring(1, Arrays.toString(scale).length() - 1) + ")");
    }

    /**
     * 根据整型数值匹配删除表中某项 大于某个数值即删除
     *
     * @param table     表名
     * @param condition 列名
     * @param value     数值
     * @return
     */
    public void DeleteDataByBiggerCondition(String table,
                                            String condition, Integer value) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + ">"

                + Integer.toString(value));
    }

    /**
     * 根据整型数值匹配删除表中某项 大于某个数值即删除
     *
     * @param table     表名
     * @param condition 列名
     * @param value     数值
     * @return
     */
    public void DeleteDataBySmallerCondition(String table,
                                             String condition, Integer value) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + "<"

                + Integer.toString(value));
    }

    /**
     * 根据字符串匹配删除表中项
     *
     * @param table     表名
     * @param condition 列名
     * @param value     字符串
     * @return
     */
    public void DeleteDataByStr(String table, String condition,
                                String value) {
        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = ?",
                new String[]{value});

    }

    /**
     * 根据整型和字符串匹配删除表中项
     *
     * @param table     表名
     * @param condition 列名
     * @param value     字符串
     * @return
     */
    public void DeleteDataByCondition(String table, String condition1, int value1, String condition,
                                      String value) {
        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = ?" + " and " + condition1 + " = ?",
                new String[]{value, Integer.toString(value1)});

    }

    /**
     * 向表中添加一条数据
     *
     * @param cv    数据内容集合
     * @param table 表名
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long AddData(ContentValues cv, String table) {
        if (mSQLiteDatabase == null) {
            return -1;
        }
        long result = mSQLiteDatabase.insert(table, null, cv);
        if (result > 0) {
            for (DBDataChangedListener lis : mUsedLists) {
                if (lis.mIsRunning) {
                    lis.onEvent(table);
                }
            }
            return result;
        } else {
            return 0;
        }

    }

    /**
     * 根据整型和两个字符串匹配删除表中项
     *
     * @param table     表名
     * @param condition 列名
     * @param value     字符串
     * @return
     */
    public void DeleteDataByCondition(String table, String condition1, int value1, String condition,
                                      String value, String condition2, String value2) {
        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = ?" + " and " + condition1 + " = ?"
                        + " and " + condition2 + " = ?",
                new String[]{value, Integer.toString(value1), value2});
    }

    /**
     * 执行sql查询语句获取对应游标
     *
     * @param sql
     * @param selectionArgs
     */
    public Cursor QueryBySQL(String sql, String[] selectionArgs) {
        return mSQLiteDatabase.rawQuery(sql, selectionArgs);
    }

    /**
     * 根据ID 查找某个表的记录是否存在
     *
     * @param tablename
     * @param whereClause
     * @return boolean 是否存在
     */
    public boolean isExistbyCondition(String tablename,
                                      String whereClause, String[] whereArgs) {
        Cursor cur = null;
        try {

            cur = mSQLiteDatabase.query(true, tablename, null, whereClause,
                    whereArgs, null, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据ID 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistbyId(String tablename,
                               String columnname, int id) {
        // TODO Auto-generated method stub
        Cursor cur = null;
        try {

            cur = mSQLiteDatabase.query(true, tablename, null, columnname + "=?",
                    new String[]{Integer.toString(id)}, null, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据ID 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistby2Id(String tablename,
                                String columnname, int id, String columnname2, int id2) {
        // TODO Auto-generated method stub
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null, columnname + "= ?"
                            + " and " + columnname2 + "= ?",
                    new String[]{Integer.toString(id), Integer.toString(id2)}, null, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据str 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistByStr(String tablename, String columnname, String str) {
        // TODO Auto-generated method stub
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(tablename,
                    null, columnname + " = ?",
                    new String[]{str}, null, null, null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据ID和字符串 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistbyIdAndStr(String tablename,
                                     String columnname, int id,
                                     String columnname2, String str2) {
        // TODO Auto-generated method stub
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null, columnname + "= ?"
                            + " and " + columnname2
                            + " = ?", new String[]{Integer.toString(id), str2}, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据ID和2个字符串 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistbyIdAnd2Str(String tablename,
                                      String columnname, int id,
                                      String columnname2, String str2, String columnname3, String str3) {
        // TODO Auto-generated method stub
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null,
                    columnname + "= ?"
                            + " and " + columnname2 + " = ?"
                            + " and " + columnname3 + " = ?",
                    new String[]{Integer.toString(id), str2, str3}, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据2ID和1个字符串 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistby2IdAndStr(String tablename,
                                      String columnname, int id, String columnname1, int id1,
                                      String columnname2, String str2) {
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null,
                    columnname + "= ?"
                            + " and " + columnname1 + " = ?"
                            + " and " + columnname2 + " = ?",
                    new String[]{Integer.toString(id), Integer.toString(id1), str2,}, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据2ID和2个字符串 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistby3IdAnd2Str(String tablename,
                                       String columnname0, int id0,
                                       String columnname, int id, String columnname1, int id1,
                                       String columnname2, String str2, String columnname3, String str3) {
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null,
                    columnname0 + "= ?"
                            + " and " + columnname + "= ?"
                            + " and " + columnname1 + " = ?"
                            + " and " + columnname2 + " = ?"
                            + " and " + columnname3 + " = ?",
                    new String[]{Integer.toString(id0), Integer.toString(id), Integer.toString(id1), str2, str3}, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据2ID和2个字符串 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistby2IdAnd2Str(String tablename,
                                       String columnname, int id, String columnname1, int id1,
                                       String columnname2, String str2, String columnname3, String str3) {
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null,
                    columnname + "= ?"
                            + " and " + columnname1 + " = ?"
                            + " and " + columnname2 + " = ?"
                            + " and " + columnname3 + " = ?",
                    new String[]{Integer.toString(id), Integer.toString(id1), str2, str3}, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据ID和2个字符串 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname
     * @return boolean 是否存在
     */
    public boolean isExistbyIdAnd3Str(String tablename,
                                      String columnname, int id, String columnname1, String str1,
                                      String columnname2, String str2, String columnname3, String str3) {
        // TODO Auto-generated method stub
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null,
                    columnname + "= ?"
                            + " and " + columnname1 + " = ?"
                            + " and " + columnname2 + " = ?"
                            + " and " + columnname3 + " = ?",
                    new String[]{Integer.toString(id), str1, str2, str3}, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 根据2个字符串 查找某个表的记录是否存在
     *
     * @param tablename
     * @param columnname2
     * @return boolean 是否存在
     */
    public boolean isExistby2Str(String tablename,
                                 String columnname2, String str2, String columnname3, String str3) {
        // TODO Auto-generated method stub
        Cursor cur = null;
        try {
            cur = mSQLiteDatabase.query(true, tablename, null,
                    columnname2 + " = ?" + " and " + columnname3 + " = ?",
                    new String[]{str2, str3}, null, null, null,
                    null);
        } catch (Exception e) {
            if (cur != null) {
                cur.close();
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            cur.close();
            return true;
        }
        if (cur != null) {
            cur.close();
        }
        return false;
    }

    /**
     * 增加数据库更改事件监听器, 注意: 需与删除监听器成对调用
     *
     * @param listener 事件监听器
     * @return true: 增加成功, false: 增加失败
     */
    public boolean addEventListener(DBDataChangedListener listener) {
        if (listener == null) {
            return false;
//            ExceptionHandler.handle(new RuntimeException());
        }
        listener.mIsRunning = true;
        if (mUsedLists.indexOf(listener) >= 0) {
            return false;
        }
        mUsedLists.add(listener);
        return true;
    }

    /**
     * 删除数据库更改事件监听器, 注意: 需与增加监听器成对调用
     *
     * @param listener 事件监听器
     * @return true: 删除成功, false: 删除失败
     */
    public boolean removeEventListener(DBDataChangedListener listener) {
        if (listener == null) {
            return false;
        }
        listener.mIsRunning = false;
        return mUsedLists.remove(listener);
    }

    /**
     * 获取数据库的游标
     */
    public Cursor getCursorDataTable(boolean distinct, String tablename, String selection, String[] selectionArgs) {
        Cursor cur = null;
        cur = mSQLiteDatabase.query(distinct, tablename,
                null, selection, selectionArgs, null, null, null, null);
        return cur;
    }

}
