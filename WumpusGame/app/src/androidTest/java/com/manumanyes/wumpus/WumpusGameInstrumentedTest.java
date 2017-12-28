package com.manumanyes.wumpus;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.manumanyes.wumpus.sprite.Catcher;
import com.manumanyes.wumpus.sprite.Gold;
import com.manumanyes.wumpus.sprite.Hole;
import com.manumanyes.wumpus.sprite.Sprite;
import com.manumanyes.wumpus.sprite.Wumpus;
import com.manumanyes.wumpus.test.GameLogicTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WumpusGameInstrumentedTest {

    @Rule
    public ActivityTestRule<WumpusActivity> mActivityRule = new ActivityTestRule<>(WumpusActivity.class, true, false);

    private WumpusActivity act;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();

        Intent intent = new Intent(context, WumpusActivity.class);
        mActivityRule.launchActivity(intent);

        act = mActivityRule.getActivity();

        GameController.getInstance().setView(act);
        GameController.getInstance().setContext(context);
    }

    @After
    public void after() {

    }

    @Test
    @UiThreadTest
    public void initialization_isCorrect() throws Exception {
        int rows = 4;
        int columns = 3;
        int holes = 2;
        int arrows = 5;

        GameLogicTest game = new GameLogicTest(rows, columns, holes, arrows);
        act.createViews(rows, columns);
        GameController.getInstance().replaceGameLogicOnTesting(game);

        Sprite[][] sprites = game.getSpritesArray();

        assertTrue(sprites.length == rows);
        assertTrue(sprites[sprites.length-1].length == columns);

        int holeCounter = 0;
        int catcherCounter = 0;
        int goldCounter = 0;
        int wumpusCounter = 0;

        for(int i = 0; i < sprites.length; i++) {
            for(int j = 0; j < sprites[i].length; j++) {
                Sprite aux = sprites[i][j];
                if(aux instanceof Hole) {
                    holeCounter++;
                } else if (aux instanceof Gold) {
                    goldCounter++;
                } else if (aux instanceof Wumpus) {
                    wumpusCounter++;
                } else if (aux instanceof Catcher) {
                    catcherCounter++;
                }
            }
        }

        assertTrue(holeCounter == holes);
        assertTrue(goldCounter == 1);
        assertTrue(catcherCounter == 1);
        assertTrue(wumpusCounter == 1);

        Catcher cat = game.getCatcher();
        assertTrue(cat.getArrows() == arrows);
    }

    @Test
    @UiThreadTest
    public void continuePlaying_isCorrect() throws Exception {
        int rows = 4;
        int columns = 3;
        int holes = 2;
        int arrows = 1;
        GameLogicTest game = new GameLogicTest(rows, columns, holes, arrows);
        act.createViews(rows, columns);
        GameController.getInstance().replaceGameLogicOnTesting(game);

        Catcher catcher = game.getCatcher();
        int x = catcher.getX();
        int y = catcher.getY();

        Sprite[][] sprites = game.getSpritesArray();
        Sprite targetPosition = sprites[x][y+1];

        boolean expectedResult = false;
        if(targetPosition == null || targetPosition instanceof Gold) {
            expectedResult = true;
        }

        GameController.getInstance().doUserAction(Utils.UserAction.RIGHT, false);

        assertTrue(expectedResult == game.isContinuePlaying());
    }

    @Test
    @UiThreadTest
    public void finishFailed_isCorrect() throws Exception {
        int rows = 4;
        int columns = 3;
        int holes = 2;
        int arrows = 1;
        GameLogicTest game = new GameLogicTest(rows, columns, holes, arrows);
        act.createViews(rows, columns);
        GameController.getInstance().replaceGameLogicOnTesting(game);

        Catcher catcher = game.getCatcher();

        assertFalse(catcher.isExited() || catcher.isDied() || catcher.isGoldPickedUp());

        GameController.getInstance().doUserAction(Utils.UserAction.DOWN, false);
        catcher = game.getCatcher();

        assertFalse(game.isContinuePlaying());
        assertTrue(catcher.isExited() && !(catcher.isDied() || catcher.isGoldPickedUp()));

    }

    @Test
    @UiThreadTest
    public void moving_isCorrect() throws Exception {
        int rows = 4;
        int columns = 3;
        int holes = 0;
        int arrows = 1;
        GameLogicTest game = new GameLogicTest(rows, columns, holes, arrows);
        act.createViews(rows, columns);
        GameController.getInstance().replaceGameLogicOnTesting(game);

        Catcher catcher = game.getCatcher();
        int x = catcher.getX();
        int y = catcher.getY();

        Sprite[][] sprites = game.getSpritesArray();
        for(int i = 0; i < sprites.length; i++) {
            for(int j = 0; j < sprites[i].length; j++) {
                sprites[i][j] = null;
            }
        }
        sprites[x][y] = catcher;
        game.setCatcher(catcher);

        //Move to the right
        y = y + 1;
        GameController.getInstance().doUserAction(Utils.UserAction.RIGHT, false);
        assertTrue(game.isContinuePlaying());
        checkCatcher(game, x, y);

        //Move to up
        x = x - 1;
        GameController.getInstance().doUserAction(Utils.UserAction.UP, false);
        assertTrue(game.isContinuePlaying());
        checkCatcher(game, x, y);

        //Move to the left
        y = y - 1;
        GameController.getInstance().doUserAction(Utils.UserAction.LEFT, false);
        assertTrue(game.isContinuePlaying());
        checkCatcher(game, x, y);

        //Move to down
        x = x + 1;
        GameController.getInstance().doUserAction(Utils.UserAction.DOWN, false);
        assertTrue(game.isContinuePlaying());
        checkCatcher(game, x, y);

    }

    private void checkCatcher(GameLogicTest game, int x, int y) {
        Sprite[][] sprites = game.getSpritesArray();
        Sprite targetSprite = sprites[x][y];

        assertTrue(targetSprite instanceof Catcher);
        assertTrue(targetSprite.getX() == x && targetSprite.getY() == y);

        targetSprite = game.getCatcher();
        assertTrue(targetSprite instanceof Catcher);
        assertTrue(targetSprite.getX() == x && targetSprite.getY() == y);

    }
}
