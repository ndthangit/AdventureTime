package com.mygdx.game.items.weapon;

import com.mygdx.game.effect.EffectType;

import static com.mygdx.game.effect.EffectType.SLASHCURVED;

public enum WeaponType {
    SWORD2 ("Items/Weapons/weapon.atlas","Sword2", SLASHCURVED, 4, 0.05f, 7, 12, 0.5f),
    BIGSWORD("Items/Weapons/weapon.atlas", "BigSword", SLASHCURVED, 6, 0.05f, 7, 12, 0.5f),
    AXE("Items/Weapons/weapon.atlas", "Axe", SLASHCURVED, 12, 0.05f, 11, 8, 1.25f),
    FORK("Items/Weapons/weapon.atlas", "Fork", SLASHCURVED, 7, 0.05f, 7, 12, 0.75f),
    HAMMER("Items/Weapons/weapon.atlas", "Hammer", SLASHCURVED, 10, 0.05f, 9, 9, 1.25f),
    LANCE("Items/Weapons/weapon.atlas", "Lance", SLASHCURVED, 8, 0.05f, 6, 16, 1f),
    SAI("Items/Weapons/weapon.atlas", "Sai", SLASHCURVED, 2, 0.05f, 7, 8, 0.1f);

    private final String atlasPath;
    private final String atlasKey;
    private final String atlasKeyIcon;
    private final EffectType effect;
    private final float frameTime;
    private final int attack;
    private final int width;
    private final int height;
    private final float speed;

    WeaponType(String atlasPath, String key, EffectType effect, int attack, float frameTime, int width, int height, float speed) {
        this.atlasPath = atlasPath;
        this.effect = effect;
        this.atlasKeyIcon = key;
        this.atlasKey = key + "InHand";
        this.frameTime = frameTime;
        this.attack = attack;
        this.width = width;
        this.height = height;
        this.speed = speed;
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

    public EffectType getEffect() {
        return effect;
    }

    public String getAtlasKeyIcon() {
        return atlasKeyIcon;
    }

    public float getSpeed() {
        return speed;
    }
}
