package com.mygdx.game.character.enemy;

public enum EnemyType {
    BEAST("Actor/Monsters/Beast/beast.atlas", "BEAST", 30, 5, 4),
    BEAST2("Actor/Monsters/Beast2/beast2.atlas", "BEAST2", 35, 5, 3),
    CYCLOPE("Actor/Monsters/Cyclope/cyclope.atlas", "CYCLONE", 20, 4, 7),
    BAMBOOYELLOW("Actor/Monsters/BambooYellow/bambooyellow.atlas", "BAMBOOYELLOW", 12, 3, 0);
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
