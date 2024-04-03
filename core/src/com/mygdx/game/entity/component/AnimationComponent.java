package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mygdx.game.view.AnimationType;

public class AnimationComponent implements Component, Poolable{

	public AnimationType aniType;
	public float aniTime;
	public float width;
	public float height;
	
	@Override
	public void reset() {
		aniType = null;
		aniTime = 0;
		width = height = 0;
	}

}
