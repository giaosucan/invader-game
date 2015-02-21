package com.prdcv.invader.world;

import android.graphics.Bitmap.Config;

import com.prdcv.api.graphics.ADImageHandler;
import com.prdcv.api.graphics.ImageHandler;
import com.prdcv.invader.Assets;
import com.prdcv.invader.world.util.Util;
import com.prdcv.invader.world.util.Vector;

/**
 * This class defines the shields which
 * 	are in front of the cannon to protect him.
 * @author nccuong.
 */
public class Shield extends Entity{
	
	public static int SHIELD_WIDTH = 40;
	public static int SHIELD_HEIGHT = 49;
	
	/**
	 * The shield can be broken at some part due to shots.
	 * This arrays contains the status of each point of the shield.
	 * It is 0 if the point is broken, 1 otherwise;
	 */
	private int[][] shieldStatus;
	/**
	 * Current image of the shield, can be broken at some part
	 * due to shots.
	 */
	private ImageHandler shieldImage;
	/**
	 * Check if all the shield is destroyed or not.
	 */
	private boolean isAlreadyExploded;
	
	public Shield(float x, float y) {
		super(x, y, SHIELD_WIDTH, SHIELD_HEIGHT);
		shieldStatus = new int[SHIELD_WIDTH][SHIELD_HEIGHT];
		shieldImage = new ADImageHandler(Assets.getShield());
		reset();
	}
	
	
	/**
	 * This shield got a shot.
	 * @param shot: the shot that hit the shield.
	 * @return a vector at the collision point, if they are not collided,
	 * return an invalid vector.
	 */
	public Vector getTouch(Entity entity){
		
		int maxX = (int) Util.min(entity.position.x + entity.getWidth(), 
					this.position.x + this.getWidth());
		int minX = (int) Util.max(entity.position.x, this.position.x);
		
		int maxY = (int) Util.min(entity.position.y + entity.getHeight(), 
				this.position.y + this.getHeight());
		int minY = (int) Util.max(entity.position.y, this.position.y);
		
		//if they are not collided
		if ((minX >= maxX) || (minY >= maxY)) 
			return Vector.nowhereVector();
		
		//destroy one part of the shield around the collision point
		for (int x = minX; x < maxX; x++)
			for (int y = minY; y < maxY; y++)
			{
				int cx = (int) (x - position.x);
				int cy = (int) (y - position.y);
				if (shieldStatus[cx][cy] !=0){
					destroyAround(cx, cy);
					return new Vector(x,y);
				}
			}
		//if we didn't find any collision.
		return Vector.nowhereVector();
	}
	
	/**
	 * At the collision point (x,y), the shield is destroyed
	 * around it.
	 * @param x : x coordinate of the point.
	 * @param y : y coordinate of the shield.
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
	 * Destroy a specific point of the shield.
	 * @param i : x coordinate of the point.
	 * @param j : y coordinate of the point.
	 */
	public void erasePoint(int i, int j){
		shieldStatus[i][j] = 0;
		shieldImage.getBitmap().setPixel(i, j, 0);
	}
	
	/**
	 * When get shot, the shield get exploded at the collision point.
	 * @param x : x coordinate of the collision.
	 * @param y : y coordinate of the collision.
	 * @return : an explosion at the collision point.
	 */
	public Explosion explode(float x, float y){
		Explosion shieldExplosion = new Explosion(x - 
				Assets.getShieldExpAnim().getWidth()/2, 
				y - Assets.getShieldExpAnim().getHeight()/2,  
				Assets.getShieldExpAnim());
		return shieldExplosion;
	}
	
	/**
	 * When alien touch the shield, all the shield is exploded.
	 * @return : the explosion of all the shield.
	 */
	public Explosion allExplode(){
		destroy();
		Explosion shieldExplosion = new Explosion(position.x, 
				position.y, Assets.getShieldAllExpAnim());
		return shieldExplosion;
	}
	
	/**
	 * @return: the current image of the shield.
	 */
	public ImageHandler getCurrentShieldImage(){
		return shieldImage;
	}
	
	/**
	 * Default bitmap is immutable, 
	 * we copy it in order to make it mutable.
	 */
	public void copyColorMap(){
		for (int i = 0; i < SHIELD_WIDTH; i++)
			for (int j = 0; j < SHIELD_HEIGHT; j++)
				shieldStatus[i][j] = Assets.getShield().getBitmap().getPixel(i, j);
	}
	
	/**
	 * Bring a fresh new shield.
	 */
	public void reset(){
		setAlreadyExploded(false);
		copyColorMap();
		shieldImage.setBitmap
		(Assets.getShield().getBitmap().copy(Config.ARGB_8888, true));
	}
	
	/**
	 * Destroy all the shield.
	 */
	public void destroy(){
		setAlreadyExploded(true);
		for (int i = 0; i < SHIELD_WIDTH; i++)
			for (int j = 0; j < SHIELD_HEIGHT; j++)
				erasePoint(i, j);
	}

	public void setAlreadyExploded(boolean isAlreadyExploded) {
		this.isAlreadyExploded = isAlreadyExploded;
	}

	public boolean isAlreadyExploded() {
		return isAlreadyExploded;
	}
}