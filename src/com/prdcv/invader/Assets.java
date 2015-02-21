package com.prdcv.invader;

import com.prdcv.api.graphics.Animation;
import com.prdcv.api.graphics.ImageHandler;

/**
 * This class store all the images of the program.
 * @author nccuong
 */
public class Assets {
	
	private Assets(){
	}
	
	private static class SingletonHolder{
		public static final Assets INSTANCE = new Assets();
	}
	
	public static Assets getInstance(){
		return SingletonHolder.INSTANCE;
	}
	
	//graphics assets
    private static ImageHandler background;
    private static ImageHandler laser;
    private static ImageHandler alienfire;
    private static ImageHandler cannon;
    private static ImageHandler[] alien = new ImageHandler[10];
    private static ImageHandler ufo;
    private static ImageHandler shield;
    private static ImageHandler shotBreak;
    private static ImageHandler boss;
    private static Animation cannonExpAnim;
    private static Animation alienExpAnim;
    private static Animation shotExpAnim;
    private static Animation shieldExpAnim;
    private static Animation shieldAllExpAnim;
    private static Animation ufoExpAnim;
    private static Animation bossExpAnim;
    
    
    
	public static ImageHandler getBackground() {
		return background;
	}
	public static void setBackground(ImageHandler background) {
		Assets.background = background;
	}
	public static ImageHandler getCannon() {
		return cannon;
	}
	public static void setCannon(ImageHandler cannon) {
		Assets.cannon = cannon;
	}
	public static ImageHandler getAlien(int i) {
		return alien[i];
	}
	public static void setAlien(ImageHandler alien, int index) {
		Assets.alien[index] = alien;
	}
	
	public static void setUfo(ImageHandler ufo) {
		Assets.ufo = ufo;
	}
	public static ImageHandler getUfo() {
		return ufo;
	}
	
	public static void setShield(ImageHandler shield) {
		Assets.shield = shield;
	}
	public static ImageHandler getShield() {
		return shield;
	}
	
	public static void setLaser(ImageHandler laser) {
		Assets.laser = laser;
	}
	public static ImageHandler getLaser() {
		return laser;
	}
	public static void setAlienfire(ImageHandler alienfire) {
		Assets.alienfire = alienfire;
	}
	public static ImageHandler getAlienfire() {
		return alienfire;
	}
	public static void setCannonExpAnim(Animation cannonExpAnim) {
		Assets.cannonExpAnim = cannonExpAnim;
	}
	public static Animation getCannonExpAnim() {
		return cannonExpAnim;
	}
	public static void setAlienExpAnim(Animation alienExpAnim) {
		Assets.alienExpAnim = alienExpAnim;
	}
	public static Animation getAlienExpAnim() {
		return alienExpAnim;
	}
	public static void setShotExpAnim(Animation shotExpAnim) {
		Assets.shotExpAnim = shotExpAnim;
	}
	public static Animation getShotExpAnim() {
		return shotExpAnim;
	}
	public static void setShieldExpAnim(Animation shieldExpAnim) {
		Assets.shieldExpAnim = shieldExpAnim;
	}
	public static Animation getShieldExpAnim() {
		return shieldExpAnim;
	}
	public static void setUfoExpAnim(Animation ufoExpAnim) {
		Assets.ufoExpAnim = ufoExpAnim;
	}
	public static Animation getUfoExpAnim() {
		return ufoExpAnim;
	}
	public static void setShieldAllExpAnim(Animation shieldAllExpAnim) {
		Assets.shieldAllExpAnim = shieldAllExpAnim;
	}
	public static Animation getShieldAllExpAnim() {
		return shieldAllExpAnim;
	}
	public static ImageHandler getShotBreak() {
		return shotBreak;
	}
	public static void setShotBreak(ImageHandler shotBreak) {
		Assets.shotBreak = shotBreak;
	}

	public static void setBoss(ImageHandler boss) {
		Assets.boss = boss;
	}

	public static ImageHandler getBoss() {
		return boss;
	}

	public static void setBossExpAnim(Animation bossExpAnim) {
		Assets.bossExpAnim = bossExpAnim;
	}

	public static Animation getBossExpAnim() {
		return bossExpAnim;
	}
	}
