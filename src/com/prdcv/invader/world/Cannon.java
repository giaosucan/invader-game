package com.prdcv.invader.world;

import com.prdcv.invader.Assets;

/**
 * This class defines the behavior of the cannon.
 * @author nccuong
 */
public class Cannon extends MovingEntity{

	/**
	 * Moving velocity of the cannon.
	 */
	public final static int CANNON_VELOCITY = 1;
	/**
	 * Number of lives the cannon has at the beginning.
	 */
	public final static int CANNON_INIT_LIVES = 3;
	/**
	 * After dead, this cannon still has a time for exploding
	 * for eye candy and relax purpose.
	 */
	public final static float CANNON_DEAD_TIME = 2.4f;
	public final static int CANNON_WIDTH = 34;
	public final static int CANNON_HEIGHT = 20;

	/**
	 * How many lives this cannon currently has.
	 */
	private int lives;

	public Cannon(float x, float y) {
		super(x, y, CANNON_WIDTH, CANNON_HEIGHT);
		lives = CANNON_INIT_LIVES;
		live();
	}
	
	/**
	 * Move the cannon depends on context.
	 * @param cycleTime : running time of one cycle.
	 * @param velo : velocity of the cannon.
	 */
	public void move(float cycleTime, float velocity) {
		if (isAlive()) 
		{
	        position.add(velocity, 0);
			if (position.x < 0)
				position.x = 0;
			if (position.x > (World.WORLD_WIDTH - Cannon.CANNON_WIDTH))
				position.x = World.WORLD_WIDTH - Cannon.CANNON_WIDTH;
		} 
		else {
			if (stateTime >= CANNON_DEAD_TIME) {
				lives--;
				if (lives > 0) live();
				position.x = World.CANNON_INIT_POSITION;
			}
		}
		stateTime += cycleTime;
	}

	/**
	 * The cannon get shot, kill him and return 
	 * 	an explosion at his position.
	 * @return an explosion at the cannon's position.
	 */
	public Explosion kill() {
		die();
		
		Explosion cannonExplosion = new Explosion(position.x, position.y, 
				Assets.getCannonExpAnim());
		return cannonExplosion;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void setLives(int lives){
		this.lives = lives;
	}
}
