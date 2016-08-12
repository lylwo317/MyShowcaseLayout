package com.kevin.myshowcaselayout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * 引导页面
 * Created by XieJiaHua on 2016/8/1.
 */
public class MyShowCaseLayout extends RelativeLayout
{

    /**
     * 指示器View，也就是要对齐到目标View的View
     */
    private View indicatorView;

    /**
     * 需要对齐到的目标
     */
    private View targetView;

    /**
     * 需要移动的View
     */
    private View needMoveView;

    private NotifyReady notifyReady;

    private Context context;

    private int indicatorLayoutResId=0;
    private int indicatorResId = 0;

    private int needMoveResId = 0;

    /**
     * 指示器所在的布局
     */
    private ViewGroup indicatorLayout;
    /**
     * 是否要一直监听目标View的位置变化。主要是有些界面目标View的位置可能会改变，一直监听者，可以跟随目标移动
     */
    private boolean alwaysTraceTargetPosition = false;

    public MyShowCaseLayout(Context context)
    {
        super(context);
        this.context = context;
        notifyReady = new NotifyReady(context);
    }

    public MyShowCaseLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyShowCaseLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setNeedMoveResId(int needMoveResId)
    {
        this.needMoveResId = needMoveResId;
    }

    public void setTargetView(final View targetView)
    {
        this.targetView = targetView;
        notifyReady.setTargetView(targetView);
        setTargetViewLocations(this.targetView);
    }

    private void setTargetViewLocations(final View view)
    {

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (view.getWidth() != 0)//只有当宽不为0时，所获取的屏幕位置才准确。
                {
                    if (!alwaysTraceTargetPosition)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        else
                        {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                    updateViewLocations(view);
                }
            }
        });
        if (view.getWidth()!=0)
        {
            updateViewLocations(view);
        }
    }

    private void updateViewLocations(View view)
    {
        int[] viewLocations = new int[2];
        view.getLocationOnScreen(viewLocations);
        if (view == this.targetView)
        {
            notifyReady.setTargetLocations(viewLocations);
        }else if (view == this.indicatorView)
        {
            notifyReady.setIndicatorLocations(viewLocations);
        }
        notifyReady.notifyReady();
    }

    private void setIndicatorLayoutId(int layoutResId)
    {
        this.indicatorLayoutResId = layoutResId;
        LayoutInflater inflater = LayoutInflater.from(context);
        indicatorLayout = (ViewGroup)inflater.inflate(layoutResId, this, false);
        addView(indicatorLayout);
    }

    private void setIndicatorResId(int resId)
    {
        this.indicatorResId = resId;
    }

    private void listenIndicatorPositions()
    {
        if (indicatorResId != 0)
        {
            indicatorView = indicatorLayout.findViewById(indicatorResId);
            indicatorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    if (indicatorView.getWidth() != 0)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        {
                            indicatorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        else
                        {
                            indicatorView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        updateViewLocations(indicatorView);
                    }
                }
            });
            notifyReady.setIndicatorView(indicatorView);
            if (needMoveResId != 0)
            {
                needMoveView = indicatorLayout.findViewById(needMoveResId);
                notifyReady.setNeedMoveView(needMoveView);
            }else
            {
                notifyReady.setNeedMoveView(indicatorLayout);
            }
        }
    }

    /**
     * 通知指示器View和目标View位置，使得该布局定位到
     */
    private static class NotifyReady
    {
        private int[] indicatorLocations;
        private int[] targetLocations;
        private View needMoveView;

        private View targetView;

        private View indicatorView;

        private float oldX;
        private float oldY;

        private DisplayMetrics displayMetrics;

        public void setTargetView(View targetView)
        {
            this.targetView = targetView;
        }

        public void setNeedMoveView(View needChangeView)
        {
            this.needMoveView = needChangeView;
        }

        public NotifyReady(Context context)
        {
            displayMetrics = DisplayUtils.getDisplaySize(context);
        }

        public void setIndicatorLocations(int[] indicatorLocations)
        {
            this.indicatorLocations = indicatorLocations;
        }

        public void setTargetLocations(int[] targetLocations)
        {
            this.targetLocations = targetLocations;
        }

        public void notifyReady()
        {
            if (indicatorLocations != null && targetLocations != null && needMoveView != null && indicatorView != null && targetView != null)
            {
                float x = targetLocations[0]%displayMetrics.widthPixels-indicatorLocations[0]%displayMetrics.widthPixels + (targetView.getWidth() - indicatorView.getWidth()) / 2f;
                float y = targetLocations[1] - indicatorLocations[1] + (targetView.getHeight() - indicatorView.getHeight()) / 2f;

                //避免无意义的移动
                if (x == oldX && y == oldY)
                {
                    return;
                }else
                {
                    oldX = x;
                    oldY = y;
                }
                needMoveView.setX(x);
                needMoveView.setY(y);
            }
        }

        public void setIndicatorView(View indicatorView)
        {
            this.indicatorView = indicatorView;
        }
    }

    public static class Builder {
        private MyShowCaseLayout myShowCaseLayout;

        private ViewGroup parent;

        private int parentIndex;

        private int indicatorResId;

        public Builder(Activity activity) {
            myShowCaseLayout = new MyShowCaseLayout(activity);
            parent = (ViewGroup)(activity.getWindow().getDecorView().getRootView());
            parentIndex = parent.getChildCount();
        }

        public MyShowCaseLayout build() {



            myShowCaseLayout.listenIndicatorPositions();
            parent.addView(myShowCaseLayout, parentIndex);
            return myShowCaseLayout;
        }

        public Builder addTargetView(View targetView)
        {
            myShowCaseLayout.setTargetView(targetView);
            return this;
        }

        public Builder setIndicatorLayoutId(int layoutResId)
        {
            myShowCaseLayout.setIndicatorLayoutId(layoutResId);
            return this;
        }

        public Builder setIndicatorResId(int resId)
        {
            myShowCaseLayout.setIndicatorResId(resId);
            return this;
        }

        public Builder setBackgroundColor(int res)
        {
            myShowCaseLayout.setBackgroundColor(res);
            return this;
        }

        public Builder setNeedChangePositionViewResId(int resId)
        {
            myShowCaseLayout.setNeedMoveResId(resId);
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener)
        {
            myShowCaseLayout.setOnClickListener(onClickListener);
            return this;
        }

        public Builder setAlwaysTraceTargetPosition(boolean isAlways)
        {
            myShowCaseLayout.setAlwaysTraceTargetPosition(isAlways);
            return this;
        }
    }

    private void setAlwaysTraceTargetPosition(boolean isAlways)
    {
        this.alwaysTraceTargetPosition = isAlways;
    }

}
