package com.mygdx.game.items.key;

public enum KeyType {
    GOLD_KEY("Items/Key.atlas", "GoldKey", 24, 16);

    private final String atlasPath;
    private final String key;
    private final float width;
    private final float height;

    KeyType(String atlasPath, String key, float width, float height) {
        this.atlasPath = atlasPath;
        this.key = key;
        this.width = width;
        this.height = height;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public String getKey() {
        return key;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
