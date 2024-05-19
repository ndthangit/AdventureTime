package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.map.GameObjectType;

public class GameObjectComponent implements Component, Pool.Poolable {
    public GameObjectType type;
    public int animationIndex;

    public Vector2 position;

    @Override
    public void reset() {
        type = null;
        animationIndex = -1;
    }
}
