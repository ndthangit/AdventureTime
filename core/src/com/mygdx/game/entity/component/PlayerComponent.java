package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.view.AnimationType;

public class PlayerComponent extends EntityComponent implements Component, Poolable{
	public PlayerType aniType;
	public boolean isAttack;
	
	@Override
	public void reset() {
		super.reset();
		aniType = null;

	}

}
