package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.character.enemy.EnemyType;

public class EnemyComponent extends EntityComponent {
    public int attack;
    public EnemyType type;

    @Override
    public void reset() {
        super.reset();
        attack = 0;
        type = null;
    }
}
