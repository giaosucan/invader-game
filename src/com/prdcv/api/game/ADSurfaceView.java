package com.prdcv.api.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ADSurfaceView extends SurfaceView implements Runnable {
    ADGame game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    
    public ADSurfaceView(ADGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }

    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }      
    
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while(running) {  
            if(!holder.getSurface().isValid())
                continue;           
            
            //get the execution time of one running cycle
            float cycleTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            //time to shoot
            game.getCurrentScreen().update(cycleTime);
            game.getCurrentScreen().present(cycleTime);
            
            //lock canvas to draw surfaceview
            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);                           
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
            	//join thread useful when press home screen.
                renderThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }        
}