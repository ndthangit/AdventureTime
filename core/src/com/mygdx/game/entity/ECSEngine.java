package com.mygdx.game.entity;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.boss.Boss;
import com.mygdx.game.character.enemy.Enemy;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.effect.Effect;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.entity.system.*;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.food.Food;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.map.GameObject;
import com.mygdx.game.view.AnimationType;

import static com.mygdx.game.CoreGame.UNIT_SCALE;
import static com.mygdx.game.view.DirectionType.DOWN;

public class ECSEngine extends PooledEngine{

	public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
	public static final ComponentMapper<Box2DComponent> box2dCmpMapper = ComponentMapper.getFor(Box2DComponent.class);
	public static final ComponentMapper<AnimationComponent> aniCmpMapper = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<GameObjectComponent> gameObjCmpMapper = ComponentMapper.getFor(GameObjectComponent.class);
	public static final ComponentMapper<WeaponComponent> weaponCmpMapper = ComponentMapper.getFor(WeaponComponent.class);
	public static final ComponentMapper<EnemyComponent> enemyCmpMapper = ComponentMapper.getFor(EnemyComponent.class);
	public static final ComponentMapper<EffectComponent> effectCmpMapper = ComponentMapper.getFor(EffectComponent.class);
	public static final ComponentMapper<ItemComponent> itemCmpMapper = ComponentMapper.getFor(ItemComponent.class);
	public static final ComponentMapper<HideLayerComponent> hideLayerCmpMapper = ComponentMapper.getFor(HideLayerComponent.class);
	public static final ComponentMapper<DoorLayerComponent> doorLayerCmpMapper = ComponentMapper.getFor(DoorLayerComponent.class);
	public static final ComponentMapper<BossComponent> bossCmpMapper = ComponentMapper.getFor(BossComponent.class);

	private final World world;

	private final Vector2 localPosition;
	private final Vector2 posBeforeRotation;
	private final Vector2 posAfterRotation;

	private final Array<Item> itemArray;
	private Entity playerEntity;

	public ECSEngine(CoreGame game) {
		super();

		world = game.getWorld();

		localPosition = new Vector2();
		posBeforeRotation = new Vector2();
		posAfterRotation = new Vector2();

		itemArray = new Array<Item>();

		// them system chon engine
		this.addSystem(new PlayerMovementSystem(game));
		this.addSystem(new PlayerCameraSystem(game));
		this.addSystem(new AnimationSystem(game));
		this.addSystem(new PlayerAnimationSystem(game));
		this.addSystem(new CollisionSystem(game));
		this.addSystem(new PlayerAttackSystem(game));
		this.addSystem(new EffectSystem(game));
		this.addSystem(new EnemyMovementSystem(game));
	}

	public Array<Item> getItemArray() {
		return itemArray;
	}

	public Entity getPlayerEntity () {
		return playerEntity;
	}
	
	public void setPlayerEntity(Entity playerEntity) {
		this.playerEntity = playerEntity;
	}

	public void createPlayer(final Vector2 playerSpawnLocation, PlayerType type, final float width, final float height, Weapon weapon) {
		final Entity player = this.createEntity();

		// player component
		final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
		playerComponent.maxLife = type.getHealth();
		playerComponent.life = playerComponent.maxLife;
		playerComponent.speed.set(type.getSpeed());
		playerComponent.aniType = PlayerType.BLACK_NINJA_MAGE;
		playerComponent.direction = DOWN;
		playerComponent.weapon = weapon;
		player.add(playerComponent);

		// box2d component
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
		CoreGame.FIXTURE_DEF.filter.maskBits = -1;
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);

		pShape.dispose();
		player.add(box2DComponent);

		//animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.width = 16 * UNIT_SCALE;
		animationComponent.height = 16 * UNIT_SCALE;
		animationComponent.aniType = AnimationType.DOWN;
		animationComponent.isSquare = true;
		animationComponent.path = playerComponent.aniType.getAtlasPath();
		player.add(animationComponent);
		this.addEntity(player);
		playerEntity = player;
	}

	public void createGameObject(final GameObject gameObject) {
		final Entity gameObjEntity = this.createEntity();

		// gameobject component
		final GameObjectComponent gameObjectComponent = this.createComponent(GameObjectComponent.class);
		gameObjectComponent.animationIndex = gameObject.getAnimationIndex();
		gameObjectComponent.type = gameObject.getType();
		gameObjEntity.add(gameObjectComponent);

		// animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.width = gameObject.getWidth();
		animationComponent.height = gameObject.getHeight();

		gameObjEntity.add(animationComponent);

		// box 2D component
		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = gameObject.getWidth() / 2;
		final float halfH = gameObject.getHeight() / 2;
		final float angleRad = -gameObject.getRotDegree() * MathUtils.degreesToRadians;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.KinematicBody;
		CoreGame.BODY_DEF.position.set(gameObject.getPosition().x + halfW, gameObject.getPosition().y + halfH);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(gameObjEntity);
		box2DComponent.width = gameObject.getWidth();
		box2DComponent.height = gameObject.getHeight();
		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x, box2DComponent.body.getPosition().y);

		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_GAME_OBJECT;
		CoreGame.FIXTURE_DEF.filter.maskBits = CoreGame.BIT_PLAYER|CoreGame.BIT_WEAPON;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		gameObjEntity.add(box2DComponent);

		this.addEntity(gameObjEntity);
	}

	public void createHideLayer(final GameObject gameObject) {
		final Entity gameObjEntity = this.createEntity();

		// hidelayer component
		final HideLayerComponent hideLayerComponent = this.createComponent(HideLayerComponent.class);
		hideLayerComponent.animationIndex = gameObject.getAnimationIndex();
		hideLayerComponent.position = new Vector2(gameObject.getPosition().x + gameObject.getWidth()/2, gameObject.getPosition().y + gameObject.getHeight()/2);

		gameObjEntity.add(hideLayerComponent);

		// animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.width = gameObject.getWidth();
		animationComponent.height = gameObject.getHeight();
		gameObjEntity.add(animationComponent);
		this.addEntity(gameObjEntity);
	}

	public void createDoorLayer(final GameObject gameObject) {
		final Entity gameObjEntity = this.createEntity();

		// door layer component
		final DoorLayerComponent doorLayerComponent = this.createComponent(DoorLayerComponent.class);
		doorLayerComponent.animationIndex = gameObject.getAnimationIndex();
		doorLayerComponent.name = gameObject.getName();
		gameObjEntity.add(doorLayerComponent);

		// box 2D component
		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = gameObject.getWidth() / 2;
		final float halfH = gameObject.getHeight() / 2;
		final float angleRad = -gameObject.getRotDegree() * MathUtils.degreesToRadians;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.KinematicBody;
		CoreGame.BODY_DEF.position.set(gameObject.getPosition().x + halfW, gameObject.getPosition().y + halfH);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(gameObjEntity);
		box2DComponent.width = gameObject.getWidth();
		box2DComponent.height = gameObject.getHeight();
		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x, box2DComponent.body.getPosition().y);

		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_DOOR;
		CoreGame.FIXTURE_DEF.filter.maskBits = CoreGame.BIT_PLAYER;
		CoreGame.FIXTURE_DEF.isSensor = true;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		gameObjEntity.add(box2DComponent);

		this.addEntity(gameObjEntity);
	}

	public void createBoss(final Boss boss) {
		final Entity bossEnity = this.createEntity();

		// boss component
		final BossComponent bossComponent = this.createComponent(BossComponent.class);
		bossComponent.maxLife = boss.getBossType().getHealth();
		bossComponent.life = bossComponent.maxLife;
		bossComponent.type = boss.getBossType();
		bossComponent.speed.set(boss.getBossType().getSpeed(), boss.getBossType().getSpeed());
		bossComponent.attack = boss.getBossType().getDamage();
		bossEnity.add(bossComponent);

		// animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.width = boss.getWidth() * UNIT_SCALE;
		animationComponent.height = boss.getHeight()* UNIT_SCALE;
		animationComponent.aniType = AnimationType.DOWN;
		animationComponent.isSquare = false;
		animationComponent.path = boss.getBossType().getAtlasPath();
		bossEnity.add(animationComponent);

		// box 2D component
		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = 40 * UNIT_SCALE / 2;
		final float halfH = 40 * UNIT_SCALE / 2;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
		CoreGame.BODY_DEF.position.set(boss.getPosition().x + halfW, boss.getPosition().y + halfH);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(bossEnity);
		box2DComponent.width = boss.getWidth() * UNIT_SCALE;
		box2DComponent.height = boss.getHeight() * UNIT_SCALE;
		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x, box2DComponent.body.getPosition().y);

		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_BOSS;
		CoreGame.FIXTURE_DEF.filter.maskBits = -1;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		bossEnity.add(box2DComponent);
		this.addEntity(bossEnity);
	}

	public void createPlayerWeapon(final Weapon weapon) {
		final Entity weaponEntity = this.createEntity();
		// weapon component
		final WeaponComponent weaponComponent = this.createComponent(WeaponComponent.class);
		weaponComponent.type = weapon.type;
		weaponComponent.direction = weapon.direction;
		weaponComponent.attack = weapon.type.getAttack();
		weaponEntity.add(weaponComponent);

		// animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.width = weapon.getWidth();
		animationComponent.height = weapon.getHeight();
		weaponEntity.add(animationComponent);

		// box 2D component
		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = weapon.getWidth() / 2;
		final float halfH = weapon.getHeight() / 2;
		final float angleRad = -weapon.getDirection().getCode() * MathUtils.degreesToRadians * 90;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
		CoreGame.BODY_DEF.position.set(weapon.getPosition().x + weapon.posDirection[weapon.direction.getCode()].x , weapon.getPosition().y + weapon.posDirection[weapon.direction.getCode()].y);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(weaponEntity);
		box2DComponent.width = weapon.getWidth();
		box2DComponent.height = weapon.getHeight();
		Vector2 position = new Vector2(weapon.getPosition().x + weapon.effDirection[weapon.direction.getCode()].x , weapon.getPosition().y + weapon.effDirection[weapon.direction.getCode()].y);
		weapon.effect.setPosition(position);

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
		CoreGame.FIXTURE_DEF.filter.maskBits = -1;
		CoreGame.FIXTURE_DEF.isSensor = true;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		weaponEntity.add(box2DComponent);

		this.addEntity(weaponEntity);
	}

	public void createEnemy(final Enemy enemy) {
		final Entity enemyEnity = this.createEntity();

		// enemy component
		final EnemyComponent enemyComponent = this.createComponent(EnemyComponent.class);
		enemyComponent.maxLife = enemy.getType().getMaxLife();
		enemyComponent.life = enemyComponent.maxLife;
		enemyComponent.type = enemy.getType();
		enemyComponent.speed.set(enemy.getType().getSpeed(), enemy.getType().getSpeed());
		enemyComponent.attack = enemy.getType().getAttack();
		// Lưu vị trí ban đầu
		enemyComponent.startPosition.x = enemy.getPosition().x + enemy.getWidth() * UNIT_SCALE / 2;
		enemyComponent.startPosition.y = enemy.getPosition().y + enemy.getHeight() * UNIT_SCALE / 2;
		enemyEnity.add(enemyComponent);
		// animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.width = enemy.getWidth() * UNIT_SCALE;
		animationComponent.height = enemy.getHeight()* UNIT_SCALE;
		animationComponent.aniType = AnimationType.DOWN;
		animationComponent.path = enemyComponent.type.getAtlasPath();
		animationComponent.isSquare = true;
		enemyEnity.add(animationComponent);

		// box 2D component
		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = enemy.getWidth() * UNIT_SCALE / 2;
		final float halfH = enemy.getHeight() * UNIT_SCALE / 2;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
		CoreGame.BODY_DEF.position.set(enemy.getPosition().x + halfW, enemy.getPosition().y + halfH);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(enemyEnity);
		box2DComponent.width = enemy.getWidth() * UNIT_SCALE;
		box2DComponent.height = enemy.getHeight() * UNIT_SCALE;
		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x, box2DComponent.body.getPosition().y);

		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_ENEMY;
		CoreGame.FIXTURE_DEF.filter.maskBits = -1;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		enemyEnity.add(box2DComponent);
		this.addEntity(enemyEnity);
	}

	public void createEffect(final Effect effect) {
		final Entity effectEntity = this.createEntity();
		// effect component
		EffectComponent effectComponent = this.createComponent(EffectComponent.class);
		effectComponent.type = effect.getType();
		effectComponent.aniTime = 0;
		effectComponent.height = effect.getType().getHeight() * UNIT_SCALE;
		effectComponent.width = effect.getType().getWidth() * UNIT_SCALE;
		effectComponent.path = effect.getType().getAtlasPath();
		effectComponent.position = effect.getPosition();
		effectComponent.direction = effect.getDirection();
		effectEntity.add(effectComponent);
		this.addEntity(effectEntity);
	}

	public void createItem(final Item item) {
		final Entity itemEntity = this.createEntity();

		//item component
		ItemComponent itemComponent = this.createComponent(ItemComponent.class);
		itemComponent.height = item.height;
		itemComponent.width = item.width;
		itemComponent.item = item;
		itemEntity.add(itemComponent);

		//box2d component
		CoreGame.resetBodiesAndFixtureDefinition();
		final float halfW = item.width / 2;
		final float halfH = item.height / 2;
		final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
		CoreGame.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
		CoreGame.BODY_DEF.position.set(item.position.x + halfW, item.position.y + halfH);
		box2DComponent.body = world.createBody(CoreGame.BODY_DEF);
		box2DComponent.body.setUserData(itemEntity);
		box2DComponent.width = item.width;
		box2DComponent.height = item.height;
		box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x, box2DComponent.body.getPosition().y);

		CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_ITEM;
		CoreGame.FIXTURE_DEF.filter.maskBits = CoreGame.BIT_PLAYER;
		CoreGame.FIXTURE_DEF.isSensor = true;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(halfW, halfH);
		CoreGame.FIXTURE_DEF.shape = pShape;
		box2DComponent.body.createFixture(CoreGame.FIXTURE_DEF);
		pShape.dispose();
		itemEntity.add(box2DComponent);

		this.addEntity(itemEntity);
	}
}
