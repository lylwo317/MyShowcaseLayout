package com.kevin.myshowcaselayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by XieJiaHua on 2016/8/1.
 */
public class EraseImageView extends ImageView
{
    public EraseImageView(Context context)
    {
        super(context);
    }

    public EraseImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EraseImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
