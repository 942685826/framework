package com.yaxon.frameWork.view.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.yaxon.frameWork.R;

import java.util.List;

/**
 * @author guojiaping
 * @version 2017/6/5 创建<br>.
 */
public class ChatAdapter extends BaseAdapter {
    public static final String KEY = "key";
    public static final String VALUE = "value";

    public static final int VALUE_TIME_TIP = 0;//7种不同的布局
    public static final int VALUE_LEFT_TEXT = 1;//7种不同的布局
    public static final int VALUE_LEFT_IMAGE = 2;//7种不同的布局
    public static final int VALUE_LEFT_AUDIO = 3;//7种不同的布局
    public static final int VALUE_RIGHT_TEXT = 4;//7种不同的布局
    public static final int VALUE_RIGHT_IMAGE = 5;//7种不同的布局
    public static final int VALUE_RIGHT_AUDIO = 6;//7种不同的布局
    private LayoutInflater mInflater;

    private List<ChatMessage> myList;

    public ChatAdapter(Context context, List<ChatMessage> myList) {
        this.myList = myList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage msg = myList.get(position);
        int type = getItemViewType(position);
        ViewHolderTime holderTime;
        ViewHoldRightText holderRightText;
        ViewHoldRightImg holderRightImg;
        ViewHoldRightAudio holderRightAudio;
        ViewHoldLeftText holderLeftText;
        ViewHoldLeftImg holderLeftImg;
        ViewHoldLeftAudio holderLeftAudio;

        if (convertView == null) {
            switch (type) {
                case VALUE_TIME_TIP:
                    holderTime = new ViewHolderTime();
                    convertView = mInflater.inflate(R.layout.list_item_time_tip, null);
                    holderTime.tvTimeTip = (TextView) convertView.findViewById(R.id.tv_time_tip);
                    holderTime.tvTimeTip.setText(msg.getValue());
                    convertView.setTag(holderTime);
                    break;
                //左边
                case VALUE_LEFT_TEXT:
                    holderLeftText = new ViewHoldLeftText();
                    convertView = mInflater.inflate(R.layout.list_item_left_text, null);
                    holderLeftText.ivLeftIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holderLeftText.btnLeftText = (Button) convertView.findViewById(R.id.btn_left_text);
                    holderLeftText.btnLeftText.setText(msg.getValue());
                    convertView.setTag(holderLeftText);
                    break;
                case VALUE_LEFT_IMAGE:
                    holderLeftImg = new ViewHoldLeftImg();
                    convertView = mInflater.inflate(R.layout.list_item_left_iamge, null);
                    holderLeftImg.ivLeftIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holderLeftImg.ivLeftImage = (ImageView) convertView.findViewById(R.id.iv_left_image);
                    holderLeftImg.ivLeftImage.setImageResource(R.drawable.test);
                    convertView.setTag(holderLeftImg);
                    break;
                case VALUE_LEFT_AUDIO:
                    holderLeftAudio = new ViewHoldLeftAudio();
                    convertView = mInflater.inflate(R.layout.list_item_left_audio, null);
                    holderLeftAudio.ivLeftIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holderLeftAudio.btnLeftAudio = (Button) convertView.findViewById(R.id.btn_left_audio);
                    holderLeftAudio.tvLeftAudioTime = (TextView) convertView.findViewById(R.id.tv_left_audio_time);
                    holderLeftAudio.tvLeftAudioTime.setText(msg.getValue() + "''");
                    convertView.setTag(holderLeftAudio);
                    break;
                // 右边
                case VALUE_RIGHT_TEXT:
                    holderRightText = new ViewHoldRightText();
                    convertView = mInflater.inflate(R.layout.list_item_right_text,
                            null);
                    holderRightText.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.iv_icon);
                    holderRightText.btnRightText = (Button) convertView
                            .findViewById(R.id.btn_right_text);
                    holderRightText.btnRightText.setText(msg.getValue());
                    convertView.setTag(holderRightText);
                    break;
                case VALUE_RIGHT_IMAGE:
                    holderRightImg = new ViewHoldRightImg();
                    convertView = mInflater.inflate(R.layout.list_item_right_iamge,
                            null);
                    holderRightImg.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.iv_icon);
                    holderRightImg.ivRightImage = (ImageView) convertView
                            .findViewById(R.id.iv_right_image);
                    holderRightImg.ivRightImage.setImageResource(R.drawable.test);
                    convertView.setTag(holderRightImg);
                    break;
                case VALUE_RIGHT_AUDIO:
                    holderRightAudio = new ViewHoldRightAudio();
                    convertView = mInflater.inflate(R.layout.list_item_right_audio,
                            null);
                    holderRightAudio.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.iv_icon);
                    holderRightAudio.btnRightAudio = (Button) convertView
                            .findViewById(R.id.btn_right_audio);
                    holderRightAudio.tvRightAudioTime = (TextView) convertView
                            .findViewById(R.id.tv_right_audio_time);
                    holderRightAudio.tvRightAudioTime.setText(msg.getValue() + "''");
                    convertView.setTag(holderRightAudio);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case VALUE_TIME_TIP:
                    holderTime = (ViewHolderTime) convertView.getTag();
                    holderTime.tvTimeTip.setText(msg.getValue());
                    break;
                case VALUE_LEFT_TEXT:
                    holderLeftText = (ViewHoldLeftText) convertView.getTag();
                    holderLeftText.btnLeftText.setText(msg.getValue());
                    break;
                case VALUE_LEFT_IMAGE:
                    holderLeftImg = (ViewHoldLeftImg) convertView.getTag();
                    holderLeftImg.ivLeftImage.setImageResource(R.drawable.test);
                    break;
                case VALUE_LEFT_AUDIO:
                    holderLeftAudio = (ViewHoldLeftAudio) convertView.getTag();
                    holderLeftAudio.tvLeftAudioTime.setText(msg.getValue() + "''");
                    break;
                case VALUE_RIGHT_TEXT:
                    holderRightText = (ViewHoldRightText) convertView.getTag();
                    holderRightText.btnRightText.setText(msg.getValue());
                    break;
                case VALUE_RIGHT_IMAGE:
                    holderRightImg = (ViewHoldRightImg) convertView.getTag();
                    holderRightImg.ivRightImage.setImageResource(R.drawable.test);
                    break;
                case VALUE_RIGHT_AUDIO:
                    holderRightAudio = (ViewHoldRightAudio) convertView.getTag();
                    holderRightAudio.tvRightAudioTime.setText(msg.getValue() + "''");
                    break;

                default:
                    break;
            }
        }
        return convertView;
    }

    /**
     * 根据数据源的position返回需要显示的layout的type
     * type值从0开始
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage message = myList.get(position);
        return message.getType();
    }

    /**
     * 返回所有的layout数量
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 7;
    }

    class ViewHolderTime {
        private TextView tvTimeTip;//时间
    }

    class ViewHoldRightText {
        ImageView ivRightIcon;//右边的头像
        Button btnRightText;//右边的文本
    }

    class ViewHoldRightImg {
        ImageView ivRightIcon;//右边的头像
        ImageView ivRightImage;//右边的图像
    }

    class ViewHoldRightAudio {
        ImageView ivRightIcon;//右边的头像
        Button btnRightAudio;//右边的声音
        TextView tvRightAudioTime;//右边声音的时间
    }

    class ViewHoldLeftText {
        ImageView ivLeftIcon;//左边的头像
        Button btnLeftText;//左边的文本
    }

    class ViewHoldLeftImg {
        ImageView ivLeftIcon;//左边的头像
        ImageView ivLeftImage;//左边的图像
    }

    class ViewHoldLeftAudio {
        ImageView ivLeftIcon;//左边的头像
        Button btnLeftAudio;//左边的声音
        TextView tvLeftAudioTime;//左边的声音时间
    }
}
