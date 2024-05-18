package com.mygdx.game.character.boss;

import com.mygdx.game.effect.Effect;
import com.mygdx.game.effect.EffectType;

public enum BossSkillType {
    CHARGE(null, 0,0,0, 0.5f),
    SLASH(EffectType.BIG_ENERGY_BALL, 48,48,6,2),
    BLAST(EffectType.BLAST, 48,48,6,2),;


    private EffectType effectType;
    private final int width;
    private final int height;
    private final int damage;
    private final float speed;
    BossSkillType(EffectType effectType, int width, int height, int damage, float speed) {
        this.effectType = effectType;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.speed = speed;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }
}
