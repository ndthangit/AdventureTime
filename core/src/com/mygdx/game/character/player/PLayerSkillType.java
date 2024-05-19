package com.mygdx.game.character.player;

import com.mygdx.game.effect.EffectType;

public enum PLayerSkillType {
    BIG_KUNAI(EffectType.BIG_KUNAI, 18, 5, 4, true, 5, 2),
    SMOKE(EffectType.SMOKE, 32, 32, 1, false, 0, 10),;
    private final EffectType effectType;
    private final int width;
    private final int height;
    private final int damage;
    private final boolean isBullet;
    private final float speed;
    private final float timeReload;
    PLayerSkillType(EffectType type, int width, int height, int damage, boolean isBullet, float speed, float timeReload) {
        effectType = type;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.isBullet = isBullet;
        this.speed = speed;
        this.timeReload = timeReload;
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

    public boolean isBullet() {
        return isBullet;
    }

    public float getSpeed() {
        return speed;
    }

    public float getTimeReload() {
        return timeReload;
    }
}
