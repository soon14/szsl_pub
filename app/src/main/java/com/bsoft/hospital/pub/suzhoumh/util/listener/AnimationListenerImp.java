package com.bsoft.hospital.pub.suzhoumh.util.listener;

import android.view.animation.Animation;

/**
 * @author :lizhengcao
 * @date :2018/6/20
 * E-mail:lizc@bsoft.com.cn
 * @类说明 启动页动画监听实现类
 */

public class AnimationListenerImp implements Animation.AnimationListener {

    private OnAnimationListener mListener;

    public AnimationListenerImp(OnAnimationListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mListener.onAnimationEndListener();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
