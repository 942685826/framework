package com.yaxon.frameWork.view.chat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.yaxon.frameWork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guojiaping
 * @version 2017/6/5 创建<br>.
 */
public class ChatActivity extends Activity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chat_layout);
        listView = (ListView) findViewById(R.id.lv_data);
        listView.setAdapter(new ChatAdapter(this, getMyData()));
    }

    private List<ChatMessage> getMyData() {
        List<ChatMessage> msgList = new ArrayList<ChatMessage>();
        ChatMessage msg;
        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("100");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_TIME_TIP);
        msg.setValue("2012-12-23 下午2:23");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("99");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("98");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_TIME_TIP);
        msg.setValue("2012-12-23 下午2:25");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("97");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("96");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("95");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_TIME_TIP);
        msg.setValue("2012-12-23 下午3:25");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("94");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
        msg.setValue("93");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("92");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("91");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
        msg.setValue("0");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("1");
        msgList.add(msg);


        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
        msg.setValue("2");
        msgList.add(msg);


        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("3");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_AUDIO);
        msg.setValue("4");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_AUDIO);
        msg.setValue("4");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("5");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("6");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("7");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("8");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_IMAGE);
        msg.setValue("9");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("10");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("11");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("12");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("13");
        msgList.add(msg);

        msg = new ChatMessage();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("14");
        msgList.add(msg);
        return msgList;
    }
}
