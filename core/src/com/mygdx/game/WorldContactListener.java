package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.CoreGame.BIT_GAME_OBJECT;
import static com.mygdx.game.CoreGame.BIT_PLAYER;

public class WorldContactListener implements ContactListener {

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
			player = (Entity) bodyA.getUserData();
		}
		else {
			return;
		}

		if ((catFixA & BIT_GAME_OBJECT) == BIT_GAME_OBJECT) {
			gameObj = (Entity) bodyA.getUserData();
		}
		else if((catFixB & BIT_GAME_OBJECT) == BIT_GAME_OBJECT){
			gameObj = (Entity) bodyA.getUserData();
		}
		else {
			return;
		}

		Gdx.app.debug("CollisionDebug", "Player collisides with game object");
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

}
