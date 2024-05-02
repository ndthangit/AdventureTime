package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DoorLayerComponent implements Component, Pool.Poolable {
    public int animationIndex;
    public String name;
    @Override
    public void reset() {
        animationIndex = -1;
        name = null;
    }
}
