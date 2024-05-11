package com.mygdx.game.character.boss;

public enum BossType {
    ;
    private final String atlasPath;
    private final String key;
    private final int width;
    private final int height;
    private final int health;
    private final int damage;
    private final float speed;
    private final BossSkillType skill1;
    private final BossSkillType skill2;

    BossType(String atlasPath, String key, int width, int height, int health, int damage, float speed, BossSkillType skill1, BossSkillType skill2) {
        this.atlasPath = atlasPath;
        this.key = key;
        this.width = width;
        this.height = height;
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.skill1 = skill1;
        this.skill2 = skill2;
    }
}
