package com.mygdx.game.items.weapon;

public enum WeaponType {
    BIG_SWORD ("Items/Weapons/weapon.atlas", 0.05f);

    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;

    WeaponType(String atlasPath, float frameTime) {
        this.atlasPath = atlasPath;
        this.atlasKey = "SpriteInHand";
        this.frameTime = frameTime;
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
}
