package com.prdcv.invader.world;

import java.util.Random;

import com.prdcv.invader.Assets;

/**
 * This class define the UFO appear after a period of time
 * above all the aliens.
 * @author nccuong
 */
public class UFO extends MovingEntity{

	/**
	 * Appear each 25 seconds.
	 */
	public final static float UFO_APPEAR_RATE = 25f;
	public final static int UFO_VELOCITY = 60;
	public final static int UFO_DISAPPEAR = 2;
	public final static float UFO_DEAD_TIME = 2f;
	public final static int UFO_WIDTH = 41;
	public final static int UFO_HEIGHT = 13;
	
	Random random;
	
	public UFO(float x, float y) {
		super(x, y, UFO_WIDTH, UFO_HEIGHT);
		state = UFO_DISAPPEAR;
		goRight();
		random = new Random();
	}
	
	/**
	 * Update the UFO.
	 * @param cycleTime: running time of one cycle
	 */
	public void update(float cycleTime) {
		
		if (this.isAlive()) {
	        position.add(velocity.x * cycleTime, velocity.y * cycleTime);
			if (position.x < 0 - UFO.UFO_WIDTH){
				position.x = 0 - UFO.UFO_WIDTH;
				disappear();
			}
			if (position.x > World.WORLD_WIDTH){
				position.x = World.WORLD_WIDTH;
				disappear();
			}
		} 
		else
			if (this.isDead() && !isDisplayScoring()){
				resetPosition();
			}
		stateTime += cycleTime;
	}
	
	/**
	 * UFO appear above aliens.
	 */
	public void appear(){
		live();
		if (isLeft())
			velocity.x = -UFO_VELOCITY;
		else
			velocity.x = +UFO_VELOCITY;
	}
	
	/**
	 * UFO disappear.
	 */
	public void disappear(){
		state = UFO_DISAPPEAR;
		velocity.x = 0;
		if (isRight()) goLeft();
		else goRight();
	}
	
	/**
	 * Come out of the screen.
	 */
	public void resetPosition(){
		if (isLeft()) position.x = - UFO.UFO_WIDTH;
		else position.x =  World.WORLD_WIDTH;
	}
	
	/**
	 * Get shot by cannon.
	 * @return an explosion at died position.
	 */
	public Explosion kill() {
		super.die();
		if (isRight()) goLeft(); 
			else goRight();
		Explosion ufoExplosion = new Explosion(position.x, position.y, 
				Assets.getUfoExpAnim());
		return ufoExplosion;		
	}

	public boolean isDisplayScoring(){
		return stateTime < UFO_DEAD_TIME;
	}
	
	/**
	 * Kill score varies from 50 to 150
	 * @return 50, 100 or 150.
	 */
	public int getKillScore(){
		return (random.nextInt(3) + 1) * 50;
	}
}
