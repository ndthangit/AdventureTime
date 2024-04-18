package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.items.weapon.WeaponType;

public class WeaponComponent implements Component, Pool.Poolable {
    public WeaponType type;
    public int direction;
    @Override
    public void reset() {
        direction = 0;
    }
}
