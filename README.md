# ShowcaseCoordinator

ShowCaseCoordinator will inflate layout,and move the needMoveView which contain the indicatorView,
let the indicatorView right above the targetView.


## Effect picture

my_show_case_layout.xml

<img src="https://github.com/lylwo317/MyShowcaseLayout/blob/master/screenshot/layout-2016-08-17-132455.png" width="400">

MainActivity

<img src="https://github.com/lylwo317/MyShowcaseLayout/blob/master/screenshot/layout-2016-08-17-132712.png" width="400">

Use ShowCaseCoordinator in MainActivity

<img src="https://github.com/lylwo317/MyShowcaseLayout/blob/master/screenshot/device-2016-08-17-134533.png" width="400">




## Usage


``` java
new ShowCaseCoordinator.Builder(this, R.layout.my_show_case_layout)
                .addShowCase(tvHelloFirst, R.id.imageView_first, R.id.llyt_move_first)
                .addShowCase(tvHelloSecond, R.id.imageView_second, R.id.llyt_move_second)
                .addShowCase(tvHelloThird, R.id.imageView_third, R.id.llyt_move_third)
                .build();
```

## ShowCaseLayout



```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:background="@color/black_alph"
              android:layout_width="match_parent"
              android:layout_height="match_parent">
    <LinearLayout
        android:id="@+id/llyt_move_first"
        android:orientation="vertical"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">
        <ImageView
            android:src="@drawable/prompt_icons_fullscreen"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/imageView_first"
            android:layout_gravity="right"/>

        <ImageView
            android:src="@drawable/prompt_choice"
            android:layout_marginRight="12dp"
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/imageView_first"
            android:layout_alignEnd="@+id/imageView_first"/>
    </LinearLayout>

    <LinearLayout
        android:id="@+id/llyt_move_second"
        android:orientation="vertical"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">
        <ImageView
            android:src="@drawable/prompt_icons_fullscreen"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/imageView_second"
            android:layout_gravity="right"/>

        <ImageView
            android:src="@drawable/prompt_choice"
            android:layout_marginRight="12dp"
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/imageView_first"
            android:layout_alignEnd="@+id/imageView_first"/>
    </LinearLayout>

    <LinearLayout
        android:id="@+id/llyt_move_third"
        android:orientation="vertical"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">
        <ImageView
            android:src="@drawable/prompt_icons_fullscreen"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/imageView_third"
            android:layout_gravity="right"/>

        <ImageView
            android:src="@drawable/prompt_choice"
            android:layout_marginRight="12dp"
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/imageView_first"
            android:layout_alignEnd="@+id/imageView_first"/>
    </LinearLayout>

</LinearLayout>
```


