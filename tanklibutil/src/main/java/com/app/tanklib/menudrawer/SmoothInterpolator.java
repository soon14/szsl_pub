package com.app.tanklib.menudrawer;

import android.view.animation.Interpolator;

class SmoothInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float t) {
        t -= 1.0f;
        return t * t * t * t * t + 1.0f;
    }
}
