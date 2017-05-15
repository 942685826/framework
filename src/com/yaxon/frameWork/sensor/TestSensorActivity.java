package com.yaxon.frameWork.sensor;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yaxon.frameWork.R;

/**
 * @author guojiaping
 * @version 2017/5/15 创建<br>.
 */
public class TestSensorActivity extends Activity implements View.OnClickListener {
    private Button start, stop;
    private TextView showText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_layout);
        initView();
        initEvent();
    }

    private void initEvent() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        RotationVectorSensor.getInstance().setHandler(handler);
    }

    private void initView() {
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        showText = (TextView) findViewById(R.id.showText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                SensorManager sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
                RotationVectorSensor.getInstance().start(sm);
                break;
            case R.id.stop:
                RotationVectorSensor.getInstance().stop();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    float[] tmp = (float[]) msg.obj;
                    StringBuffer str = new StringBuffer();
                    str.append("x:");
                    str.append(tmp[0]);
                    str.append("\n");
                    str.append("y:");
                    str.append(tmp[1]);
                    str.append("\n");
                    str.append("z:");
                    str.append(tmp[2]);
                    showText.setText(str.toString());
                    break;
            }
        }
    };
}
