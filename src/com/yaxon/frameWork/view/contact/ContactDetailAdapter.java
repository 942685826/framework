package com.yaxon.frameWork.view.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yaxon.frameWork.R;


import java.util.List;

/**
 * Company	yaxon
 *
 * @author guojiaping
 * @version 2015/11/29
 */
public class ContactDetailAdapter extends BaseAdapter {
    private List<String> phones;
    private ViewHold viewHold;
    private Context context;

    public ContactDetailAdapter(List<String> phones, Context context) {
        this.phones = phones;
        this.context = context;
    }

    @Override
    public int getCount() {
        return phones.size();
    }

    @Override
    public Object getItem(int position) {
        return phones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_detail_item, null);
            viewHold.phoneNumber = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.phoneNumber.setText(phones.get(position));
        return convertView;
    }

    class ViewHold {
        public TextView phoneNumber;
    }
}
