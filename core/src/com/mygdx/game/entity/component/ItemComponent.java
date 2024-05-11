package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.items.Item;

public class ItemComponent implements Component, Pool.Poolable {
    public Item item;
    public float width;
    public float height;


    @Override
    public void reset() {
        width = 0;
        height = 0;
        item = null;
    }
}
