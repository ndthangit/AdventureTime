package com.mygdx.game.character.boss;

public enum BossSkillType {
    ;
    private final String atlasPath;
    private final String key;
    private final int width;
    private final int height;
    private final int damage;
    private final int speed;
    BossSkillType(String atlasPath, String key, int width, int height, int damage, int speed) {

        this.atlasPath = atlasPath;
        this.key = key;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.speed = speed;
    }
}
