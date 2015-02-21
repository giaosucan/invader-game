package com.prdcv.invader.world;

import com.prdcv.api.graphics.Animation;
import com.prdcv.api.graphics.ImageHandler;

/**
 * This class defines the explosion.
 * @author nccuong
 */
public class Explosion extends Entity{

	public static int EXPLOSION_ORIGIN_WIDTH = 36;
	public static int EXPLOSION_ORIGIN_HEIGHT = 36;
	public static int EXPLOSION_FRAME = 16;
	
	/**
	 * Animation images of the explosion.
	 */
	Animation explosionAnim;
	
	public Explosion(){
		super(0, 0, 0, 0);
	}
	
	public Explosion(float x, float y, Animation explosionAnim) {
		super(x, y, (int) explosionAnim.getWidth(), 
				(int) explosionAnim.getHeight());
		this.explosionAnim = explosionAnim;
	}
	
	/**
	 * Update time of the explosion.
	 * @param cycleTime : running time of one cycle.
	 */
	public void explode(float cycleTime){
		stateTime += cycleTime;
	}
	
	/**
	 * Get current frame to draw.
	 * @return Current frame of the explosion.
	 */
	public ImageHandler getCurrentFrame(){
		return explosionAnim.getFrame(stateTime);
	}
	
	/**
	 * Check if the explosion has finished his job.
	 * @return the explosion is done or not.
	 */
	public boolean isFinished(){
		return explosionAnim.isFinished(stateTime);
	}
}
