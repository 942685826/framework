package com.yaxon.frameWork.view.contact;

import java.io.Serializable;
import java.util.List;

/**
 * @author guojiaping
 * @version 2015/11/29
 */
public class ContactBean implements Serializable {
    private int id;
    private String title;
    private String name;
    private List<String> phones;

    public ContactBean() {
    }

    public ContactBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
