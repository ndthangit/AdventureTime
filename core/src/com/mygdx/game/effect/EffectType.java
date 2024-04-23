package com.mygdx.game.effect;

public enum EffectType {
    SLASHCURVED("FX/Effect.atlas", "SlashCurved", 0.1f, 16, 16),
    SMOKE("FX/Effect.atlas", "Smoke", 0.1f, 16, 16);

    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;
    private final int width;
    private final int height;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

    EffectType(String atlasPath, String atlasKey, float frameTime, int width, int height) {
        this.atlasPath = atlasPath;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        this.width = width;
        this.height = height;
    }
}
