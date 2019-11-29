package com.example.filemanager.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MemoryView extends View {
    private int mWidth;
    private RectF mRect1;
    private RectF mRect2;
    private Paint mPaint;
    private double mScale;
    private int [] mColors = new int[]{Color.parseColor("#C4D4E8"),Color.parseColor("#FF727A")};
    public MemoryView(Context context) {
        super(context);
        init();
    }

    public MemoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MemoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left - 20;

    }

    private void init() {
        mRect1 = new RectF(0,0,0, 15);
        mRect2 = new RectF(0,0,0, 15);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w - 20;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRect1.right = mWidth;
        mRect2.right = (int) (mWidth*mScale);
        mPaint.setColor(mColors[0]);
        canvas.drawRoundRect(mRect1,15,15,mPaint);
        mPaint.setColor(mColors[1]);
        canvas.drawRoundRect(mRect2,15,15,mPaint);
    }

    public void setScales(double scales){
        mScale = scales;
        invalidate();
    }
}
