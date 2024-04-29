package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class HideLayerComponent implements Component, Pool.Poolable{
    public Vector2 position;
    public int animationIndex;

    @Override
    public void reset() {

    }
}
