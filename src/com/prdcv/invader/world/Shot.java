package com.prdcv.invader.world;

import com.prdcv.invader.Assets;

/**
 * This class defines the shots, shots from
 * cannon and from alien both use this object.
 * @author nccuong.
 */
public class Shot extends MovingEntity {
	public final static int SHOT_CANNON_WIDTH = 7; 
	public final static int SHOT_CANNON_HEIGHT = 24;
	public final static int SHOT_ALIEN_WIDTH = 5;
	public final static int SHOT_ALIEN_HEIGHT = 18;
	public final static float SHOT_CANNON_VELOCITY = 400f;
	public final static float SHOT_ALIEN_VELOCITY = 200f;
	
	public Shot(float x, float y, float velocityY, int shotWidth, int shotHeight) {
		super(x, y, shotWidth, shotHeight);
		velocity.y = velocityY;
	}

	/**
	 * Move the shot at the current time.
	 * @param cycleTime: running time of one render cycle,
	 * to ensure its speed display the same at every device.
	 */
	public void move(float cycleTime) {
		position.y += velocity.y * cycleTime;
	}
	
	/**
	 * When the shot collide with something, it create an explosion.
	 * @return An explosion at the collision point.
	 */
	public Explosion collide(){
		int collideY;
		if (isFromCannon()) collideY = (int) position.y;
			else collideY = (int) (position.y + getHeight());
		Explosion shotCollide = new 
			Explosion(position.x + this.getWidth()/2 - Assets.getShotExpAnim().getWidth()/2 
					,collideY - Assets.getShotExpAnim().getHeight()/2, 
					Assets.getShotExpAnim());
		return shotCollide;
	}
	
	public boolean isFromCannon(){
		return velocity.y < 0;
	}
	
	public boolean isFromAlien(){
		return velocity.y > 0;
	}
}