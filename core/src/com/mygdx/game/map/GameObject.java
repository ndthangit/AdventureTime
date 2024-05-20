package com.mygdx.game.map;

import com.badlogic.gdx.math.Vector2;

public class GameObject {
    private final GameObjectType type;
    private final Vector2 position;
    private String name; // Using for door layer
    private final float width;
    private final float height;
    private final float rotDegree;
    private final int animationIndex;
    private boolean isUsed;

    public GameObject(GameObjectType type, Vector2 position, float width, float height, float rotDegree, int animationIndex) {
        this.type = type;
        this.name = null;
        this.position = position;
        this.width = width;
        this.height = height;
        this.rotDegree = rotDegree;
        this.animationIndex = animationIndex;
        isUsed = false;
    }

    public GameObject(GameObjectType type, Vector2 position, String name, float width, float height, float rotDegree, int animationIndex) {
        this.type = type;
        this.name = name;
        this.position = position;
        this.width = width;
        this.height = height;
        this.rotDegree = rotDegree;
        this.animationIndex = animationIndex;
        isUsed = false;
    }

    public GameObjectType getType() {
        return type;
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
    public float getRotDegree() {
        return rotDegree;
    }
    public int getAnimationIndex() {
        return animationIndex;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
    public boolean getIsUsed() {
        return isUsed;
    }
}
