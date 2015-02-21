package com.prdcv.api.graphics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class ADImageHandler implements ImageHandler {
    Bitmap bitmap;
    Config format;
    
    public ADImageHandler(Bitmap bitmap, Config format) {
        this.bitmap = bitmap;
        this.format = format;
    }
    
    public ADImageHandler(ImageHandler image) {
        this.bitmap = image.getBitmap();
        this.format = image.getFormat();
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Config getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }

    @Override
	public Bitmap getBitmap() {
		return bitmap;
	}

    @Override
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public ImageHandler createImage(int x, int y, int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(this.getBitmap(), x, y, width, height);
		ImageHandler dest = new ADImageHandler(bitmap, this.getFormat());
		return dest;
	}

	@Override
	public ImageHandler createScaledImage(int width, int height) {
		Bitmap bitmap = Bitmap.createScaledBitmap(this.getBitmap(), 
				width, height, true);
		ImageHandler dest = new ADImageHandler(bitmap, this.getFormat());
		return dest;
	}
	
	@Override
	public ImageHandler createScaledImage(float ratio) {
		int newWidth = (int) (this.getBitmap().getWidth() * ratio); 
		int newHeight = (int) (this.getBitmap().getHeight() * ratio);
		
		Bitmap bitmap = Bitmap.createScaledBitmap(this.getBitmap(), 
				newWidth, newHeight, true);
		ImageHandler dest = new ADImageHandler(bitmap, this.getFormat());
		return dest;
	}
    
    
    
}
