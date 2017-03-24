/**
 * Copyright (C) 2016 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.yaxon.frameWork.utils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.yaxon.frameWork.debug.LogUtils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author guojiaping
 * @version create on 2016/3/18.
 */
public class GsensorUtils implements SensorEventListener {
    private SensorManager sensorManager;
    private boolean reg = false;
    private long sensorRate = 100 * 1000; // 100ms频率
    private float[] sensorData;
    private static GsensorUtils gsensor = new GsensorUtils();
    private long lastGsensorChangeTime;
    private static final int GYRO_DATA_BUF_SIZE = 5;
    private float[][] gyroscopeBuf = new float[GYRO_DATA_BUF_SIZE][3];
    private float[] gyroscopeData;
    private int lastGyroscopebufIndex;
    private long lastGyroscopeChangeTime;

    private float[] getGyroscopebufNext() {
        int index;
        if (lastGyroscopebufIndex + 1 >= GYRO_DATA_BUF_SIZE) {
            index = 0;
        } else {
            index = lastGyroscopebufIndex + 1;
        }
        return gyroscopeBuf[index];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] tmp = new float[3];
            tmp[0] = event.values[SensorManager.DATA_X];
            tmp[1] = event.values[SensorManager.DATA_Y];
            tmp[2] = event.values[SensorManager.DATA_Z];
            sensorData = tmp;
            lastGsensorChangeTime = System.nanoTime();
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float[] tmp = getGyroscopebufNext();
            tmp[0] = event.values[SensorManager.DATA_X];
            tmp[1] = event.values[SensorManager.DATA_Y];
            tmp[2] = event.values[SensorManager.DATA_Z];
            if (lastGyroscopebufIndex + 1 >= GYRO_DATA_BUF_SIZE) {
                lastGyroscopebufIndex = 0;
            } else {
                lastGyroscopebufIndex = lastGyroscopebufIndex + 1;
            }
            gyroscopeData = tmp;
            lastGyroscopeChangeTime = System.nanoTime();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private GsensorUtils() {
    }

    public static GsensorUtils getInstance() {
        return gsensor;
    }

    public float[] getSensorData() throws RuntimeException {
        long ptime = System.nanoTime() - lastGsensorChangeTime;
        long tmp = 5 * sensorRate * 1000000;
        if (ptime > tmp) {
            throw new RuntimeException(ptime + "Gsensor is not valid!" + tmp);
        }
        if (ptime > 3 * sensorRate * 1000) {
            LogUtils.w("Gsensor", "Gsensor update delay" + ptime);
        }

        return sensorData;
    }

    public float[] getGyroscopeData() throws RuntimeException {
        long ptime = System.nanoTime() - lastGyroscopeChangeTime;
        if (ptime > 5 * sensorRate * 1000000) {
            throw new RuntimeException("Gyroscope is not valid!");
        }
        if (ptime > 3 * sensorRate * 1000) {
            LogUtils.w("Gsensor", "Gyroscope update delay" + ptime);
        }

        return gyroscopeData;
    }

    /**
     * 获取重力传感器状态
     *
     * @return 0-未启动；1-工作正常，2-未收到数据，若超过获取周期无数据，模块异常
     */
    public int getState() {
        if (!reg) return 0;
        if (System.nanoTime() - lastGsensorChangeTime < 5 * sensorRate * 1000000) return 1;
        //if (valid) return 1;
        return 2;
    }

    /**
     * 获取陀螺仪状态
     *
     * @return 0-未启动；1-工作正常；2-未收到数据，若超过获取周期无数据，模块异常
     */
    public int getGyroState() {
        if (!reg) return 0;
        if (System.nanoTime() - lastGyroscopeChangeTime < 5 * sensorRate * 1000000) return 1;
        return 2;
    }

    public static String timeFormat() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        Date now = new Date();
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
        myFmt.setTimeZone(tz);
        String str = myFmt.format(now);
        return str;
    }

    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (sensorManager == null) {
                timer.cancel();
                return;
            }
            Sensor gsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            boolean bres = sensorManager.registerListener(GsensorUtils.this, gsSensor, SensorManager.SENSOR_DELAY_UI); //sensorRate);
            if (bres) {
                timer.cancel();
            }
        }
    };

    public boolean start(SensorManager sm) {
        if (sm == null) return false;
        sensorManager = sm;
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean res = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        if (res) {
            reg = true;
        }
        timer.schedule(timerTask, 1000);
        return res;
    }

    /**
     * 设置重力传感器数据采集频率
     *
     * @param rate 采集频率，单位：ms
     * @return true-设置成功
     */
    public boolean setSensorRate(int rate) {
        if (0 == rate) return false;
        if (rate < sensorRate) {
            sensorRate = rate;
            if (reg) {
                Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.unregisterListener(this);
                boolean res = sensorManager.registerListener(this, accelerometerSensor, (int) sensorRate);
            }
        }
        return true;
    }

    public void stop() {
        if (reg) {
            sensorManager.unregisterListener(this);
            reg = false;
        }
    }
}
