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
 * Created by XieJiaHua on 2016/8/1.
 */
public class MyShowCaseLayout extends RelativeLayout
{

    private View indicatorView;

    private View targetView;

    private View needChangeView;

    private NotifyReady notifyReady;

    private Context context;

    private int indicatorLayoutResId=0;
    private int indicatorResId = 0;

    private int needChangeResId = 0;

    private ViewGroup indicatorLayout;
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

    public void setNeedChangeResId(int needChangeResId)
    {
        this.needChangeResId = needChangeResId;
    }

    public void addTargetView(final View targetView)
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
                if (view.getWidth() != 0)//只有当宽不为0时，位置才有意义
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
            if (needChangeResId != 0)
            {
                needChangeView = indicatorLayout.findViewById(needChangeResId);
                notifyReady.setNeedChangeView(needChangeView);
            }else
            {
                notifyReady.setNeedChangeView(indicatorLayout);
            }
        }
    }

    private static class NotifyReady
    {
        private int[] indicatorLocations;
        private int[] targetLocations;
        private View needChangeView;

        private View targetView;

        private View indicatorView;

        private float oldX;
        private float oldY;

        private DisplayMetrics displayMetrics;

        public void setTargetView(View targetView)
        {
            this.targetView = targetView;
        }

        public void setNeedChangeView(View needChangeView)
        {
            this.needChangeView = needChangeView;
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
            if (indicatorLocations != null && targetLocations != null && needChangeView != null && indicatorView != null && targetView != null)
            {
                float x = targetLocations[0]%displayMetrics.widthPixels-indicatorLocations[0]%displayMetrics.widthPixels + (targetView.getWidth() - indicatorView.getWidth()) / 2f;
                float y = targetLocations[1] - indicatorLocations[1] + (targetView.getHeight() - indicatorView.getHeight()) / 2f;

                if (x == oldX && y == oldY)//剔除无意义的移动
                {
                    return;
                }else
                {
                    oldX = x;
                    oldY = y;
                }
                needChangeView.setX(x);
                needChangeView.setY(y);
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
            myShowCaseLayout.addTargetView(targetView);
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
            myShowCaseLayout.setNeedChangeResId(resId);
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
