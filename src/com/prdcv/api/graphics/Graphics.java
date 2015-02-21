package com.prdcv.api.graphics;

import android.graphics.Bitmap.Config;

public interface Graphics {
 
    public ImageHandler newImage(String fileName, Config format);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawImage(ImageHandler image, int x, int y);

    public int getWidth();

    public int getHeight();
    
    public void drawText(String text, int x, int y, int size, int color );
    
}
