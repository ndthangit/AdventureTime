package com.mygdx.game.entity.component;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.character.enemy.EnemyType;

public class BossComponent extends EntityComponent {
    public int attack;
    public BossType type;
    //get startPosition
    public Vector2 startPosition = new Vector2();

    @Override
    public void reset() {
        super.reset();
        attack = 0;
        type = null;
    }
}
