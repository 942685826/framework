/**
 * Copyright (C) 2016 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.yaxon.frameWork.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Message;


/**
 * 方向传感器
 *
 * @author guojiaping
 * @version 2016/3/18 创建<br>.
 */
public class OrientationSensor extends SensorBase {
    private static OrientationSensor instance = new OrientationSensor();

    private OrientationSensor() {
    }

    public static OrientationSensor getInstance() {
        return instance;
    }

    public boolean start(SensorManager sm) {
        if (sm == null) return false;
        sensorManager = sm;
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        boolean res = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        if (res) {
            reg = true;
        }
        return res;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float[] tmp = new float[3];
            tmp[0] = event.values[SensorManager.DATA_X];
            tmp[1] = event.values[SensorManager.DATA_Y];
            tmp[2] = event.values[SensorManager.DATA_Z];
            if (handler != null) {
                if (sensorData == null || tmp[0] - sensorData[0] > 1 || tmp[1] - sensorData[1] > 1 || tmp[2] - sensorData[2] > 1) {
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
