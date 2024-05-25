package com.mygdx.game.character.enemy;

public enum EnemyType {
    SLIME2("Actor/Monsters/Slime2/slime2.atlas","SLIME2",EnemySkillType.NULL,null, 10, 2, 0.5f, 3),
    SLIME4("Actor/Monsters/Slime4/slime4.atlas","SLIME4",EnemySkillType.NULL,null,10, 1, 0.5f, 3),
    REPTILE2("Actor/Monsters/Reptile2/reptile2.atlas","REPTILE2",EnemySkillType.NULL,null, 20, 3, 1.5f, 5),
    SKULLBLUE("Actor/Monsters/SkullBlue/skullblue.atlas", "SKULLBLUE",EnemySkillType.PROJECTILE, EnemyDetailSkillType.ENERGY_BALL, 20, 2, 1, 6),
    LARVA("Actor/Monsters/Larva/larva.atlas", "LARVA",EnemySkillType.NULL,null, 20, 2 , 1, 3),
    REDFIGHTER("Actor/Characters/RedFighter/redfighter.atlas", "REDFIGHTER", EnemySkillType.ATTACK,EnemyDetailSkillType.ATTACK_SLASH, 20, 4, 2, 3),
    REDNINJA("Actor/Characters/RedNinja/redninja.atlas", "REDNINJA", EnemySkillType.PROJECTILE,EnemyDetailSkillType.SHURIKEN, 15, 2, 2, 3),
    REDNINJA2("Actor/Characters/RedNinja2/redninja2.atlas", "REDNINJA2", EnemySkillType.PROJECTILE, EnemyDetailSkillType.SHURIKEN, 15, 2, 2, 6),
    REDSAMURAI("Actor/Characters/RedSamurai/redsamurai.atlas", "REDSAMURAI", EnemySkillType.ATTACK, EnemyDetailSkillType.ATTACK_SLASH, 20, 2, 1.5f, 3),
    SAMURAI("Actor/Characters/Samurai/samurai.atlas", "SAMURAI",EnemySkillType.ATTACK, EnemyDetailSkillType.ATTACK_SLASH,30, 5, 1f, 6),
    ORANGESORCERER("Actor/Characters/OrangeSorcerer/orangeSorcerer.atlas", "ORANGESORCERER", EnemySkillType.PROTECT, EnemyDetailSkillType.SWAP_ROCK, 20, 4, 1, 6),
    SKELETON("Actor/Characters/Skeleton/skeleton.atlas", "SKELETON", EnemySkillType.NULL,null, 20, 2, 1, 6),
    CYCLOPE("Actor/Monsters/Cyclope/Cyclope.atlas", "CYCLOPE", EnemySkillType.PROJECTILE, EnemyDetailSkillType.FIRE_BALL, 20, 2, 1, 6),
    SKULL("Actor/Monsters/Skull/skull.atlas", "SKULL", EnemySkillType.PROJECTILE, EnemyDetailSkillType.FIRE_BALL, 20, 2, 1, 4),
    SPIRIT("Actor/Monsters/Spirit/spirit.atlas", "SKULL", EnemySkillType.NULL, null, 15, 2, 1, 4),
    SPIRIT2("Actor/Monsters/Spirit2/spirit2.atlas", "SKULL", EnemySkillType.NULL, null, 15, 5, 1, 2);
    private final String atlasPath;
    private final String name;
    private final int maxLife;
    private final int attack;
    private final float speed;
    private final float range;
    private final EnemySkillType skillType;
    private final EnemyDetailSkillType detailSkillType;
    EnemyType(String atlasPath, String name, EnemySkillType type, EnemyDetailSkillType skill, int maxLife, int  attack, float speed, float range) {
        this.atlasPath = atlasPath;
        this.name = name;
        this.maxLife = maxLife;
        this.attack = attack;
        this.speed = speed;
        this.skillType = type;
        this.detailSkillType = skill;
        this.range = range;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public EnemySkillType getSkillType() {
        return skillType;
    }

    public EnemyDetailSkillType getDetailSkillType() {
        return detailSkillType;
    }

    public float getSpeed() {
        return speed;
    }

    public float getRange() {
        return range;
    }
}
