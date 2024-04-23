package com.mygdx.game.view;

public enum DirectionType {
    DOWN(0),
    UP(2),
    LEFT(1),
    RIGHT(3);
    private int code;
    private DirectionType(int code) {
        this.code = code;
    }
    public int getCode() {return code;}
}
