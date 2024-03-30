package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;

public class PlayerCameraSystem extends IteratingSystem{
	private final OrthographicCamera gameCamera;
	

	public PlayerCameraSystem(final CoreGame game) {
		super(Family.all(PlayerComponent.class, Box2DComponent.class).get());
		gameCamera = game.getGameCamera();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		gameCamera.position.set(ECSEngine.box2dCmpMapper.get(entity).body.getPosition(), 0);
		
	}

}
