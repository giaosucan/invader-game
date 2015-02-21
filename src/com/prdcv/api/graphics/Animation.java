package com.prdcv.api.graphics;

import com.prdcv.invader.world.Explosion;

public class Animation {

	public static final int LOOPING = 0;
	public static final int NONLOOPING = 1;

	ImageHandler[] frames;
	final float duration;
	int mode;
	float ratio;
	private float width;
	private float height;

	public Animation(){
		duration = 0;
	}
	
	public Animation(float frameDuration, int mode, float ratio, 
			ImageHandler ... frames) {
		this.duration = frameDuration;
		this.ratio = ratio;
		this.frames = new ImageHandler[frames.length];
		
		width = Explosion.EXPLOSION_ORIGIN_WIDTH * ratio ;
		height = Explosion.EXPLOSION_ORIGIN_HEIGHT * ratio ;
		for (int i=0; i < frames.length; i++){
			this.frames[i] = frames[i].createScaledImage(ratio);
		}
		this.mode = mode;
	}
	
	public float getRatio(){
		return ratio;
	}

	public boolean isLooping(){
		return mode == LOOPING;
	}
	
	public boolean isFinished(float stateTime){
		return (stateTime > (duration * (float) frames.length)) && !isLooping();
	}
	
	public ImageHandler getFrame(float stateTime) {
		int frameNumber = (int)(stateTime / duration);

		if (!isLooping()) {
			frameNumber = Math.min(frames.length-1, frameNumber);            
		} else {
			frameNumber = frameNumber % frames.length;
		}        
		return frames[frameNumber];
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}