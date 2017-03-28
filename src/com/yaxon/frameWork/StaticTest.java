package com.yaxon.frameWork;

import android.widget.Toast;
import com.yaxon.frameWork.timer.Timer;
import com.yaxon.frameWork.timer.TimerListener;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/18.
 */
public class StaticTest {
    private int count = 0;
    public StaticTest(String str) {
        System.out.println((++k) + ":" + str + " i=" + i + " n=" + n);
        ++n;
        ++i;
    }
    public static int k = 0;
    public static StaticTest t1 = new StaticTest("t1");
    static {
        print("静态块");
    }
    public static StaticTest t2 = new StaticTest("t2");
    public static int i = print("i");
    public static int n = 99;
    {
        print("构造快");
    }
    public int j = print("j");

    public static int print(String str) {
        System.out.println((++k) + ":" + str + " i=" + i + " n=" + n);
        ++i;
        return ++n;
    }

    public static void main(String[] args) {
//        new Thread() {
//            @Override
//            public void run() {
//                new StaticTest("init").test();
//            }
//        }.start();

        StaticTest t = new StaticTest("init");
        t.test();
    }
    public void test() {
        Timer timer = new Timer(new TimerListener() {
            @Override
            public void onPeriod(Object obj) {
                if (count % 3 == 0) {
                    System.out.println(new Date() + ":" + count);
                }
                count++;
            }
        });


        timer.start(1000);
    }

}
