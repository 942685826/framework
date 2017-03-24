package com.yaxon.frameWork.view.contact;


import java.util.Comparator;

/**
 * Company	yaxon
 *
 * @author guojiaping
 * @version 2015/11/29
 */
public class PinyinComparator implements Comparator<ContactBean> {

    public int compare(ContactBean o1, ContactBean o2) {
        if (o1.getTitle().equals("@")
                || o2.getTitle().equals("#")) {
            return -1;
        } else if (o1.getTitle().equals("#")
                || o2.getTitle().equals("@")) {
            return 1;
        } else {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }

}
