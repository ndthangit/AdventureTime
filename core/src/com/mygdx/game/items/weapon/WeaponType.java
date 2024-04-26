package com.mygdx.game.items.weapon;

public enum WeaponType {
    BIG_SWORD ("Items/Weapons/weapon.atlas", 5, 0.05f);

    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;
    private final int attack;

    WeaponType(String atlasPath,int attack, float frameTime) {
        this.atlasPath = atlasPath;
        this.atlasKey = "SpriteInHand";
        this.frameTime = frameTime;
        this.attack = attack;
    }

    public String getAtlasKey() {

        return atlasKey;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public float getFrameTime() {
        return frameTime;
    }

    public int getAttack() {
        return attack;
    }
}
