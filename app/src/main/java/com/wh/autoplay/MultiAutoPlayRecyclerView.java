package com.wh.autoplay;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.List;

public class MultiAutoPlayRecyclerView extends LinearLayout {

    private static final long TIME_AUTO_PLAY = 16;
    private boolean mRunning;
    private boolean mCanRun;
    private boolean mScrolling;
    private AutoPlayTask mAutoPlayTask;
    private ScrollTask mScrollTask;
    private float mLastX = -1;
    private float mLastY = -1;
    private AutoPlayAdapter.OnAutoPlayClickListener mOnAutoPlayClickListener;

    public MultiAutoPlayRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public MultiAutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MultiAutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mAutoPlayTask = new AutoPlayTask(this);
        mScrollTask = new ScrollTask(this);
        setOrientation(LinearLayout.VERTICAL);
        // setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    /**
     * 自动滚动
     */
    static class AutoPlayTask implements Runnable {
        private final WeakReference<MultiAutoPlayRecyclerView> mReference;

        public AutoPlayTask(MultiAutoPlayRecyclerView reference) {
            this.mReference = new WeakReference<>(reference);
        }

        @Override
        public void run() {
            MultiAutoPlayRecyclerView recyclerViews = mReference.get();
            if (recyclerViews != null && recyclerViews.mRunning && recyclerViews.mCanRun) {
                int childCount = recyclerViews.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View view = recyclerViews.getChildAt(i);
                    if (view instanceof RecyclerView) {
                        RecyclerView recyclerView = (RecyclerView) view;
                        recyclerView.scrollBy(2, 2);
                    }
                }
                recyclerViews.postDelayed(recyclerViews.mAutoPlayTask, TIME_AUTO_PLAY);
            }
        }
    }

    /**
     * 手动联动滚动
     */
    static class ScrollTask implements Runnable {
        private final WeakReference<MultiAutoPlayRecyclerView> mReference;
        private int dx, dy;

        public ScrollTask(MultiAutoPlayRecyclerView reference) {
            this.mReference = new WeakReference<>(reference);
        }

        public void scroll(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void run() {
            MultiAutoPlayRecyclerView recyclerViews = mReference.get();
            if (recyclerViews != null && !recyclerViews.mRunning && recyclerViews.mCanRun) {
                int childCount = recyclerViews.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View view = recyclerViews.getChildAt(i);
                    if (view instanceof RecyclerView) {
                        RecyclerView recycler = (RecyclerView) view;
                        recycler.scrollBy(dx, dy);
                    }
                }
            }
        }
    }

    /**
     * 设置数据
     *
     * @param list
     * @param maxLine 最大的期望行数（会根据数据的多少自动显示行数）
     */
    public void setData(final List<String> list, int maxLine) {
        if (maxLine <= 0) {
            throw new RuntimeException("maxLine 不能等于、小于 0");
        }
        if (list == null || list.size() == 0) {
            throw new RuntimeException("list == null || list.size() == 0");
        }

        int lineSize = 8;//保证每行8个
        int line = list.size() / lineSize;//行数

        int recyclerSize = Math.min(line, maxLine);

        List<List<String>> dataList = ListUtils.averageAssign(list, recyclerSize);
        for (int i = 0; i < dataList.size(); i++) {
            final int currentLine = i + 1;
            List<String> data = dataList.get(i);
            AutoPlayRecyclerView recyclerView = new AutoPlayRecyclerView(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.weight = 1;
            recyclerView.setLayoutParams(params);
            AutoPlayAdapter adapter = new AutoPlayAdapter(data, new AutoPlayAdapter.OnAutoPlayClickListener() {
                @Override
                public void onClick(View view, int position, String data) {
                    position = list.indexOf(data);//因为被分成了多组数据，具体的position不好抛出来，先用indexOf得到position
                    if (mOnAutoPlayClickListener != null) {
                        mOnAutoPlayClickListener.onClick(view, position, data);
                    }
                }
            });
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            addView(recyclerView);
        }

        start();
    }

    public void setOnAutoPlayClickListener(AutoPlayAdapter.OnAutoPlayClickListener onAutoPlayClickListener) {
        mOnAutoPlayClickListener = onAutoPlayClickListener;
    }

    public void start(long delay) {
        if (mRunning) {
            stop();
        }
        mCanRun = true;
        mRunning = true;
        scrolling(false);
        postDelayed(mAutoPlayTask, delay);
        removeCallbacks(mScrollTask);
    }

    //开始自动滚动
    public void start() {
//        start(TIME_AUTO_PLAY);
        start(800L);
    }

    //停止滚动
    public void stop() {
        mRunning = false;
        scrolling(false);
        removeCallbacks(mAutoPlayTask);
        removeCallbacks(mScrollTask);
    }

    //手动滚动
    private void scroll(int dx, int dy) {
        scrolling(true);
        removeCallbacks(mAutoPlayTask);
        mScrollTask.scroll(-dx, -dy);
        post(mScrollTask);
    }

    private void scrolling(boolean scrolling) {
        if (mScrolling != scrolling) {
            mScrolling = scrolling;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view instanceof AutoPlayRecyclerView) {
                    AutoPlayRecyclerView recyclerView = (AutoPlayRecyclerView) view;
                    recyclerView.setCanScroll(!scrolling);
                }
            }
        }
    }

    private void release() {
        stop();
        removeCallbacks(mAutoPlayTask);
        removeCallbacks(mScrollTask);
    }

    /**
     * 更新数据
     */
    public final void notifyDataSetChanged() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mRunning) {
                    stop();
                }
                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (ev.getRawX() - mLastX);
                int dy = (int) (ev.getRawY() - mLastY);
                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                scroll(dx, dy);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (mCanRun) {
                    start();
                }
                mLastX = -1;
                mLastY = -1;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
