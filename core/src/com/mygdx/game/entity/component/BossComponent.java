package com.mygdx.game.entity.component;

import com.mygdx.game.character.boss.BossType;
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
    public boolean isHit;
    public float timeRest;
    public DirectionType direction;
    public boolean isRest;

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
