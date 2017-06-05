package com.yaxon.frameWork.view.listview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.yaxon.frameWork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guojiaping
 * @version 2017/6/3 创建<br/>.
 */
public class ExListView extends Activity {
    private static final String G_TEXT = "g_text";

    private static final String C_TEXT1 = "c_text1";
    private static final String C_TEXT2 = "c_text1";
    List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

    ExAdapter adapter;
    ExpandableListView exList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_layout);
        for (int i = 0; i < 5; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(G_TEXT, "Group " + i);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < 5; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(C_TEXT1, "Child " + j);
                curChildMap.put(C_TEXT2, "Child " + j);
            }
            childData.add(children);
        }

        adapter = new ExAdapter(ExListView.this);
        exList = (ExpandableListView) findViewById(R.id.list);
        exList.setAdapter(adapter);

        exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        exList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Map<String, String> curChildMap = childData.get(groupPosition).get(childPosition);
                curChildMap.put(C_TEXT1, "click " + curChildMap.get(C_TEXT1));
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        exList.setGroupIndicator(null);
        exList.setDivider(null);
    }

    class ExAdapter extends BaseExpandableListAdapter {
        ExListView exListView;

        public ExAdapter(ExListView exListView) {
            super();
            this.exListView = exListView;
        }

        @Override
        public int getGroupCount() {
            return groupData.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupData.get(groupPosition).get(G_TEXT).toString();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.ex_listview, null);
            }
            TextView title = (TextView) view.findViewById(R.id.content_001);
            title.setText(getGroup(groupPosition).toString());
            ImageView image = (ImageView) view.findViewById(R.id.tubiao);
            if (isExpanded) {
                image.setImageResource(R.drawable.btn_browser2);
            } else {
                image.setImageResource(R.drawable.btn_browser);
            }
            return view;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childData.get(groupPosition).size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childData.get(groupPosition).get(childPosition).get(C_TEXT1).toString();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.ex_list_item, null);
            }
            final TextView title = (TextView) view.findViewById(R.id.child_text);
            title.setText(childData.get(groupPosition).get(childPosition).get(C_TEXT1).toString());
            final TextView title2 = (TextView) view.findViewById(R.id.child_text2);
            title2.setText(childData.get(groupPosition).get(childPosition).get(C_TEXT2).toString());

            return view;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
