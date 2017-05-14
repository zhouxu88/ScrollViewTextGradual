package com.zx.scrollviewtextgradual;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ScrollView滑动文字的渐变
 */
public class MainActivity extends AppCompatActivity implements ObservableScrollView.ScrollViewListener {

    private static final String TAG = "MainActivity";
    private ObservableScrollView scrollView;
    private ImageView headerIv; //头部图片
    private ImageView backIv; //返回
    private ImageView shopCartIv; //购物车
    private ImageView moreIv; //更多
    private LinearLayout headLayout; //渐变的头部
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private View dividerView; //分割线

    private Context mContext;
    private int imageHeight; //图片高度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        headerIv = (ImageView) findViewById(R.id.iv_header);
        backIv = (ImageView) findViewById(R.id.iv_back);
        shopCartIv = (ImageView) findViewById(R.id.iv_shopping_cart);
        moreIv = (ImageView) findViewById(R.id.iv_more);
        headLayout = (LinearLayout) findViewById(R.id.head_layout);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        dividerView = findViewById(R.id.divide_line);

        initListener();
        setStatusBar();
    }

    //设置沉浸式状态栏
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_dark_transparent));
        }
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initListener() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver treeObserver = headerIv.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headerIv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageHeight = headerIv.getHeight();
                Log.i(TAG, "imageHeight:-------->" + imageHeight);
                scrollView.setScrollViewListener(MainActivity.this);

            }
        });
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.i(TAG, "y:-------->" + y);
        Log.i(TAG, "oldy:-------->" + oldy);
        if (y <= 0) {
            //设置渐变的头部的背景颜色
            Log.i(TAG, "y <= 0:----------->");
            headLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            tv1.setTextColor(Color.TRANSPARENT);
            tv2.setTextColor(Color.TRANSPARENT);
            tv3.setTextColor(Color.TRANSPARENT);
            tv4.setTextColor(Color.TRANSPARENT);
            dividerView.setVisibility(View.GONE);
        } else if (y > 0 && y <= imageHeight) {
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            Log.i(TAG, "滑动距离小于banner图的高度---->" + imageHeight);
            float scale = (float) y / imageHeight;
            int alpha = (int) (scale * 255);
            headLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            tv1.setTextColor(Color.argb(alpha, 1, 24, 28));
            tv2.setTextColor(Color.argb(alpha, 1, 24, 28));
            tv3.setTextColor(Color.argb(alpha, 1, 24, 28));
            tv4.setTextColor(Color.argb(alpha, 1, 24, 28));
            backIv.getBackground().setAlpha(255 - alpha);
            shopCartIv.getBackground().setAlpha(255 - alpha);
            moreIv.getBackground().setAlpha(255 - alpha);
            if (oldy < y) {
                // 手指向上滑动，屏幕内容下滑
                backIv.setImageResource(R.mipmap.ic_back_dark);
                shopCartIv.setImageResource(R.mipmap.ic_shopping_dark);
                moreIv.setImageResource(R.mipmap.ic_more_dark);
            } else if (oldy > y) {
                // 手指向下滑动，屏幕内容上滑
                backIv.setImageResource(R.mipmap.ic_back);
                shopCartIv.setImageResource(R.mipmap.ic_shopping_cart);
                moreIv.setImageResource(R.mipmap.ic_more);
            }
        } else {
            //滑动到banner下面设置普通颜色
            Log.i(TAG, "滑动到banner下面---->" + imageHeight);
            headLayout.setBackgroundColor(Color.WHITE);
            tv1.setTextColor(Color.BLACK);
            tv2.setTextColor(Color.BLACK);
            tv3.setTextColor(Color.BLACK);
            tv4.setTextColor(Color.BLACK);
            dividerView.setVisibility(View.VISIBLE);
        }
    }
}
