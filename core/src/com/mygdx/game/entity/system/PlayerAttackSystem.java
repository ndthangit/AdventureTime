package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.effect.Effect;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.items.weapon.WeaponType;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;

import static com.mygdx.game.CoreGame.UNIT_SCALE;

public class PlayerAttackSystem extends IteratingSystem{
	private CoreGame game;
	public PlayerAttackSystem(CoreGame game) {
		super(Family.all(AnimationComponent.class, PlayerComponent.class, Box2DComponent.class).get());
		this.game = game;

	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
		final Box2DComponent b2DComponent = ECSEngine.box2dCmpMapper.get(entity);
		if (playerComponent.isAttack) {
			createWeapon(playerComponent, b2DComponent);

		}
		else {
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
			game.getEcsEngine().createPlayerWeapon(weapon);
			game.getEcsEngine().createEffect(weapon.effect);
		}
	}

	void destroyWeapon() {
		ImmutableArray<Entity> weaponEnities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, Box2DComponent.class).get());
		for (Entity weaponEntity: weaponEnities)
			weaponEntity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
		ImmutableArray<Entity> effectEtities = game.getEcsEngine().getEntitiesFor(Family.all(EffectComponent.class).get());
		for (Entity effectEntity: effectEtities)
			effectEntity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));
	}
}
