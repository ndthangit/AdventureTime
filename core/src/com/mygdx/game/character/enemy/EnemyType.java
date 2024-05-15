package com.mygdx.game.character.enemy;

public enum EnemyType {
    BEAST("Actor/Monsters/Beast/beast.atlas", "BEAST", 30, 3, 1),
    BEAST2("Actor/Monsters/Beast2/beast2.atlas", "BEAST2", 35, 3, 2),
    CYCLOPE("Actor/Monsters/Cyclope/cyclope.atlas", "CYCLONE", 20, 2, 2),
    BAMBOOYELLOW("Actor/Monsters/BambooYellow/bambooyellow.atlas", "BAMBOOYELLOW", 12, 3, 0),
    SLIME2("Actor/Monsters/Slime2/slime2.atlas","SLIME2", 25, 2, 1),
    SLIME4("Actor/Monsters/Slime4/slime4.atlas","SLIME4",20, 1, 1),
    REPTILE2("Actor/Monsters/Reptile2/reptile2.atlas","REPTILE2", 25, 3, 1.5f),
    SKULLBLUE("Actor/Monsters/SkullBlue/skullblue.atlas", "SKULLBLUE", 20, 4, 1),
    LARVA("Actor/Monsters/Larva/larva.atlas", "LARVA", 20, 2 , 1),
    REDFIGHTER("Actor/Characters/RedFighter/redfighter.atlas", "REDFIGHTER", 30, 5, 2),
    REDNINJA("Actor/Characters/RedNinja/redninja.atlas", "REDNINJA", 30, 2, 2),
    REDNINJA2("Actor/Characters/RedNinja2/redninja2.atlas", "REDNINJA2", 28, 2, 2),
    REDSAMURAI("Actor/Characters/RedSamurai/redsamurai.atlas", "REDSAMURAI", 32, 2, 1.5f),
    SAMURAI("Actor/Characters/Samurai/samurai.atlas", "SAMURAI", 32, 2, 1.5f),
    ORANGESORCERER("Actor/Characters/OrangeSorcerer/orangeSorcerer.atlas", "ORANGESORCERER", 20, 4, 1),
    ;
    private final String atlasPath;
    private final String name;
    private final int maxLife;
    private final int attack;
    private final float speed;
    EnemyType(String atlasPath, String name, int maxLife, int  attack, float speed) {
        this.atlasPath = atlasPath;
        this.name = name;
        this.maxLife = maxLife;
        this.attack = attack;
        this.speed = speed;
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

    public float getSpeed() {
        return speed;
    }
}
