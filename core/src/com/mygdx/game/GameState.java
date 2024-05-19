package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.GameObjectComponent;
import com.mygdx.game.map.MapManager;
import com.mygdx.game.map.MapType;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private Map<MapType, MapState> mapState;

    public GameState() {

        mapState = new HashMap<>();
        mapState.put(MapType.BEGIN_FOREST, new MapState());

    }

    public void setGameState(final ECSEngine engine) {
        ImmutableArray<Entity> gameObjects = engine.getEntitiesFor(Family.all(GameObjectComponent.class, Box2DComponent.class, AnimationComponent.class).get());
        // set game state
//        mapState.get(MapType.BEGIN_FOREST).loadInitialValues(gameObjects);
//        mapState.get(MapType.BEGIN_FOREST).loadInitialValues(new Array<>());
    }

    public void updateMapState(MapType nextMapType) {
        // update map state
    }

    public MapState getMapState(MapType currentMapType) {
        return mapState.get(currentMapType);
    }

}
