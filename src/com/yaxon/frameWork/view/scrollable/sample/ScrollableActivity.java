package com.yaxon.frameWork.view.scrollable.sample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.view.scrollable.CanScrollVerticallyDelegate;
import com.yaxon.frameWork.view.scrollable.OnScrollChangedListener;
import com.yaxon.frameWork.view.scrollable.ScrollableLayout;
import com.yaxon.frameWork.view.scrollable.TabsLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScrollableActivity extends BaseActivity implements ConfigurationFragmentCallbacks {

    private static final String ARG_LAST_SCROLL_Y = "arg.LastScrollY";

    private ScrollableLayout mScrollableLayout;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollable_layout);
        initStatusBar();
//		setNewActionHeight();


        int statusBarHeight = 0;
        Rect localRect = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusBarHeight = localRect.top;

        LinearLayout action_bar = (LinearLayout) findViewById(R.id.my_actionbar);
        LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) action_bar
                .getLayoutParams();
        linearLayoutParams.height = 55 - statusBarHeight;
        action_bar.setLayoutParams(linearLayoutParams);


        final View header = findViewById(R.id.header);
        final TabsLayout tabs = findView(this, R.id.tabs);
        mScrollableLayout = findView(this, R.id.scrollable_layout);
        mScrollableLayout.setDraggableView(tabs);
        final ViewPager viewPager = findView(this, R.id.view_pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager(), getResources(), getFragments());
        viewPager.setAdapter(adapter);

        tabs.setViewPager(viewPager);

        mScrollableLayout
                .setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
                    @Override
                    public boolean canScrollVertically(int direction) {
                        return adapter.canScrollVertically(
                                viewPager.getCurrentItem(), direction);
                    }
                });

        mScrollableLayout
                .setOnScrollChangedListener(new OnScrollChangedListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onScrollChanged(int y, int oldY, int maxY) {

                        final float tabsTranslationY;
                        if (y < maxY) {
                            tabsTranslationY = .0F;
                        } else {
                            tabsTranslationY = y - maxY;
                        }

                        tabs.setTranslationY(tabsTranslationY);

                        header.setTranslationY(y / 2);
                    }
                });

        if (savedInstanceState != null) {
            final int y = savedInstanceState.getInt(ARG_LAST_SCROLL_Y);
            mScrollableLayout.post(new Runnable() {
                @Override
                public void run() {
                    mScrollableLayout.scrollTo(0, y);
                }
            });
        }
    }

    private void setNewActionHeight() {
        int statusBarHeight = 0;
        Rect localRect = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusBarHeight = localRect.top;

        LinearLayout action_bar = (LinearLayout) findViewById(R.id.my_actionbar);
        LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) action_bar
                .getLayoutParams();
        linearLayoutParams.height = 55 - statusBarHeight;
        action_bar.setLayoutParams(linearLayoutParams);

    }

    @SuppressLint("NewApi")
    public void initStatusBar() {
        // 判断手机当前的android版本，高于19则调用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.header_background);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_LAST_SCROLL_Y, mScrollableLayout.getScrollY());
        super.onSaveInstanceState(outState);
    }

    private List<BaseFragment> getFragments() {

        final FragmentManager manager = getSupportFragmentManager();
        final ColorRandomizer colorRandomizer = new ColorRandomizer(
                getResources().getIntArray(R.array.fragment_colors));
        final List<BaseFragment> list = new ArrayList<>();

        ConfigurationFragment configurationFragment = (ConfigurationFragment) manager
                .findFragmentByTag(ConfigurationFragment.TAG);
        if (configurationFragment == null) {
            configurationFragment = ConfigurationFragment
                    .newInstance(colorRandomizer.next());
        }

        ListViewFragment listViewFragment = (ListViewFragment) manager
                .findFragmentByTag(ListViewFragment.TAG);
        if (listViewFragment == null) {
            listViewFragment = ListViewFragment.newInstance(colorRandomizer
                    .next());
        }

        ScrollViewFragment scrollViewFragment = (ScrollViewFragment) manager
                .findFragmentByTag(ScrollViewFragment.TAG);
        if (scrollViewFragment == null) {
            scrollViewFragment = ScrollViewFragment.newInstance(colorRandomizer
                    .next());
        }

        RecyclerViewFragment recyclerViewFragment = (RecyclerViewFragment) manager
                .findFragmentByTag(RecyclerViewFragment.TAG);
        if (recyclerViewFragment == null) {
            recyclerViewFragment = RecyclerViewFragment
                    .newInstance(colorRandomizer.next());
        }

        Collections.addAll(list, configurationFragment, listViewFragment,
                scrollViewFragment, recyclerViewFragment);

        return list;
    }

    @Override
    public void onFrictionChanged(float friction) {
        mScrollableLayout.setFriction(friction);
    }

    @Override
    public void openDialog(float friction) {
        final ScrollableDialog dialog = ScrollableDialog.newInstance(friction);
        dialog.show(getSupportFragmentManager(), null);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapterExt {

        private final Resources mResources;
        private final List<BaseFragment> mFragments;

        public ViewPagerAdapter(FragmentManager fm, Resources r,
                                List<BaseFragment> fragments) {
            super(fm);
            this.mResources = r;
            this.mFragments = fragments;
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments != null ? mFragments.size() : 0;
        }

        @Override
        public String makeFragmentTag(int position) {
            return mFragments.get(position).getSelfTag();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle(mResources);
        }

        boolean canScrollVertically(int position, int direction) {
            return getItem(position).canScrollVertically(direction);
        }
    }

}
