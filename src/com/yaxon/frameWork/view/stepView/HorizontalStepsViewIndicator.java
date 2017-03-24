package com.yaxon.frameWork.view.stepView;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.yaxon.frameWork.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guojiaping
 * @version 2017-3-23 创建<br>
 */
public class HorizontalStepsViewIndicator extends View{
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
            getResources().getDisplayMetrics());//定义默认的高度
    private float mCompletedLineHeight;//完成线的高度
    private float  mCircleRadius;//圆的半径

    private Drawable mCompleteIcon;//完成的默认图片
    private Drawable mAttentionIcon;//正在进行的默认图片
    private Drawable mDefaultIcon;//默认的背景图
    private Drawable mLastCompleteIcon;//终点未完成图片
    private Drawable mLastUnCompleteIcon;//终点完成图片

    private float mCenterY;//该view的Y轴中间位置
    private float mLeftY;//左上方的Y位置
    private float mRightY;//右下方的Y位置

    private List<StepBean> mStepBeanList;//当前有几步流程
    private int mStepNum = 0;
    private float mLinePadding;//俩条连线之间的距离

    private List<Float> mCircleCenterPointPositionList;//定义所有圆的圆心点位置集合
    private Paint mUnCompletedPaint;//未完成paint
    private Paint mCompletedPaint;//完成paint
    private int mUnCompletedLineColor;//定义默认未完成的颜色
    private int mCompletedLineColor;//定义默认完成的颜色
    private PathEffect  mEffects;
    private int mCompletingPosition;//正在进行position

    private Path mPath;
    private OnDrawIndicatorListener mOnDrawListener;
    private int screenWidth;

    /**
     * 设置监听
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener){
        mOnDrawListener = onDrawListener;
    }

    /**
     * 设置对view监听
     */
    public interface OnDrawIndicatorListener{
        void ondrawIndicator();
    }

    /**
     * 获取圆的半径
     * @return
     */
    public float getCircleRadius() {
        return mCircleRadius;
    }
    public HorizontalStepsViewIndicator(Context context) {
        this(context, null);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mStepBeanList = new ArrayList<>();
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);
        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setStrokeWidth(2);
        mCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        mCompletedLineHeight = 0.03f * defaultStepIndicatorNum;//已经完成线的宽高
        mCircleRadius = 0.28f * defaultStepIndicatorNum;//圆的半径
        mLinePadding = 1.0f * defaultStepIndicatorNum;//线与线之间的间距

        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.dir_choose);
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.dir_choose);
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.dir_choose);
        mLastCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.dir_choose);
        mLastUnCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.dir_choose);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = defaultStepIndicatorNum * 2;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = defaultStepIndicatorNum * 2;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height,MeasureSpec.getSize(heightMeasureSpec));
        }
        width = (int) (mStepNum * mCircleRadius * 2 - (mStepNum - 1 ) * mLinePadding);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取中间的高度,目的是为了让改view绘制的线和圆在改view垂直居中
        mCenterY = 0.5f * getHeight();
        //获取左上方Y的位置，获取改点的意义是为了方便画矩形左上方Y位置
        mLeftY = mCenterY - mCompletedLineHeight / 2;
        //获取右下方Y的位置，获取改点的意义是为了方便画矩形右下方Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;

        mCircleCenterPointPositionList.clear();
        for(int i = 0; i < mStepNum; i++) {
            //先计算全部最左边的padding值（getWidth() - (圆形直径 + 俩圆之间距离) * 2）
            float paddingLeft = (screenWidth - mStepNum * mCircleRadius * 2 -
                    (mStepNum - 1) * mLinePadding) / 2;
            mCircleCenterPointPositionList.add(paddingLeft + mCircleRadius +
                    i * mCircleRadius * 2 + i * mLinePadding);
        }

        if(mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        //画线
        for(int i = 0 ; i < mCircleCenterPointPositionList.size() - 1; i++) {
            final  float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            final  float afterComplectedXPosition = mCircleCenterPointPositionList.get(i + 1);
            if(i <= mCompletingPosition && mStepBeanList.get(0).getState() != StepBean.STEP_UNDO) {//判断在完成之前的所有点
                //判断在完成之前的所有点，画完成的线，这里是矩形，很细的矩形。类似线，为了做区分，好看些
                canvas.drawRect(preComplectedXPosition + mCircleRadius - 10, mLeftY,
                        afterComplectedXPosition - mCircleRadius + 10, mRightY, mCompletedPaint);
            } else {
                mPath.moveTo(preComplectedXPosition + mCircleRadius, mCenterY);
                mPath.lineTo(afterComplectedXPosition - mCircleRadius, mCenterY);
                canvas.drawPath(mPath, mUnCompletedPaint);
            }
        }

        //画图标
        for(int i = 0; i <mCircleCenterPointPositionList.size(); i++) {
            final float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition - mCircleRadius),
                    (int)(mCenterY - mCircleRadius),(int) (currentComplectedXPosition + mCircleRadius),
                    (int)(mCenterY + mCircleRadius));
            StepBean stepBean = mStepBeanList.get(i);
            if(stepBean.getState() == StepBean.STEP_UNDO) {
                mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            } else if(stepBean.getState() == StepBean.STEP_CURRENT) {
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(currentComplectedXPosition, mCenterY, mCircleRadius * 1.1f, mCompletedPaint);
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            } else if(stepBean.getState() == StepBean.STEP_COMPLETED) {
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            } else if(stepBean.getState() == StepBean.STEP_LAST_COMPLETED) {
                mLastCompleteIcon.setBounds(rect);
                mLastCompleteIcon.draw(canvas);
            } else if(stepBean.getState() == StepBean.STEP_LAST_UNCOMPLETED) {
                mLastUnCompleteIcon.setBounds(rect);
                mLastUnCompleteIcon.draw(canvas);
            }
        }
    }

    public List<Float> getCitcleCenterPointPositionList() {
        return  mCircleCenterPointPositionList;
    }

    /**
     * 设置流程步数
     * @param stepsBeanList
     */
    public void setStepNum(List<StepBean> stepsBeanList) {
        this.mStepBeanList = stepsBeanList;
        mStepNum = mStepBeanList.size();

        if(mStepBeanList != null && mStepBeanList.size() > 0) {
            for(int i = 0; i < mStepNum; i++) {
                StepBean stepsBean = mStepBeanList.get(i);
                if(stepsBean.getState() == StepBean.STEP_COMPLETED) {
                    mCompletingPosition = i;
                }
            }
        }
        requestLayout();
    }

    /**
     * 设置未完成的颜色
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor) {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor){
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * 设置默认图片
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon){
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon){
        this.mCompleteIcon = completeIcon;
    }

    public void setLastCompleteIcon(Drawable lastcompleteIcon){
        this.mLastCompleteIcon = lastcompleteIcon;
    }

    public void setLastUnCompleteIcon(Drawable lastUnCompleteIcon){
        this.mLastUnCompleteIcon = lastUnCompleteIcon;
    }

    /**
     * 设置正在进行中的图片
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon){
        this.mAttentionIcon = attentionIcon;
    }

}
