package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mygdx.game.view.AnimationType;

public class AnimationComponent implements Component, Poolable{
	public AnimationType aniType;
	public AnimationType nextType;
	public String path;
	public float aniTime;
	public float blinkTime;
	public float width;
	public float height;
	public boolean isSquare;
	public boolean isFinished;
	public float alpha = 1;
	public boolean isDamaged = false;

	@Override
	public void reset() {
		path = null;
		aniType = null;
		aniTime = 0;
		width = height = 0;
		isSquare = false;
	}

	public void setDamaged(boolean setVal, float duration, float deltatime) {
		if(blinkTime < duration){
			isDamaged = !setVal;
			blinkTime += deltatime;
			return;
		}
		isDamaged = setVal;
		blinkTime = 0;
	}

}
