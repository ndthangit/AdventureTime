package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.mygdx.game.CoreGame.BIT_GAME_OBJECT;
import static com.mygdx.game.CoreGame.BIT_PLAYER;
import static com.mygdx.game.CoreGame.BIT_WEAPON;

public class WorldContactListener implements ContactListener {
	private final Array<PlayerCollisionListener> listeners;
	private boolean hasPlayer;
	private boolean hasGameObj;
	private boolean hasWeapon;
    public WorldContactListener() {
        this.listeners = new Array<PlayerCollisionListener>();
    }

	public void addPlayerCollisionListener(PlayerCollisionListener listener) {
		listeners.add(listener);
	}
    @Override
	public void beginContact(Contact contact) {
		final Entity player;
		final Entity gameObj;
		final Entity weapon;
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
		
		if (hasPlayer && hasGameObj) {
			for (final PlayerCollisionListener listener : listeners) {
				listener.playerCollision(player, gameObj);
			}
		}
//		if (hasWeapon && hasGameObj) {
//			for (final PlayerCollisionListener listener : listeners) {
//				listener.weaponCollision(weapon, gameObj);
//			}
//		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	public interface PlayerCollisionListener{
		void playerCollision(final Entity player, final Entity gameObj);
		void weaponCollision(final Entity weapon, final Entity gameObj);
	}
}
