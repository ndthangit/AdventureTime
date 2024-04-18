package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.GameObjectComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.entity.component.RemoveComponent;
import com.mygdx.game.entity.component.WeaponComponent;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.items.weapon.WeaponType;
import com.mygdx.game.view.AnimationType;

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
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);
		int direction = 0;
		if (playerComponent.isAttack) {
			if (aniComponent.aniType == AnimationType.HERO_ATTACK_DOWN) {
				direction = 0;
			}
			else if (aniComponent.aniType == AnimationType.HERO_ATTACK_UP) {
				direction = 2;
			}
			else if (aniComponent.aniType == AnimationType.HERO_ATTACK_LEFT) {
				direction = 1;
			}
			else if (aniComponent.aniType == AnimationType.HERO_ATTACK_RIGHT) {
				direction = 3;
			}
			ImmutableArray<Entity> weaponEnities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, Box2DComponent.class).get());
			if (weaponEnities.size() == 0) {
				Vector2 speed = new Vector2(0, 0);
				Weapon weapon = new Weapon(WeaponType.BIG_SWORD, b2DComponent.body.getPosition(), direction, speed, 7 * UNIT_SCALE,12 * UNIT_SCALE);
				game.getEcsEngine().createPlayerWeapon(weapon);
			}
		}
		else {
			ImmutableArray<Entity> weaponEnities = game.getEcsEngine().getEntitiesFor(Family.all(WeaponComponent.class, Box2DComponent.class).get());
			for (Entity weaponEntity: weaponEnities)
				weaponEntity.add(((ECSEngine) getEngine()).createComponent(RemoveComponent.class));;
		}
	}
	
}
