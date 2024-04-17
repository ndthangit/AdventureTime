package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.mygdx.game.CoreGame.BIT_GAME_OBJECT;
import static com.mygdx.game.CoreGame.BIT_PLAYER;

public class WorldContactListener implements ContactListener {
	private final Array<PlayerCollisionListener> listeners;

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
		final Body bodyA = contact.getFixtureA().getBody();
		final Body bodyB = contact.getFixtureB().getBody();
		final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
		final int catFixB = contact.getFixtureB().getFilterData().categoryBits;

		if ((catFixA & BIT_PLAYER) == BIT_PLAYER) {
			player = (Entity) bodyA.getUserData();
		}
		else if((catFixB & BIT_PLAYER) == BIT_PLAYER){
			player = (Entity) bodyB.getUserData();
		}
		else {
			return;
		}

		if ((catFixA & BIT_GAME_OBJECT) == BIT_GAME_OBJECT) {
			gameObj = (Entity) bodyA.getUserData();
		}
		else if((catFixB & BIT_GAME_OBJECT) == BIT_GAME_OBJECT){
			gameObj = (Entity) bodyB.getUserData();
		}
		else {
			return;
		}

		for (final PlayerCollisionListener listener : listeners) {
			listener.playerCollision(player, gameObj);
		}
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
	}
}
