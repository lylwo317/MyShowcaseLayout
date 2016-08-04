package com.kevin.myshowcaselayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvHello;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHello = (TextView) findViewById(R.id.tv_hello);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
        imageView.setBackgroundColor(getResources().getColor(R.color.black_50));

        new MyShowCaseLayout.Builder(this)
                .addTargetView(tvHello)
                //.addIndicatorView(imageView)
                .setIndicatorLayoutId(R.layout.indicator_layout)
                .setIndicatorResId(R.id.imageView)
                .setBackgroundColor(R.color.black_alph)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ViewParent vp = v.getParent();
                        if (vp instanceof ViewGroup)
                            ((ViewGroup)vp).removeView(v);
                    }
                })
                .build();
    }

    private void checkViewSomeValue(View view)
    {
        Log.d(TAG, "getWidth=" + view.getWidth());
        Log.d(TAG, "getHeight=" + view.getHeight());
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        Log.d(TAG, "locations=" + Arrays.toString(locations));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume");
    }
}
