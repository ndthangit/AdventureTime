package com.mygdx.game.entity;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.CoreGame;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.entity.system.*;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.map.GameObject;
import com.mygdx.game.view.AnimationType;

import static com.mygdx.game.items.weapon.WeaponType.BIG_SWORD;

public class ECSEngine extends PooledEngine{
	
	public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
	public static final ComponentMapper<Box2DComponent> box2dCmpMapper = ComponentMapper.getFor(Box2DComponent.class);
	public static final ComponentMapper<AnimationComponent> aniCmpMapper = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<GameObjectComponent> gameObjCmpMapper = ComponentMapper.getFor(GameObjectComponent.class);
	public static final ComponentMapper<WeaponComponent> weaponCmpMapper = ComponentMapper.getFor(WeaponComponent.class);


	private final World world;

	private final Vector2 localPosition;
	private final Vector2 posBeforeRotation;
	private final Vector2 posAfterRotation;
	
	public ECSEngine(CoreGame game) {
		super();
		
		world = game.getWorld();

		localPosition = new Vector2();
		posBeforeRotation = new Vector2();
		posAfterRotation = new Vector2();
		
		this.addSystem(new PlayerMovementSystem(game));
		this.addSystem(new PlayerCameraSystem(game));
		this.addSystem(new AnimationSystem(game));
		this.addSystem(new PlayerAnimationSystem(game));
		this.addSystem(new PlayerCollisionSystem(game));
		this.addSystem(new PlayerAttackSystem(game));
	}
	
	
	public void createPlayer(final Vector2 playerSpawnLocation, final float width, final float height) {
		final Entity player = this.createEntity();
		
		final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
		playerComponent.maxLife = 12;
		playerComponent.life = playerComponent.maxLife;
		playerComponent.speed.set(6, 6);
		player.add(playerComponent);

		CoreGame.resetBodiesAndFixtureDefinition();
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.position.set(playerSpawnLocation.x, playerSpawnLocation.y);
		CoreGame.BODY_DEF.fixedRotation = true;
		CoreGame.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(player);
		box2DComponent.width = width;
		box2DComponent.height = height;
		box2DComponent.renderPosition = box2DComponent.body.getPosition();
//		box2DComponent.renderPosition.set(box2DComponent.body.getPosition());

		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(width * 0.5f, height * 0.5f);
		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_PLAYER;
		CoreGame.FIXTURE_DEF.filter.maskBits = CoreGame.BIT_GROUND | CoreGame.BIT_GAME_OBJECT;
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		
		pShape.dispose();
		player.add(box2DComponent);
		
		//animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.aniType = AnimationType.HERO_DOWN;
		animationComponent.width = 16 * CoreGame.UNIT_SCALE;
		animationComponent.height = 16 * CoreGame.UNIT_SCALE;
		player.add(animationComponent);
		this.addEntity(player);
		
	}

	public void createGameObject(final GameObject gameObject) {
		final Entity gameObjEntity = this.createEntity();

		final GameObjectComponent gameObjectComponent = this.createComponent(GameObjectComponent.class);
		gameObjectComponent.animationIndex = gameObject.getAnimationIndex();
		gameObjectComponent.type = gameObject.getType();
		gameObjEntity.add(gameObjectComponent);


		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.aniType = null;
		animationComponent.width = gameObject.getWidth();
		animationComponent.height = gameObject.getHeight();
		gameObjEntity.add(animationComponent);

		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = gameObject.getWidth() / 2;
		final float halfH = gameObject.getHeight() / 2;
		final float angleRad = -gameObject.getRotDegree() * MathUtils.degreesToRadians;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.StaticBody;
		CoreGame.BODY_DEF.position.set(gameObject.getPosition().x + halfW, gameObject.getPosition().y + halfH);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(gameObjEntity);
		box2DComponent.width = gameObject.getWidth();
		box2DComponent.height = gameObject.getHeight();


		// save position before rotation - Tiled is rotated around the bottom left corner instead of the center of a Tile

		localPosition.set(-halfW, -halfH);
		posBeforeRotation.set(box2DComponent.body.getWorldPoint(localPosition));
		// rotate body
		box2DComponent.body.setTransform(box2DComponent.body.getPosition(), angleRad);
		// get position after rotation
		posAfterRotation.set(box2DComponent.body.getWorldPoint(localPosition));
		//adjust position to its original value before the rotation
		box2DComponent.body.setTransform(box2DComponent.body.getPosition().add(posBeforeRotation).sub(posAfterRotation), angleRad);
//		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x - animationComponent.width * 1f, box2DComponent.body.getPosition().y - animationComponent.height * 1f);
		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x, box2DComponent.body.getPosition().y);

		//animation component
		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_GAME_OBJECT;
		CoreGame.FIXTURE_DEF.filter.maskBits = CoreGame.BIT_PLAYER;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		gameObjEntity.add(box2DComponent);

		this.addEntity(gameObjEntity);
	}

	public void createPlayerWeapon(final Weapon weapon) {
		final Entity weaponEntity = this.createEntity();

		final WeaponComponent weaponComponent = this.createComponent(WeaponComponent.class);
		weaponComponent.type = weapon.type;
		weaponComponent.direction = weapon.direction;
		weaponEntity.add(weaponComponent);


		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.aniType = null;
		animationComponent.width = weapon.getWidth();
		animationComponent.height = weapon.getHeight();
		weaponEntity.add(animationComponent);

		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = weapon.getWidth() / 2;
		final float halfH = weapon.getHeight() / 2;
		final float angleRad = -weapon.getDirection() * MathUtils.degreesToRadians * 90;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
		CoreGame.BODY_DEF.position.set(weapon.getPosition().x + weapon.posDirection[weapon.direction].x , weapon.getPosition().y + weapon.posDirection[weapon.direction].y);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(weaponEntity);
		box2DComponent.width = weapon.getWidth();
		box2DComponent.height = weapon.getHeight();


		// save position before rotation - Tiled is rotated around the bottom left corner instead of the center of a Tile

		localPosition.set(0, 0);
		posBeforeRotation.set(box2DComponent.body.getWorldPoint(localPosition));
		// rotate body
		box2DComponent.body.setTransform(box2DComponent.body.getPosition(), angleRad);
		// get position after rotation
		posAfterRotation.set(box2DComponent.body.getWorldPoint(localPosition));
//		adjust position to its original value before the rotation
		box2DComponent.body.setTransform(box2DComponent.body.getPosition().add(posBeforeRotation).sub(posAfterRotation), angleRad);
//		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x - animationComponent.width * 1f, box2DComponent.body.getPosition().y - animationComponent.height * 1f);
		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x, box2DComponent.body.getPosition().y);

		//animation component
		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_WEAPON;
		CoreGame.FIXTURE_DEF.filter.maskBits = CoreGame.BIT_GAME_OBJECT;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		weaponEntity.add(box2DComponent);

		this.addEntity(weaponEntity);
	}
}
