package com.mygdx.game.items.key;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.items.Item;

public class Key extends Item {
    KeyType type;
    public Key(KeyType key,Vector2 position) {
        super(position, key.getAtlasPath(), key.getKey(), key.getWidth(), key.getHeight());
        this.type = key;
    }
}
