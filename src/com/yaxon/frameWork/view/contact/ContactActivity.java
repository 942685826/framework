package com.yaxon.frameWork.view.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.view.sideview.CharacterParser;
import com.yaxon.frameWork.view.sideview.PinnedHeaderListView;
import com.yaxon.frameWork.view.sideview.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author guojiaping
 * @version 15/11/26 创建<br>
 */
public class ContactActivity extends Activity implements AdapterView.OnItemClickListener {
    private CharacterParser characterParser;//汉字转换为拼音类
    private List<ContactBean> contactBeans;
    private PinnedHeaderListView listView;
    private ContactAdapter contactAdapter;
    private SideBar sideBar;
    private RelativeLayout createContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        initView();
        initData();
    }

    private void initView() {
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        TextView toastDialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(toastDialog);
        listView = (PinnedHeaderListView) findViewById(R.id.listView);
        View headView = LayoutInflater.from(this).inflate(R.layout.listview_item_header, listView, false);
        listView.setPinnedHeader(headView);
        createContact = (RelativeLayout) findViewById(R.id.new_contact);
        createContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, CreateContactActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initData() {
        PinyinComparator pinyinComparator = new PinyinComparator();
        characterParser = CharacterParser.getInstance();
        contactBeans = getPhoneContacts();
        contactBeans = filledData(contactBeans);
        Collections.sort(contactBeans, pinyinComparator);
        contactAdapter = new ContactAdapter(this, contactBeans);
        listView.setAdapter(contactAdapter);
        listView.setOnScrollListener(contactAdapter);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = contactAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
    }

    private List<ContactBean> filledData(List<ContactBean> list) {
        for (int i = 0; i < list.size(); i++) {
            //汉字转换成拼音
            String name = list.get(i).getName();
            String pinyin = characterParser.getSelling(list.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                list.get(i).setTitle(sortString.toUpperCase());
            } else {
                list.get(i).setTitle("#");
            }
            if (list.get(i).getName().equals("")) {
                pinyin = characterParser.getSelling(list.get(i).getName());
                pinyin = "";
            }
        }
        return list;
    }

    private List<ContactBean> getPhoneContacts() {
        Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问所有联系人
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        List<ContactBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int contactsId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            ContactBean contactBean = new ContactBean(contactsId, name);
            list.add(contactBean);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactBean contactBean = contactBeans.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contactBean", contactBean);
        Intent intent = new Intent(ContactActivity.this, ContactDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        new MenuInflater(this).inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuitem_del:
//                Toast.makeText(ContactActivity.this, "删除", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.menuitem_update:
//                Toast.makeText(ContactActivity.this, "修改", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.menuitem_look:
//                Toast.makeText(ContactActivity.this, "查看", Toast.LENGTH_LONG).show();
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }
}
