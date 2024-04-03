package com.mygdx.game.entity;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.entity.system.AnimationSystem;
import com.mygdx.game.entity.system.PlayerAnimationSystem;
import com.mygdx.game.entity.system.PlayerCameraSystem;
import com.mygdx.game.entity.system.PlayerMovementSystem;
import com.mygdx.game.view.AnimationType;

public class ECSEngine extends PooledEngine{
	
	public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
	public static final ComponentMapper<Box2DComponent> box2dCmpMapper = ComponentMapper.getFor(Box2DComponent.class);
	public static final ComponentMapper<AnimationComponent> aniCmpMapper = ComponentMapper.getFor(AnimationComponent.class);
	
	private final World world;
	private final BodyDef bodyDef;
	private final FixtureDef fixtureDef;
	
	
	
	public ECSEngine(CoreGame game) {
		super();
		
		world = game.getWorld();
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		
		this.addSystem(new PlayerMovementSystem(game));
		this.addSystem(new PlayerCameraSystem(game));
		this.addSystem(new AnimationSystem(game));
		this.addSystem(new PlayerAnimationSystem(game));
	}
	
	
	public void createPlayer(final Vector2 playerSpawnLocation, final float width, final float height) {
		final Entity player = this.createEntity();
		
		final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
		playerComponent.speed.set(6, 6);
		player.add(playerComponent);
		
		resetBodiesAndFixtureDefinition();
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		bodyDef.position.set(playerSpawnLocation.x, playerSpawnLocation.y);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		box2DComponent.body = world.createBody(bodyDef);
		box2DComponent.body.setUserData("PLAYER");
		box2DComponent.width = width;
		box2DComponent.height = height;
//		box2DComponent.renderPosition.set(box2DComponent.body.getPosition());
		box2DComponent.renderPosition = box2DComponent.body.getPosition();
		
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(width * 0.5f, height * 0.5f);
		fixtureDef.filter.categoryBits = CoreGame.BIT_PLAYER;
		fixtureDef.filter.maskBits = CoreGame.BIT_GROUND ;
		fixtureDef.shape = pShape;
		box2DComponent.body.createFixture(fixtureDef);	
		
		pShape.dispose();
		player.add(box2DComponent);
		
		//animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.aniType = AnimationType.HERO_DOWN;
		animationComponent.width = 12 * CoreGame.UNIT_SCALE;
		animationComponent.height = 12 * CoreGame.UNIT_SCALE;
		player.add(animationComponent);
		this.addEntity(player);
		
	}
	
	private void resetBodiesAndFixtureDefinition() {
		bodyDef.gravityScale = 1;
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.fixedRotation = false;
		
		fixtureDef.isSensor = false;
		fixtureDef.restitution = 0;
		fixtureDef.friction = 0;
		fixtureDef.density = 0;
		fixtureDef.filter.categoryBits = 0x0001;
		fixtureDef.filter.maskBits = -1;
		fixtureDef.shape = null;
		
	} 
}
