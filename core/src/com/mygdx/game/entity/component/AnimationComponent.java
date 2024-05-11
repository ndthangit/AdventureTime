package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mygdx.game.view.AnimationType;

public class AnimationComponent implements Component, Poolable{
	public AnimationType aniType;
	public String path;
	public float aniTime;
	public float width;
	public float height;
	public boolean isSquare;
	
	@Override
	public void reset() {
		path = null;
		aniType = null;
		aniTime = 0;
		width = height = 0;
		isSquare = false;
	}

}
