package com.prdcv.invader.world.util;

import com.prdcv.invader.world.Entity;

/**
 * This class contains some frequently use functions of the program.
 * @author nccuong
 */
public class Util {
	
	/**
	 * Get the minimum value of two variables.
	 * @param x 
	 * @param y
	 * @return minimum of x and y
	 */
	public static float min(float x, float y){
		if (x < y) return x;
		else return y;
	}
	
	/**
	 * Get the maximum value of two variables.
	 * @param x
	 * @param y
	 * @return maximum of x and y.
	 */
	public static float max(float x, float y){
		if (x > y) return x;
			else return y;
	}
	
    /**
     * Check if the two entities has common point or not.
     * @param e1 the first entity.
     * @param e2 the second entity.
     * @return true if two entities has collision point, false otherwise.
     */
    public static boolean testOverlap(Entity e1, Entity e2) {
        if(e1.position.x < e2.position.x + e2.getWidth() &&
           e1.position.x + e1.getWidth() > e2.position.x &&
           e1.position.y < e2.position.y + e2.getHeight() &&
           e1.position.y + e1.getHeight() > e2.position.y)
            return true;
        else
            return false;
    }
}