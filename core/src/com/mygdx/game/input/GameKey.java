package com.mygdx.game.input;

import com.badlogic.gdx.Input;

public enum GameKey {
	UP(Input.Keys.W, Input.Keys.UP), 
	DOWN(Input.Keys.S, Input.Keys.DOWN), 
	LEFT(Input.Keys.A, Input.Keys.LEFT), 
	RIGHT(Input.Keys.D, Input.Keys.RIGHT), 
	SELECT(Input.Keys.U, Input.Keys.NUMPAD_4),
	ATTACK(Input.Keys.J, Input.Keys.NUMPAD_1),
	BACK(Input.Keys.ESCAPE, Input.Keys.NUMPAD_7),
	SKILL1(Input.Keys.I, Input.Keys.NUMPAD_5),
	SKILL2(Input.Keys.O, Input.Keys.NUMPAD_8),
	INVENTORY(Input.Keys.P, Input.Keys.NUMPAD_6),
	USE_ITEM_1 (Input.Keys.NUM_1),
	USE_ITEM_2 (Input.Keys.NUM_2),
	USE_ITEM_3 (Input.Keys.NUM_3),
	USE_ITEM_4 (Input.Keys.NUM_4),
	SWAP_WEAPON (Input.Keys.R),;
	final int[] keyCode;
	
	GameKey(final int... keyCode) {
		this.keyCode = keyCode;
	}
}
