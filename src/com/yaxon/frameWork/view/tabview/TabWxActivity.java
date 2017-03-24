package com.yaxon.frameWork.view.tabview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.nineoldandroids.view.ViewHelper;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.fargment.ChatMainTabFragment;
import com.yaxon.frameWork.fargment.ContactMainTabFragment;
import com.yaxon.frameWork.fargment.FriendMainTabFragment;
import com.yaxon.frameWork.fargment.MeMainTabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guojiaping
 * @version 2015-8-4 创建<br>
 */
public class TabWxActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<>();
    private ChatMainTabFragment chatMainTabFragment;
    private ContactMainTabFragment contactMainTabFragment;
    private FriendMainTabFragment friendMainTabFragment;
    private MeMainTabFragment meMainTabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_wx_layout);
        initView();
        initEvent();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        one.setIconAlpha(1.0f);
        mViewPager.setOffscreenPageLimit(mTabIndicators.size());
    }

    /**
     * 初始化所有事件
     */
    private void initEvent() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        clickTab(view);
    }

    /**
     * 点击Tab按钮
     */
    private void clickTab(View v) {
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    private boolean isSmall(float positionOffset) {
        return Math.abs(positionOffset) < 0.0001;
    }

    protected void animateFade(View left, View right, float positionOffset) {
        if (left != null) {
            ViewHelper.setAlpha(left, 1 - positionOffset);
        }
        if (right != null) {
            ViewHelper.setAlpha(right, positionOffset);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
        float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;

        animateFade(mViewPager.getChildAt(position), mViewPager.getChildAt(position + 1), effectOffset);
    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTabIndicators.size();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (chatMainTabFragment == null) {
                        chatMainTabFragment = new ChatMainTabFragment();
                    }
                    return chatMainTabFragment;
                case 1:
                    if (contactMainTabFragment == null) {
                        contactMainTabFragment = new ContactMainTabFragment();
                    }
                    return contactMainTabFragment;

                case 2:
                    if (friendMainTabFragment == null) {
                        friendMainTabFragment = new FriendMainTabFragment();
                    }
                    return friendMainTabFragment;

                case 3:
                    if (meMainTabFragment == null) {
                        meMainTabFragment = new MeMainTabFragment();
                    }
                    return meMainTabFragment;
                default:
                    return null;
            }
        }
    }
}
