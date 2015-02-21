package com.prdcv.api.graphics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public interface ImageHandler {
    public int getWidth();

    public int getHeight();

    public Config getFormat();
    
    public Bitmap getBitmap();
    
    public ImageHandler	createImage(int x, int y, int width, int height);
    
    public ImageHandler createScaledImage(int width, int height);
    
    public ImageHandler createScaledImage(float ratio);
    
    public void setBitmap(Bitmap bitmap);
    
    public void dispose();
}
