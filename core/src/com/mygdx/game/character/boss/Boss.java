package com.mygdx.game.character.boss;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.Charater;

public class Boss extends Charater {
    private final BossType bossType;
    private boolean isDead ;
    public Boss(BossType bossType,Vector2 position) {
        super(position, bossType.getWidth(), bossType.getHeight());
        this.bossType = bossType;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public BossType getBossType() {
        return bossType;
    }
}
