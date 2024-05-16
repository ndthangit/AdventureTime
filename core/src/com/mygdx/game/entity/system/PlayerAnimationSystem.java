package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.view.AnimationType;

import static com.mygdx.game.view.DirectionType.*;

public class PlayerAnimationSystem extends IteratingSystem {

	public PlayerAnimationSystem(CoreGame game) {
		super(Family.all(AnimationComponent.class, PlayerComponent.class, Box2DComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final Box2DComponent b2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
		if(b2DComponent.body.getLinearVelocity().isZero()) {
			if (playerComponent.isAttack) {
				if (playerComponent.direction == DOWN) {
					aniComponent.aniType = AnimationType.ATTACK_DOWN;
				}
				else if (playerComponent.direction == UP) {
					aniComponent.aniType = AnimationType.ATTACK_UP;
				}
				else if (playerComponent.direction == LEFT) {
					aniComponent.aniType = AnimationType.ATTACK_LEFT;
				}
				else if (playerComponent.direction == RIGHT) {
					aniComponent.aniType = AnimationType.ATTACK_RIGHT;
				}
			}
			else {
				if (playerComponent.direction == DOWN) {
					aniComponent.aniType = AnimationType.IDLE_DOWN;
				}
				else if (playerComponent.direction == UP) {
					aniComponent.aniType = AnimationType.IDLE_UP;
				}
				else if (playerComponent.direction == LEFT) {
					aniComponent.aniType = AnimationType.IDLE_LEFT;
				}
				else if (playerComponent.direction == RIGHT) {
					aniComponent.aniType = AnimationType.IDLE_RIGHT;
				}
			}
		}
		else if (b2DComponent.body.getLinearVelocity().x > 0) {
			aniComponent.aniType = AnimationType.RIGHT;
			playerComponent.direction = RIGHT;
		}
		else if (b2DComponent.body.getLinearVelocity().x < 0) {
			aniComponent.aniType = AnimationType.LEFT;
			playerComponent.direction = LEFT;
		}
		else if (b2DComponent.body.getLinearVelocity().y > 0) {
			aniComponent.aniType = AnimationType.UP;
			playerComponent.direction = UP;
		}
		else if (b2DComponent.body.getLinearVelocity().y < 0) {
			aniComponent.aniType = AnimationType.DOWN;
			playerComponent.direction = DOWN;
		}
	}
}
