package com.prdcv.invader.world;

import java.util.Random;

import android.graphics.Bitmap.Config;

import com.prdcv.api.graphics.ImageHandler;
import com.prdcv.invader.Assets;
import com.prdcv.invader.world.util.Util;
import com.prdcv.invader.world.util.Vector;

/**
 * This class defines the define the behavior of the big UFO,
 * the boss, appear after all the aliens are killed.
 * @author nccuong
 */

public class Boss extends MovingEntity{

	public static final int BOSS_WIDTH = 120;
	public static final int BOSS_HEIGHT = 38;
	public final static float BOSS_VELOCITY = 20f;
	public final static int BOSS_INIT_POSITION = 20;
	public final static int BOSS_SCORE = 500;
	public final static float BOSS_TURN_RATE = 0.005f;
	public final static float BOSS_DEAD_TIME = 4f;
	public final static float BOSS_SHOOT_RATE = 0.4f;
	public final static float BOSS_SHOOT_VELOCITY = 250f;
	public final static int BOSS_SHOOT_NUMBER = 5;
	public final static float BOSS_LEVEL_INCREASE_RATE = 0.2f;
	public final static int BOSS_HP = 10;
	public final int STATE_NO_HP = 2;
	
	/**
	 * Health point of the boss, need to shoot \hp time in order
	 * to kill him.
	 */
	private int hp;
	private float shootRate;
	private int shootNumber;
	private int currentHp;
	private float shootVelocity;
	
	/**
	 * Some parts which are broken has the status 0.
	 */
	private int[][] bossStatus;
	
	/**
	 * Current image of the boss, it may be broken some part 
	 * due to get shot.
	 */
	private ImageHandler bossImage;
	Random rand = new Random();
	
	public Boss(){
		super(BOSS_INIT_POSITION, 100, BOSS_WIDTH, BOSS_HEIGHT);
	}
	public Boss(float x, float y, ImageHandler bossImage) {
		super(x, y, BOSS_WIDTH, BOSS_HEIGHT);
		die();
		bossStatus = new int[BOSS_WIDTH][BOSS_HEIGHT];
		this.bossImage = bossImage;
		reset();
	}
	
	/**
	 * Bring the boss to the battle, init position, set velocity,
	 * and set boss parameter.
	 * @param level: current level of the game to set the 
	 * parameters accordingly.
	 */
	public void appear(int level){
		reset();
		super.live();
		this.position.x = BOSS_INIT_POSITION;
		this.position.y = 100;
		velocity.set(5, 0);
		goRight();
		hp = (int) (BOSS_HP * (1 + (level -1 )* BOSS_LEVEL_INCREASE_RATE));
		currentHp = hp;
		shootRate = BOSS_SHOOT_RATE * (1 + BOSS_LEVEL_INCREASE_RATE* (level -1));
		shootVelocity = BOSS_SHOOT_VELOCITY * (1 + BOSS_LEVEL_INCREASE_RATE*(level-1));
		shootNumber = (int) (BOSS_SHOOT_NUMBER * (1 + (level -1) * BOSS_LEVEL_INCREASE_RATE));
	}

	/**
	 * This boss has been shot a lot of time, he dies.
	 * @return An explosion at the position of the boss.
	 */
	public Explosion kill() {
		Explosion bossExpAnim = new Explosion(position.x
				+ getWidth()/2 - Assets.getBossExpAnim().getWidth()/2
				, position.y + getHeight()/2 - Assets.getBossExpAnim().getHeight()/2, 
				Assets.getBossExpAnim());
		return bossExpAnim;		
	}

	/**
	 * The boss get shot by cannon, find the position.
	 * @param shot 
	 * @return the position of the collision.
	 */
	public Vector getShot(Shot shot){

		int maxX = (int) Util.min(shot.position.x + shot.getWidth(), 
				this.position.x + this.getWidth());
		int minX = (int) Util.max(shot.position.x, this.position.x);

		int maxY = (int) Util.min(shot.position.y + shot.getHeight(), 
				this.position.y + this.getHeight());
		int minY = (int) Util.max(shot.position.y, this.position.y);

		//if they are not collided
		if ((minX >= maxX) || (minY >= maxY)) 
			return Vector.nowhereVector();
		for (int x = minX; x < maxX; x++)
			for (int y = minY; y < maxY; y++)
			{
				int cx = (int) (x - position.x);
				int cy = (int) (y - position.y);
				//boss got this shot.
				if (bossStatus[cx][cy] !=0){
					//destroy one part of the boss around the collision point
					destroyAround(cx, cy);
					currentHp--;
					if (currentHp == 0){
						state = STATE_NO_HP;
						stateTime = 0;
					}
					return new Vector(x,y);
				}
			}
		return Vector.nowhereVector();
	}

	/**
	 * When the boss get shot at (x,y), he is destroyed some part around (x,y).
	 * @param x collision point x
	 * @param y collision point y
	 */
	private void destroyAround(int x, int y) {

		int w = Assets.getShotBreak().getWidth();
		int h = Assets.getShotBreak().getHeight();
		int minX = (int) Util.max(x - w/2, 0);
		int maxX = (int) Util.min(x + w/2, this.getWidth());

		int minY = (int) Util.max(y - h/2, 0);
		int maxY = (int) Util.min(y + h/2, this.getHeight());

		for (int i = minX; i < maxX; i++)
			for (int j = minY; j < maxY; j++){
				int sX = i - x + w/2;
				int sY = j - y + h/2;
				if (Assets.getShotBreak().getBitmap().getPixel(sX, sY) !=0)
					erasePoint(i,j);
			}
	}
	
	
	/**
	 * Erase one point of the boss which got shot.
	 * @param i : position x of the point.
	 * @param j : position y of the point.
	 */
	public void erasePoint(int i, int j){
		bossStatus[i][j] = 0;
		bossImage.getBitmap().setPixel(i, j, 0);
	}
	
	/**
	 * Bring a fresh new boss.
	 */
	public void reset(){
		copyColorMap();
		bossImage.setBitmap
		(Assets.getBoss().getBitmap().copy(Config.ARGB_8888, true));
	}
	
	/**
	 * Reset the color of the boss image, in order to 
	 * bring a fresh new one.
	 */
	public void copyColorMap(){
		for (int i = 0; i < BOSS_WIDTH; i++)
			for (int j = 0; j < BOSS_HEIGHT; j++)
				bossStatus[i][j] = Assets.getBoss().getBitmap().getPixel(i, j);
	}
	
	/**
	 * When the boss is out of health point, he dies but still has time
	 * for exploding.
	 * @return the state of the boss is exploding or not.
	 */
	public boolean isNoHP(){
		return state == STATE_NO_HP;
	}
	
	/**
	 * This boss has died and finished exploding.
	 * @return is really dead or not.
	 */
	public boolean isReallyDead(){
		return (isDead() && !isDisplayScoring());
	}
	
	/**
	 * Display scoring when exploding
	 * @return is displaying score or not.
	 */
	public boolean isDisplayScoring(){
		return (isDead() && (stateTime < Boss.BOSS_DEAD_TIME));
	}

	/**
	 * Move the boss, randomly.
	 * @param cycleTime : running time of one cycle,
	 * 	to ensure the frame rate is the same in all devices.
	 */
	public void move(float cycleTime){
		if  (isAlive()){
			if (isLeft()) {
				if (rand.nextFloat() < BOSS_TURN_RATE){
					this.velocity.set(+cycleTime * BOSS_VELOCITY * (rand.nextInt(5)+1), 0);
					goRight();
				}
			}

			if (isRight()){
				if (rand.nextFloat() < BOSS_TURN_RATE) {
					this.velocity.set(-cycleTime * BOSS_VELOCITY * (rand.nextInt(5)+1), 0);
					goLeft();
				}
			}
			this.position.add(velocity);
			if (position.x < 0) {
				position.x = 0; 
				this.velocity.set(+cycleTime * BOSS_VELOCITY * (rand.nextInt(5)+1), 0);
				goRight();
			}
			if (position.x > World.WORLD_WIDTH - Boss.BOSS_WIDTH){
				position.x = World.WORLD_WIDTH - Boss.BOSS_WIDTH;
				this.velocity.set(-cycleTime * BOSS_VELOCITY * (rand.nextInt(5)+1), 0);
				goLeft();
			}
		}
		else if (isNoHP())
		{
			if (stateTime > Boss.BOSS_DEAD_TIME){
				this.die();
			}
		}
		stateTime += cycleTime;
	}

	public void setShootNumber(int shootNumber) {
		this.shootNumber = shootNumber;
	}

	public int getShootNumber() {
		return shootNumber;
	}
	
	public float getShootRate(){
		return shootRate;
	}
	
	public float getShootVelocity(){
		return shootVelocity;
	}
	
	public ImageHandler getBossImage(){
		return bossImage;
	}
}
