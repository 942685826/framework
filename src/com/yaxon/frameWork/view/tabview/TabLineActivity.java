package com.yaxon.frameWork.view.tabview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jauker.widget.BadgeView;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.fargment.ChatMainTabFragment;
import com.yaxon.frameWork.fargment.ContactMainTabFragment;
import com.yaxon.frameWork.fargment.FriendMainTabFragment;
import com.yaxon.frameWork.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部导航栏滑动
 *
 * @author guojiaping
 * @version 2016-8-5 创建<br>
 */
public class TabLineActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;

    private TextView mChatTextView;
    private TextView mFriendTextView;
    private TextView mContactTextView;

    private LinearLayout mChatLinearLayout;
    private LinearLayout mFriendLinearLayout;
    private LinearLayout mContactLinearLayout;
    private BadgeView mBadgeView;

    private ImageView mTabLine;
    private int mScreen1_3;
    private int mCurrentPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_line_layout);
        initTabLine();
        initView();
        initData();
        initEvent();
    }

    private void initTabLine() {
        mTabLine = (ImageView) findViewById(R.id.id_iv_tabline);
        mScreen1_3 = ScreenUtils.getScreenWidth(this) / 3;
        ViewGroup.LayoutParams lp = mTabLine.getLayoutParams();
        lp.width = mScreen1_3;
        mTabLine.setLayoutParams(lp);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mChatTextView = (TextView) findViewById(R.id.id_tv_chat);
        mFriendTextView = (TextView) findViewById(R.id.id_tv_friend);
        mContactTextView = (TextView) findViewById(R.id.id_tv_contact);
        mChatLinearLayout = (LinearLayout) findViewById(R.id.id_ll_chat);
        mFriendLinearLayout = (LinearLayout) findViewById(R.id.id_ll_friend);
        mContactLinearLayout = (LinearLayout) findViewById(R.id.id_ll_contact);
    }

    private void initData() {
        mDatas = new ArrayList<Fragment>();

        ChatMainTabFragment tab01 = new ChatMainTabFragment();
        FriendMainTabFragment tab02 = new FriendMainTabFragment();
        ContactMainTabFragment tab03 = new ContactMainTabFragment();

        // 将Fragment加入存放Fragment的list
        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mDatas.get(i);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };
    }

    private void initEvent() {
        mChatLinearLayout.setOnClickListener(this);
        mFriendLinearLayout.setOnClickListener(this);
        mContactLinearLayout.setOnClickListener(this);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }


    private void resetTextView() {
        mChatTextView.setTextColor(Color.BLACK);
        mFriendTextView.setTextColor(Color.BLACK);
        mContactTextView.setTextColor(Color.BLACK);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPx) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLine
                .getLayoutParams();
        if (mCurrentPageIndex == 0 && position == 0)// 0->1：第0页到第1页
        {
            lp.leftMargin = (int) (positionOffset * mScreen1_3 + mCurrentPageIndex
                    * mScreen1_3);
        } else if (mCurrentPageIndex == 1 && position == 0)// 1->0：第1页到第0页
        {
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1)
                    * mScreen1_3);
        } else if (mCurrentPageIndex == 1 && position == 1) // 1->2：第1页到第2页
        {
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset
                    * mScreen1_3);
        } else if (mCurrentPageIndex == 2 && position == 1) // 2->1：第2页到第1页
        {
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1)
                    * mScreen1_3);
        }
        mTabLine.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        resetTextView();
        switch (position) {
            case 0:
                if (mBadgeView != null) {
                    mChatLinearLayout.removeView(mBadgeView);
                }
                mBadgeView = new BadgeView(TabLineActivity.this);
                mBadgeView.setBadgeCount(7);
                mChatLinearLayout.addView(mBadgeView);
                mChatTextView.setTextColor(Color.parseColor("#008000"));
                break;
            case 1:
                mFriendTextView.setTextColor(Color.parseColor("#008000"));
                break;
            case 2:
                mContactTextView.setTextColor(Color.parseColor("#008000"));
                break;
        }
        mCurrentPageIndex = position;
    }


    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    public void onClick(View v) {
        resetTextView();
        switch (v.getId()) {
            case R.id.id_ll_chat:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.id_ll_friend:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.id_ll_contact:
                mViewPager.setCurrentItem(2);
                break;
        }
    }
}