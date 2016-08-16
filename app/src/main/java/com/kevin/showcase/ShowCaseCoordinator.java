package com.kevin.showcase;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * ShowCaseCoordinator will inflate {@link #showCaseLayout},and move the needMoveView which contain the indicatorView,
 * let the indicatorView right above the targetView.
 * @author Kevin Xie (lylwo317@gmail.com)
 * @since 2016/8/1.
 */
public class ShowCaseCoordinator
{

    private Context context;

    /**
     * Special which view in {@link #showCaseLayout} to dismiss showcase. Default view is {@link #showCaseLayout}
     **/
    private int dismissViewId = 0;

    private List<Showcase> showCaseList;

    private ViewGroup showCaseLayout;

    private OnDismissShowCaseLayoutListener dismissShowCaseLayoutListener = null;

    enum ViewType{

        INDICATOR_VIEW,

        TARGET_VIEW
    }
    private View.OnClickListener dismissListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            dismiss();
        }
    };

    private void setOnDismissShowCaseLayoutListener(OnDismissShowCaseLayoutListener listener)
    {
        dismissShowCaseLayoutListener = listener;
    }

    public ShowCaseCoordinator(Context context, int layoutResId)
    {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(this.context);
        showCaseLayout  = (ViewGroup)inflater.inflate(layoutResId, null);
    }

    /**
     * Remove {@link #showCaseLayout} from it's parent
     */
    private void dismiss()
    {
        if (dismissShowCaseLayoutListener != null)
        {
            dismissShowCaseLayoutListener.onDismiss();
        }
        ViewParent vp = showCaseLayout.getParent();
        if (vp instanceof ViewGroup)
        {
            ((ViewGroup)vp).removeView(showCaseLayout);
        }
    }

    /**
     * Get showCaseLayout
     * @return return {@link #showCaseLayout}
     */
    public ViewGroup getShowCaseLayout()
    {
        return showCaseLayout;
    }


    //////////////////////////////////////////////////dismiss showCaseLayout///////////////////////////////////////////////////

    private void bindDismissListenerToView()
    {
        if (dismissViewId == 0)
        {
            showCaseLayout.setOnClickListener(dismissListener);
        }else
        {
            View dismissShowCaseLayoutView = showCaseLayout.findViewById(dismissViewId);
            if (dismissShowCaseLayoutView != null)
            {
                dismissShowCaseLayoutView.setOnClickListener(dismissListener);
            }
        }
    }

    private void setViewToDismissShowcase(int resId)
    {
        this.dismissViewId = resId;
    }

    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\



    ////////////////////////////////////////////////////locations change////////////////////////////////////////////////////////////

    private void updateViewLocations(View view, ViewType viewType, NotifyReady notifyReady)
    {
        if (view.getWidth() != 0)
        {
            int[] viewLocations = new int[2];
            view.getLocationOnScreen(viewLocations);

            switch (viewType)
            {
                case INDICATOR_VIEW:
                    notifyReady.setIndicatorView(view);
                    notifyReady.setIndicatorLocations(viewLocations);
                    break;
                case TARGET_VIEW:
                    notifyReady.setTargetView(view);
                    notifyReady.setTargetLocations(viewLocations);
                    break;
            }
            notifyReady.ready();
        }
    }

    /**
     * When call {@link Builder#build()},this method will be invoked.
     */
    private void onBuild()
    {
        bindShowCaseListener();
        bindDismissListenerToView();
    }

    /**
     * Listen targetView and indicatorView locations.
     */
    private void bindShowCaseListener()
    {
        if (showCaseList != null)
        {
            for (Showcase viewIdRecord : showCaseList)
            {
                NotifyReady ready = new NotifyReady(context);

                View indicatorView = showCaseLayout.findViewById(viewIdRecord.getIndicatorViewId());
                View targetView = viewIdRecord.getTargetView();
                View needMoveView = showCaseLayout.findViewById(viewIdRecord.getNeedMoveViewId());

                if (indicatorView != null && targetView != null && needMoveView != null)
                {
                    listenIndicatorViewLocations(indicatorView, needMoveView, ready);
                    listenTargetViewLocations(targetView,ready);
                }
            }
        }
    }

    /**
     * Listen indicatorView locations.
     */
    private void listenIndicatorViewLocations(final View indicatorView, final View needMoveView, final NotifyReady notifyReady)
    {

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
                    updateViewLocations(indicatorView, ViewType.INDICATOR_VIEW, notifyReady);
                }
            }
        });

        notifyReady.setNeedMoveView(needMoveView);
    }

    /**
     * Listen targetView locations.
     */
    private void listenTargetViewLocations(final View targetView, final NotifyReady notifyReady)
    {

        targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (targetView.getWidth() != 0)
                {
                    updateViewLocations(targetView, ViewType.TARGET_VIEW, notifyReady);
                }
            }
        });
        if (targetView.getWidth()!=0)
        {
            updateViewLocations(targetView,ViewType.TARGET_VIEW,notifyReady);
        }
    }

    /**
     * Add showcase list to {@link #showCaseList}
     *
     * @param showcaseList showcase collections
     */
    private void addShowCaseList(List<Showcase> showcaseList)
    {
        if (showCaseList == null)
        {
            showCaseList = new ArrayList<>();
        }
        this.showCaseList.addAll(showcaseList);
    }

    /**
     * Add showcase to {@link #showCaseList}
     *
     * @param showcase Showcase instance
     */
    private void addShowCase(Showcase showcase)
    {
        if (showCaseList == null)
        {
            showCaseList = new ArrayList<>();
        }
        this.showCaseList.add(showcase);
    }

    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * When the positions of targetView and indicatorView are set. {@link NotifyReady#ready()} will move the {@link #needMoveView} position right above
     * {@link #targetView}
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
            displayMetrics = context.getResources().getDisplayMetrics();
        }

        public void setIndicatorLocations(int[] indicatorLocations)
        {
            this.indicatorLocations = indicatorLocations;
        }

        public void setTargetLocations(int[] targetLocations)
        {
            this.targetLocations = targetLocations;
        }

        /**
         * Move the {@link #needMoveView} position right above
         */
        public void ready()
        {
            if (indicatorLocations != null && targetLocations != null && needMoveView != null && indicatorView != null && targetView != null)
            {
                float x = targetLocations[0]%displayMetrics.widthPixels-indicatorLocations[0]%displayMetrics.widthPixels + (targetView.getWidth() - indicatorView.getWidth()) / 2f;
                float y = targetLocations[1] - indicatorLocations[1] + (targetView.getHeight() - indicatorView.getHeight()) / 2f;

                //if position no change,don't move it.
                if (x == oldX && y == oldY)
                {
                    return;
                }else
                {
                    oldX = x;
                    oldY = y;
                }
                needMoveView.setTranslationX(x);
                needMoveView.setTranslationY(y);
            }
        }

        public void setIndicatorView(View indicatorView)
        {
            this.indicatorView = indicatorView;
        }
    }

    /**
     * To build {@link ShowCaseCoordinator} instance.
     */
    public static class Builder {
        private ShowCaseCoordinator showCaseCoordinator;

        private ViewGroup parent;

        private int parentIndex;

        public Builder(Activity activity,int layoutResId)
        {
            if (activity == null)
            {
                throw new NullPointerException("Activity can not be Null!");
            }
            showCaseCoordinator = new ShowCaseCoordinator(activity,layoutResId);
            parent = (ViewGroup)(activity.getWindow().getDecorView().getRootView());
            parentIndex = parent.getChildCount();
        }

        public ShowCaseCoordinator build()
        {
            showCaseCoordinator.onBuild();
            parent.addView(showCaseCoordinator.getShowCaseLayout(), parentIndex);
            return showCaseCoordinator;
        }

        public Builder addShowCaseList(List<Showcase> idRecordList)
        {
            showCaseCoordinator.addShowCaseList(idRecordList);
            return this;
        }

        public Builder addShowCase(View targetView, int indicatorViewId, int needMoveViewId)
        {

            showCaseCoordinator.addShowCase(new Showcase(targetView, indicatorViewId, needMoveViewId));
            return this;
        }

        public Builder addShowCase(Showcase showcase)
        {
            showCaseCoordinator.addShowCase(showcase);
            return this;
        }

        public Builder setViewToDismissShowcase(int resId)
        {
            showCaseCoordinator.setViewToDismissShowcase(resId);
            return this;
        }

        public Builder setOnDismissShowCaseLayoutListener(OnDismissShowCaseLayoutListener listener)
        {
            showCaseCoordinator.setOnDismissShowCaseLayoutListener(listener);
            return this;
        }
    }

    /**
     * This is a collection that what you want to coordinate views.
     */
    public static class Showcase
    {
        private View targetView;
        private int indicatorViewId;
        private int needMoveViewId;

        /**
         * Constructing Showcase instance.
         * @param targetView which view that you want {@link #indicatorViewId} align at.
         * @param indicatorViewId which view you want to right above {@link #targetView}.
         * @param needMoveViewId which view you want to move actually.
         */
        public Showcase(View targetView, int indicatorViewId, int needMoveViewId)
        {
            this.targetView = targetView;
            this.indicatorViewId = indicatorViewId;
            this.needMoveViewId = needMoveViewId;
        }

        public View getTargetView()
        {
            return targetView;
        }

        public int getIndicatorViewId()
        {
            return indicatorViewId;
        }

        public int getNeedMoveViewId()
        {
            return needMoveViewId;
        }
    }

    /**
     * Listen showCaseLayout dismiss event.
     */
    public interface OnDismissShowCaseLayoutListener
    {
        void onDismiss();
    }

}
