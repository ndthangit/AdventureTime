package com.mygdx.game.character.player;

import com.badlogic.gdx.math.Vector2;

public enum PlayerType {
    BLACK_NINJA_MAGE("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", 16,16,12, 6, 3);

    private final String atlasPath;
    private final int health;
    private final Vector2 speed;
    private final int baseDamage;
    private final float width;
    private final float height;

    

    PlayerType (String atlasPath, int health,float width, float height, float speed, int baseDamage) {
        this.atlasPath = atlasPath;
        this.health = health;
        this.speed = new Vector2(speed, speed);
        this.baseDamage = baseDamage;
        this.width = width;
        this.height = height;
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

    public int getBaseDamage() {
        return baseDamage;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
}
