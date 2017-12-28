package com.manumanyes.wumpus;

import com.manumanyes.wumpus.sprite.Sprite;

public class Utils {

    public enum MoveResult {
        MOVED, MOVE_NOT_ALLOWED,
        DIED_BY_WUMPUS, DIED_BY_HOLE, FINISH_PLACE, ERROR
    };

    public enum UserAction {
        UP, DOWN, LEFT, RIGHT, SHOOT_UP, SHOOT_DOWN, SHOOT_LEFT, SHOOT_RIGHT, ERROR
    };

    public static int random(int max) {
        return (int) (Math.random() * max);
    }

    public static boolean isOnArrayBounds(Sprite[][] array, int x, int y) {
        return x < array.length && x >= 0 && y >= 0 && y < array[x].length;
    }

    public static boolean isMoveAllowed(Sprite[][] array, int x, int y) {
        if (isOnArrayBounds(array, x, y) || isExitLocation(array, x, y)) {
            return true;
        }
        return false;
    }

    public static boolean isExitLocation(Sprite[][] array, int x, int y) {
        if (x == array.length && y == 0) {
            return true;
        }
        return false;
    }

    public static String getString(int id) {
        return GameController.getInstance().getContext().getResources().getString(id);
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) { }
    }
}

