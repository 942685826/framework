package com.yaxon.frameWork.db.litepal;

import org.litepal.crud.DataSupport;

/**
 * @author guojiaping
 * @version 2016/7/12 创建<br>
 */
public class UserTable extends DataSupport {
    private int id;
    private String name;
    private int age;
    private int sex;

    public UserTable() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
