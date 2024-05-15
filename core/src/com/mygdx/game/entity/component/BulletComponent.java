package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class BulletComponent implements Component, Pool.Poolable {
    public Vector2 start;
    public Vector2 dir;
    @Override
    public void reset() {
        start = null;
        dir=new Vector2();
    }
}