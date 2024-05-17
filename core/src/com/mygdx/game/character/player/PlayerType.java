package com.mygdx.game.character.player;

import com.badlogic.gdx.math.Vector2;

public enum PlayerType {
    BLACK_NINJA_MAGE("Actor/Characters/BlackNinjaMage/blackninjamage.atlas", 16,16,12, 4, PLayerSkillType.BIG_KUNAI, PLayerSkillType.SMOKE);

    private final String atlasPath;
    private final int health;
    private final float speed;
    private final float width;
    private final float height;
    private final PLayerSkillType skillType1;
    private final PLayerSkillType skillType2;

    

    PlayerType (String atlasPath, int health,float width, float height, float speed, PLayerSkillType skillType1, PLayerSkillType skillType2) {
        this.atlasPath = atlasPath;
        this.health = health;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.skillType1 = skillType1;
        this.skillType2 = skillType2;
    }

	public int getHealth() {
		return health;
	}

	public float getSpeed() {
		return speed;
	}
	
	public String getAtlasPath() {
        return atlasPath;
    }

    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public PLayerSkillType getSkillType2() {
        return skillType2;
    }

    public PLayerSkillType getSkillType1() {
        return skillType1;
    }
}
