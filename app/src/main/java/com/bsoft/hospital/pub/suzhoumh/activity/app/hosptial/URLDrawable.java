package com.bsoft.hospital.pub.suzhoumh.activity.app.hosptial;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class URLDrawable extends BitmapDrawable {  
    protected Bitmap bitmap;  
  
    @Override  
    public void draw(Canvas canvas) {  
        if (bitmap != null) {  
            canvas.drawBitmap(bitmap, 0, 0, getPaint());  
        }  
    }  
}  
