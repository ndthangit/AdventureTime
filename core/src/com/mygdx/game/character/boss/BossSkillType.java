package com.mygdx.game.character.boss;

public enum BossSkillType {
    CHARGE(null, null, 0,0,0, 0.5f),
    LEFT_SLASH("", "", 0,0,0,0),
    RIGHT_SLASH("", "", 0,0,0,0);
    private final String atlasPath;
    private final String key;
    private final int width;
    private final int height;
    private final int damage;
    private final float speed;
    BossSkillType(String atlasPath, String key, int width, int height, int damage, float speed) {

        this.atlasPath = atlasPath;
        this.key = key;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.speed = speed;
    }
}
