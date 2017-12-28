package com.manumanyes.wumpus.sprite;

import com.manumanyes.wumpus.Utils;
import com.manumanyes.wumpus.Utils.MoveResult;
import com.manumanyes.wumpus.Utils.UserAction;

import java.util.Locale;

import com.manumanyes.wumpus.R;

public class Catcher extends Sprite {

    private int arrows = 0;
    private boolean goldPickedUp = false;
    private boolean died = false;
    private boolean exited = false;
    private String perception = "";

    public Catcher(int arrows) {
        super();
        this.arrows = arrows;
    }

    @Override
    public void generateRandomPlace(Sprite[][] array) {
        x = array.length - 1;
        y = 0;
    }

    public boolean doShoot(Sprite[][] spritesArray, UserAction shoot, Wumpus wump) {
        boolean wumpusKilled = false;
        perception = "";

        if (wump == null) {
            addPerception(R.string.no_wumpus);
        } else if (arrows <= 0) {
            addPerception(R.string.no_arrows);
        } else {
            arrows--;

            switch (shoot) {
                case SHOOT_UP:
                    if (!isColumnwDown(wump) && isOnSameRow(wump))
                        wumpusKilled = true;
                    break;
                case SHOOT_DOWN:
                    if (isColumnwDown(wump) && isOnSameRow(wump))
                        wumpusKilled = true;
                    break;
                case SHOOT_LEFT:
                    if (isOnSameColumn(wump) && !isRowDown(wump))
                        wumpusKilled = true;
                    break;
                case SHOOT_RIGHT:
                    if (isOnSameColumn(wump) && isRowDown(wump))
                        wumpusKilled = true;
                    break;
                default:
                    break;
            }

            if (wumpusKilled) {
                addPerception(R.string.wumpus_killed);
            } else {
                String arrowsLeft = Utils.getString(R.string.wumpus_shoot);
                arrowsLeft = String.format(Locale.ENGLISH, arrowsLeft, arrows);
                addPerception(arrowsLeft);
            }
        }
        calculatePerceptions(spritesArray);

        return wumpusKilled;
    }

    public MoveResult makeMove(Sprite[][] array, int addX, int addY) {

        int targetX = x + addX;
        int targetY = y + addY;

        boolean moveAllowed = Utils.isMoveAllowed(array, targetX, targetY);
        if (!moveAllowed) {
            return MoveResult.MOVE_NOT_ALLOWED;
        }

        // Delete current location
        array[x][y] = null;

        // Catcher is going on target location
        x = targetX;
        y = targetY;

        perception = "";
        MoveResult result = checkNewLocation(array);
        calculatePerceptions(array);

        return result;
    }

    private MoveResult checkNewLocation(Sprite[][] array) {
        //If the user has exited the table
        if (Utils.isExitLocation(array, x, y)) {
            exited = true;
            return MoveResult.FINISH_PLACE;
        }

        // Check what happens at new location
        Sprite spr = array[x][y];
        if (spr == null) {
            array[x][y] = this;
            return MoveResult.MOVED;

        } else if (spr instanceof Gold) {
            goldPickedUp = true;
            array[x][y] = this;
            addPerception(R.string.gold_brillo);
            return MoveResult.MOVED;

        } else if (spr instanceof Wumpus) {
            died = true;
            addPerception(R.string.wumpus_see);
            return MoveResult.DIED_BY_WUMPUS;

        } else if (spr instanceof Hole) {
            died = true;
            addPerception(R.string.hole_caida);
            return MoveResult.DIED_BY_HOLE;
        }
        return MoveResult.ERROR;
    }

    public boolean success() {
        return exited && goldPickedUp;
    }

    private void addPerception(String msg) {
        perception += msg + "\n";
    }

    private void addPerception(int id) {
        addPerception(Utils.getString(id));
    }

    public String getPerception() {
        return perception;
    }

    public String calculatePerceptions(Sprite[][] array) {
        if (exited || died) {
            if (success()) {
                addPerception(R.string.win);
            } else {
                addPerception(R.string.lose);
            }
        } else {
            calculateWalls(array);
            calculateOtherSprites(array);
        }

        return perception;
    }

    private void calculateWalls(Sprite[][] array) {
        if (x == 0) {
            addPerception(R.string.pared_arriba);
        } else if (x == array.length - 1 && y != 0) {
            addPerception(R.string.pared_abajo);
        }

        if (y == 0) {
            addPerception(R.string.pared_izquierda);
        } else if (y == array[x].length - 1) {
            addPerception(R.string.pared_derecha);
        }
    }

    private void calculateOtherSprites(Sprite[][] array) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                checkPerception(array, x + i, y + j);
            }
        }
    }

    private void checkPerception(Sprite[][] array, int x, int y) {
        if (Utils.isOnArrayBounds(array, x, y)) {
            Sprite spr = array[x][y];
            if (spr == null) {
                return;
            } else if (spr instanceof Wumpus) {
                addPerception(R.string.wumpus_hedor);
            } else if (spr instanceof Hole) {
                addPerception(R.string.hole_brisa);
            }
        }
    }

    public int getArrows() {
        return arrows;
    }

    public boolean isGoldPickedUp() {
        return goldPickedUp;
    }

    public boolean isDied() {
        return died;
    }

    public boolean isExited() {
        return exited;
    }
}
