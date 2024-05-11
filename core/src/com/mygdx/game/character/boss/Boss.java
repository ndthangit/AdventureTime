package com.mygdx.game.character.boss;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.Charater;

public class Boss extends Charater {
    private final BossType bossType;
    public Boss(BossType bossType,Vector2 position) {
        super(position, bossType.getWidth(), bossType.getHeight());
        this.bossType = bossType;
    }
    public BossType getBossType() {
        return bossType;
    }
}
