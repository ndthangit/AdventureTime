package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerComponent implements Component, Poolable{

	public int life;
	public int maxLife;
	public boolean hasAxe;
	public boolean isAttack;
	public Vector2 speed = new Vector2();
	
	@Override
	public void reset() {
		hasAxe = false;
		speed.set(0,0);
	}

}
