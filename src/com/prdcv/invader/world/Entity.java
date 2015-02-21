package com.prdcv.invader.world;

import com.prdcv.invader.world.util.Vector;

/**
 * This class defines the Entity, an abstract class which include
 * the common attributes and operations of every objects in the game. 
 * @author nccuong
 */
public abstract class Entity {
	
	/**
	 * life state of the entity. 
	 */
	public static final int STATE_ALIVE = 1;
	
	/**
	 * Death state of the entity.
	 */
	public static final int STATE_DEAD = 0;
	
	public final Vector position;
	protected int width;
	protected int height;
	
    /**
     * Current state of the entity. 
     */
    protected int state;
    
	/**
	 * How long this entity has been lived.
	 */
	protected float stateTime;
    
    /**
     * An entity consists of a position, a width length and a height 
     * in the space.
     * @param x: position x of the Entity
     * @param y: position y of the Entity
     * @param width: width of the entity.
     * @param height: height of the entity.
     */
    public Entity(float x, float y, int width, int height) {
        this.position = new Vector(x,y);
        this.width = width;
        this.height = height;
        stateTime = 0;
    }
    
    /**
     * Bring the entity to life.
     */
    public void live(){
    	state = STATE_ALIVE;
    	stateTime = 0;
    }
    
    /**
     * Bring the entity to death.
     */
    public void die(){
    	state = STATE_DEAD;
    	stateTime = 0;
    }
    
    /**
     * Check if this entity is alive or not.
     * @return true if entity is alive, false otherwise.
     */
    public boolean isAlive(){
    	return state == STATE_ALIVE;
    }
    
    /**
     * Check if this entity is dead or not.
     * @return true if entity is dead, false otherwise.
     */
    public boolean isDead(){
    	return state == STATE_DEAD;
    }

	public float getWidth() {
		return width;
	}
    
	public float getHeight() {
		return height;
	}
}