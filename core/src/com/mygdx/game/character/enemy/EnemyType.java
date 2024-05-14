package com.mygdx.game.character.enemy;

public enum EnemyType {
    BEAST("Actor/Monsters/Beast/beast.atlas", "BEAST", 30, 3, 4),
    BEAST2("Actor/Monsters/Beast2/beast2.atlas", "BEAST2", 35, 3, 3),
    CYCLOPE("Actor/Monsters/Cyclope/cyclope.atlas", "CYCLONE", 20, 2, 7),
    BAMBOOYELLOW("Actor/Monsters/BambooYellow/bambooyellow.atlas", "BAMBOOYELLOW", 12, 3, 0),
    SLIME2("Actor/Monsters/Slime2/slime2.atlas","SLIME2", 25, 2, 3),
    SLIME4("Actor/Monsters/Slime4/slime4.atlas","SLIME4",20, 1, 3),
    REPTILE2("Actor/Monsters/Reptile2/reptile2.atlas","REPTILE2", 25, 3, 4),
    SKULLBLUE("Actor/Monsters/SkullBlue/skullblue.atlas", "SKULLBLUE", 20, 4, 4),
    LARVA("Actor/Monsters/Larva/larva.atlas", "LARVA", 20, 2 , 3),
    REDFIGHTER("Actor/Characters/RedFighter/redfighter.atlas", "REDFIGHTER", 30, 5, 4),
    REDNINJA("Actor/Characters/RedNinja/redninja.atlas", "REDNINJA", 30, 2, 5),
    REDNINJA2("Actor/Characters/RedNinja2/redninja2.atlas", "REDNINJA2", 28, 2, 5),
    REDSAMURAI("Actor/Characters/RedSamurai/redsamurai.atlas", "REDSAMURAI", 32, 2, 5),
    SAMURAI("Actor/Characters/Samurai/samurai.atlas", "SAMURAI", 32, 2, 5),
    ORANGESORCERER("Actor/Characters/OrangeSorcerer/orangeSorcerer.atlas", "ORANGESORCERER", 20, 4, 3),
    ;
    private final String atlasPath;
    private final String name;
    private final int maxLife;
    private final int attack;
    private final int speed;
    EnemyType(String atlasPath, String name, int maxLife, int  attack, int speed) {
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

    public int getSpeed() {
        return speed;
    }
}
