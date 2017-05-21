package com.yaxon.frameWork.view.scrollable.sample;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.yaxon.frameWork.view.scrollable.CanScrollVerticallyDelegate;


/**
 * @author guojiaping
 * @version 2017/5/19 创建<br>
 */
public abstract class BaseFragment extends Fragment implements CanScrollVerticallyDelegate {

    static final String ARG_COLOR = "arg.Color";

    protected <V> V findView(View view, int id) {
        //noinspection unchecked
        return (V) view.findViewById(id);
    }

    public abstract CharSequence getTitle(Resources r);

    public abstract String getSelfTag();

    protected int getColor() {
        return getArguments().getInt(ARG_COLOR);
    }

    @Override
    public void onViewCreated(View view, Bundle sis) {
        super.onViewCreated(view, sis);

        view.setBackgroundColor(getColor());
    }
}
