/**
 * Copyright (C) 2016 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.yaxon.frameWork.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Message;


/**
 * 接近传感器
 *
 * @author guojiaping
 * @version create on 2016/3/18.
 */
public class ProximitySensor extends SensorBase {
    private static ProximitySensor instance = new ProximitySensor();

    private ProximitySensor() {
    }

    public static ProximitySensor getInstance() {
        return instance;
    }

    public boolean start(SensorManager sm) {
        if (sm == null) return false;
        sensorManager = sm;
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        boolean res = sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        if (res) {
            reg = true;
        }
        return res;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float[] tmp = new float[3];
            tmp[0] = event.values[0];//光线值
            if (handler != null) {
                if (sensorData == null || tmp[0] - sensorData[0] > 0.1 || tmp[1] - sensorData[1] > 0.1 || tmp[2] - sensorData[2] > 0.1) {
                    Message msg = Message.obtain();
                    msg.obj = tmp;
                    msg.what = 1;
                    handler.handleMessage(msg);
                }
            }
            sensorData = tmp;
            lastSensorChangeTime = System.nanoTime();
        }
    }


}
