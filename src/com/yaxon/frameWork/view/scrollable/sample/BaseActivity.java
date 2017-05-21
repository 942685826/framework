package com.yaxon.frameWork.view.scrollable.sample;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;

/**
 * @author guojiaping
 * @version 2017/5/19
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected <V> V findView(Activity activity, int id) {
        //noinspection unchecked
        return (V) activity.findViewById(id);
    }
}
