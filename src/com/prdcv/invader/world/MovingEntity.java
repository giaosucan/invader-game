package com.prdcv.invader.world;

import com.prdcv.invader.world.util.Vector;

/**
 * A moving entity is an Entity, plus the velocity 
 * 	and moving direction.
 * @author nccuong
 */
public abstract class MovingEntity extends Entity {
	
	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_UP = 2;
	public static final int DIRECTION_DOWN = 3;
	
    /**
     * Current vector velocity of the moving entity.
     */
    protected final Vector velocity;
    
    /**
     * Current direction of the entity,
     * possibly one of the four directions. 
     */
    protected int direction;
    
    public MovingEntity(float x, float y, int width, int height) {
        super(x, y, width, height);
        velocity = new Vector();
    }
    
    /* (non-Javadoc)
     * @see com.prdcv.invader.world.Entity#die()
     */
    public void die(){
    	super.die();
    	velocity.x = 0;
    	velocity.y = 0;
    }
    
    public boolean isLeft(){
    	return direction == DIRECTION_LEFT;
    }
    
    public boolean isRight(){
    	return direction == DIRECTION_RIGHT;
    }
    
    public boolean isUp(){
    	return direction == DIRECTION_UP;
    }
    
    public boolean isDown(){
    	return direction == DIRECTION_DOWN;
    }
    
    public void goLeft(){
    	direction = DIRECTION_LEFT;
    }
    
    public void goRight(){
    	direction = DIRECTION_RIGHT;
    }
    
    public void goUp(){
    	direction = DIRECTION_UP;
    }
    
    public void goDown(){
    	direction = DIRECTION_DOWN;
    }
}