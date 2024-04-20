package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.mygdx.game.CoreGame.*;

public class WorldContactListener implements ContactListener {
	private final Array<CollisionListener> listeners;
	private boolean hasPlayer;
	private boolean hasGameObj;
	private boolean hasWeapon;
	private boolean hasEnemy;
    public WorldContactListener() {

		this.listeners = new Array<CollisionListener>();
    }

	public void addPlayerCollisionListener(CollisionListener listener) {

		listeners.add(listener);
	}
    @Override
	public void beginContact(Contact contact) {
		final Entity player;
		final Entity gameObj;
		final Entity weapon;
		final Entity enemy;
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
			hasPlayer = false;
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
			hasGameObj = false;
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
			hasWeapon = false;
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
			hasEnemy = false;
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
		else if (hasWeapon && hasEnemy) {
			for (final CollisionListener listener : listeners) {
				listener.weaponVSEnemy(weapon, enemy);
			}
		}

	}

	@Override
	public void endContact(Contact contact) {
		final Body bodyA = contact.getFixtureA().getBody();
		final Body bodyB = contact.getFixtureB().getBody();
		bodyA.applyLinearImpulse(-bodyA.getLinearVelocity().x*bodyA.getMass(), -bodyA.getLinearVelocity().y*bodyA.getMass(), bodyA.getPosition().x, bodyA.getPosition().y, true);
		bodyB.applyLinearImpulse(-bodyB.getLinearVelocity().x*bodyB.getMass(), -bodyB.getLinearVelocity().y*bodyB.getMass(), bodyB.getPosition().x, bodyB.getPosition().y, true);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	public int checkEntity (int fixture) {
		return 0;
	}
	
	public interface CollisionListener{
		void playerCollision(final Entity player, final Entity gameObj);
		void weaponCollision(final Entity weapon, final Entity gameObj);
		void playerVSEnemy(final Entity player, final Entity enemy);
		void weaponVSEnemy(final Entity weapon, final Entity enemy);
	}
}
