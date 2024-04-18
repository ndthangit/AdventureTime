package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.view.AnimationType;

public class PlayerAnimationSystem extends IteratingSystem {

	public PlayerAnimationSystem(CoreGame game) {
		super(Family.all(AnimationComponent.class, PlayerComponent.class, Box2DComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final Box2DComponent b2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
		if(b2DComponent.body.getLinearVelocity().equals(Vector2.Zero)) {
			aniComponent.aniTime = 0;
			if (playerComponent.isAttack) {
				if (aniComponent.aniType == AnimationType.HERO_DOWN) {
					aniComponent.aniType = AnimationType.HERO_ATTACK_DOWN;
				}
				else if (aniComponent.aniType == AnimationType.HERO_UP) {
					aniComponent.aniType = AnimationType.HERO_ATTACK_UP;
				}
				else if (aniComponent.aniType == AnimationType.HERO_LEFT) {
					aniComponent.aniType = AnimationType.HERO_ATTACK_LEFT;
				}
				else if (aniComponent.aniType == AnimationType.HERO_RIGHT) {
					aniComponent.aniType = AnimationType.HERO_ATTACK_RIGHT;
				}
			}
			else {
				if (aniComponent.aniType == AnimationType.HERO_ATTACK_DOWN) {
					aniComponent.aniType = AnimationType.HERO_DOWN;
				}
				else if (aniComponent.aniType == AnimationType.HERO_ATTACK_UP) {
					aniComponent.aniType = AnimationType.HERO_UP;
				}
				else if (aniComponent.aniType == AnimationType.HERO_ATTACK_LEFT) {
					aniComponent.aniType = AnimationType.HERO_LEFT;
				}
				else if (aniComponent.aniType == AnimationType.HERO_ATTACK_RIGHT) {
					aniComponent.aniType = AnimationType.HERO_RIGHT;
				}
			}
		}
		else if (b2DComponent.body.getLinearVelocity().x > 0) {
			aniComponent.aniType = AnimationType.HERO_RIGHT;
		}
		else if (b2DComponent.body.getLinearVelocity().x < 0) {
			aniComponent.aniType = AnimationType.HERO_LEFT;
		}
		else if (b2DComponent.body.getLinearVelocity().y > 0) {
			aniComponent.aniType = AnimationType.HERO_UP;
		}
		else if (b2DComponent.body.getLinearVelocity().y < 0) {
			aniComponent.aniType = AnimationType.HERO_DOWN;
		}
		
	}

}
