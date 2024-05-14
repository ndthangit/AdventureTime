package com.mygdx.game.effect;

public enum EffectType {
    NONE(null, null, 0, 0,0),
    SLASHCURVED("FX/Effect.atlas", "SlashCurved", 0.125f, 16, 16),
    SMOKE("FX/Effect.atlas", "Smoke", 0.1f, 16, 16),
    BIG_ENERGY_BALL("FX/Effect.atlas", "BigEnergyBall", 0.1f, 12, 12);


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
