package com.prdcv.invader.world;

import java.util.List;

import android.graphics.Color;

import com.prdcv.api.graphics.Graphics;
import com.prdcv.invader.Assets;
import com.prdcv.invader.Settings;

/**
 * This class draw everything in the game.
 * @author nccuong
 */
public class Renderer {

	/**
	 * Draw the background, top and bottom line.
	 * @param g : Current graphics pointer of the game
	 */
	public static void renderUI(Graphics g){
		g.drawImage(Assets.getBackground(), 0, 0);
		//draw top line
		g.drawLine(0, World.TOP_SPACE, World.WORLD_WIDTH, World.TOP_SPACE, 
				Color.WHITE);
		//draw bottom line
		g.drawLine(0, World.PLAY_BORDER_BOTTOM, World.WORLD_WIDTH, 
				World.PLAY_BORDER_BOTTOM, Color.WHITE);
	}

	/**
	 * Draw the cannon.
	 * @param g: the graphics object
	 * @param cannon: the cannon object
	 */
	public static void renderCannon(Graphics g, Cannon cannon) {
		if (cannon.isDead()) return;
		int x = (int) (cannon.position.x);
		int y = (int) (cannon.position.y);
		g.drawImage(Assets.getCannon(), x, y);
	}

	/**
	 * Draw the alien.
	 * @param g: the graphics object.
	 * @param aliens: list of aliens.
	 */
	public static void renderAlien(Graphics g, List<Alien> aliens) {
		for (Alien alien:aliens)
		{
			int x = (int) (alien.position.x);
			int y = (int) (alien.position.y);
			if (alien.isAlive()){		
				g.drawImage(Assets.getAlien(alien.getPicNumber()), x, y);
			}
			//Alien score
			if (alien.isDead()){
				g.drawText(Integer.toString(alien.getKillScore()), x, y, 15, Color.WHITE);
			}
		}
	}

	/**
	 * Draw shields.
	 * @param g: the graphics object.
	 * @param shields: list of shield objects.
	 */
	public static void renderShield(Graphics g, List<Shield> shields) {
		for (Shield shield:shields){
			int x = (int ) (shield.position.x);
			int y = (int ) (shield.position.y);			
			g.drawImage(shield.getCurrentShieldImage(), x, y);
		}
	}

	/**
	 * Draw of UFO.
	 * @param g: graphics object of the game.
	 * @param ufo: the UFO object. 
	 * @param killScore: score for killing this UFO to display.
	 */
	public static void renderUFO(Graphics g, UFO ufo, int killScore) {
		int x = (int) (ufo.position.x);
		int y = (int) (ufo.position.y);
		if (ufo.isAlive())
			g.drawImage(Assets.getUfo(), x, y);
		else
			if (ufo.isDead() && (ufo.isDisplayScoring()))
				g.drawText(Integer.toString(killScore), x, y + UFO.UFO_HEIGHT/2, 
						12, Color.WHITE);
	}
	
	/**
	 * Draw the boss, big UFO appear after each wave.
	 * @param g: the graphics object.
	 * @param boss: the boss object.
	 * @param score: score to kill this boss.
	 */
	public static void renderBoss(Graphics g, Boss boss, int score) {
		int x = (int) (boss.position.x);
		int y = (int) (boss.position.y);
		if (boss.isAlive())
			g.drawImage(boss.getBossImage(), x, y);
		else
			if (boss.isNoHP())
				g.drawText(Integer.toString(score), x - 30 + 
						Boss.BOSS_WIDTH/2, 
			y + Boss.BOSS_HEIGHT/2, 16, Color.WHITE);
		
	}

	/**
	 * Draw the shot(s) from cannon.
	 * @param g: the graphics object.
	 * @param shots: all of shots from cannon.
	 */
	public static void renderCannonShot(Graphics g, List<Shot> shots){
		for (Shot shot:shots)
		{
			int x = (int) (shot.position.x);
			int y = (int) (shot.position.y);
			g.drawImage(Assets.getLaser(), x, y);
		}
	}

	/**
	 * Draw the shots from Aliens.
	 * @param g: the graphics object.
	 * @param shots: all of shots from aliens.
	 */
	public static void renderAlienShot(Graphics g, List<Shot> shots){
		for (Shot shot:shots)
		{
			int x = (int) (shot.position.x);
			int y = (int) (shot.position.y);
			g.drawImage(Assets.getAlienfire(), x, y);
		}
	}
	
	/**
	 * Draw all the explosions.
	 * @param g: the graphics object.
	 * @param explosions: all the explosions from the game.
	 */
	public static void renderExplosion(Graphics g, List<Explosion> explosions){
		for (Explosion explosion:explosions){
			int x = (int) (explosion.position.x);
			int y = (int) (explosion.position.y);
			g.drawImage(explosion.getCurrentFrame(), x, y);
		}
	}

	/**
	 * Draw score, hi-score and current level of the game.
	 * @param g: the graphics object.
	 * @param score: current score.
	 * @param level: current level.
	 */
	public static void renderScoreAndLevel(Graphics g, int score, int level){
		g.drawText("Level "+Integer.toString(level), 0, 20, 16, Color.WHITE);
		g.drawText("Score "+ Integer.toString(score), 100, 20, 16, Color.WHITE);
		g.drawText("Hi-score " + Integer.toString(Settings.highscores[0]), 200, 20, 16, Color.WHITE);
	}

	/**
	 * Draw the number of lives of cannon at the bottom of screen.
	 * @param g: the graphics object.
	 * @param lives: the number of lives.
	 */
	public static void renderLives(Graphics g, int lives){
		for (int i=0; i < lives; i++){
			g.drawImage(Assets.getCannon(), i*(Cannon.CANNON_WIDTH) , 445);
		}
	}
}
