package com.mygdx.game.entity.component;

import com.mygdx.game.character.boss.Boss;
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.view.DirectionType;

public class BossComponent extends EntityComponent {
    public int attack;
    public BossType type;
    public float reload;
    public float time;
    public float timeAttack;
    public boolean isCharge;
    public boolean isAttack;
    public boolean isSkill1;
    public boolean isSkill2;
    public boolean isHit;
    public float timeRest;
    public DirectionType direction;
    public float duration = 2;
    public boolean invincible;
    public boolean isUsedSkill2 = false;

    public Boss boss;

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
    public boolean isReady(float deltaTime) {
        timeRest += deltaTime;
        if (timeRest > 2f) {
            if (timeRest > 2.5f) {
                timeRest = 0;
            }
            return true;
        }
        else {
            return false;
        }
    }
}
