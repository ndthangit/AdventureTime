package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;

public class AnimationSystem extends IteratingSystem {

	public AnimationSystem(CoreGame game) {
		super(Family.all(AnimationComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		if (aniComponent != null) {
			aniComponent.aniTime += deltaTime;

			if (aniComponent.isFinished) {
				aniComponent.aniTime = 0;
			}
		}
	}
}
