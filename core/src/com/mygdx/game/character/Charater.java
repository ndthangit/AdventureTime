package com.mygdx.game.character;

import com.badlogic.gdx.math.Vector2;

public class Charater {
    private final Vector2 position;
    private final float width;
    private final float height;

    public Charater(Vector2 position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }
    public Vector2 getPosition() {
        return position;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
}
