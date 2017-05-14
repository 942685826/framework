package com.yaxon.frameWork.view.image;

import android.app.Activity;
import android.graphics.*;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.VideoView;

import java.util.ArrayList;

/**
 * @author guojiaping
 * @version 2017/5/3 创建<br>
 */
public class ShotUtils {

    /**
     * 根据指定的Activity截图（带空白的状态栏）
     *
     * @param context 要截图的Activity
     * @return Bitmap
     */
    public static Bitmap shotActivity(Activity context) {
        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 根据指定的Activity截图（去除状态栏）
     *
     * @param activity 要截图的Activity
     * @return Bitmap
     */
    public static Bitmap shotActivityNoStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        //获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        //运行当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeights,
                widths, heights - statusBarHeights);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 根据指定的view截图
     *
     * @param view 要截图的view
     * @return bitmap
     */
    public static Bitmap getViewBitmap(View view) {
        if (null == view) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
            view.layout((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getMeasuredWidth(),
                    (int) view.getY() + view.getMeasuredHeight());
        } else {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * scrollview截屏
     *
     * @param scrollView 要截图的scrollview
     * @return
     */
    public static Bitmap shotScrollView(ScrollView scrollView) {
        int height = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            height += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), height, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * listView截图
     *
     * @param listView 要截图的ListView
     * @return
     */
    public static Bitmap shotListView(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int itemsCount = adapter.getCount();
        int allItemsHeight = 0;

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < itemsCount; i++) {
            View childView = adapter.getView(i, null, listView);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bitmaps.add(childView.getDrawingCache());
            allItemsHeight += childView.getMeasuredHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(listView.getMeasuredWidth(), allItemsHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int itemHeight = 0;
        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bmp = bitmaps.get(i);
            canvas.drawBitmap(bmp, 0, itemHeight, paint);
            itemHeight += bmp.getHeight();
            bmp.recycle();
        }
        return bitmap;
    }

    /**
     * 截取webview可视区域的截图
     *
     * @param webView 前提：webview要设置webview.setDrawingCacheEnable(true)
     * @return
     */
    private Bitmap captureWebViewVisibleSize(WebView webView) {
        Bitmap bitmap = webView.getDrawingCache();
        return bitmap;
    }

    /**
     * 截取webview快照（webview加载的整个内容的大小）
     *
     * @param webView
     * @return
     */
    private Bitmap captureWebView(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bitmap = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        snapShot.draw(canvas);
        return bitmap;
    }

    /**
     * RecyclerView截屏
     *
     * @param view 要截图的RecyclerView
     * @return Bitmap
     */
//    public static Bitmap shotRecyclerView(RecyclerView view) {
//        RecyclerView.Adapter adapter = view.getAdapter();
//        Bitmap bigBitmap = null;
//        if (adapter != null) {
//            int size = adapter.getItemCount();
//            int height = 0;
//            Paint paint = new Paint();
//            int iHeight = 0;
//            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//
//            // Use 1/8th of the available memory for this memory cache.
//            final int cacheSize = maxMemory / 8;
//            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
//            for (int i = 0; i < size; i++) {
//                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
//                adapter.onBindViewHolder(holder, i);
//                holder.itemView.measure(
//                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
//                        holder.itemView.getMeasuredHeight());
//                holder.itemView.setDrawingCacheEnabled(true);
//                holder.itemView.buildDrawingCache();
//                Bitmap drawingCache = holder.itemView.getDrawingCache();
//                if (drawingCache != null) {
//
//                    bitmaCache.put(String.valueOf(i), drawingCache);
//                }
//                height += holder.itemView.getMeasuredHeight();
//            }
//
//            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
//            Canvas bigCanvas = new Canvas(bigBitmap);
//            Drawable lBackground = view.getBackground();
//            if (lBackground instanceof ColorDrawable) {
//                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
//                int lColor = lColorDrawable.getColor();
//                bigCanvas.drawColor(lColor);
//            }
//
//            for (int i = 0; i < size; i++) {
//                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
//                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
//                iHeight += bitmap.getHeight();
//                bitmap.recycle();
//            }
//        }
//        return bigBitmap;
//    }

}
