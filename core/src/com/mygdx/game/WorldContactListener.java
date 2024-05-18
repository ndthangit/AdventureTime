package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.EnemyComponent;
import com.mygdx.game.items.food.Food;

import static com.mygdx.game.CoreGame.*;

public class WorldContactListener implements ContactListener {
	private final Array<CollisionListener> listeners;

	private boolean hasPlayer;
	private boolean hasGameObj;
	private boolean hasWeapon;
	private boolean hasEnemy;
	private boolean hasItem;
	private boolean hasDoor;
	private boolean hasBoss;
	private boolean hasGround;
	private boolean hasDamageArea;
	private final Array<EnemyComponent> stoEnemy;
    public WorldContactListener() {
		this.listeners = new Array<CollisionListener>();
		stoEnemy = new Array<EnemyComponent>();
    }

	public void addPlayerCollisionListener(CollisionListener listener) {

		listeners.add(listener);
	}
    @Override
	public void beginContact(Contact contact) {
		reset();
		final Entity player;
		final Entity gameObj;
		final Entity weapon;
		final Entity enemy;
		final Entity item;
		final Entity door;
		final Entity boss;
		final Entity damageArea;
		final Body bodyA = contact.getFixtureA().getBody();
		final Body bodyB = contact.getFixtureB().getBody();
		final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
		final int catFixB = contact.getFixtureB().getFilterData().categoryBits;


		if ((catFixA & BIT_PLAYER) == BIT_PLAYER) {
			player = (Entity) bodyA.getUserData();
			hasPlayer = true;
		}
		else if((catFixB & BIT_PLAYER) == BIT_PLAYER){
			player = (Entity) bodyB.getUserData();
			hasPlayer = true;
		}
		else {
			player = null;
		}

		if ((catFixA & BIT_GAME_OBJECT) == BIT_GAME_OBJECT) {
			gameObj = (Entity) bodyA.getUserData();
			hasGameObj = true;
		}
		else if((catFixB & BIT_GAME_OBJECT) == BIT_GAME_OBJECT){
			gameObj = (Entity) bodyB.getUserData();
			hasGameObj = true;
		}
		else {
			gameObj = null;
		}
		
		if ((catFixA & BIT_WEAPON) == BIT_WEAPON) {
			weapon = (Entity) bodyA.getUserData();
			hasWeapon = true;
		}
		else if((catFixB & BIT_WEAPON) == BIT_WEAPON){
			weapon = (Entity) bodyB.getUserData();
			hasWeapon = true;
		}
		else {
			weapon = null;
		}

		if ((catFixA & BIT_ENEMY) == BIT_ENEMY) {
			enemy = (Entity) bodyA.getUserData();
			hasEnemy = true;
		}
		else if((catFixB & BIT_ENEMY) == BIT_ENEMY){
			enemy = (Entity) bodyB.getUserData();
			hasEnemy = true;
		}
		else {
			enemy = null;
		}

		if ((catFixA & BIT_ITEM) == BIT_ITEM) {
			item = (Entity) bodyA.getUserData();
			hasItem = true;
		}
		else if((catFixB & BIT_ITEM) == BIT_ITEM){
			item = (Entity) bodyB.getUserData();
			hasItem = true;
		}
		else {
			item = null;
		}

		if ((catFixA & BIT_DOOR) == BIT_DOOR) {
			door = (Entity) bodyA.getUserData();
			hasDoor = true;
		}
		else if((catFixB & BIT_DOOR) == BIT_DOOR){
			door = (Entity) bodyB.getUserData();
			hasDoor = true;
		}
		else {
			door = null;
		}

		if ((catFixA & BIT_BOSS) == BIT_BOSS) {
			boss = (Entity) bodyA.getUserData();
			hasBoss = true;
		}
		else if ((catFixB & BIT_BOSS) == BIT_BOSS) {
			boss = (Entity) bodyB.getUserData();
			hasBoss = true;
		}
		else {
			boss = null;
		}

		if ((catFixA & BIT_DAMAGE_AREA) == BIT_DAMAGE_AREA) {
			damageArea = (Entity) bodyA.getUserData();
			hasDamageArea = true;
		}
		else if ((catFixB & BIT_DAMAGE_AREA) == BIT_DAMAGE_AREA) {
			damageArea = (Entity) bodyB.getUserData();
			hasDamageArea = true;
		}
		else {
			damageArea = null;
		}

		if ((catFixA & BIT_GROUND) == BIT_GROUND) {
			hasGround = true;
		}
		else if ((catFixB & BIT_DAMAGE_AREA) == BIT_DAMAGE_AREA) {
			hasGround = true;
		}

		if (hasPlayer && hasGameObj) {
			for (final CollisionListener listener : listeners) {
				listener.playerCollision(player, gameObj);
			}
		}
		else if (hasPlayer && hasEnemy) {
			for (final CollisionListener listener : listeners) {
				listener.playerVSEnemy(player, enemy);
			}
		}
		else if (hasWeapon && hasGameObj) {
			for (final CollisionListener listener : listeners) {
				listener.weaponCollision(weapon, gameObj);
			}
		}
		else if (hasWeapon && hasEnemy) {
			for (final CollisionListener listener : listeners) {
				listener.weaponVSEnemy(weapon, enemy);
				stoEnemy.add(ECSEngine.enemyCmpMapper.get(enemy));
			}
		}
		else if (hasWeapon && hasBoss) {
			for (final CollisionListener listener : listeners) {
				listener.weaponVSEnemy(weapon, boss);
			}
		}
		else if (hasPlayer && hasItem) {
			for (final CollisionListener listener : listeners) {
				listener.playerVSItem(player, item);
			}
		}
		else if (hasPlayer && hasDoor) {
			for (final CollisionListener listener : listeners) {
				listener.playerVSDoor(player, door);
			}
		}
		else if (hasPlayer && hasDamageArea) {
			for (final CollisionListener listener : listeners) {
				listener.playerVSDamageArea(player, damageArea);
			}
		}
		else if (hasPlayer && hasBoss) {
			for (final CollisionListener listener : listeners) {
				listener.playerVSEnemy(player, boss);
			}
		}
		else if (hasDamageArea && hasEnemy) {
			for (final CollisionListener listener : listeners) {
				listener.damageAreaVSEnemy(damageArea, enemy);
			}
		}
		else if (hasDamageArea && hasBoss) {
			for (final CollisionListener listener : listeners) {
				listener.damageAreaVSEnemy(damageArea, boss);
			}
		}
		else if (hasDamageArea && hasGround) {
			for (final CollisionListener listener : listeners) {
				listener.damageAreaVSGround(damageArea);
			}
		}
		else if (hasDamageArea && hasWeapon) {
			for (final CollisionListener listener : listeners) {
				listener.damageAreaVSWeapon(damageArea);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		final Body bodyA = contact.getFixtureA().getBody();
		final Body bodyB = contact.getFixtureB().getBody();
		bodyA.applyLinearImpulse(-bodyA.getLinearVelocity().x*bodyA.getMass(), -bodyA.getLinearVelocity().y*bodyA.getMass(), bodyA.getPosition().x, bodyA.getPosition().y, true);
		bodyB.applyLinearImpulse(-bodyB.getLinearVelocity().x*bodyB.getMass(), -bodyB.getLinearVelocity().y*bodyB.getMass(), bodyB.getPosition().x, bodyB.getPosition().y, true);

		for (final EnemyComponent enemyCmp : stoEnemy) {
			enemyCmp.stop = false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	void reset() {
		hasPlayer = false;
		hasGameObj = false;
		hasItem = false;
		hasEnemy = false;
		hasWeapon = false;
		hasDoor = false;
		hasBoss = false;
		hasDamageArea = false;
		hasGround = false;
	}
	
	public interface CollisionListener{
		void playerCollision(final Entity player, final Entity gameObj);
		void weaponCollision(final Entity weapon, final Entity gameObj);
		void playerVSEnemy(final Entity player, final Entity enemy);
		void weaponVSEnemy(final Entity weapon, final Entity enemy);
		void playerVSItem(final Entity player, final Entity item);
		void playerVSDoor(final Entity player, final Entity door);
		void playerVSDamageArea(final Entity player, final Entity damageArea);
		void damageAreaVSEnemy(final Entity damageArea, final Entity enemy);
		void damageAreaVSGround(final Entity damageArea);
		void damageAreaVSWeapon(final Entity damageArea);
	}
}
