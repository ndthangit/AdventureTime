package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.Effect;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.input.GameKey;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.input.KeyInputListener;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.items.weapon.WeaponType;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;

import static com.mygdx.game.CoreGame.*;

public class PlayerAttackSystem extends IteratingSystem implements KeyInputListener {
	private CoreGame game;

	private boolean isAttack;
	private boolean isSkill1;
	private boolean readySkill1;
	private boolean isSkill2;
	private boolean readySkill2;
	private float time;

	public PlayerAttackSystem(CoreGame game) {
		super(Family.all(AnimationComponent.class, PlayerComponent.class, Box2DComponent.class).get());
		this.game = game;
		game.getInputManager().addInputListener(this);
		time = 0;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
		final Box2DComponent b2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		playerComponent.durationSkill2 = playerComponent.durationSkill2 - deltaTime < 0 ? 0 : playerComponent.durationSkill2 - deltaTime;
		if (playerComponent.durationSkill2 == 0) {
			aniComponent.alpha = 1;
			playerComponent.vincible = false;
			playerComponent.speedBuffSkill2 = 0;
		}
		if (!readySkill1) {
			playerComponent.reloadSkill1 += deltaTime;
			if (playerComponent.reloadSkill1 > playerComponent.aniType.getSkillType1().getTimeReload()) {
				playerComponent.reloadSkill1 = 0;
				readySkill1 = true;
			}
		}
		if (!readySkill2) {
			playerComponent.reloadSkill2 += deltaTime;
			if (playerComponent.reloadSkill2 > playerComponent.aniType.getSkillType2().getTimeReload()) {
				playerComponent.reloadSkill2 = 0;
				readySkill2 = true;
			}
		}
		if (isSkill1 && readySkill1) {
			usingSkill1(entity);
			game.getAudioManager().playAudio(AudioType.HIT1);
		}
		else if (isSkill2 && readySkill2) {
			usingSkill2(entity);
			game.getAudioManager().playAudio(AudioType.HIT2);
		}
		else if (isAttack) {
			playerComponent.isAttack = true;
			playerComponent.stop = true;
			createWeapon(playerComponent, b2DComponent);
			time += deltaTime;
			if (time > playerComponent.weapon.type.getSpeed()) {
				isAttack = false;
				playerComponent.isAttack = false;
				playerComponent.stop = false;
				time = 0;
			}
		}
		if(!isAttack) {
			destroyWeapon();
		}
	}

	void createWeapon(PlayerComponent playerComponent, Box2DComponent b2DComponent) {
		ImmutableArray<Entity> weaponEnities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, Box2DComponent.class).get());
		if (weaponEnities.size() == 0) {
			Weapon weapon = playerComponent.weapon;
			weapon.position.set(b2DComponent.body.getPosition());
			weapon.direction = playerComponent.direction;
			weapon.effect.setPosition(b2DComponent.body.getPosition());
			weapon.effect.setDirection(playerComponent.direction);
			game.getEcsEngine().createPlayerWeapon(weapon, playerComponent.damageBuff);
			game.getEcsEngine().createEffect(weapon.effect);
			game.getAudioManager().playAudio(AudioType.SWORD);
		}
	}

	void destroyWeapon() {
		ImmutableArray<Entity> weaponEnities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, Box2DComponent.class).get());
		for (Entity weaponEntity: weaponEnities)
			weaponEntity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
		ImmutableArray<Entity> effectEtities = game.getEcsEngine().getEntitiesFor(Family.all(EffectComponent.class).get());
		for (Entity effectEntity: effectEtities) {
			EffectComponent effectCmp = ECSEngine.effectCmpMapper.get(effectEntity);
			DamageAreaComponent damageAreaCmp = ECSEngine.damageAreaCmpMapper.get(effectEntity);
			if (effectCmp.owner == BIT_WEAPON) {
				effectEntity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
			}
		}
	}

	void usingSkill1(Entity entity) {
		final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
		final Box2DComponent b2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		PlayerType type = playerComponent.aniType;
		Vector2 position = new Vector2(b2DComponent.body.getPosition().x, b2DComponent.body.getPosition().y);
		DamageArea area = new DamageArea(position, BIT_PLAYER, playerComponent.direction, type.getSkillType1().getWidth(), type.getSkillType1().getHeight(), type.getSkillType1().getDamage(), type.getSkillType1().getEffectType(), true, type.getSkillType1().getSpeed(), playerComponent.upgradeSkill1);
		game.getEcsEngine().createDamageArea(area);
		isSkill1 = false;
		readySkill1 = false;
	}

	void usingSkill2(Entity entity) {
		final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
		final Box2DComponent b2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		PlayerType type = playerComponent.aniType;
		Vector2 position = new Vector2(b2DComponent.body.getPosition().x, b2DComponent.body.getPosition().y);
		DamageArea area = new DamageArea(position, BIT_PLAYER, playerComponent.direction, type.getSkillType2().getWidth(), type.getSkillType2().getHeight(), type.getSkillType2().getDamage(), type.getSkillType2().getEffectType(), type.getSkillType2().isBullet(), type.getSkillType2().getSpeed(), false);
		game.getEcsEngine().createDamageArea(area);
		isSkill2 = false;
		readySkill2 = false;
		playerComponent.durationSkill2 = 5;
		aniComponent.alpha = 0.5f;
		playerComponent.speedBuffSkill2 = 2;
		playerComponent.vincible = true;

	}

	@Override
	public void keyPressed(InputManager manager, GameKey gameKey) {
		switch (gameKey) {
			case ATTACK:
				isAttack = true;
				break;
			case SKILL1:
				isSkill1 = true;
				break;
			case SKILL2:
				isSkill2 = true;
				break;
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKey gameKey) {
		switch (gameKey) {
			case SKILL1:
				isSkill1 = false;
				break;
			case SKILL2:
				isSkill2 = false;
				break;
		}
	}

}
