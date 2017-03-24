package com.yaxon.frameWork.xml;

import java.util.List;

/**
 * @author gjp
 * @version 2017/1/13 创建<br>
 */
public class CANSetting {
    private byte channel;
    private byte braudRate;
    private byte frameType;
    private byte workMode;
    private List<FilterID> filterIDList;

    public List<FilterID> getFilterIDList() {
        return filterIDList;
    }

    public void setFilterIDList(List<FilterID> filterIDList) {
        this.filterIDList = filterIDList;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getBraudRate() {
        return braudRate;
    }

    public void setBraudRate(byte braudRate) {
        this.braudRate = braudRate;
    }

    public byte getFrameType() {
        return frameType;
    }

    public void setFrameType(byte frameType) {
        this.frameType = frameType;
    }

    public byte getWorkMode() {
        return workMode;
    }

    public void setWorkMode(byte workMode) {
        this.workMode = workMode;
    }




}
