package com.yaxon.frameWork.xml;

import android.os.Parcel;

/**
 *@author gjp
 * @version 2017/1/13 创建<br>
 */
public class FilterID {
    public int id; // ID值
    public int mask; // 滤波掩码
    public int rptPeriod; // 上报周期

    public FilterID() {
    }

    public FilterID(Parcel in){
        id = in.readInt();
        mask = in.readInt();
        rptPeriod = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getRptPeriod() {
        return rptPeriod;
    }

    public void setRptPeriod(int rptPeriod) {
        this.rptPeriod = rptPeriod;
    }

}
