package com.szpt.zzq.smarttrafficdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szpt.zzq.smarttrafficdemo.util.SharedPreferenceUtil;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    private boolean mIsDebug = true;

    private ViewPager mViewPager;
    private Button mSkipButton;
    private Button mNextButton;
    private LinearLayout mDotsLayout;

    private SharedPreferenceUtil mSharedPreferenceUtil;
    private WelcomeViewPagerAdapter mWelcomeViewPagerAdapter;
    private int[] mLayoutResIds;
    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 首次引导检查
        mSharedPreferenceUtil = new SharedPreferenceUtil(this);

        if (mIsDebug) {
            mSharedPreferenceUtil.setIsFirstRun(true);
        }

        if (!mSharedPreferenceUtil.IsFirstRun()) {
            // 跳过引导界面
            Log.d(TAG, "WelcomeActivity has been visited");
            launchLoginActivity();
        }

        // 设置全屏，显示状态栏
        if (Build.VERSION.SDK_INT >= 16) {
            // SYSTEM_UI_FLAG_LAYOUT_STABLE：防止系统栏隐藏时内容区域大小发生变化
            // SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        mViewPager = (ViewPager) findViewById(R.id.activity_welcome_view_pager);
        mSkipButton = (Button) findViewById(R.id.activity_welcome_skip_btn);
        mNextButton = (Button) findViewById(R.id.activity_welcome_next_btn);
        mDotsLayout = (LinearLayout) findViewById(R.id.activity_welcome_dots_layout);

        mLayoutResIds = new int[] {
                R.layout.welcome_part_1,
                R.layout.welcome_part_2,
                R.layout.welcome_part_3,
                R.layout.welcome_part_4,
                R.layout.welcome_part_5,
        };

        // 设置状态栏背景为透明
        changeStatusBarColor();

        // 初始化底部小圆点显示
        updateBottomDots(0);

        mWelcomeViewPagerAdapter = new WelcomeViewPagerAdapter();
        mViewPager.setAdapter(mWelcomeViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d(TAG, "onPageScrolled: position " + position);
            }

            @Override
            public void onPageSelected(int position) {
                updateBottomDots(position);

                if (position == mLayoutResIds.length - 1) {
                    mSkipButton.setVisibility(View.GONE);
                    mNextButton.setText(getString(R.string.start));
                } else {
                    mSkipButton.setVisibility(View.VISIBLE);
                    mNextButton.setText(getString(R.string.next));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.d(TAG, "onPageScrollStateChanged: state " + state);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentIndex = mViewPager.getCurrentItem() + 1;
                Log.d(TAG, "onClick: "+ currentIndex);
                if (currentIndex < mLayoutResIds.length) {
                    mViewPager.setCurrentItem(currentIndex);
                    updateBottomDots(currentIndex);
                } else {
                    launchLoginActivity();
                }
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLoginActivity();
            }
        });
    }

    /**
     * 跳转至LoginActivity
     */
    private void launchLoginActivity() {
        mSharedPreferenceUtil.setIsFirstRun(false);
        Intent intent = LoginActivity.newIntent(this);
        startActivity(intent);
        finish();
    }

    /**
     * 更新底部小圆点的状态
     * @param currentPage
     */
    private void updateBottomDots(int currentPage) {
        mDots = new TextView[mLayoutResIds.length];

        int[] activeColors = getResources().getIntArray(R.array.array_dot_active);
        int[] inactiveColors = getResources().getIntArray(R.array.array_dot_inactive);

        mDotsLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mDots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            } else {
                mDots[i].setText(Html.fromHtml("&#8226;"));
            }
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(inactiveColors[currentPage]);
            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[currentPage].setTextColor(activeColors[currentPage]);
        }
    }

    /**
     * 将状态栏背景颜色设为透明
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * ViewPager适配器
     */
    private class WelcomeViewPagerAdapter extends PagerAdapter {
        private LayoutInflater mLayoutInflater;

        // 实例化
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = mLayoutInflater.inflate(mLayoutResIds[position],container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mLayoutResIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
