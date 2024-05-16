package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.view.DirectionType;

import static com.mygdx.game.view.DirectionType.DOWN;

public class EntityComponent implements Component, Pool.Poolable {
    public int life;
    public int maxLife;
    public float speed;
    public DirectionType direction;

    @Override
    public void reset() {
        direction = DOWN;
        life = 0;
        maxLife = 0;
        speed = 0;
    }
}
