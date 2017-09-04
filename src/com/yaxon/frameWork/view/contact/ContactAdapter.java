package com.yaxon.frameWork.view.contact;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.adapter.CommonAdapter;
import com.yaxon.frameWork.adapter.CommonViewHold;
import com.yaxon.frameWork.view.sideview.PinnedHeaderListView;

import java.util.List;

/**
 * @author guojiaping
 * @version 15/11/26 创建<br>
 */
public class ContactAdapter extends CommonAdapter<ContactBean> implements AbsListView.OnScrollListener, PinnedHeaderListView.PinnedHeaderAdapter, SectionIndexer {


    public ContactAdapter(Context pContext, List<ContactBean> pData) {
        super(pData, pContext, R.layout.concact_sign_item);
    }

    @Override
    public void convert(CommonViewHold commonViewHold, ContactBean itemEntity) {
        commonViewHold.setText(R.id.list_tab2_sign, itemEntity.getName());
        TextView textView = commonViewHold.getView(R.id.list_tab2_title);
        if (needTitle(getListData().indexOf(itemEntity))) {
            // 显示标题并设置内容
            textView.setText(itemEntity.getTitle());
            textView.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
        }
    }

    @Override
    public int getPinnedHeaderState(int position) {
        if (getCount() == 0 || position < 0) {
            return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_GONE;
        }

        if (position >= 0 && isMove(position)) {
            return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
        }

        return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
    }

    @Override
    public void configurePinnedHeader(View headerView, int position, int alpaha) {
        if (position >= 0) {
            // 设置标题的内容
            ContactBean itemEntity = (ContactBean) getItem(position);
            String headerValue = itemEntity.getTitle();
            if (!TextUtils.isEmpty(headerValue)) {

                TextView headerTextView = (TextView) headerView.findViewById(R.id.list_tab2_header);
                headerTextView.setText(headerValue);
            }
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getListData().get(i).getTitle();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return getListData().get(position).getTitle().charAt(0);
    }

    /**
     * 判断是否需要显示标题
     */
    private boolean needTitle(int position) {
        // 第一个肯定是分类
        if (position == 0) {
            return true;
        }

        // 异常处理
        if (position < 0) {
            return false;
        }

        // 当前  // 上一个
        ContactBean currentEntity = (ContactBean) getItem(position - 1);

        ContactBean previousEntity = (ContactBean) getItem(position);
        if (null == currentEntity || null == previousEntity) {
            return false;
        }

        String currentTitle = currentEntity.getTitle();
        String previousTitle = previousEntity.getTitle();
        if (null == previousTitle || null == currentTitle) {
            return false;
        }

        // 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
        return !currentTitle.equals(previousTitle);

    }

    private boolean isMove(int position) {
        // 获取当前与下一项
        ContactBean currentEntity = (ContactBean) getItem(position);
        ContactBean nextEntity = (ContactBean) getItem(position + 1);
        if (null == currentEntity || null == nextEntity) {
            return false;
        }

        // 获取两项header内容
        String currentTitle = currentEntity.getTitle();
        String nextTitle = nextEntity.getTitle();
        if (null == currentTitle || null == nextTitle) {
            return false;
        }

        // 当前不等于下一项header，当前项需要移动了
        return !currentTitle.equals(nextTitle);

    }
}
