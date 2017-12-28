package com.manumanyes.wumpus;

import android.content.Context;
import android.widget.Toast;

import com.manumanyes.wumpus.test.GameLogicTest;

/**
 * Created by mmanes on 28/12/2017.
 */

public class GameController {

    private static GameController sInstance;

    private static WumpusActivity view;

    private static Context mContext;

    private static GameLogic game;

    private GameController() { }

    public static GameController getInstance() {
        if(sInstance == null) {
            sInstance = new GameController();
        }
        return sInstance;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void setView(WumpusActivity view) {
        this.view = view;
    }

    public void startNewGame(int rows, int columns, int holes, int arrows) {
        game = new GameLogic(rows, columns, holes, arrows);
        view.repaint();
    }

    public void onPerceptionChanged(String perception) {
        view.onPerceptionChanged(perception);
    }

    public void repaint() {
        view.repaint();
    }

    public void doUserAction(Utils.UserAction action, boolean isShoot) {
        game.doUserAction(action, isShoot);
    }

    public void setVisibleSprites() {
        game.changeSpritesVisible();
    }

    public int getColor(int x, int y) {
        return game.getColor(x,y);
    }

    public void log(String msg) {
        Toast t = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        t.show();
    }

    public void replaceGameLogicOnTesting(GameLogicTest game) {
        this.game = game;
        view.repaint();
    }
}
