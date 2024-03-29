package com.bsoft.hospital.pub.suzhoumh.view;

import android.os.CountDownTimer;

/**
 * 倒计时帮助类
 *
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-8-15 10:41
 */
public class CountDownHelper {

    //倒计时timer
    private CountDownTimer countDownTimer;

    private boolean isCounting = false;

    public CountDownHelper(int max, int interval) {
        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        countDownTimer = new CountDownTimer(max * 1000, interval * 1000 - 10) {

            @Override
            public void onTick(long time) {
                isCounting=true;

                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                if (countDownListener != null) {
                    countDownListener.onCounting((time + 15) / 1000);
                }
            }

            @Override
            public void onFinish() {
                isCounting=false;
                countDownListener.onFinish();
            }
        };
    }

    /**
     * 开始倒计时
     */
    public void start() {
        countDownTimer.start();
    }

    public boolean isCounting() {
        return isCounting;
    }

    public interface CountDownListener {

        void onCounting(long time);

        void onFinish();
    }

    private CountDownListener countDownListener;

    public void setCountDownListener(CountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }
}
