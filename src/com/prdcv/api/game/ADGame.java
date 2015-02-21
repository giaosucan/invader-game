package com.prdcv.api.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.prdcv.api.io.ADFileIO;
import com.prdcv.api.io.ADInput;
import com.prdcv.api.io.FileIO;
import com.prdcv.api.io.Input;
import com.prdcv.api.graphics.ADGraphics;
import com.prdcv.api.graphics.Graphics;


import com.apperhand.device.android.AndroidSDKProvider;
import com.startapp.android.publish.StartAppAd; 

public abstract class ADGame extends Activity implements Game {
	ADSurfaceView renderView;
    Graphics graphics;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;
 
    int laser;
    private StartAppAd startAppAd = null; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // integrate StartAPP SDK
      //  AndroidSDKProvider.setTestMode(true); 
        AndroidSDKProvider.initSDK(this); 
        if (startAppAd == null) {
     	   startAppAd = new StartAppAd(this);
     	   startAppAd.load();
     	} 
        // Go full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int frameBufferWidth = 320;
        int frameBufferHeight = 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.ARGB_8888);
        
        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new ADSurfaceView(this, frameBuffer);
        graphics = new ADGraphics(getAssets(), frameBuffer);
        fileIO = new ADFileIO(getAssets());
        input = new ADInput(this, renderView, scaleX, scaleY);
          
        screen = getStartScreen();
        setContentView(renderView);
       
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "InvaderGame");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (startAppAd == null) {
        	   startAppAd = new StartAppAd(this);
        	   startAppAd.load();
        	} 
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(startAppAd != null) {
            boolean showAd = startAppAd.doHome();
            if (showAd) {
               startAppAd = null;
            }
         } 
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }
    
    @Override
    public void onBackPressed(){
        if (startAppAd != null){
           startAppAd.show();
           startAppAd = null;
        }
        super.onBackPressed();
    } 
    
    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen can not be null");
        //destroy all screen and start new screen
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {
        return screen;
    }
}