package com.wh.autoplay;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AutoPlayRecyclerView extends RecyclerView {

    private boolean mCanScroll;

    public AutoPlayRecyclerView(Context context) {
        super(context);

    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCanScroll(boolean canScroll) {
        mCanScroll = canScroll;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!mCanScroll) {
            return true;
        }
        return super.onTouchEvent(e);
    }
}
