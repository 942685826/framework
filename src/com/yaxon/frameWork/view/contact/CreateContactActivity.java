package com.yaxon.frameWork.view.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.yaxon.frameWork.R;

/**
 * Company	yaxon
 *
 * @author guojiaping
 * @version 2015/11/29
 */
public class CreateContactActivity extends Activity implements View.OnClickListener {
    private EditText name, number1, number2, company, position;
    private Button cancel, confirm;
    private int type;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_contact_layout);
        initView();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        if (type != 1) {
            Bundle bundle = intent.getExtras();
            ContactBean contactBean = (ContactBean) bundle.get("contactBean");
            name.setText(contactBean.getName());
            for (int i = 0; i < contactBean.getPhones().size(); i++) {
                if (i == 0) {
                    number1.setText(contactBean.getPhones().get(i));
                } else {
                    number2.setText(contactBean.getPhones().get(i));
                }
            }
        }
    }

    private void initView() {
        name = (EditText) findViewById(R.id.name);
        number1 = (EditText) findViewById(R.id.number1);
        number2 = (EditText) findViewById(R.id.number2);
        company = (EditText) findViewById(R.id.company);
        position = (EditText) findViewById(R.id.position);
        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        imageView = (ImageView) findViewById(R.id.imageView);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.confirm:
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                ContentResolver resolver = getContentResolver();
                ContentValues values = new ContentValues();
                long contactId = ContentUris.parseId(resolver.insert(uri, values));
                uri = Uri.parse("content://com.android.contacts/data");
                values.put("raw_contact_id", contactId);
                values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/name");
                values.put("data1", "加平");
                resolver.insert(uri, values);
                values.clear();
                values.put("raw_contact_id", contactId);
                values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
                values.put("data1", "18650166821");
                resolver.insert(uri, values);
                values.clear();
            case R.id.imageView:
//                Intent cameraIntent = new Intent(Intent.ACTION_PICK);
//                cameraIntent.setType("/*");
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"myImage/image.jpg"));
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                startActivityForResult(cameraIntent, 1);
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/myImage/image.jpg");
                imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
//                String sdStatus = Environment.getExternalStorageState();
//                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//                    Log.v("TestFile",
//                            "SD card is not avaiable/writeable right now.");
//                    return;
//                }
//
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//                FileOutputStream b = null;
//                File file = new File("/sdcard/myImage/");
//                file.mkdirs();// 创建文件夹
//                String fileName = "/sdcard/myImage/111.jpg";
//
//                try {
//                    b = new FileOutputStream(fileName);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        b.flush();
//                        b.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                break;
        }
    }
}
