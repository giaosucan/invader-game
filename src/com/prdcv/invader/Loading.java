package com.prdcv.invader;

import android.graphics.Bitmap.Config;

import com.prdcv.api.game.Game;
import com.prdcv.api.game.Screen;
import com.prdcv.api.graphics.Animation;
import com.prdcv.api.graphics.Graphics;
import com.prdcv.api.graphics.ImageHandler;
import com.prdcv.invader.world.Alien;
import com.prdcv.invader.world.Boss;
import com.prdcv.invader.world.Cannon;
import com.prdcv.invader.world.Explosion;

/**
 * This class load all the assets from files and store in the Assets class.
 * @author nccuong
 */
public class Loading extends Screen {
    public Loading(Game game) {
        super(game);
    }

    @Override
    public void update(float cycleTime) {
        Graphics g = game.getGraphics();
        
        Assets.getInstance();
		//load images
        Assets.setBackground(g.newImage("bg.jpg", Config.RGB_565));
        Assets.setCannon(g.newImage("cannon.png", Config.ARGB_8888));
        Assets.setLaser(g.newImage("laser.png", Config.ARGB_8888));
        Assets.setAlienfire(g.newImage("alienfire.png", Config.ARGB_8888));
        
        Assets.setUfo(g.newImage("ufo.png", Config.ARGB_8888));
        Assets.setBoss(g.newImage("boss.png", Config.ARGB_8888));
        Assets.setShield(g.newImage("shield.png", Config.ARGB_8888));
        for (int i=0; i < 10; i++){        	
        	Assets.setAlien(g.newImage("alien"+ Integer.toString(i)+".png", Config.ARGB_8888), i);
        }
        
        ImageHandler shotBreakImage = g.newImage("shot_break.png", Config.ARGB_8888);
        Assets.setShotBreak(shotBreakImage.createScaledImage(0.4f));
        
        //load exploding texture
        
        ImageHandler explosionTexture = g.newImage("explosion.png", 
        		Config.ARGB_8888);
		ImageHandler[] explosionFrames = new ImageHandler[16];
		int frame = 0;
		for (int y = 0; y < 256; y += 64) {
			for (int x = 0; x < 256; x += 64) {
				explosionFrames[frame++] = explosionTexture.createImage(x, y, 64, 64);
			}
		}
		Animation cannonExpAnim = new Animation(Cannon.CANNON_DEAD_TIME / Explosion.EXPLOSION_FRAME,
				Animation.NONLOOPING, 144/256f,
				explosionFrames);
		
		Animation alienExpAnim = new Animation(Alien.ALIEN_DEAD_TIME / Explosion.EXPLOSION_FRAME, 
				Animation.NONLOOPING, 0.8f*144/256,
				explosionFrames);
		
		Animation shotExpAnim = new Animation(0.07f, Animation.NONLOOPING, 0.5f*144/256,
				explosionFrames);
		
		Animation shieldExpAnim = new Animation(0.07f, Animation.NONLOOPING, 0.7f*144/256,
				explosionFrames);
		
		Animation shieldAllExpAnim = new Animation(0.2f, Animation.NONLOOPING, 1.5f*144/256,
				explosionFrames);
		
		Animation ufoExpAnim = new Animation(0.2f, Animation.NONLOOPING, 1f*144/256,
				explosionFrames);
		
		Animation bossExpAnim = new Animation(Boss.BOSS_DEAD_TIME/Explosion.EXPLOSION_FRAME,
				Animation.NONLOOPING, 1.3f,
				explosionFrames);
		
		Assets.setCannonExpAnim(cannonExpAnim);
		Assets.setAlienExpAnim(alienExpAnim);
		Assets.setShotExpAnim(shotExpAnim);
		Assets.setUfoExpAnim(ufoExpAnim);
		Assets.setShieldExpAnim(shieldExpAnim);
		Assets.setShieldAllExpAnim(shieldAllExpAnim);
		Assets.setBossExpAnim(bossExpAnim);
       
        //load config file
        Settings.load(game.getFileIO());
		
        game.setScreen(new PlayScreen(game));
    }

	@Override
    public void present(float cycleTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}