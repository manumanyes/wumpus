package com.manumanyes.wumpus.sprite;

import com.manumanyes.wumpus.Utils;

public class Sprite {

    protected int x = 0;
    protected int y = 0;

    public String toString() {
        return "Sprite [x=" + x + ", y=" + y + "]";
    }

    public void generateRandomPlace(Sprite[][] array) {
        x = Utils.random(array.length);
        y = Utils.random(array[x].length);
    }

    protected boolean isOnSameColumn(Object obj) {
        Sprite other = (Sprite) obj;
        return x==other.x;
    }

    protected boolean isOnSameRow(Object obj) {
        Sprite other = (Sprite) obj;
        return y==other.y;
    }

    protected boolean isColumnwDown(Object obj) {
        Sprite other = (Sprite) obj;
        return x<other.x;
    }

    protected boolean isRowDown(Object obj) {
        Sprite other = (Sprite) obj;
        return y<other.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
