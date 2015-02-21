package com.prdcv.api.game;

import com.prdcv.api.io.Input;
import com.prdcv.api.io.FileIO;
import com.prdcv.api.graphics.Graphics;;

public interface Game {
    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();

}