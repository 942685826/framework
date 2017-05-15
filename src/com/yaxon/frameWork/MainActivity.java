package com.yaxon.frameWork;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.yaxon.frameWork.debug.LogUtils;
import com.yaxon.frameWork.http.HttpHandler;
import com.yaxon.frameWork.view.date.DateUtils;
import com.yaxon.frameWork.xml.CANSetting;
import com.yaxon.frameWork.xml.XmlParse;
import org.json.JSONObject;

public class MainActivity extends Activity{
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = (Button) findViewById(R.id.timeSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateUtils.choseDateAndTime(MainActivity.this);
            }
        });
        new Thread(){
            @Override
            public void run() {
                super.run();
                CANSetting chanel = XmlParse.xmlResourceParse(MainActivity.this, R.xml.canid);
                LogUtils.d("", "");
            }
        }.start();
    }


    HttpHandler handler = new HttpHandler(MainActivity.this){
        @Override
        protected void succeed(String result, int request) {
            super.succeed(result, request);
        }

        @Override
        protected void failed(JSONObject jObject) {
            super.failed(jObject);
        }
    };
}
