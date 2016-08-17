package com.kevin.showcase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private TextView tvHelloFirst;
    private TextView tvHelloSecond;
    private TextView tvHelloThird;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHelloFirst = (TextView) findViewById(R.id.tv_hello_first);
        tvHelloSecond = (TextView)findViewById(R.id.tv_hello_second);
        tvHelloThird = (TextView)findViewById(R.id.tv_hello_third);

        new ShowcaseCoordinator.Builder(this, R.layout.my_show_case_layout)
                .addShowcase(tvHelloFirst, R.id.imageView_first, R.id.llyt_move_first)
                .addShowcase(tvHelloSecond, R.id.imageView_second, R.id.llyt_move_second)
                .addShowcase(tvHelloThird, R.id.imageView_third, R.id.llyt_move_third)
                .build();
    }
}
