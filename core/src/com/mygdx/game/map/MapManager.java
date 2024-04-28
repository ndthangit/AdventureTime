package com.mygdx.game.map;

import java.util.EnumMap;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.enemy.Enemy;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.effect.Effect;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.items.weapon.WeaponType;
import com.mygdx.game.view.DirectionType;

public class  MapManager {
	public static final String TAG = MapManager.class.getSimpleName();

	private final ECSEngine ecsEngine;
	
	private final World world;
	private final Array<Body> bodies;
	private final AssetManager assetManager;
	
	private MapType currentMapType, nextMapType;
	private Map currentMap;
	private final EnumMap<MapType, Map>mapCache;
	private final Array<MapListener> listeners;
	private Array<Entity> entityToRemove;

	public MapManager(final CoreGame game) {
		currentMapType = null;
		currentMap = null;
		world = game.getWorld();
		ecsEngine = game.getEcsEngine();
		assetManager = game.getAssetManager();
		bodies = new Array<Body>();
		mapCache = new EnumMap<MapType, Map>(MapType.class);
		listeners = new Array<MapListener>();
		entityToRemove = new Array<Entity>();;
	}
	
	public void addMapListener(final MapListener listener) {
		listeners.add(listener);
	}
	
	public void destroyMap() {
		// clean map entities and body
		world.getBodies(bodies);
		destroyCollisionArea();
		destroyGameObjects();
		destroyEnemy();
		destroyPlayer();
	}
	
	public void setMap() {
		if (currentMapType == nextMapType) {
			return;
		}
		// chuyen sang destroymap
//		if (currentMap != null) {
//			// clean map entities and body
//			world.getBodies(bodies);
//			destroyCollisionArea();
//			destroyGameObjects();
//			destroyEnemy();
//		}
		
		
		// set new map
		Gdx.app.debug(TAG, "Changing to map " + nextMapType);
		currentMap = mapCache.get(nextMapType);
		currentMapType = nextMapType;
		final TiledMap tiledMap = assetManager.get(nextMapType.getFilePath(), TiledMap.class);
		if (currentMap == null) {
			Gdx.app.debug(TAG, "Creating new map of type" + nextMapType);
			currentMap = new Map(tiledMap);
			mapCache.put(nextMapType, currentMap);
		}
		
		// create map entities/bodies
		spawnPlayer();
		spawnCollisionAreas();
		spawnGameObjects();
		spawnEnemy();
		
		for (final MapListener listener: listeners) {
			listener.mapChange(currentMap);
		}
	}

	private void spawnPlayer() {
		Weapon weapon = new Weapon(WeaponType.BIG_SWORD,new Effect(EffectType.SLASHCURVED, this.getCurrentMap().getStartPosition(), DirectionType.DOWN), this.getCurrentMap().getStartPosition(), DirectionType.DOWN);

		ecsEngine.createPlayer(this.getCurrentMap().getStartPosition(), PlayerType.BLACK_NINJA_MAGE, 0.75f, 0.75f, weapon);
	}

	private void destroyPlayer() {
		for (Entity player :  ecsEngine.getEntitiesFor(Family.all(PlayerComponent.class, Box2DComponent.class, AnimationComponent.class).get())){
			ecsEngine.removeEntity(player);
		}
	}

	private void spawnGameObjects() {
		for (final GameObject gameObject: currentMap.getGameObjects()) {
			ecsEngine.createGameObject(gameObject);
		}
	}

	private void destroyGameObjects() {
		for(final Entity entity: ecsEngine.getEntitiesFor(Family.all(GameObjectComponent.class, Box2DComponent.class, AnimationComponent.class).get())) {
			if (ECSEngine.gameObjCmpMapper.get(entity) != null) {
				entityToRemove.add(entity);
			}
		}
		for (final Entity entity: entityToRemove) {
			ecsEngine.removeEntity(entity);
		}
		entityToRemove.clear();
	}

	private void spawnEnemy() {
		for (final Enemy enemy: currentMap.getEnemies()) {
			ecsEngine.createEnemy(enemy);
		}
	}

	private void destroyEnemy() {
		for(final Entity entity: ecsEngine.getEntitiesFor(Family.all(EnemyComponent.class, Box2DComponent.class, AnimationComponent.class).get())) {
			if (ECSEngine.enemyCmpMapper.get(entity) != null) {
				entityToRemove.add(entity);
			}
		}
		for (final Entity entity: entityToRemove) {
			ecsEngine.removeEntity(entity);
		}
		entityToRemove.clear();
	}

	private void spawnCollisionAreas() {
		for (final CollisionArea collisionArea: currentMap.getCollisionAreas()) {
			CoreGame.resetBodiesAndFixtureDefinition();
			CoreGame.BODY_DEF.position.set(collisionArea.getX(), collisionArea.getY());
			Body body = world.createBody(CoreGame.BODY_DEF);
			
			CoreGame.FIXTURE_DEF.filter.categoryBits = CoreGame.BIT_GROUND;
			CoreGame.FIXTURE_DEF.filter.maskBits = -1;
			ChainShape cShape = new ChainShape();
			cShape.createLoop(collisionArea.getVertices());
			CoreGame.FIXTURE_DEF.shape = cShape;
			body.createFixture(CoreGame.FIXTURE_DEF);	
			body.setUserData("GROUND");
			cShape.dispose();
		}
	}
	
	

	private void destroyCollisionArea() {
		for (final Body body: bodies) {
			if ("GROUND".equals(body.getUserData())) {
				world.destroyBody(body);
			}
		}
		
	}
	
	public Map getCurrentMap() {
		return currentMap;
	}

	public MapType getCurrentMapType() {
		return currentMapType;
	}

	public MapType getNextMapType() {
		return nextMapType;
	}

	public void setNextMapType(MapType nextMapType) {
		this.nextMapType = nextMapType;
	}

}
