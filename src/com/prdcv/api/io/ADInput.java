package com.prdcv.api.io;

import java.util.List;

import android.content.Context;
import android.view.View;

public class ADInput implements Input {    
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;

    public ADInput(Context context, View view, float scaleX, float scaleY) {
        keyHandler = new KeyboardHandler(view);               
        touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
}
