package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.map.MapManager;

import static com.mygdx.game.CoreGame.UNIT_SCALE;

public class PlayerCameraSystem extends IteratingSystem{
	private final OrthographicCamera gameCamera;
	private CoreGame game;

	public PlayerCameraSystem(final CoreGame game) {
		super(Family.all(PlayerComponent.class, Box2DComponent.class).get());
		gameCamera = game.getGameCamera();
        this.game = game;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		float width = game.getMapManager().getCurrentMapType().getWidth();
		float height = game.getMapManager().getCurrentMapType().getHeight();
		Vector2 position = ECSEngine.box2dCmpMapper.get(entity).body.getPosition();
//		gameCamera.position.set(ECSEngine.box2dCmpMapper.get(entity).renderPosition, 0);
		float x = position.x <= gameCamera.viewportWidth/2? gameCamera.viewportWidth/2 : position.x >= width - gameCamera.viewportWidth/2 ? width - gameCamera.viewportWidth/2 : position.x;
		float y = position.y <= gameCamera.viewportHeight/2? gameCamera.viewportHeight/2 : position.y >= height - gameCamera.viewportHeight/2 ? height - gameCamera.viewportHeight/2 : position.y;
		gameCamera.position.set(x, y, 0);

	}

}
