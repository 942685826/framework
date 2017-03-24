package com.yaxon.frameWork.timer;

import android.os.Handler;
import android.os.Message;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 定时服务器
 *
 * @author guojiaping V1.00 2011.05.18 创建<br>
 */
public class TimerServer {
    private TimerThread mThread = null;
    private LinkedList<Timer> runList = new LinkedList<Timer>();
    private LinkedList<Timer> addList = new LinkedList<Timer>();
    private boolean handling = false;
    private boolean needClear = false;

    /**
     * 构造器
     *
     * @param period 定时周期, 单位: 毫秒
     */
    public TimerServer(int period) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message ms) {
                // 遍历链表中的定时器, 测试定时时间是否已到
                handling = true;
                for (Timer tm : runList) {
                    if (tm.mIsRunning && --tm.mLeft == 0) {
                        tm.mLeft = tm.mPeriod;
                        if (tm.mListener != null) {
                            tm.mListener.onPeriod(tm.mObj);
                        }
                    }
                }
                handling = false;

                // 将追加链表中的定时器添加到运行链表中
                for (Timer tm : addList) {
                    runList.add(tm);
                }
                addList.clear();

                // 移除链表中待删除的定时器
                if (needClear) {
                    Iterator<Timer> it = runList.iterator();
                    while (it.hasNext()) {
                        Timer timer = it.next();
                        if (!timer.mIsRunning) {
                            it.remove();
                        }
                    }
                    needClear = false;
                }

                // 如链表中无定时器, 则使得定时线程进入睡眠
                if (runList.size() == 0) {
                    mThread.setStop();
                }
            }
        };

        mThread = new TimerThread(period);
        mThread.start();
        mThread.setServerHandler(handler);
    }

    /**
     * 往定时器服务器中追加一个定时器
     *
     * @param timer 待追加的定时器
     */
    public void addTimer(Timer timer) {
        if (runList.indexOf(timer) >= 0) {
            return;
        }
        if (addList.indexOf(timer) >= 0) {
            return;
        }
        if (handling) {
            addList.add(timer);
        } else {
            runList.add(timer);
            if (runList.size() == 1) {
                // 如链表中追加前无定时器, 则启动定时线程
                mThread.setRun();
            }
        }
    }

    /**
     * 从定时器服务器中移除一个定时器
     *
     * @param timer 待移除的定时器
     */
    public void removeTimer(Timer timer) {
        if (addList.remove(timer)) {
            return;
        }
        // 先测试是否处于处理定时溢出阶段
        if (!handling) {
            runList.remove(timer);
        } else {
            needClear = true;
        }
    }
}