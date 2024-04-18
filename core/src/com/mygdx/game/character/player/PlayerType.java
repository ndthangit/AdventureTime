package com.mygdx.game.character.player;

import com.badlogic.gdx.math.Vector2;

public enum PlayerType {
    BLACK_NINJA_MAGE("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", 12, 6);

    private final String atlasPath;
    private final int health;
    private final Vector2 speed;

    

    PlayerType (String atlasPath, int health, float speed) {
        this.atlasPath = atlasPath;
        this.health = health;
        this.speed = new Vector2(speed, speed);
    }

	public int getHealth() {
		return health;
	}

	public Vector2 getSpeed() {
		return speed;
	}
	
	public String getAtlasPath() {
        return atlasPath;
    }

}
