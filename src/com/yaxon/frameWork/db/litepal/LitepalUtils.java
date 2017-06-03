package com.yaxon.frameWork.db.litepal;

import android.content.ContentValues;
import com.yaxon.frameWork.debug.LogUtils;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * litepal 添加相关的jar包，在assets文件下添加映射文件，
 * 清单文件添加org.litepal.LitePalApplication（或者application集成）
 * 实体类继承DataSupport
 *
 * @author guojiaping
 * @version 2016/7/12 创建<br>
 */
public class LitepalUtils {

    public static void insert() {
        UserTable user = new UserTable();
        user.setAge(47);
        user.setName("guo");
        user.setSex(1);
        user.save();
    }

    public static void update() {
        ContentValues values = new ContentValues();
        values.put("age", 26);
        DataSupport.update(UserTable.class, values, 1);
    }

    public static void getUser() {
        List<UserTable> info = DataSupport.where("1").limit(10).find(UserTable.class);
        if (info != null && info.size() > 0) {
            for (UserTable use : info) {
                LogUtils.d("litapal", use.getName() + "," + use.getId() + "," + use.getAge() + "," + use.getSex());
            }
        }
    }
}
