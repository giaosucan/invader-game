package com.prdcv.invader;

import com.prdcv.api.game.ADGame;
import com.prdcv.api.game.Screen;

public class InvaderMain extends ADGame {
    /** Loading and start the game */
    @Override
    public Screen getStartScreen() {
        return new Loading(this); 
    }
}