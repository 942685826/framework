package com.yaxon.frameWork.db;

/**
 * 数据库更改事件监听器
 *
 * @author guojiaping 2015-04-15 创建<br>
 */
public abstract class DBDataChangedListener {
    boolean mIsRunning = false; // 用于标识监听器是否生效

    /**
     * 事件处理
     *
     * @param tablename 发生改变的表名
     */
    public abstract void onEvent(String tablename);
}