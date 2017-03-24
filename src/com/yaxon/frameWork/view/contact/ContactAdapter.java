package com.yaxon.frameWork.view.contact;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.view.sideview.PinnedHeaderListView;


import java.util.List;

/**
 * @author guojiaping
 * @version 15/11/26 创建<br>
 */
public class ContactAdapter extends BaseAdapter implements AbsListView.OnScrollListener, PinnedHeaderListView.PinnedHeaderAdapter, SectionIndexer {
    public int width = 0;
    ViewHolder viewHolder = null;
    private List<ContactBean> mData;
    private LayoutInflater mLayoutInflater;

    public ContactAdapter(Context pContext, List<ContactBean> pData) {
        mData = pData;
        mLayoutInflater = LayoutInflater.from(pContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.concact_sign_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_tab2_title);
            viewHolder.name = (TextView) convertView.findViewById(R.id.list_tab2_sign);
            width = viewHolder.name.getWidth();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 获取数据
        ContactBean itemEntity = (ContactBean) getItem(position);
        viewHolder.name.setText(itemEntity.getName());
        width = viewHolder.name.getWidth();
        if (needTitle(position)) {
            // 显示标题并设置内容
            viewHolder.title.setText(itemEntity.getTitle());
            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.title.setVisibility(View.GONE);
        }
        viewHolder.name.getWidth();
        return convertView;
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
            String sortStr = mData.get(i).getTitle();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mData.get(position).getTitle().charAt(0);
    }

    private class ViewHolder {
        TextView title;
        TextView name;
        ImageView carIcon;
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
