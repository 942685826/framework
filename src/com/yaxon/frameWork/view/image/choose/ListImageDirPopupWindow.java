package com.yaxon.frameWork.view.image.choose;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.adapter.CommonAdapter;
import com.yaxon.frameWork.adapter.CommonViewHold;
import com.yaxon.frameWork.view.image.ImageLoader;

import java.util.List;

/**
 * @author guojiaping
 * @version 2016/11/30 创建<br>
 */
public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder> {
    private ListView mListDir;

    public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFloder> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        mListDir.setAdapter(new CommonAdapter<ImageFloder>(mDatas, context,
                R.layout.list_dir_item) {
            @Override
            public void convert(CommonViewHold helper, ImageFloder item) {
                helper.setText(R.id.id_dir_item_name, item.getName());
                ImageView imageView = helper.getView(R.id.id_dir_item_image);
                ImageLoader.getInstance(3, ImageLoader.Type.FIFO).loadImage(item.getFirstImagePath(), imageView);
                helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
            }

        });
    }

    public interface OnImageDirSelected {
        void selected(ImageFloder floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mDatas.get(position));
                }
            }
        });
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
        // TODO Auto-generated method stub
    }

}
