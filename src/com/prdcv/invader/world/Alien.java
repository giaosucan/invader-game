package com.prdcv.invader.world;

import java.util.Random;

import com.prdcv.invader.Assets;

/**
 * This class defines the behavior of the alien.
 * @author nccuong
 */
public class Alien extends MovingEntity{

	public final static int ALIEN_WIDTH = 20;
	public final static int ALIEN_HEIGHT = 20;
	public final static int ALIEN_VELOCITY = 8;
	public final static float ALIEN_DEAD_TIME = 1.6f;
	public final static float ALIEN_SHOOT_RATE = 0.05f;
	public final static float ALIEN_SPEED_MULTIPLIER = 0.3f;	
	public final static int ALIEN_MOVEDOWN_DISTANCE = 15;
	public final static float ANIM_RATE =1f;
		
	private int rank;
	private int column;
	boolean wasLeft = false;
	float movedDistance = 0;
	private int picNumber = 0;
	float changePicTime = 0;
	Random rand = new Random();

	public Alien(float x, float y, int rank, int column) {
		super(x, y, ALIEN_WIDTH, ALIEN_HEIGHT);
		picNumber = rank;
		live();
		goRight();
		setRank(rank);
		setColumn(column);
	}

	/**Update the alien depends on the context.
	 * @param cycleTime: running time of one cycle
	 * @param speedMultiplier: multiplier, depends on time and level
	 * @param currentAlienMinColumn: current column on the left
	 * @param currentAlienMaxColumn: current column on the right
	 */
	public void move(float cycleTime, float accel, 
			int currentAlienMinColumn, int currentAlienMaxColumn) {
		if (isAlive()) {
			float delta = cycleTime * ALIEN_VELOCITY * accel;
			movedDistance += delta;
			if (isLeft()) {
				position.x -= delta;
				if (position.x - ((getColumn() - currentAlienMinColumn) *
						World.ALIEN_SPACE)  < 0) {
					goDown();
					wasLeft = true;
					movedDistance = 0;
				}
			}
			if (isRight()) {
				position.x += delta;
				if (position.x + ((currentAlienMaxColumn - getColumn() + 1)
						* World.ALIEN_SPACE ) > World.WORLD_WIDTH) {
					goDown();
					wasLeft = false;
					movedDistance = 0;
				}
			}
			if (isDown()) {
				position.y += delta;
				if (movedDistance > ALIEN_MOVEDOWN_DISTANCE) {
					if (wasLeft) goRight();
					else goLeft();
					movedDistance = 0;
				}
			}
		}
		stateTime += cycleTime;
		changePicTime += cycleTime;
		if (changePicTime > ANIM_RATE){
			picNumber = 9 - picNumber;
			changePicTime = 0;
		}
	}

	/**
	 * This alien get shot, kill him.
	 * @return An explosion at the position of the alien
	 */
	public Explosion kill() {
		die();
		Explosion alienExplosion = new Explosion(position.x, position.y, 
				Assets.getAlienExpAnim());
		return alienExplosion;
	}
	
	/** After dead, alien still has time for explosion at this position.
	 * @return is really dead or not
	 */
	public boolean isReallyDead(){
		//exploding time and display score time ended
		return (isDead() && (stateTime > ALIEN_DEAD_TIME));
	}
	
	/** Get the killing score of this alien.
	 * @return the kill score
	 */
	public int getKillScore(){
		if (rank == 0) return 30;
		if ((rank == 1) || (rank ==2)) return 20;
		if ((rank == 3) || (rank ==4)) return 10;
		return 0;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}

	public void setPicNumber(int picNumber) {
		this.picNumber = picNumber;
	}

	public int getPicNumber() {
		return picNumber;
	}
}
