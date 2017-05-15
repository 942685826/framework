/**
 * Copyright (C) 2016 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.yaxon.frameWork.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Message;


/**
 * 磁场传感器
 *
 * @author guojiaping
 * @version 2016/3/18 创建<br>.
 */
public class MagneticFieldSensor extends SensorBase {
    private static MagneticFieldSensor instance = new MagneticFieldSensor();

    private MagneticFieldSensor() {
    }

    public static MagneticFieldSensor getInstance() {
        return instance;
    }

    public boolean start(SensorManager sm) {
        if (sm == null) return false;
        sensorManager = sm;
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        boolean res = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        if (res) {
            reg = true;
        }
        return res;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float[] tmp = new float[3];
            tmp[0] = event.values[SensorManager.DATA_X];
            tmp[1] = event.values[SensorManager.DATA_Y];
            tmp[2] = event.values[SensorManager.DATA_Z];
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
