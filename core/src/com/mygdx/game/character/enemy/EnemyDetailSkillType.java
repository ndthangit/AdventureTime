package com.mygdx.game.character.enemy;

import com.mygdx.game.effect.EffectType;

public enum EnemyDetailSkillType {
    SWAP_ROCK(EffectType.ROCK, 16,16,3,0),
    ENERGY_BALL(EffectType.ENERGY_BALL, 8,8,2,1),
    ATTACK_SLASH(EffectType.SLASHCURVED, 16, 16, 2, 0),
    SHURIKEN(EffectType.SHURIKEN, 12, 12, 2, 4),
    FIRE_BALL(EffectType.FIREBALL, 8, 8, 2, 1);
    private final EffectType effectType;
    private final int width;
    private final int height;
    private final int damage;
    private final float speed;
    EnemyDetailSkillType(EffectType effectType, int width, int height, int damage, float speed) {

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
