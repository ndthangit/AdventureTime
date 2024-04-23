package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.items.weapon.WeaponType;
import com.mygdx.game.view.DirectionType;

import static com.mygdx.game.view.DirectionType.DOWN;

public class WeaponComponent implements Component, Pool.Poolable {
    public WeaponType type;

    public DirectionType direction;
    public int attack;
    @Override
    public void reset() {
        attack = 0;
        direction = DOWN;
    }
}
