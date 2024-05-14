package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.view.DirectionType;

public class EffectComponent implements Component, Pool.Poolable {
    public EffectType type;
    public DirectionType direction;
    public short owner;
    public String path;
    public float width;
    public float height;
    public float aniTime;
    public Vector2 position;

    @Override
    public void reset() {
        direction = DirectionType.DOWN;
        aniTime = 0;
        type = null;
        path = null;
        width = 0;
        height = 0;
        position = null;
    }
}
