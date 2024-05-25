package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EffectComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.input.KeyInputListener;

public class PlayerMovementSystem extends IteratingSystem implements KeyInputListener {

	private CoreGame game;
	private boolean directionChange;
	private boolean isAttack;
	private int xFactor;
	private int yFactor;
	private float time;
	private float mTimeToNextStep = 0f;
	private static final float TIME_BETWEEN_STEP_SOUNDS = 0.3f;
	public PlayerMovementSystem(final CoreGame game) {
		super(Family.all(PlayerComponent.class, Box2DComponent.class).get());
		game.getInputManager().addInputListener(this);
		this.game = game;
		time = 0;
	}

	@Override
	protected void processEntity(final Entity entity, float deltaTime) {
		final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
		final Box2DComponent b2dComponent = ECSEngine.box2dCmpMapper.get(entity);
		final EffectComponent effectComponent = ECSEngine.effectCmpMapper.get(entity);

		//buff
		playerComponent.timeBuff -= deltaTime;
		if (playerComponent.timeBuff <= 0) {
			playerComponent.resetBuff();
			effectComponent.type = EffectType.NONE;
		} else {
			effectComponent.type = EffectType.LOOP_AURA;
		}

		// movement
		float speed = (float) Math.sqrt(xFactor * xFactor + yFactor * yFactor);
		if (playerComponent.stop) {
			speed = 0;
		}
		float speedx = speed > 0 ? (playerComponent.speed + playerComponent.speedBuff + playerComponent.speedBuffSkill2) * xFactor / speed : 0;
		float speedy = speed > 0 ? (playerComponent.speed + playerComponent.speedBuff + playerComponent.speedBuffSkill2) * yFactor / speed : 0;

		if (directionChange) {
			b2dComponent.body.applyLinearImpulse(speedx - b2dComponent.body.getLinearVelocity().x * b2dComponent.body.getMass(),
					speedy - b2dComponent.body.getLinearVelocity().y * b2dComponent.body.getMass(),
					b2dComponent.body.getWorldCenter().x,
					b2dComponent.body.getWorldCenter().y, true);
		}
		if (speed > 0) {
			mTimeToNextStep -= deltaTime;
			if (mTimeToNextStep < 0) {
				game.getAudioManager().playAudio(AudioType.FOOTSTEPS);
				while (mTimeToNextStep < 0) {
					mTimeToNextStep += TIME_BETWEEN_STEP_SOUNDS;
				}
			}
//			if(playerComponent.speedBuff>0||playerComponent.speedBuffSkill2>0){
//				game.getAudioManager().playAudio(AudioType.SPEEDBOOST);
//			}
		}


//		if (speed > 0) {
//			mTimeToNextStep -= deltaTime;
//			if (mTimeToNextStep < 0) {
//				game.getAudioManager().playAudio(AudioType.FOOTSTEPS);
//				while (mTimeToNextStep < 0) {
//					mTimeToNextStep += TIME_BETWEEN_STEP_SOUNDS;
//				}
//			}
//		} else {
//			game.getAudioManager().stopCurrentMusic(); // Đặt lại thời gian cho lần phát tiếng bước chân đầu tiên
//		}
	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		switch(gameKey) {
		case UP:
			directionChange = true;
			yFactor = 1;
			break;
		case DOWN:
			directionChange = true;
			yFactor = -1;
			break;
		case LEFT:
			directionChange = true;
			xFactor = -1;
			break;
		case RIGHT:
			directionChange = true;
			xFactor = 1;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		switch(gameKey) {
		case UP:
			directionChange = true;
			yFactor = manager.isKeyPressed(GameKey.DOWN) ? -1: 0;
			break;
		case DOWN:
			directionChange = true;
			yFactor = manager.isKeyPressed(GameKey.UP) ? 1: 0;
			break;
		case LEFT:
			directionChange = true;
			xFactor = manager.isKeyPressed(GameKey.RIGHT) ? 1: 0;
			break;
		case RIGHT:
			directionChange = true;
			xFactor = manager.isKeyPressed(GameKey.LEFT) ? -1: 0;
			break;
		default:
			break;
		}
		
	}
	
}
