package com.yaxon.frameWork.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import com.yaxon.frameWork.debug.LogUtils;

/**
 * @author guojiaping
 * @version on 2017/5/15.
 */
public abstract class SensorBase implements SensorEventListener {
    protected SensorManager sensorManager;
    protected boolean reg = false;
    protected long sensorRate = 100 * 1000; // 100ms频率

    protected float[] sensorData;
    protected long lastSensorChangeTime;
    protected Handler handler;

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float[] getSensorData() throws RuntimeException {
        long ptime = System.nanoTime() - lastSensorChangeTime;
        if (ptime > 5 * sensorRate * 1000000) {
            throw new RuntimeException("Gyroscope is not valid!");
        }
        if (ptime > 3 * sensorRate * 1000) {
            LogUtils.w("Gsensor", "Gyroscope update delay" + ptime);
        }

        return sensorData;
    }

    /**
     * 获取传感器状态
     *
     * @return 0-未启动；1-工作正常；2-未收到数据，若超过获取周期无数据，模块异常
     */
    public int getSensorState() {
        if (!reg) return 0;
        if (System.nanoTime() - lastSensorChangeTime < 5 * sensorRate * 1000000) return 1;
        return 2;
    }


    /**
     * 设置传感器数据采集频率
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

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
