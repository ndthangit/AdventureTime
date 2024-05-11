package com.mygdx.game.items;

import com.badlogic.gdx.math.Vector2;

public class Item {
    public Vector2 position;
    public String atlasPath;
    public String key;
    public float width;
    public float height;
    public int quatity;
    public Item(Vector2 position, String atlasPath, String key, float width, float height) {
        this.position = position;
        this.atlasPath = atlasPath;
        this.key = key;
        this.width = width;
        this.height = height;
    }
}
