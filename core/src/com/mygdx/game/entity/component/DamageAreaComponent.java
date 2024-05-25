package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.view.DirectionType;

public class DamageAreaComponent implements Component, Pool.Poolable {

    public int damage;
    public boolean isbullet;
    public short owner;
    public boolean upgrade;
    public EffectType type;
    public float bulletSpeed;
    public DirectionType direction;
    @Override
    public void reset() {
        damage = 0;
        isbullet = false;
        bulletSpeed = 0;
        direction = null;
    }
}
