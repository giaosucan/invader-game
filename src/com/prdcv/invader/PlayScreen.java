package com.prdcv.invader;

import java.util.List;

import android.graphics.Color;

import com.prdcv.api.game.Game;
import com.prdcv.api.game.Screen;
import com.prdcv.api.graphics.Graphics;
import com.prdcv.api.io.Input;
import com.prdcv.api.io.Input.KeyEvent;
import com.prdcv.api.io.Input.TouchEvent;
import com.prdcv.invader.world.Cannon;
import com.prdcv.invader.world.World;

public class PlayScreen extends Screen{
	enum STATE {
		Running,
		GameOver
	}

	private static final int KEY_LEFT = 21;
	private static final int KEY_RIGHT = 22;
	private static final int KEY_SHOT = 23;
	
	STATE state = STATE.Running;
	World world;
	int oldX = 0;

	public PlayScreen(Game game) {
		super(game);
		world = new World();
	}

	@Override
	public void update(float cycleTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		switch (state) {
		case Running:
			updateRunning(cycleTime, touchEvents, keyEvents);
			break;
		case GameOver:
			updateGameOver(touchEvents);
			break;
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x >= 70 && event.x <= 210 &&
                   event.y >= 250 && event.y <= 370) {
                    game.setScreen(new PlayScreen(game));
                    return;
                }
            }
        }
	}
		
	private void updateRunning(float cycleTime, List<TouchEvent> listTouchEvent,
			List<KeyEvent> keyEvents) {
		
		//NguyenDT modified here
//		if (game.getInput().isKeyPressed(KEY_SHOT)){
//			world.fire();
//		}
		if (game.getInput().isTouchDown(Input.TouchEvent.TOUCH_DOWN)) {
			world.fire();
		}
		
		world.move(cycleTime, calculateSpeed(listTouchEvent));
		if (world.isGameOver()){
			Settings.addScore(world.getScore());
	        Settings.save(game.getFileIO());
			state = STATE.GameOver; 
		}
	}

	private float calculateSpeed(List<TouchEvent> listTouchEvent) {
		float velocity = 0;
		int newX;
		for(TouchEvent event: listTouchEvent) {
			if(event.type == TouchEvent.TOUCH_DOWN) 
				oldX = event.x;
			if(event.type == TouchEvent.TOUCH_DRAGGED) {
				newX = event.x;
				velocity = Cannon.CANNON_VELOCITY * (newX - oldX); 
				oldX = newX;
			    break;
			}
		}
		
		//handle left right key event
		if (game.getInput().isKeyPressed(KEY_LEFT)) 
		{
			velocity = -Cannon.CANNON_VELOCITY * 20;
		}
		
		if (game.getInput().isKeyPressed(KEY_RIGHT))
		{
			velocity = Cannon.CANNON_VELOCITY * 20;
		}
		return velocity;
	}

	@Override
	public void present(float cycleTime) {
		Graphics g = game.getGraphics();
		//render the world
		world.render(g);

		if(state == STATE.GameOver)
			drawGameOver();
	        
	}

	private void drawGameOver() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.getBackground(), 0, 0);
		g.drawText("Game Over", 100, 230, 30, Color.WHITE);
		g.drawText("Score "+ Integer.toString(world.getScore()), 
				140, 255, 14, Color.WHITE);
		g.drawText("Shoot again", 120, 280, 20, Color.WHITE);
		
		g.drawText("Highscore", 120, 340, 16, Color.WHITE);
		 for (int i = 0; i < 5; i++) {
	            g.drawText(i+1+". "+ 
	            		Settings.highscores[i], 120, 360 + i*20, 12, Color.WHITE);
	        }
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
