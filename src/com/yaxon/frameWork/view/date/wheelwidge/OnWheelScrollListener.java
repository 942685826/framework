package com.yaxon.frameWork.view.date.wheelwidge;

/**
 * Wheel scrolled listener interface.
 *
 * @author guojiaping 2016-10-28 创建<br>
 */
public interface OnWheelScrollListener {
    /**
     * Callback method to be invoked when scrolling started.
     *
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     *
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingFinished(WheelView wheel);
}
