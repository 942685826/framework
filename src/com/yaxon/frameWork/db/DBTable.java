package com.yaxon.frameWork.db;

/**
 * ���ݿ�������й���(�������ʵ��ҵ����Ӧ���޸�)
 *
 * @author guojiaping 2015-04-15 ����<br>
 */
public class DBTable {
    public static final int DATABASE_VER = 1;
    public static final String DATABASE_NAME = "test.db"; // ���ݿ���
    // ===============������Ϣ���ݿ��===========================
    public static String TABLE_BASIC_CREATE[] = {"create table vehicle (_id integer primary key autoincrement," +
            "ProvinceID varchar(20),CityID varchar(20)," +
            "ManuID varchar(20),TerminalType varchar(20),TerminalId varchar(20),VehicleColor varchar(20),VehicleVin varchar(20),IdentifierCode varchar(20),IsActive byte)"

    };

    // ===============ҵ���������ݿ��(ʹ�ù����б��������)===========================
    public static String TABLE_WORK_CREATE[] = {

    };

    // ===============������Ϣ���ݿ��(������������֮��)===========================
    public static String TABLE_OTHER_CREATE[] = {

    };

    /**
     * ɾ���û�ҵ������
     */
    public static void clearUserDataTable() {

    }

    /**
     * �����������
     *
     * @return
     */
    public static void cleanNotTodayTables() {

    }


}
