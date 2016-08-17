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
 * ShowcaseCoordinator will inflate {@link #showcaseLayout},and move the needMoveView which contain the indicatorView,
 * let the indicatorView right above the targetView.
 * @author Kevin Xie (lylwo317@gmail.com)
 * @since 2016/8/1.
 */
public class ShowcaseCoordinator
{

    private Context context;

    /**
     * Special which view in {@link #showcaseLayout} to dismiss showcase. Default view is {@link #showcaseLayout}
     **/
    private int dismissViewId = 0;

    private List<Showcase> showcaseList;

    private ViewGroup showcaseLayout;

    private OnDismissShowcaseLayoutListener dismissShowcaseLayoutListener = null;

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

    private void setOnDismissShowcaseLayoutListener(OnDismissShowcaseLayoutListener listener)
    {
        dismissShowcaseLayoutListener = listener;
    }

    public ShowcaseCoordinator(Context context, int layoutResId)
    {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(this.context);
        showcaseLayout = (ViewGroup)inflater.inflate(layoutResId, null);
    }

    /**
     * Remove {@link #showcaseLayout} from it's parent
     */
    private void dismiss()
    {
        if (dismissShowcaseLayoutListener != null)
        {
            dismissShowcaseLayoutListener.onDismiss();
        }
        ViewParent vp = showcaseLayout.getParent();
        if (vp instanceof ViewGroup)
        {
            ((ViewGroup)vp).removeView(showcaseLayout);
        }
    }

    /**
     * Get showcaseLayout
     * @return return {@link #showcaseLayout}
     */
    public ViewGroup getShowcaseLayout()
    {
        return showcaseLayout;
    }


    //////////////////////////////////////////////////dismiss showcaseLayout///////////////////////////////////////////////////

    private void bindDismissListenerToView()
    {
        if (dismissViewId == 0)
        {
            showcaseLayout.setOnClickListener(dismissListener);
        }else
        {
            View dismissShowcaseLayoutView = showcaseLayout.findViewById(dismissViewId);
            if (dismissShowcaseLayoutView != null)
            {
                dismissShowcaseLayoutView.setOnClickListener(dismissListener);
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
        bindShowcaseListener();
        bindDismissListenerToView();
    }

    /**
     * Listen targetView and indicatorView locations.
     */
    private void bindShowcaseListener()
    {
        if (showcaseList != null)
        {
            for (Showcase viewIdRecord : showcaseList)
            {
                NotifyReady ready = new NotifyReady(context);

                View indicatorView = showcaseLayout.findViewById(viewIdRecord.getIndicatorViewId());
                View targetView = viewIdRecord.getTargetView();
                View needMoveView = showcaseLayout.findViewById(viewIdRecord.getNeedMoveViewId());

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
     * Add showcase list to {@link #showcaseList}
     *
     * @param showcaseList showcase collections
     */
    private void addShowcaseList(List<Showcase> showcaseList)
    {
        if (this.showcaseList == null)
        {
            this.showcaseList = new ArrayList<>();
        }
        this.showcaseList.addAll(showcaseList);
    }

    /**
     * Add showcase to {@link #showcaseList}
     *
     * @param showcase Showcase instance
     */
    private void addShowcase(Showcase showcase)
    {
        if (showcaseList == null)
        {
            showcaseList = new ArrayList<>();
        }
        this.showcaseList.add(showcase);
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
     * To build {@link ShowcaseCoordinator} instance.
     */
    public static class Builder {
        private ShowcaseCoordinator showcaseCoordinator;

        private ViewGroup parent;

        private int parentIndex;

        public Builder(Activity activity,int layoutResId)
        {
            if (activity == null)
            {
                throw new NullPointerException("Activity can not be Null!");
            }
            showcaseCoordinator = new ShowcaseCoordinator(activity,layoutResId);
            parent = (ViewGroup)(activity.getWindow().getDecorView().getRootView());
            parentIndex = parent.getChildCount();
        }

        public ShowcaseCoordinator build()
        {
            showcaseCoordinator.onBuild();
            parent.addView(showcaseCoordinator.getShowcaseLayout(), parentIndex);
            return showcaseCoordinator;
        }

        public Builder addShowcaseList(List<Showcase> idRecordList)
        {
            showcaseCoordinator.addShowcaseList(idRecordList);
            return this;
        }

        public Builder addShowcase(View targetView, int indicatorViewId, int needMoveViewId)
        {

            showcaseCoordinator.addShowcase(new Showcase(targetView, indicatorViewId, needMoveViewId));
            return this;
        }

        public Builder addShowcase(Showcase showcase)
        {
            showcaseCoordinator.addShowcase(showcase);
            return this;
        }

        public Builder setViewToDismissShowcase(int resId)
        {
            showcaseCoordinator.setViewToDismissShowcase(resId);
            return this;
        }

        public Builder setOnDismissShowcaseLayoutListener(OnDismissShowcaseLayoutListener listener)
        {
            showcaseCoordinator.setOnDismissShowcaseLayoutListener(listener);
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
     * Listen showcaseLayout dismiss event.
     */
    public interface OnDismissShowcaseLayoutListener
    {
        void onDismiss();
    }

}
