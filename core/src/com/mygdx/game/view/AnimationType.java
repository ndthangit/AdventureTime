package com.mygdx.game.view;

public enum AnimationType {
	IDLE_DOWN("Idle", 0.1f, 0),
	IDLE_UP("Idle", 0.1f, 1),
	IDLE_LEFT("Idle", 0.1f, 2),
	IDLE_RIGHT("Idle", 0.1f, 3),

	DOWN("Walk", 0.2f,0),
	UP("Walk", 0.2f, 1),
	LEFT("Walk", 0.2f, 2),
	RIGHT("Walk", 0.2f, 3),

	// for player
	ATTACK_DOWN("Attack", 0.1f, 0),
	ATTACK_UP("Attack", 0.1f, 1),
	ATTACK_LEFT("Attack", 0.1f, 2),
	ATTACK_RIGHT("Attack", 0.1f, 3),

	SKILL("Special1", 0.1f, 0),

	// for boss
	CHARGE_RIGHT("ChargeLeft", 0.4f, 0),
	CHARGE_LEFT("ChargeRight", 0.4f, 0),
	B_ATTACK_RIGHT("AttackLeft", 0.2f, 0),
	B_ATTACK_LEFT("AttackRight", 0.2f, 0),

	B_HIT("Hit", 0.4f, 0);

	public String getAtlasKey() {
		return atlasKey;
	}

	public float getFrameTime() {
		return frameTime;
	}

	public int getColIndex() {
		return colIndex;
	}

	private final String atlasKey;
	private final float frameTime;
	private final int colIndex;

	AnimationType(String atlasKey, float frameTime, int colIndex) {
		this.atlasKey = atlasKey;
		this.frameTime = frameTime;
		// TODO Auto-generated constructor stub
		this.colIndex = colIndex;
	}
}
