package com.mygdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.CoreGame;

public class Map {
	public static String TAG = Map.class.getSimpleName();
	
	private final TiledMap tiledMap;
	private final Array<CollisionArea>collisionAreas;
	private final Vector2 startPosition;
	
	public Map(final TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		collisionAreas = new Array<CollisionArea>();
		
		parseCollisionLayer();
		startPosition = new Vector2();
		parsePlayerLayer();
	}
	
	private void parsePlayerLayer() {
		final MapLayer playerLayer = tiledMap.getLayers().get("playerStartPosition");
		if (playerLayer == null) {
			Gdx.app.debug(TAG, "There is no player start position");
			return;
		}
		final MapObjects objects = playerLayer.getObjects();
		for (MapObject obj: objects) {
			if (obj instanceof RectangleMapObject) {
				final RectangleMapObject rectangleMapObject = (RectangleMapObject) obj;
				Rectangle rectangle = rectangleMapObject.getRectangle();
				startPosition.set(rectangle.x * CoreGame.UNIT_SCALE, rectangle.y * CoreGame.UNIT_SCALE);
			}
		}
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
			else if (mapObj instanceof PolygonMapObject) { // can sua
				final PolygonMapObject polygonMapObject = (PolygonMapObject) mapObj;
				final Polygon polygon = polygonMapObject.getPolygon();
				collisionAreas.add(new CollisionArea(polygon.getX(),polygon.getY(),polygon.getVertices()));
			}
			
		}
	}

	public Array<CollisionArea> getCollisionAreas() {
		return collisionAreas;
	}

	public Vector2 getStartPosition() {
		return startPosition;
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}
}
