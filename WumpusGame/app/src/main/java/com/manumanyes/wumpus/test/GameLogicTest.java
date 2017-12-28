package com.manumanyes.wumpus.test;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.manumanyes.wumpus.GameLogic;
import com.manumanyes.wumpus.sprite.Catcher;
import com.manumanyes.wumpus.sprite.Sprite;
import com.manumanyes.wumpus.sprite.Wumpus;

/**
 * Created by mmanes on 27/12/2017.
 */

public class GameLogicTest extends GameLogic {

    public GameLogicTest(int x, int y, int holes, int arrows) {
        super(x, y, holes, arrows);
        makeSpritesVisible = true;
    }

    public Sprite[][] getSpritesArray() {
        return spritesArray;
    }

    public Catcher getCatcher() {
        return catcher;
    }

    public Wumpus getWump() {
        return wump;
    }

    public boolean isContinuePlaying() {
        return continuePlaying;
    }

    public boolean isMakeSpritesVisible() {
        return makeSpritesVisible;
    }


    public void setSpritesArray(Sprite[][] spritesArray) {
        this.spritesArray = spritesArray;
    }

    public void setCatcher(Catcher catcher) {
        this.catcher = catcher;
    }

    public void setWump(Wumpus wump) {
        this.wump = wump;
    }

    public void setContinuePlaying(boolean continuePlaying) {
        this.continuePlaying = continuePlaying;
    }

    public void setMakeSpritesVisible(boolean makeSpritesVisible) {
        this.makeSpritesVisible = makeSpritesVisible;
    }

}
