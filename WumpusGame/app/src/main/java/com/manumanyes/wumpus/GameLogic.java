package com.manumanyes.wumpus;

import android.graphics.Color;

import com.manumanyes.wumpus.sprite.Catcher;
import com.manumanyes.wumpus.sprite.Gold;
import com.manumanyes.wumpus.sprite.Hole;
import com.manumanyes.wumpus.sprite.Sprite;
import com.manumanyes.wumpus.sprite.Wumpus;
import com.manumanyes.wumpus.Utils.MoveResult;
import com.manumanyes.wumpus.Utils.UserAction;

public class GameLogic {

    protected Sprite[][] spritesArray;
    protected Catcher catcher;
    protected Wumpus wump;
    protected boolean continuePlaying = true;
    protected boolean makeSpritesVisible = false;

    public GameLogic(int x, int y, int holes, int arrows) {
        spritesArray = new Sprite[x][y];

        generateSprites(holes, arrows, x);
        continuePlaying = true;
        makeSpritesVisible = false;
        String perception = catcher.calculatePerceptions(spritesArray);
        GameController.getInstance().onPerceptionChanged(perception);
    }

    private void generateSprites(int holes, int arrows, int x) {
        catcher = new Catcher(arrows);
        addSprite(catcher);

        wump = new Wumpus();
        addSprite(wump);

        Gold gold = new Gold();
        addSprite(gold);

        Hole hole;
        for (int i = 0; i < holes; i++) {
            hole = new Hole();
            addSprite(hole);
        }
    }

    public void changeSpritesVisible() {
        makeSpritesVisible = !makeSpritesVisible;
        GameController.getInstance().repaint();
    }

    public int getColor(int x, int y) {
        if (Utils.isOnArrayBounds(spritesArray, x, y)) {
            Sprite aux = spritesArray[x][y];
            if (aux == null) {
                return Color.GRAY;
            } else if (aux instanceof Catcher) {
                return Color.GREEN;
            } else if (!makeSpritesVisible) {
                return Color.GRAY;
            } else if (aux instanceof Gold) {
                return Color.YELLOW;
            } else if (aux instanceof Wumpus) {
                return Color.RED;
            } else if (aux instanceof Hole) {
                return Color.BLUE;
            }
        }
        return Color.GRAY;
    }

    private boolean addSprite(Sprite sprite) {
        // TODO: comprobar que tengo espacio en spritesArray
        Sprite aux;
        do {
            sprite.generateRandomPlace(spritesArray);
            aux = spritesArray[sprite.getX()][sprite.getY()];
            if (aux == null) {
                spritesArray[sprite.getX()][sprite.getY()] = sprite;
                return true;
            }
        } while (aux != null);

        return false;
    }

    public void doUserAction(UserAction action, boolean isShoot) {

        if (!continuePlaying) {
            return;
        }

        if (isShoot) {
            switch (action) {
                case UP:
                    action = UserAction.SHOOT_UP;
                    break;
                case DOWN:
                    action = UserAction.SHOOT_DOWN;
                    break;
                case LEFT:
                    action = UserAction.SHOOT_LEFT;
                    break;
                case RIGHT:
                    action = UserAction.SHOOT_RIGHT;
                    break;
                default:
                    action = UserAction.ERROR;
                    break;
            }
        }

        switch (action) {
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
                doUserMove(action);
                break;
            case SHOOT_UP:
            case SHOOT_DOWN:
            case SHOOT_LEFT:
            case SHOOT_RIGHT:
                doUserShoot(action);
                break;
            default:
                continuePlaying = false;
                break;
        }

        String perception = catcher.getPerception();
        GameController.getInstance().onPerceptionChanged(perception);
        GameController.getInstance().repaint();

    }

    private void doUserMove(UserAction action) {
        MoveResult result;

        switch (action) {
            case UP:
                result = catcher.makeMove(spritesArray, -1, 0);
                break;
            case DOWN:
                result = catcher.makeMove(spritesArray, 1, 0);
                break;
            case LEFT:
                result = catcher.makeMove(spritesArray, 0, -1);
                break;
            case RIGHT:
                result = catcher.makeMove(spritesArray, 0, 1);
                break;
            default:
                result = MoveResult.ERROR;
                break;
        }

        switch (result) {
            case MOVED:
            case MOVE_NOT_ALLOWED:
                continuePlaying = true;
                break;
            default:
                continuePlaying = false;
                break;
        }
    }

    private void doUserShoot(UserAction shoot) {
        boolean wumpusKilled = catcher.doShoot(spritesArray, shoot, wump);
        if (wumpusKilled) {
            spritesArray[wump.getX()][wump.getY()] = null;
            wump = null;
        }

    }

}
