package com.yaxon.frameWork.db;

/**
 * 数据库表名集中管理(本类根据实际业务相应的修改)
 *
 * @author guojiaping 2015-04-15 创建<br>
 */
public class DBTable {
    public static final int DATABASE_VER = 1;
    public static final String DATABASE_NAME = "test.db"; // 数据库名
    // ===============基础信息数据库表===========================
    public static String TABLE_BASIC_CREATE[] = {"create table vehicle (_id integer primary key autoincrement," +
            "ProvinceID varchar(20),CityID varchar(20)," +
            "ManuID varchar(20),TerminalType varchar(20),TerminalId varchar(20),VehicleColor varchar(20),VehicleVin varchar(20),IdentifierCode varchar(20),IsActive byte)"

    };

    // ===============业务数据数据库表(使用过程中保存的数据)===========================
    public static String TABLE_WORK_CREATE[] = {

    };

    // ===============其它信息数据库表(上述两种情形之外)===========================
    public static String TABLE_OTHER_CREATE[] = {

    };

    /**
     * 删除用户业务数据
     */
    public static void clearUserDataTable() {

    }

    /**
     * 清除过期数据
     *
     * @return
     */
    public static void cleanNotTodayTables() {

    }


}
