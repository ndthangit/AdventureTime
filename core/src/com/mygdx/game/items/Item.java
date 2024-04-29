package com.mygdx.game.items;

import com.badlogic.gdx.math.Vector2;

public class Item {
    public Vector2 position;
    public String atlasPath;
    public String key;
    public Item(Vector2 position, String atlasPath, String key) {
        this.position = position;
        this.atlasPath = atlasPath;
        this.key = key;
    }
}
