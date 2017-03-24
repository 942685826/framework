package com.yaxon.frameWork.view.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yaxon.frameWork.R;


import java.util.ArrayList;

/**
 * @author guojiaping
 * @version 2015/11/29
 */
public class ContactDetailActivity extends Activity implements AdapterView.OnItemClickListener {
    private ContactBean contactBean;
    private ListView listView;
    private TextView textView;
    private RelativeLayout editContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail_layout);
        contactBean = (ContactBean) getIntent().getExtras().getSerializable("contactBean");
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.name);
        textView.setText(contactBean.getName());
        getPhoneContacts(contactBean.getId());
        editContact = (RelativeLayout) findViewById(R.id.edit_contact);
        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailActivity.this, CreateContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactBean", contactBean);
                intent.putExtras(bundle);
                intent.putExtra("type", 2);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void getPhoneContacts(int id) {
        ContentResolver resolver = getContentResolver();
        Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
        contactBean.setPhones(new ArrayList<String>());
        while (phone.moveToNext()) { //取得电话号码(可能存在多个号码)
            int phoneFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNumber = phone.getString(phoneFieldColumnIndex);
            contactBean.getPhones().add(phoneNumber);
        }
        phone.close();
        ContactDetailAdapter adapter = new ContactDetailAdapter(contactBean.getPhones(), this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactBean.getPhones().get(position)));
        startActivity(intent);
    }
}
