package com.bsoft.hospital.pub.suzhoumh.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.tanklib.Preferences;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.account.LoginActivity;
import com.bsoft.hospital.pub.suzhoumh.util.AntiHijackingUtil;
import com.bsoft.hospital.pub.suzhoumh.util.SystemUiVisibilityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import butterknife.Unbinder;

/**
 * 引导页
 *
 * @author Administrator
 */
public class BootPageActivity extends AppCompatActivity implements OnClickListener {

    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.viewpager)
    ViewPager vp;
    @BindView(R.id.ll)
    LinearLayout ll;

    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    //引导图片资源
    private static final int[] pics = {R.drawable.bg_bootpager_1,
            R.drawable.bg_bootpager_2, R.drawable.bg_bootpager_3};

    //底部小点图片
    private ImageView[] dots;

    //记录当前选中位置
    private int currentIndex;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bootpage);
        mUnbinder = ButterKnife.bind(this);

        views = new ArrayList<>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        mBtnStart.setOnClickListener(this);
        //初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pics[i]);
            views.add(iv);
        }
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);

        //初始化底部小点
        initDots();

    }


    private void initDots() {

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }


    @OnPageChange(value = R.id.viewpager, callback = OnPageChange.Callback.PAGE_SELECTED)
    public void onPageSelected(int position) {
        //设置底部小点选中状态
        setCurDot(position);
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;

        if (currentIndex == 2) {
            mBtnStart.setVisibility(View.VISIBLE);
        } else {
            mBtnStart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Preferences.getInstance().setStringData("first", "1");
                Intent intent = new Intent(BootPageActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                SystemUiVisibilityUtil.hideStatusBar(getWindow(), false);
                finish();
                break;
            default:
                int position = (Integer) v.getTag();
                setCurView(position);
                setCurDot(position);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AntiHijackingUtil.checkActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null)
            mUnbinder.unbind();
        super.onDestroy();
    }
}  
