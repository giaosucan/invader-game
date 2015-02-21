package com.prdcv.invader.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.prdcv.api.graphics.ADImageHandler;
import com.prdcv.api.graphics.Graphics;
import com.prdcv.invader.Assets;
import com.prdcv.invader.world.util.Util;
import com.prdcv.invader.world.util.Vector;

/**
 * This class defines all the interaction between object in the game
 * including shooting time, killing, ufo appear, and boss.
 * @author nccuong.
 */
public class World {

	public final static int WORLD_WIDTH = 320;
	public final static int WORLD_HEIGHT = 480;
	final static int TOP_SPACE = 30;
	final static int BOTTOM_SPACE = 50;
	final static int PLAY_BORDER_BOTTOM = WORLD_HEIGHT - BOTTOM_SPACE;
	final static int SHIELD_CANNON_SPACE = 100;
	final static int UFO_ALIEN_SPACE = 40;
	final static int SHIELD_SPACE = Shield.SHIELD_WIDTH * 2;
	final static int CANNON_BORDER_SPACE = 5;
	final static int CANNON_INIT_POSITION = 0;

	final static int ALIEN_ROW = 5;
	final static int ALIEN_COLUMN = 11;
	final static int SHIELD_NUMBER = 4;
	final static int ALIEN_ACCEL_THRESHOLD = 7;
	final static float ALIEN_SPACE = 1.2f * Alien.ALIEN_WIDTH;

	final Cannon cannon;
	final UFO ufo;
	final Boss boss;
	final List<Shot> shotsAlien = new ArrayList<Shot>();
	final List<Shot> shotsCannon = new ArrayList<Shot>();
	final List<Explosion> explosions = new ArrayList<Explosion>();
	final List<Alien> aliens = new ArrayList<Alien>();
	final List<Shield> shields = new ArrayList<Shield>();

	private int currentAlienMinColumn;
	private int currentAlienMaxColumn;
	private int ufoKillScore = 0;

	boolean alienTouchBottom = false;
	int[] alienColumnCount = new int[ALIEN_COLUMN];
	float ufoTime;
	int currentAlienNumber;;

	int level = 1;
	int score = 0;
	float accelFactor;

	boolean cannonCanShoot = true;
	Random random;

	public World() {
		cannon = new Cannon(CANNON_INIT_POSITION, WORLD_HEIGHT - 
				BOTTOM_SPACE - Cannon.CANNON_HEIGHT - 
				CANNON_BORDER_SPACE);
		createAliens();
		createShields();
		ufo = new UFO(0, TOP_SPACE + UFO.UFO_HEIGHT/2);
		boss = new Boss(Boss.BOSS_INIT_POSITION, 100, new ADImageHandler(Assets.getBoss()));
		//boss.appear(2);
		random = new Random();
		ufoTime = 0.0f;
		accelFactor = 0.5f;
	}

	/**
	 * Create new aliens.
	 */
	private void createAliens() {
		accelFactor = 1 + (level - 1)* Alien.ALIEN_SPEED_MULTIPLIER;
		currentAlienMinColumn = 0;
		currentAlienMaxColumn = ALIEN_COLUMN - 1;
		currentAlienNumber = ALIEN_ROW * ALIEN_COLUMN;
		for (int row = 0; row < ALIEN_ROW; row++) {
			for (int column = 0; column < ALIEN_COLUMN; column++) {
				Alien alien = new Alien(column * ALIEN_SPACE, TOP_SPACE + 
						UFO_ALIEN_SPACE + row * ALIEN_SPACE, row, column);
				aliens.add(alien);
			}
		}

		//number count of each alien column
		for (int column = 0; column < ALIEN_COLUMN; column++){
			alienColumnCount[column] = ALIEN_ROW;
		}
	}

	/**
	 * Create fresh new shields in front of the cannon.
	 */
	private void createShields() {
		for (int i = 0; i < SHIELD_NUMBER; i++) {
			shields.add(new Shield(Shield.SHIELD_WIDTH/2 + i * SHIELD_SPACE, 
					PLAY_BORDER_BOTTOM - SHIELD_CANNON_SPACE));
		}
	}
	
	/**
	 * Move everything in the world.
	 * @param cycleTime: running time of one render cycle.
	 * @param cannonVelocity: velocity of the cannon.
	 */
	public void move(float cycleTime, float cannonVelocity) {
		cannon.move(cycleTime, cannonVelocity);

		if (boss.isDead())
		{
			updateAliens(cycleTime);
			updateUFO(cycleTime);
			if (aliens.isEmpty()) { 
				boss.appear(level);
				ufo.disappear();
			}
		}
		else
		{
			updateBoss(cycleTime);
		}
		updateShots(cycleTime);
		updateExplosions(cycleTime);

		checkShot();
		checkAliens();

	}
	
	/**
	 * Move Boss.
	 * @param cycleTime : running time of one cycle.
	 */
	private void updateBoss(float cycleTime){
		boss.move(cycleTime);
		if (boss.isAlive())			
			if (random.nextFloat() < boss.getShootRate() * cycleTime){
				for (int i = 0; i < boss.getShootNumber(); i++){
					Shot shot = new Shot(boss.position.x + i*boss.getWidth()/ 
							boss.getShootNumber(), boss.position.y + boss.getHeight(),
							boss.getShootVelocity(), Shot.SHOT_ALIEN_WIDTH, Shot.SHOT_ALIEN_HEIGHT);
					shotsAlien.add(shot);
				}
			}
		if (boss.isDead()){
			score += Boss.BOSS_SCORE * level;
			levelUp();
		}
	}

	/**
	 * All aliens and boss die, go to the next level.
	 */
	public void levelUp(){
		level++;
		createAliens();
		resetShields();
	}

	/**
	 * Check explosions.
	 * @param cycleTime: running time of one render cycle.
	 */
	private void updateExplosions(float cycleTime) {
		Iterator<Explosion> explosionsItr = explosions.iterator();
		while (explosionsItr.hasNext()){
			Explosion explosion = explosionsItr.next();
			explosion.explode(cycleTime);
			if (explosion.isFinished()){
				explosionsItr.remove();
			}
		}
	}	

	private void updateAliens(float cycleTime) {
		Iterator<Alien> alienItr = aliens.iterator();
		while (alienItr.hasNext()){
			Alien alien = alienItr.next();
			alien.move(cycleTime, accelFactor, currentAlienMinColumn,
					currentAlienMaxColumn);

			if (alien.isAlive()) {

				//alien touch bottom, game over
				if ( alien.position.y > PLAY_BORDER_BOTTOM){
					alienTouchBottom = true;
					break;
				}

				//alien shoot if time has come.
				if (random.nextFloat() < (accelFactor * Alien.ALIEN_SHOOT_RATE 
						* cycleTime)) {
					Shot shot = new Shot(alien.position.x + Alien.ALIEN_WIDTH/2,
							alien.position.y, Shot.SHOT_ALIEN_VELOCITY, 
							Shot.SHOT_ALIEN_WIDTH, Shot.SHOT_ALIEN_HEIGHT);
					shotsAlien.add(shot);
				}
			}

			//end of explosion time
			if (alien.isReallyDead())
				alienItr.remove();
		}
	}

	private void updateShots(float cycleTime){
		updateCannonShots(cycleTime);
		updateAlienShots(cycleTime);
	}

	private void updateAlienShots(float cycleTime) {
		//update alien shots
		Iterator<Shot> shotsAlienItr = shotsAlien.iterator(); 
		while (shotsAlienItr.hasNext()) {
			Shot shotAlien = shotsAlienItr.next();
			shotAlien.move(cycleTime);
			if (shotAlien.position.y > PLAY_BORDER_BOTTOM - shotAlien.getHeight() - 5) {
				shotAlien.position.y = PLAY_BORDER_BOTTOM - shotAlien.getHeight() - 5;
				explosions.add(shotAlien.collide());
				shotsAlienItr.remove();
			}
		}
	}

	private void updateCannonShots(float cycleTime){
		//update cannon shots
		Iterator<Shot> shotsCannonItr = shotsCannon.iterator(); 
		while (shotsCannonItr.hasNext()){
			Shot shotCannon = shotsCannonItr.next();
			shotCannon.move(cycleTime);
			if (shotCannon.position.y < TOP_SPACE)
			{
				shotCannon.position.y = TOP_SPACE;
				explosions.add(shotCannon.collide());
				shotsCannonItr.remove();
				cannonCanShoot = true;
			}
		}
	}

	private void updateUFO(float cycleTime) {
		ufoTime += cycleTime;
		if (ufoTime > UFO.UFO_APPEAR_RATE){
			ufo.appear();
			ufoTime = 0;
		}
		ufo.update(cycleTime);
	}

	private void checkAliens() {
		if (cannon.isDead()) return;
		for (Alien alien:aliens) {
			if (Util.testOverlap(cannon, alien)) {
				cannon.setLives(1);
				Explosion cannonExplosion = cannon.kill();
				explosions.add(cannonExplosion);
				return;
			}

			for (Shield shield : shields) {
				if (!shield.isAlreadyExploded())
					if (Util.testOverlap(shield, alien))
						if (shield.getTouch(alien).isValid())
						{
							explosions.add(shield.allExplode());
						}
			}
		}
	}

	private void checkShot(){
		checkShotAlien();
		checkShotCannon();
	}

	private void checkShotCannon(){
		Iterator<Shot> shotsCannonItr = shotsCannon.iterator();
		//check cannon shots
		while (shotsCannonItr.hasNext())
		{
			Shot shot = shotsCannonItr.next();
			boolean wasPurged = false;
			//shield get shot by cannon
			for (Shield shield : shields) {
				if (Util.testOverlap(shield, shot)){
					Vector collidePosition = shield.getTouch(shot); 
					if (collidePosition.isValid())
					{
						explosions.add(shield.explode(collidePosition.x, 
								collidePosition.y));
						shotsCannonItr.remove();	
						wasPurged = true;
						cannonCanShoot = true;
						break;
					}
				}
			}
			if (wasPurged) { continue;}

			//cannon shot meet alien shot, remove both.
			Iterator<Shot> shotsAlienItr = shotsAlien.iterator();
			// check if shots collided
			while (shotsAlienItr.hasNext())
			{
				Shot shotAlien = shotsAlienItr.next();
				if (Util.testOverlap(shot, shotAlien)){

					//when shot collide, create an explosion.
					explosions.add(shot.collide());
					shotsCannonItr.remove();
					shotsAlienItr.remove();
					cannonCanShoot = true;
					wasPurged = true;
					break;
				}
			}
			if (wasPurged) continue;
			//check if alien get shot by cannon
			for (Alien alien:aliens) {
				if (Util.testOverlap(alien, shot)
						&& alien.isAlive()) {
					//make explosion
					Explosion alienExplosion = alien.kill();
					explosions.add(alienExplosion);
					//kill this alien
					killAlien(alien);
					score += alien.getKillScore(); 			
					shotsCannonItr.remove(); 		
					cannonCanShoot = true; 			
					wasPurged = true;
					break;
				}
			}

			if (wasPurged) continue;

			//check if ufo get shot by cannon
			if (Util.testOverlap(ufo, shot) && ufo.isAlive()) {
				//kill and explode the UFO.
				Explosion ufoExplosion = ufo.kill();
				explosions.add(ufoExplosion);
				//update score
				ufoKillScore = ufo.getKillScore();
				score += ufoKillScore;
				//remove this shot
				shotsCannonItr.remove();
				cannonCanShoot = true;
				break;
			}
			
			if (wasPurged) continue;
			
			//check if boss get shot by cannon
			if (Util.testOverlap(boss, shot) && boss.isAlive()) {
				if (boss.getShot(shot).isValid()){
					Explosion bossExplosion = shot.collide();
					explosions.add(bossExplosion);
					shotsCannonItr.remove();
					cannonCanShoot = true;				
					if (boss.isNoHP()){
						explosions.add(boss.kill());
					}
					break;
				}
			}
		}
	}

	/**
	 * @param alien
	 */
	private void killAlien(Alien alien) {
		//update number of alien each column and min, max column
		alienColumnCount[alien.getColumn()]--;
		if (alienColumnCount[alien.getColumn()] == 0){

			//check if the left column all die
			if (alien.getColumn() == currentAlienMinColumn){
				while ((alienColumnCount[currentAlienMinColumn] == 0)&&
						currentAlienMinColumn < ALIEN_COLUMN - 1)
					currentAlienMinColumn++;
			}

			//check if the right column all die
			if (alien.getColumn() == currentAlienMaxColumn){
				while ((alienColumnCount[currentAlienMaxColumn] == 0) &&
						currentAlienMaxColumn > 0)
					currentAlienMaxColumn--;
			}
		}	

		currentAlienNumber--;
		if ((currentAlienNumber < World.ALIEN_ACCEL_THRESHOLD) && 
				(currentAlienNumber > 0))
			accelFactor += 10/currentAlienNumber; 
		
	}

	/**
	 * Check alien shot if they collide something.
	 */
	private void checkShotAlien() {
		Iterator<Shot> shotsAlienItr = shotsAlien.iterator();
		while (shotsAlienItr.hasNext())
		{
			Shot shot = shotsAlienItr.next();
			boolean wasPurged = false;
			//shield get shot by aliens
			for (Shield shield : shields) {
				if (Util.testOverlap(shield, shot)){
					Vector collidePosition = shield.getTouch(shot); 
					if (collidePosition.isValid())
					{
						explosions.add(shield.explode(collidePosition.x, 
								collidePosition.y));
						shotsAlienItr.remove();	
						wasPurged = true;
						break;
					}
				}
			}
			if (wasPurged) { continue;}
			//cannon get shot by aliens
			if (Util.testOverlap(shot, cannon)&& cannon.isAlive()) {
				Explosion cannonExplosion = cannon.kill();
				explosions.add(cannonExplosion);
				shotsAlienItr.remove();
			}
		}
	}

	/**
	 * Bring the shields to new.
	 */
	private void resetShields() {
		for (Shield shield:shields) {
			shield.reset();
		}
	}

	/**
	 * Cannon shot when player press.
	 */
	public void fire() {
		if (cannon.isDead() || !cannonCanShoot) return;
		shotsCannon.add(new Shot(cannon.position.x + Cannon.CANNON_WIDTH/2 - 
				Shot.SHOT_CANNON_WIDTH/2,
				cannon.position.y - Shot.SHOT_CANNON_HEIGHT, 
				- Shot.SHOT_CANNON_VELOCITY,
				Shot.SHOT_CANNON_WIDTH, Shot.SHOT_CANNON_HEIGHT));
		cannonCanShoot = false;
	}

	/**
	 * Render everything in the game.
	 * @param g : graphics object.
	 */
	public void render(Graphics g) {
		//render UIs
		Renderer.renderUI(g);
		Renderer.renderScoreAndLevel(g, score, level);
		Renderer.renderLives(g, cannon.getLives());
		//render objects
		Renderer.renderCannon(g, cannon);
		Renderer.renderAlien(g, aliens);
		Renderer.renderUFO(g, ufo, ufoKillScore);
		Renderer.renderShield(g, shields);
		Renderer.renderCannonShot(g, shotsCannon);
		Renderer.renderAlienShot(g, shotsAlien);
		Renderer.renderExplosion(g, explosions);
		Renderer.renderBoss(g, boss, Boss.BOSS_SCORE * level);
	}

	public boolean isGameOver() {
		return ((cannon.getLives() == 0) || alienTouchBottom);
	}

	public int getScore(){
		return score;
	}
}
