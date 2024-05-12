package com.mygdx.game.entity.component;

import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.character.enemy.EnemyType;
import com.mygdx.game.view.DirectionType;

public class BossComponent extends EntityComponent {
    public int attack;
    public BossType type;
    public float reload;
    public float time;
    public float timeCharge;
    public float timeAttack;
    public boolean isCharge;
    public boolean isAttack;
    public boolean isSkill1;
    public boolean isSkill2;
    public DirectionType direction;
    @Override
    public void reset() {
        super.reset();
        attack = 0;
        type = null;
    }
    public void resetState() {
        isCharge = false;
        isAttack = false;
        isSkill1 = false;
        isSkill2 = false;
    }
}
