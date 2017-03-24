package com.yaxon.frameWork.xml;

import android.content.Context;
import android.content.res.XmlResourceParser;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gjp
 * @version 2017/1/13 创建<br>
 */
public class XmlParse {

    /**
     * 通过xmlResource解析
     *
     * @param context 上下文
     * @param id      XML文件
     * @return
     */
    public static CANSetting xmlResourceParse(Context context, int id) {
        XmlResourceParser xrp = context.getResources().getXml(id);
        List<FilterID> filterIDList = new ArrayList<>();
        CANSetting channel = null;
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                // 如果遇到了开始标签
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("filterid")) {
                        FilterID filterID = new FilterID();
                        try {
                            String str = xrp.getAttributeValue(null, "id");
                            filterID.setId(Integer.valueOf(str, 16));
                            int v = Long.valueOf(xrp.getAttributeValue(null, "mask"), 16).intValue();
                            filterID.setMask(v);
                            int rptPeriod = Integer.valueOf(xrp.getAttributeValue(null, "rptPeriod"));
                            filterID.setRptPeriod(rptPeriod);
                            filterIDList.add(filterID);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else if (tagName.equals("channel")) {
                        //  channel参数
                        channel = new CANSetting();
                        try {
                            channel.setChannel(Byte.parseByte(xrp.getAttributeValue(null, "no")));
                            channel.setBraudRate(Byte.parseByte(xrp.getAttributeValue(null, "braudRate")));
                            channel.setFrameType(Byte.parseByte(xrp.getAttributeValue(null, "frameType")));
                            channel.setWorkMode(Byte.parseByte(xrp.getAttributeValue(null, "workMode")));
                            //if (channelList == null) channelList = new LinkedList<CANSetting>();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (xrp.getEventType() == XmlResourceParser.END_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("channel")) {
                        if (channel != null) {
                            channel.setFilterIDList(filterIDList);
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return channel;
    }

    public static void xmlPullParse() {

    }
}
