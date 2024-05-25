package com.mygdx.game.items.special;

public enum SpecialType {
    SCROLLFIRE ("Items/special.atlas", "ScrollFire", 12,12),
    LIFEPOT("Items/special.atlas", "LifePot", 12,12);
    private final String atlasPath;
    private final String atlasName;
    private final int width;
    private final int height;
    SpecialType(String atlasPath, String key, int width, int height) {
        this.atlasPath = atlasPath;
        this.atlasName = key;
        this.width = width;
        this.height = height;
    }
    public String getAtlasPath() {
        return atlasPath;
    }
    public String getAtlasName() {
        return atlasName;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}

