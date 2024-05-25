package com.mygdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.boss.Boss;
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.character.enemy.Enemy;
import com.mygdx.game.character.enemy.EnemyType;

public class Map {
	public static String TAG = Map.class.getSimpleName();
	
	private final TiledMap tiledMap;
	private final Array<CollisionArea>collisionAreas;
	private final ObjectMap<String, Vector2> startPosition;
	private final Array<GameObject> gameObject;
	private final Array<GameObject> hideObject;
	private final Array<GameObject> doorObject;
	private final Array<Enemy> enemies;
	private final Array<Boss> bosses;
	private final IntMap<Animation<Sprite>> mapAnimations;
	private boolean hasBoss;
	private boolean bossKilled;

	public Map(final TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		collisionAreas = new Array<CollisionArea>();
		
		parseCollisionLayer();
		startPosition = new ObjectMap<String, Vector2>();
		parsePlayerLayer();

		gameObject = new Array<GameObject>();
		hideObject = new Array<GameObject>();
		doorObject = new Array<GameObject>();
		bosses = new Array<Boss>();
		enemies = new Array<Enemy>();


		mapAnimations = new IntMap<Animation<Sprite>>();
		parseGameObjectLayer();
		parseEnemyLayer();
		parseBossLayer();
		parseHideLayer();
		parseDoorLayer();
	}

	private void parsePlayerLayer() {
		final MapLayer playerLayer = tiledMap.getLayers().get("playerStartPosition");
		if (playerLayer == null) {
			Gdx.app.debug(TAG, "There is no player start position");
			return;
		}
		final MapObjects objects = playerLayer.getObjects();
		Vector2 position;
		for (MapObject obj: objects) {

			if (obj instanceof RectangleMapObject) {
				final RectangleMapObject rectangleMapObject = (RectangleMapObject) obj;
				Rectangle rectangle = rectangleMapObject.getRectangle();
				position = new Vector2(rectangle.x * CoreGame.UNIT_SCALE, rectangle.y * CoreGame.UNIT_SCALE);
				startPosition.put(obj.getName(), position);
			}
		}
	}

	private void parseGameObjectLayer() {
		final MapLayer gameObjectsLayer = tiledMap.getLayers().get("gameObjects");
		if (gameObjectsLayer == null) {
			Gdx.app.debug(TAG, "There is no gameObjects layer");
			return;
		}
		for (final MapObject mapObj: gameObjectsLayer.getObjects()) {
			if (!(mapObj instanceof TiledMapTileMapObject)) {
				Gdx.app.debug(TAG, "GameObject of type " + mapObj + " is not supported");
				continue;
			}

			final TiledMapTileMapObject tiledMapObject = (TiledMapTileMapObject) mapObj;
			final MapProperties tiledMapObjProperties = tiledMapObject.getProperties();
			final MapProperties tileProperties = tiledMapObject.getTile().getProperties();
			final GameObjectType gameObjType;
			if (mapObj.getName() != null) {
				gameObjType = GameObjectType.valueOf(mapObj.getName());
			}
			else {
				Gdx.app.debug(TAG, "There is no gameObject type defined for tile" + tiledMapObject.getProperties().get("id",Integer.class));
				continue;
			}

			final int  animationIndex = tiledMapObject.getTile().getId();
			if (!createAnimation(animationIndex, tiledMapObject.getTile())) {
				Gdx.app.log(TAG, "Could not create animation for tile " + tiledMapObjProperties.get("id",Integer.class));
				continue;
			}

			final float width = tiledMapObjProperties.get("width", Float.class) * CoreGame.UNIT_SCALE ;
			final float height = tiledMapObjProperties.get("height", Float.class) * CoreGame.UNIT_SCALE;
			gameObject.add(new GameObject(gameObjType, new Vector2(tiledMapObject.getX()*CoreGame.UNIT_SCALE, tiledMapObject.getY()*CoreGame.UNIT_SCALE), width, height, tiledMapObject.getRotation(), animationIndex));
		}
	}



	private void parseHideLayer() {
		final MapLayer hideLayer = tiledMap.getLayers().get("hide");
		if (hideLayer == null) {
			Gdx.app.debug(TAG, "There is no hide layer");
			return;
		}
		for (final MapObject mapObj: hideLayer.getObjects()) {
			if (!(mapObj instanceof TiledMapTileMapObject)) {
				Gdx.app.debug(TAG, "GameObject of type " + mapObj + " is not supported");
				continue;
			}

			final TiledMapTileMapObject tiledMapObject = (TiledMapTileMapObject) mapObj;
			final MapProperties tiledMapObjProperties = tiledMapObject.getProperties();

			final int  animationIndex = tiledMapObject.getTile().getId();
			if (!createAnimation(animationIndex, tiledMapObject.getTile())) {
				Gdx.app.log(TAG, "Could not create animation for tile " + tiledMapObjProperties.get("id",Integer.class));
				continue;
			}

			final float width = tiledMapObjProperties.get("width", Float.class) * CoreGame.UNIT_SCALE ;
			final float height = tiledMapObjProperties.get("height", Float.class) * CoreGame.UNIT_SCALE;
			hideObject.add(new GameObject(GameObjectType.HIDE, new Vector2(tiledMapObject.getX()*CoreGame.UNIT_SCALE, tiledMapObject.getY()*CoreGame.UNIT_SCALE), width, height, tiledMapObject.getRotation(), animationIndex));
		}
	}

	private void parseDoorLayer() {
		final MapLayer doorLayer = tiledMap.getLayers().get("door");
		if (doorLayer == null) {
			Gdx.app.debug(TAG, "There is no hide layer");
			return;
		}
		for (final MapObject mapObj: doorLayer.getObjects()) {
			if (!(mapObj instanceof TiledMapTileMapObject)) {
				Gdx.app.debug(TAG, "GameObject of type " + mapObj + " is not supported");
				continue;
			}

			final TiledMapTileMapObject tiledMapObject = (TiledMapTileMapObject) mapObj;
			final MapProperties tiledMapObjProperties = tiledMapObject.getProperties();

			final int  animationIndex = tiledMapObject.getTile().getId();
			if (!createAnimation(animationIndex, tiledMapObject.getTile())) {
				Gdx.app.log(TAG, "Could not create animation for tile " + tiledMapObjProperties.get("id",Integer.class));
				continue;
			}

			final float width = tiledMapObjProperties.get("width", Float.class) * CoreGame.UNIT_SCALE ;
			final float height = tiledMapObjProperties.get("height", Float.class) * CoreGame.UNIT_SCALE;
			doorObject.add(new GameObject(GameObjectType.DOOR, new Vector2(tiledMapObject.getX()*CoreGame.UNIT_SCALE, tiledMapObject.getY()*CoreGame.UNIT_SCALE), mapObj.getName(), width, height, tiledMapObject.getRotation(), animationIndex));
		}
	}

	private void parseEnemyLayer() {
		final MapLayer enemyLayer = tiledMap.getLayers().get("enemy");
		if (enemyLayer == null) {
			Gdx.app.debug(TAG, "There is no enemy layer");
			return;
		}
		EnemyType enemyType;
		for (final MapObject obj: enemyLayer.getObjects()) {
			if (obj instanceof RectangleMapObject) {
				final RectangleMapObject rectangleMapObject = (RectangleMapObject) obj;
				Rectangle rectangle = rectangleMapObject.getRectangle();
				enemyType = EnemyType.valueOf(rectangleMapObject.getName());

				enemies.add(new Enemy(enemyType, new Vector2(rectangle.x*CoreGame.UNIT_SCALE, rectangle.y*CoreGame.UNIT_SCALE), 16, 16));
			}
		}
	}

	private void parseBossLayer() {
		final MapLayer bossLayer = tiledMap.getLayers().get("boss");
		if (bossLayer == null) {
			Gdx.app.debug(TAG, "There is no boss layer");
			hasBoss = false;
			return;
		}
		BossType bossType;
		hasBoss = true;
		for (final MapObject obj: bossLayer.getObjects()) {
			if (obj instanceof RectangleMapObject) {
				final RectangleMapObject rectangleMapObject = (RectangleMapObject) obj;
				Rectangle rectangle = rectangleMapObject.getRectangle();
				bossType = BossType.valueOf(rectangleMapObject.getName());

				bosses.add(new Boss(bossType, new Vector2(rectangle.x*CoreGame.UNIT_SCALE, rectangle.y*CoreGame.UNIT_SCALE)));
			}
		}
	}

	private boolean createAnimation(int animationIndex, TiledMapTile tile) {
		Animation<Sprite> animation = mapAnimations.get(animationIndex);
		if (animation == null) {
			Gdx.app.debug(TAG, "Creating new map animation for tiled  " + tile.getId());
			if (tile instanceof AnimatedTiledMapTile) {
				final AnimatedTiledMapTile aniTile = (AnimatedTiledMapTile) tile;
				final Sprite[] keyFrame = new Sprite[aniTile.getFrameTiles().length];
				int i = 0;
				for (final StaticTiledMapTile staticTile: aniTile.getFrameTiles()) {
					keyFrame[i++] = new Sprite(staticTile.getTextureRegion());
				}
				animation = new Animation<Sprite>(aniTile.getAnimationIntervals()[0] * 0.001f, keyFrame);
				animation.setPlayMode(Animation.PlayMode.LOOP);
				mapAnimations.put(animationIndex, animation);
			}
			else if (tile instanceof StaticTiledMapTile) {
				animation = new Animation<Sprite>(0, new Sprite(tile.getTextureRegion()));
				mapAnimations.put(animationIndex, animation);
			}
			else {
				Gdx.app.log(TAG, "Tile of type " + tile + " is not supported for map animation");
				return false;
			}
		}
		return true;
	}

	private void parseCollisionLayer() {
		final MapLayer collisionLayer = tiledMap.getLayers().get("collision");
		if (collisionLayer == null) {
			Gdx.app.debug(TAG, "There is no collision layer");
			return;
		}
		
		for (final MapObject mapObj: collisionLayer.getObjects()) {
			if (mapObj instanceof RectangleMapObject) {
				final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObj;
				final Rectangle rectangle = rectangleMapObject.getRectangle();
				final float[] rectVertices = new float[10];

				rectVertices[0] = 0;
				rectVertices[1] = 0;

				rectVertices[2] = 0;
				rectVertices[3] = rectangle.height;

				rectVertices[4] = rectangle.width;
				rectVertices[5] = rectangle.height;

				rectVertices[6] = rectangle.width;
				rectVertices[7] = 0;

				rectVertices[8] = 0;
				rectVertices[9] = 0;

				collisionAreas.add(new CollisionArea(rectangle.getX(), rectangle.getY(), rectVertices));
			}
			else if (mapObj instanceof PolygonMapObject) {
				final PolygonMapObject polygonMapObject = (PolygonMapObject) mapObj;
				final Polygon polygon = polygonMapObject.getPolygon();
				collisionAreas.add(new CollisionArea(polygon.getX(),polygon.getY(),polygon.getVertices()));
			}

		}
	}

	public Array<CollisionArea> getCollisionAreas() {
		return collisionAreas;
	}

	public Vector2 getStartPosition(String key) {
		return startPosition.get(key);
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public Array<GameObject> getGameObjects() {
		return gameObject;
	}

	public Array<Enemy> getEnemies() {
		return enemies;
	}

	public IntMap<Animation<Sprite>> getMapAnimations() {
		return mapAnimations;
	}

	public Array<GameObject> getHideObject() {
		return hideObject;
	}

	public Array<GameObject> getDoorObject() {
		return doorObject;
	}

	public Array<Boss> getBosses() {
		return bosses;
	}

	public boolean isHasBoss() {
		return hasBoss;
	}

	public void setBossKilled(boolean bossKilled) {
		this.bossKilled = bossKilled;
	}

	public boolean isBossKilled() {
		return bossKilled;
	}



}
