package com.sgb.library.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sgb.library.R;


/**
 * Created by panda on 16/7/20 上午9:21.
 */
public class ClassicIndicator extends LinearLayout implements IPagerIndicator {

    private static final int STYLE_OVAL = 0;
    private static final int STYLE_RECT = 1;
    private static final int STYLE_LINE = 2;

    private Context mContext;

    private int mStyle;
    private float mSpan;
    private float mWidth;
    private float mHeight;
    private int mSelectedColor;
    private int mUnSelectedColor;

    private int mTotalNum;
    private int mCurIndex;

    public ClassicIndicator(Context context) {
        this(context, null);
    }

    public ClassicIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicIndicator, 0, 0);
        if (ta != null) {
            mStyle = ta.getInt(R.styleable.ClassicIndicator_dot_style, STYLE_OVAL);
            mSpan = ta.getDimension(R.styleable.ClassicIndicator_dot_span, 10);
            mWidth = ta.getDimension(R.styleable.ClassicIndicator_dot_width, 10);
            mHeight = ta.getDimension(R.styleable.ClassicIndicator_dot_height, 10);
            mSelectedColor = ta.getColor(R.styleable.ClassicIndicator_dot_selected_color, 0xFFFF4081);
            mUnSelectedColor = ta.getColor(R.styleable.ClassicIndicator_dot_unselected_color, 0xFF3F51B5);
            ta.recycle();
        }
    }

    @Override
    public void setTotalNum(int totalNum) {

        mTotalNum = totalNum;

        removeAllViews();
        setOrientation(HORIZONTAL);

        for (int i = 0; i < mTotalNum; i++) {
            DotView dotView = new DotView(mContext);
            dotView.setColor(i == mCurIndex ? mSelectedColor : mUnSelectedColor);
            if (mStyle == STYLE_LINE) {
                dotView.setLayoutParams(new ViewGroup.LayoutParams((int) (mWidth + mSpan), (int) mHeight));
            } else {
                dotView.setLayoutParams(new ViewGroup.LayoutParams((int) (Math.max(mWidth, mHeight) + mSpan), (int) Math.max(mWidth, mHeight)));
            }

            addView(dotView);
        }
    }

    @Override
    public int getTotalNum() {
        return mTotalNum;
    }

    @Override
    public void setCurIndex(int index) {
        if (index < 0 || index > getChildCount() || mCurIndex == index) {
            return;
        }

        if (mCurIndex >= 0 && mCurIndex < getChildCount()) {
            ((DotView) getChildAt(mCurIndex)).setColor(mUnSelectedColor);
        }
        ((DotView) getChildAt(index)).setColor(mSelectedColor);

        mCurIndex = index;
    }

    @Override
    public int getCurIndex() {
        return mCurIndex;
    }

    private class DotView extends View {

        private Paint mPaint;
        private int mColor;

        public DotView(Context context) {
            this(context, null);
        }

        public DotView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mPaint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            mPaint.setColor(mColor);
            mPaint.setAntiAlias(true);

            switch (mStyle) {
                case STYLE_OVAL:
                default:
                    float radius = Math.min(mWidth, mHeight) / 2;
                    canvas.drawCircle(radius + mSpan / 2, radius, radius, mPaint);
                    break;
                case STYLE_RECT:
                    float width = Math.min(mWidth, mHeight);
                    canvas.drawRect(mSpan / 2, 0, width + mSpan / 2, width, mPaint);
                    break;
                case STYLE_LINE:
                    mPaint.setStrokeWidth(mHeight);
                    canvas.drawLine(0, 0, mWidth, 0, mPaint);
                    break;
            }
        }

        public void setColor(int color) {
            if (mColor == color) {
                return;
            }
            mColor = color;
            invalidate();
        }
    }
}
