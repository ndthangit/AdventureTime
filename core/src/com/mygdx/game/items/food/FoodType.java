package com.mygdx.game.items.food;

import com.mygdx.game.effect.EffectType;

public enum FoodType {
    BEAF("Items/Food/food.atlas", "Beaf", 12,12, 0, 2, 0, 60),
    CALAMARI("Items/Food/food.atlas", "Calamari", 12, 12, 3, 0, 2, 15);
    private final String atlasPath;
    private final String key;
    private final int width;
    private final int height;
    private final int heal;
    private final int strength;
    private final int speed;
    private final float time;

    FoodType(String atlasPath, String key, int width, int height, int heal, int strength, int speed, float time) {
        this.atlasPath = atlasPath;
        this.key = key;
        this.width = width;
        this.height = height;
        this.heal = heal;
        this.strength = strength;
        this.speed = speed;
        this.time = time;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public String getKey() {
        return key;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHeal() {
        return heal;
    }

    public int getStrength() {
        return strength;
    }

    public int getSpeed() {
        return speed;
    }

    public float getTime() {
        return time;
    }
}
