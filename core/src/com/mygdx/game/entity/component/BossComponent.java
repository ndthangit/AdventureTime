package com.mygdx.game.entity.component;

import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.character.enemy.EnemyType;

public class BossComponent extends EntityComponent {
    public int attack;
    public BossType type;

    @Override
    public void reset() {
        super.reset();
        attack = 0;
        type = null;
    }
}
