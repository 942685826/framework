package com.yaxon.frameWork.timer;

import android.os.Handler;

import java.util.concurrent.TimeUnit;

/**
 * 定时线程
 *
 * @author guojiaping V1.00 2011.05.18 创建<br>
 */
public class TimerThread extends Thread {
    private Handler serHandler; // server消息处理句柄
    private boolean running = false;
    private int period = 0;

    /**
     * 构造器
     *
     * @param period 定时周期, 单位: 毫秒
     */
    TimerThread(int period) {
        super();
        this.period = period;
    }

    /**
     * 设置server的消息处理句柄
     *
     * @param serHandler 消息处理句柄
     */
    void setServerHandler(Handler serHandler) {
        synchronized (this) {
            this.serHandler = serHandler;
        }
    }

    /**
     * 线程执行函数
     */
    @Override
    public void run() {
        // 设置线程名称
        setName("timer thread");
        while (true) {
            synchronized (this) {
                while (!running || period == 0) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        ;
                    }
                }
            }
            if (period > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(period);
                } catch (InterruptedException e) {
                    ;
                }
                synchronized (this) {
                    if (serHandler != null) {
                        serHandler.sendEmptyMessage(0);
                    }
                }
            }
        }
    }

    /**
     * 设置成运行状态
     */
    void setRun() {
        synchronized (this) {
            if (!running) {
                running = true;
                this.notify();
            }
        }
    }

    /**
     * 设置成停止状态
     */
    void setStop() {
        synchronized (this) {
            if (running) {
                running = false;
            }
        }
    }
}