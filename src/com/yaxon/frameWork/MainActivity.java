package com.yaxon.frameWork;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import com.yaxon.frameWork.db.litepal.LitepalUtils;
import com.yaxon.frameWork.debug.LogUtils;
import com.yaxon.frameWork.debug.ToastUtils;
import com.yaxon.frameWork.http.HttpHandler;
import com.yaxon.frameWork.utils.DensityUtils;
import com.yaxon.frameWork.utils.ScreenUtils;
import com.yaxon.frameWork.utils.ZipUtil;
import com.yaxon.frameWork.view.date.DateDialog;
import com.yaxon.frameWork.view.date.DateUtils;
import com.yaxon.frameWork.xml.CANSetting;
import com.yaxon.frameWork.xml.XmlParse;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
