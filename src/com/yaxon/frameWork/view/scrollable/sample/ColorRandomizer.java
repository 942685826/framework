package com.yaxon.frameWork.view.scrollable.sample;

import android.os.SystemClock;

import java.util.Random;

/**
 * @author guojiaping
 * @version 2017/5/19 创建<br>
 */
public class ColorRandomizer {

    private final Random mRandom;
    private final int[] mColors;
    private final int mMax;

    public ColorRandomizer(int[] colors) {
        this.mRandom = new Random(SystemClock.elapsedRealtime());
        this.mColors = colors;
        this.mMax = mColors.length - 1;
    }

    public int next() {
        final int index = mRandom.nextInt(mMax);
        return mColors[index];
    }
}
