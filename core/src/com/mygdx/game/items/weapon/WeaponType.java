package com.mygdx.game.items.weapon;

import com.mygdx.game.effect.EffectType;

import static com.mygdx.game.effect.EffectType.SLASHCURVED;

public enum WeaponType {
    KATANA ("Items/Weapons/weapon.atlas","Katana", SLASHCURVED, 5, 0.05f, 7, 12, 0.5f),
    BIGSWORD("Items/Weapons/weapon.atlas", "BigSword", SLASHCURVED, 3, 0.05f, 7, 16, 0.75f),
    CLUB("Items/Weapons/weapon.atlas", "Club", SLASHCURVED, 3, 0.05f, 8, 14, 0.5f),;

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
