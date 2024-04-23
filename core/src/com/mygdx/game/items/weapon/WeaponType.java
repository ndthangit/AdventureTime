package com.mygdx.game.items.weapon;

import com.mygdx.game.effect.EffectType;

import static com.mygdx.game.effect.EffectType.SLASHCURVED;

public enum WeaponType {
    BIG_SWORD ("Items/Weapons/weapon.atlas", SLASHCURVED, 5, 0.05f, 7, 12);

    private final String atlasPath;
    private final String atlasKey;
    private final EffectType effect;
    private final float frameTime;
    private final int attack;
    private final int width;
    private final int height;

    WeaponType(String atlasPath, EffectType effect, int attack, float frameTime, int width, int height) {
        this.atlasPath = atlasPath;
        this.effect = effect;
        this.atlasKey = "SpriteInHand";
        this.frameTime = frameTime;
        this.attack = attack;
        this.width = width;
        this.height = height;
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
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
