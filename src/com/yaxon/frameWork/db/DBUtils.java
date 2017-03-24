package com.yaxon.frameWork.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * ���ݿ����������
 *
 * @author guojiaping 2015-04-15 ����<br>
 */
public class DBUtils {
    private static DBUtils mInstance = null;
    private LinkedList<DBDataChangedListener> mUsedLists = new LinkedList<DBDataChangedListener>();
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * ���ݿ⹹�� ����
     */
    private DBUtils(Context context) {
        super();
        // TODO Auto-generated constructor stub
        mSQLiteDatabase = Database.getInstance(context).openSQLiteDatabase();
    }

    /**
     * ��ȡ���ݿ����ʵ��
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
     * ��ʼ���� ���ڶ������ݲ���ǰ���� Ŀǰ�����ŵꡢ��Ʒ����Э��ʹ��
     */
    public void beginTransaction() {
        mSQLiteDatabase.beginTransaction();
    }

    /**
     * �������� ���ڶ��������������� ���� ��ʱһ���ύ����
     */
    public void endTransaction() {
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    /**
     * ɾ�����ݿ�
     *
     * @param databaseName : ���ݿ���
     * @return
     */
    public void deleteDataBase(String databaseName) {
        this.deleteDataBase(databaseName);
    }

    /**
     * ɾ��һ�����ݿ��
     *
     * @param tableName : ����
     * @return
     */
    public void deleteTable(String tableName) {
        mSQLiteDatabase.execSQL("DROP TABLE " + tableName);
    }

    /**
     * ���һ�����ݿ������
     *
     * @param tableName ������
     * @return
     */
    public void clearTable(String tableName) {
        mSQLiteDatabase.execSQL("delete from  " + tableName);
    }

    /**
     * ��������
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
     * ����������ֵƥ����±�
     *
     * @param table     :���ݿ����
     * @param cv        :����
     * @param condition :����
     * @param value     :������ֵ
     */
    public void updateTable(String table, ContentValues cv, String condition, Integer value) {
        mSQLiteDatabase.update(table, cv, condition + "= ?", new String[]{Integer.toString(value)});
    }

    /**
     * ����������ֵƥ����±�
     *
     * @param table     :���ݿ����
     * @param cv        :����
     * @param condition :����
     * @param value     :�ַ���
     */
    public int updateTable(String table, ContentValues cv, String condition, String value) {
        return mSQLiteDatabase.update(table, cv, condition + " = ?", new String[]{value});
    }

    /**
     * �������ͺ��ַ��� ��ֵƥ����±�
     *
     * @param table      :���ݿ����
     * @param cv         :����
     * @param condition  :����
     * @param value      :������ֵ
     * @param condition2 :����2
     * @param value2     :�ַ���
     */
    public void updateTable(String table, ContentValues cv,
                            String condition, Integer value, String condition2, String value2) {
        mSQLiteDatabase.update(table, cv, condition + " = ? " + " and " + condition2 + " = ?",
                new String[]{Integer.toString(value), value2});
    }

    /**
     * ���������ַ��� ��ֵƥ����±�
     *
     * @param table      :���ݿ����
     * @param cv         :����
     * @param condition  :����
     * @param value      :������ֵ
     * @param condition2 :����2
     * @param value2     :�ַ���
     */
    public void updateTable(String table, ContentValues cv,
                            String condition, String value, String condition2, String value2) {
        mSQLiteDatabase.update(table, cv, condition + " = ? " + " and " + condition2 + " = ?",
                new String[]{value, value2});
    }

    /**
     * ��������������ֵƥ����±�
     *
     * @param table      :���ݿ����
     * @param cv         :����
     * @param value      :������ֵ
     * @param condition2 :����2
     * @param value2     :������ֵ
     */
    public void updateTable(String table, ContentValues cv,
                            String condition1, Integer value, String condition2, Integer value2) {
        mSQLiteDatabase.update(table, cv, condition1 + " = ?" + " and "
                + condition2 + " = ?", new String[]{Integer.toString(value), Integer.toString(value2)});

    }

    /**
     * ��������������ֵƥ����±�
     *
     * @param table      :���ݿ����
     * @param cv         :����
     * @param condition1 :����1
     * @param value1     :������ֵ1
     * @param condition2 :����2
     * @param value2     :������ֵ2
     * @param condition3 :����3
     * @param value3     :������ֵ3
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
     * ��������������ֵƥ����±�
     *
     * @param table      :���ݿ����
     * @param cv         :����
     * @param condition1 :����1
     * @param value1     :�ַ���1
     * @param condition2 :����2
     * @param value2     :������ֵ2
     * @param condition3 :����3
     * @param value3     :������ֵ3
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
     * ��������������ֵƥ����±�
     *
     * @param table      :���ݿ����
     * @param cv         :����
     * @param condition1 :����1
     * @param value1     :�ַ���1
     * @param condition2 :����2
     * @param value2     :�ַ���2
     * @param condition3 :����3
     * @param value3     :������ֵ3
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
     * ���������ַ���ƥ��ɾ�����е�����
     *
     * @param table     ����
     * @param condition �����ַ���
     */
    public void DeleteData(String table, String condition) {
        // mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " +
        // condition);
        mSQLiteDatabase.delete(table, condition, null);
    }

    /**
     * ɾ������
     *
     * @param table       ����
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public void delete(String table, String whereClause, String[] whereArgs) {

        mSQLiteDatabase.delete(table, whereClause, whereArgs);
    }

    /**
     * ����������ֵƥ��ɾ������ĳ��
     *
     * @param table     ����
     * @param condition ����
     * @param value     ��ֵ
     * @return
     */
    public void DeleteDataByCondition(String table,
                                      String condition, Integer value) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + "="
                + Integer.toString(value));
    }

    /**
     * ����������ֵƥ��ɾ������ĳ��
     *
     * @param table      ����
     * @param condition  ����
     * @param value      ��ֵ
     * @param condition2 ����
     * @param value2     ��ֵ
     * @return
     */
    public void DeleteDataBy2Condition(String table,
                                       String condition, Integer value, String condition2, Integer value2) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = "
                + Integer.toString(value) + " and " + condition2 + " = " + value2);
    }

    /**
     * ����������ֵƥ��ɾ������ĳ��
     *
     * @param table      ����
     * @param condition  ����
     * @param value      ��ֵ
     * @param condition2 ����
     * @param value2     ��ֵ
     * @param condition3 ����
     * @param value3     �ַ���
     * @return
     */
    public void DeleteDataBy3Condition(String table,
                                       String condition, Integer value, String condition2, Integer value2, String condition3, String value3) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = "
                + Integer.toString(value) + " and " + condition2 + " = " + value2
                + " and " + condition3 + "= '" + value3 + "'");
    }

    /**
     * �ӱ���ɾ���������������ַ�����������
     *
     * @param table     ����
     * @param condition ����
     * @param str       �ַ���
     * @return
     */
    public void DeleteDataByNotLikeCondition(String table,
                                             String condition, String str) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " not like \'%"
                + str + "%\'");
    }

    /**
     * ������������ƥ��ɾ��������,ֻҪ�����ڸ����鷶Χ�ڼ�ɾ��
     *
     * @param table     ����
     * @param condition ����
     * @param scale     ��������
     * @return
     */
    public void DeleteDataByCondition(String table,
                                      String condition, int[] scale) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " in ("
                + Arrays.toString(scale).substring(1, Arrays.toString(scale).length() - 1) + ")");
    }

    /**
     * ����������ֵƥ��ɾ������ĳ�� ����ĳ����ֵ��ɾ��
     *
     * @param table     ����
     * @param condition ����
     * @param value     ��ֵ
     * @return
     */
    public void DeleteDataByBiggerCondition(String table,
                                            String condition, Integer value) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + ">"

                + Integer.toString(value));
    }

    /**
     * ����������ֵƥ��ɾ������ĳ�� ����ĳ����ֵ��ɾ��
     *
     * @param table     ����
     * @param condition ����
     * @param value     ��ֵ
     * @return
     */
    public void DeleteDataBySmallerCondition(String table,
                                             String condition, Integer value) {

        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + "<"

                + Integer.toString(value));
    }

    /**
     * �����ַ���ƥ��ɾ��������
     *
     * @param table     ����
     * @param condition ����
     * @param value     �ַ���
     * @return
     */
    public void DeleteDataByStr(String table, String condition,
                                String value) {
        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = ?",
                new String[]{value});

    }

    /**
     * �������ͺ��ַ���ƥ��ɾ��������
     *
     * @param table     ����
     * @param condition ����
     * @param value     �ַ���
     * @return
     */
    public void DeleteDataByCondition(String table, String condition1, int value1, String condition,
                                      String value) {
        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = ?" + " and " + condition1 + " = ?",
                new String[]{value, Integer.toString(value1)});

    }

    /**
     * ��������һ������
     *
     * @param cv    �������ݼ���
     * @param table ����
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
     * �������ͺ������ַ���ƥ��ɾ��������
     *
     * @param table     ����
     * @param condition ����
     * @param value     �ַ���
     * @return
     */
    public void DeleteDataByCondition(String table, String condition1, int value1, String condition,
                                      String value, String condition2, String value2) {
        mSQLiteDatabase.execSQL("DELETE FROM " + table + " WHERE " + condition + " = ?" + " and " + condition1 + " = ?"
                        + " and " + condition2 + " = ?",
                new String[]{value, Integer.toString(value1), value2});
    }

    /**
     * ִ��sql��ѯ����ȡ��Ӧ�α�
     *
     * @param sql
     * @param selectionArgs
     */
    public Cursor QueryBySQL(String sql, String[] selectionArgs) {
        return mSQLiteDatabase.rawQuery(sql, selectionArgs);
    }

    /**
     * ����ID ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param whereClause
     * @return boolean �Ƿ����
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
     * ����ID ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����ID ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����str ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����ID���ַ��� ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����ID��2���ַ��� ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����2ID��1���ַ��� ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����2ID��2���ַ��� ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����2ID��2���ַ��� ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����ID��2���ַ��� ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname
     * @return boolean �Ƿ����
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
     * ����2���ַ��� ����ĳ����ļ�¼�Ƿ����
     *
     * @param tablename
     * @param columnname2
     * @return boolean �Ƿ����
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
     * �������ݿ�����¼�������, ע��: ����ɾ���������ɶԵ���
     *
     * @param listener �¼�������
     * @return true: ���ӳɹ�, false: ����ʧ��
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
     * ɾ�����ݿ�����¼�������, ע��: �������Ӽ������ɶԵ���
     *
     * @param listener �¼�������
     * @return true: ɾ���ɹ�, false: ɾ��ʧ��
     */
    public boolean removeEventListener(DBDataChangedListener listener) {
        if (listener == null) {
            return false;
        }
        listener.mIsRunning = false;
        return mUsedLists.remove(listener);
    }

    /**
     * ��ȡ���ݿ���α�
     */
    public Cursor getCursorDataTable(boolean distinct, String tablename, String selection, String[] selectionArgs) {
        Cursor cur = null;
        cur = mSQLiteDatabase.query(distinct, tablename,
                null, selection, selectionArgs, null, null, null, null);
        return cur;
    }

}
