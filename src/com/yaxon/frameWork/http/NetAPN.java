/**
 * Copyright (C) 2012 XiaMen Yaxon NetWorks Co.,LTD.
 */

package com.yaxon.frameWork.http;

/**
 * 返回值类型
 *
 * @author: zzh 2013-3-22 创建<br>
 */
public interface NetAPN {
    String CMNET = "cmnet"; // 中国移动
    String CMWAP = "cmwap"; // 中国移动
    String CTNET = "ctnet"; // 中国电信
    String CTWAP = "ctwap"; // 中国电信
    String UNINET = "uninet"; // 中国联通
    String UNIWAP = "uniwap"; // 中国联通
    String G3NET = "3gnet"; // 中国联通
    String G3WAP = "3gwap"; // 中国联通
    String UNKNOWN = "unknown"; // 未知接入点
}
