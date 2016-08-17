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

