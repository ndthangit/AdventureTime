package com.mygdx.game.effect;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.game.view.AnimationType;

public enum EffectType {
    NONE(null, null, 0, 0,0, 0, null),
    SLASHCURVED("FX/Effect.atlas", "SlashCurved", 0.125f, 16, 16, 1, Animation.PlayMode.NORMAL),
    SMOKE("FX/Effect.atlas", "Smoke", 0.1f, 16, 16, 0, Animation.PlayMode.NORMAL),
    BIG_ENERGY_BALL("FX/Effect.atlas", "BigEnergyBall", 0.1f, 12, 12, 2, Animation.PlayMode.LOOP),
    ENERGY_BALL("FX/Effect.atlas", "EnergyBall", 0.1f, 8, 8, 2, Animation.PlayMode.LOOP),
    ROCK("FX/Effect.atlas", "Rock", 0.1f, 15, 15, 2, Animation.PlayMode.NORMAL),
    SHURIKEN("FX/Effect.atlas", "Shuriken", 0.1f, 8, 8, 1, Animation.PlayMode.LOOP),
    BIG_KUNAI("FX/Effect.atlas", "BigKunai", 0.1f, 18, 5, 1, Animation.PlayMode.LOOP),
    FIREBALL("FX/Effect.atlas", "Fireball", 0.1f, 8, 8, 2, Animation.PlayMode.LOOP),
    AURA("FX/Effect.atlas", "WhiteAura", 0.1f, 16, 16, 0, Animation.PlayMode.NORMAL),
    BLAST("FX/Effect.atlas", "Blast", 0.1f, 16, 16, 0, Animation.PlayMode.NORMAL),
    CUTLEAVES( "FX/Effect.atlas", "Grass", 0.1f, 6, 6, 0, Animation.PlayMode.NORMAL),
    LOOP_AURA("FX/Effect.atlas", "WhiteAura", 0.1f, 16, 16, 0, Animation.PlayMode.LOOP),
    SHIELD("FX/Effect.atlas", "Shield", 0.1f, 12, 13, 0, Animation.PlayMode.LOOP)
    ;

    private final String atlasPath;
    private final String atlasKey;
    private final float frameTime;
    private final int width;
    private final int height;
    private final int fixDir;
    private final Animation.PlayMode mode;
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

    public Animation.PlayMode getMode() {
        return mode;
    }

    public int getFixDir() {
        return fixDir;
    }

    EffectType(String atlasPath, String atlasKey, float frameTime, int width, int height,int fixDir, Animation.PlayMode mode) {
        this.atlasPath = atlasPath;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.fixDir = fixDir;
    }
}
