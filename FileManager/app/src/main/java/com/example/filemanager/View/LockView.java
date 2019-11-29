package com.example.filemanager.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LockView extends View {

    private int mSpacing;     //点之间的间距，横竖间距相同的
    private int mBaseX;       //第一个点的X坐标
    private int mBaseY;       //第一个点的Y坐标
    private int mRadius = 15;       //点半径
    private int mDotCount = 3;      //一行或一列的点的数量
    private int mWidth;
    private int mHeight;
    private String mSetPassword;
    private Paint mPathPaint;
    private Paint mDotPaint;
    private Path mConnectPath;
    private Path mUnconnectPath;
    private LockViewListener mListener;
    private ArrayList<Dot> mDotList = new ArrayList<>();
    private List<Integer> mSelectedDotNumberList = new ArrayList<>();

    public LockView(Context context) {
        super(context);
        init();
    }

    public LockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        mSpacing = mWidth / 3;
        mBaseX = mSpacing / 2;
        mBaseY = mHeight / 3 / 2;
        initDot();
    }

    public void init(){
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);      //设置抗锯齿
        mPathPaint.setDither(true);     //设置防抖动
        mPathPaint.setColor(Color.WHITE);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(8);

        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);      //设置抗锯齿
        mDotPaint.setDither(true);
        mDotPaint.setColor(Color.WHITE);

        mConnectPath = new Path();
        mUnconnectPath = new Path();
    }

    private void initDot(){
        int x = mBaseX;
        int y = mBaseY;
        int mDotNumber = 1;
        for(int i = 0;i < mDotCount;i++,y+=mSpacing){
            for(int j = 0;j < mDotCount;j++,x+=mSpacing){
                Dot dot = new Dot(x,y,mDotNumber);
                mDotList.add(dot);
            }
            mDotNumber++;
            x = mBaseX;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        canvas.drawPath(mConnectPath,mPathPaint);
        canvas.drawPath(mUnconnectPath,mPathPaint);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                return true;
            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                return true;
        }
        return false;
    }

    private void drawCircle(Canvas canvas){
        for(Dot dot : mDotList){
            mDotPaint.setColor(dot.mColor);
            canvas.drawCircle(dot.x,dot.y,dot.mScale * mRadius,mDotPaint);
        }
    }

    private void handleActionDown(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        deleteSelectAndPath();      //删除选择的点及已经绘制的路径
        detectSelectAndAddDot(x,y);  //判断点及添加选择
    }

    private void deleteSelectAndPath(){
        mConnectPath.reset();      //清楚上次绘制的路径
        if(mSelectedDotNumberList.size() != 0){      //重置选择状态，清空选择列表
            for(Integer dotNumber : mSelectedDotNumberList){
                mDotList.get(dotNumber).mState = false;
                mDotList.get(dotNumber).mColor = Color.WHITE;
            }
            mSelectedDotNumberList.clear();
        }
        mPathPaint.setColor(Color.WHITE);
    }

    /*
    判断点击或滑动是否到点，调用doSelectedDotAnimation完成点击动画
     */
    private boolean detectSelectAndAddDot(float x,float y){
        for(int dotNumber = 0;dotNumber<mDotList.size();dotNumber++){
            Dot dot = mDotList.get(dotNumber);
            if(dot.mState == false && y < dot.y + mSpacing * 0.2 && y > dot.y - mSpacing * 0.2
                    && x <dot.x + mSpacing * 0.2 && x > dot.x - mSpacing * 0.2){
                doSelectedDotAnimation(dot);
                if(mSelectedDotNumberList.size() == 0){
                    mConnectPath.moveTo(dot.x,dot.y);
                }else {
                    mConnectPath.lineTo(dot.x,dot.y);
                }
                mUnconnectPath.reset();
                mUnconnectPath.moveTo(dot.x,dot.y);
                mUnconnectPath.lineTo(dot.x,dot.y);
                mSelectedDotNumberList.add(dotNumber);
                dot.mState = true;
                return true;
            }
        }
        return false;
    }

    /*
    完成点击或滑动到点的动画
     */
    private void doSelectedDotAnimation(final Dot selectedDot){
        doDotAnimation(1f, 2f, selectedDot, new Runnable() {
            @Override
            public void run() {
                doDotAnimation(2f,1f,selectedDot,null);
            }
        });
    }

    private void doDotAnimation(float start, float end, final Dot selectedDot, final Runnable endRunnable){
        final ValueAnimator animator =ValueAnimator.ofFloat(start,end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                selectedDot.mScale = (Float)animator.getAnimatedValue();
                invalidate();
            }
        });
        if(endRunnable != null){
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(endRunnable != null){
                        endRunnable.run();
                    }
                }
            });
        }
        animator.setDuration(200);
        animator.start();
    }
    
    private void handleActionMove(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        if(!detectSelectAndAddDot(x,y)){
            mUnconnectPath.setLastPoint(x,y);
            invalidate();
        }
    }
    
    private void handleActionUp(MotionEvent event){
        if(mSelectedDotNumberList.toString().equals(mSetPassword)){
            mListener.onComplete(mSelectedDotNumberList);
        }else {
            mPathPaint.setColor(Color.RED);
            for(Integer dotNumber:mSelectedDotNumberList){
                mDotList.get(dotNumber).mColor = Color.RED;
            }
        }
        mUnconnectPath.reset();
        invalidate();
    }

    public void addListener(LockViewListener lockViewListener){
        mListener = lockViewListener;
        mSetPassword = mListener.onStart();
    }
    
    public static class Dot{
        int x;
        int y;
        int mNumber;
        float mScale = 1f;
        int mColor = Color.WHITE;
        boolean mState = false;

        public Dot(int x, int y, int number) {
            this.x = x;
            this.y = y;
            this.mNumber = number;
        }
    }
}
