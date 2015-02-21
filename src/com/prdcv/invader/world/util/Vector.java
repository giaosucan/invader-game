package com.prdcv.invader.world.util;

/**
 * This class defines a vector 2 dimensions of a position.
 * @author nccuong
 */
public class Vector {
    public float x, y;
    
    public Vector() {    
    }       
    
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector(Vector other) {
        this.x = other.x;
        this.y = other.y;
    }
    
    public boolean isValid(){
    	return (x > -50) && (y > -50);
    }
    
    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    public Vector add(Vector other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }
    
    public static Vector nowhereVector(){
		return new Vector(-100, -100);
    }
}