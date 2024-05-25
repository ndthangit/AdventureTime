package com.mygdx.game.map;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.boss.Boss;
import com.mygdx.game.character.enemy.Enemy;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.effect.Effect;
import com.mygdx.game.entity.ECSEngine;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.items.weapon.WeaponType;
import com.mygdx.game.view.DirectionType;

public class MapManager {
	public static final String TAG = MapManager.class.getSimpleName();

	private final ECSEngine ecsEngine;
	private final CoreGame game;

	private final World world;
	private final Array<Body> bodies;
	private final AssetManager assetManager;
	
	private MapType currentMapType, nextMapType, previousMapType;
	private Map currentMap;
	private final EnumMap<MapType, Map>mapCache;
	private final Array<MapListener> listeners;
	private Array<Entity> entityToRemove;

	public MapManager(final CoreGame game) {
		currentMapType = null;
		currentMap = null;
		world = game.getWorld();
		ecsEngine = game.getEcsEngine();
		this.game = game;
		assetManager = game.getAssetManager();
		bodies = new Array<Body>();
		mapCache = new EnumMap<MapType, Map>(MapType.class);
		listeners = new Array<MapListener>();
		entityToRemove = new Array<Entity>();
	}
	
	public void addMapListener(final MapListener listener) {
		listeners.add(listener);
	}
	
	public void destroyMap() {
		// clean map entities and body
		world.getBodies(bodies);
		destroyCollisionArea();
		destroyObjects();
	}
	
	public void setMap() {
		if (currentMapType == nextMapType) {
			return;
		}
		destroyMap();
		// set new map
		Gdx.app.debug(TAG, "Changing to map " + nextMapType);
		currentMap = mapCache.get(nextMapType);
		previousMapType = currentMapType;
		currentMapType = nextMapType;
		final TiledMap tiledMap = assetManager.get(nextMapType.getFilePath(), TiledMap.class);
		if (currentMap == null) {
			Gdx.app.debug(TAG, "Creating new map of type" + nextMapType);
			currentMap = new Map(tiledMap);
			mapCache.put(currentMapType, currentMap);
		}
		
		// create map entities/bodies
		spawnDoorLayer();
		spawnPlayer();
		spawnCollisionAreas();
		spawnGameObjects();
		spawnEnemy();
		spawnBoss();
		spawnHideLayer();

		for (final MapListener listener: listeners) {
			listener.mapChange(currentMap);
		}
	}

	public void spawnPlayer() {
		if (ecsEngine.getPlayerEntity() == null) {
			Weapon weapon = new Weapon(WeaponType.SWORD2,new Effect(WeaponType.SWORD2.getEffect(),CoreGame.BIT_WEAPON, Vector2.Zero, DirectionType.DOWN), Vector2.Zero, DirectionType.DOWN);
			ecsEngine.createPlayer(this.getCurrentMap().getStartPosition("START"), PlayerType.BLACK_NINJA_MAGE, 0.75f, 0.75f, weapon);
		}
		else {
//			ECSEngine.box2dCmpMapper.get(ecsEngine.getPlayerEntity()).renderPosition = this.currentMap.getStartPosition(previousMapType.toString());
			ECSEngine.box2dCmpMapper.get(ecsEngine.getPlayerEntity()).body.setTransform(this.currentMap.getStartPosition(previousMapType.toString()), 0);
		}
	}

	public void destroyPlayer() {

		ecsEngine.removeEntity(ecsEngine.getPlayerEntity());
		ecsEngine.setPlayerEntity(null);
		currentMapType = null;
		setNextMapType(MapType.DOJO);
	}

	private void spawnGameObjects() {
		for (final GameObject gameObject: currentMap.getGameObjects()) {
			if (gameObject.getType() == GameObjectType.CHEST && gameObject.isUsed()) continue;
			ecsEngine.createGameObject(gameObject);
		}

	}

	private void spawnHideLayer() {
		for (final GameObject hide : currentMap.getHideObject()) {
			ecsEngine.createHideLayer(hide);
		}
	}

	private void spawnDoorLayer() {
//		}
		for (final GameObject door : currentMap.getDoorObject()) {
			ecsEngine.createDoorLayer(door);
		}
	}

	private void spawnEnemy() {
		for (final Enemy enemy: currentMap.getEnemies()) {
			ecsEngine.createEnemy(enemy);
		}
	}

	private void spawnBoss() {
		if (currentMap.isBossKilled()) {return;}
		for (final Boss boss: currentMap.getBosses()) {
			if (boss.isDead()) continue;
			ecsEngine.createBoss(boss);
			if (game.getGameUI() == null) continue;
			game.getGameUI().createLifeBar(boss);
		}
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

	private void destroyObjects() {
		for(final Entity entity: ecsEngine.getEntities()) {
			if (ECSEngine.playerCmpMapper.get(entity) == null) {
				entityToRemove.add(entity);
			}
		}
		for (final Entity entity: entityToRemove) {
			ecsEngine.removeEntity(entity);
		}
		entityToRemove.clear();
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

	public EnumMap<MapType, Map> getMapCache() {
		return mapCache;
	}
}
