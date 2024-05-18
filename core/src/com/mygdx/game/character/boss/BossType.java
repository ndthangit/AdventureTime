package com.mygdx.game.character.boss;

public enum BossType {
    GIANTBLUESAMURAI("Actor/Boss/GiantBlueSamurai/giantbluesamurai.atlas", 48, 48, 60, 5, 4, 4, BossSkillType.SLASH, BossSkillType.CHARGE),
    GIANTSPIRIT("Actor/Boss/GiantSpirit/giantspirit.atlas", 48, 48, 60, 5, 4, 8, BossSkillType.SLASH, BossSkillType.CHARGE),;
    private final String atlasPath;
    private final int width;
    private final int height;
    private final int health;
    private final int damage;
    private final float speed;
    private final float reload;
    private final BossSkillType skill1;
    private final BossSkillType skill2;

    BossType(String atlasPath, int width, int height, int health, int damage, float speed,float reload, BossSkillType skill1, BossSkillType skill2) {
        this.atlasPath = atlasPath;
        this.width = width;
        this.height = height;
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.reload = reload;
        this.skill1 = skill1;
        this.skill2 = skill2;
    }

    public int getWidth() {
        return width;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public int getHeight() {
        return height;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }

    public BossSkillType getSkill1() {
        return skill1;
    }

    public BossSkillType getSkill2() {
        return skill2;
    }

    public float getReload() {
        return reload;
    }
}
