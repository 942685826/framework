package com.yaxon.frameWork.db;

/**
 * ���ݿ�����¼�������
 *
 * @author guojiaping 2015-04-15 ����<br>
 */
public abstract class DBDataChangedListener {
    boolean mIsRunning = false; // ���ڱ�ʶ�������Ƿ���Ч

    /**
     * �¼�����
     *
     * @param tablename �����ı�ı���
     */
    public abstract void onEvent(String tablename);
}