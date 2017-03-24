/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.yaxon.frameWork.timer;


/**
 * 定时器驱动
 *
 * @author guojiaping V1.00 2011.05.18 创建<br>
 */
public class Timer {
    private static final int PERIOD_MS = 50; // 定时线程定时周期, 单位: 毫秒
    private static TimerServer mServer = new TimerServer(PERIOD_MS);
    public int mPeriod; // 定时周期
    public int mLeft; // 剩下的计时时间
    public boolean mIsRunning; // 标识是否处于运行状态
    public Object mObj; // 用户对象
    public TimerListener mListener; // 定时器监听器

    /**
     * 缺省构造器
     */
    public Timer() {
        mPeriod = 0;
        mLeft = 0;
        mIsRunning = false;
        mObj = null;
        mListener = null;
    }

    /**
     * 构造器
     *
     * @param listener 定时器监听器
     */
    public Timer(TimerListener listener) {
        this(listener, null);
    }

    /**
     * 构造器
     *
     * @param listener 定时器监听器
     * @param obj      用户数据对象
     */
    public Timer(TimerListener listener, Object obj) {
        this();
        this.mObj = obj;
        this.mListener = listener;
    }

    /**
     * 启动定时器
     *
     * @param period 定时周期, 单位: ms
     * @return true: 启动成功, false: 启动失败
     */
    public boolean start(int period) {
        return start(period, this.mListener, this.mObj);
    }

    /**
     * 启动定时器
     *
     * @param period   定时周期, 单位: 毫秒
     * @param listener 定时监听器
     * @return true: 启动成功, false: 启动失败
     */
    public boolean start(int period, TimerListener listener) {
        return start(period, listener, this.mObj);
    }

    /**
     * 启动定时器
     *
     * @param period   定时器周期
     * @param listener 定时监听器
     * @param obj      用户数据对象
     * @return true: 启动成功, false: 启动失败
     */
    public boolean start(int period, TimerListener listener, Object obj) {
        if (listener == null) {
            return false;
        }
        this.mListener = listener;
        this.mObj = obj;
        this.mPeriod = period / PERIOD_MS;
        if (this.mPeriod == 0) {
            this.mPeriod = 1;
        }
        mLeft = this.mPeriod;
        if (!mIsRunning) {
            mIsRunning = true;
            mServer.addTimer(this);
        }
        return true;
    }

    /**
     * 停止定时器
     */
    public void stop() {
        if (mIsRunning) {
            mIsRunning = false;
            mServer.removeTimer(this);
        }
    }

    /**
     * 测试定时器是否处于运行状态
     *
     * @return true: 运行中, false: 已停止
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * 获取定时剩余时间
     *
     * @return 剩余时间, 单位: 毫秒
     */
    public int leftTime() {
        if (mIsRunning) {
            return mLeft * PERIOD_MS;
        } else {
            return 0;
        }
    }


}
