package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.map.GameObject;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.MapType;

public class MapState implements Json.Serializable {
    public static final String SAVE_STATE_PREFERENCE_KEY = "saveState";
    private static final String SAVE_STATE_REMAINING_GAME_OBJ_IDS_KEY = "remainingGameObjects";

    private final Array<GameObjectComponent> gameObjectsArray;
    private final Array<Vector2> enemiesArray;
    private final Array<Vector2> itemsArray;
    private final ComponentMapper<Box2DComponent> b2dCmpMapper;
    private final ComponentMapper<RemoveComponent> removeCmpMapper;
    private final ComponentMapper<GameObjectComponent> gameObjCmpMapper;
    private final ComponentMapper<EnemyComponent> enemyCmpMapper;
    private final ComponentMapper<ItemComponent> itemCmpMapper;
    private final ComponentMapper<BossComponent> bossCmpMapper;
    private final Json json;
    private final JsonReader jsonReader;

    public MapState() {
        gameObjectsArray = new Array<>();
        enemiesArray = new Array<>();
        itemsArray = new Array<>();
        bossCmpMapper = ComponentMapper.getFor(BossComponent.class);
        b2dCmpMapper = ComponentMapper.getFor(Box2DComponent.class);
        removeCmpMapper = ComponentMapper.getFor(RemoveComponent.class);
        gameObjCmpMapper = ComponentMapper.getFor(GameObjectComponent.class);
        enemyCmpMapper = ComponentMapper.getFor(EnemyComponent.class);
        itemCmpMapper = ComponentMapper.getFor(ItemComponent.class);
        json = new Json();
        jsonReader = new JsonReader();
    }

    public void loadState(final ECSEngine ecsEngine, final Map map) {
        // load state
    }

    public void updateMapState(final ECSEngine ecsEngine) {
        // update map state
        if ( gameObjectsArray.size > 0) {
            gameObjectsArray.clear();
            for (Entity entity : ecsEngine.getEntitiesFor(
                    Family.all(GameObjectComponent.class).get())) {
                gameObjectsArray.add(gameObjCmpMapper.get(entity));
            }
        } 
    }

    public void createMapState(final ECSEngine ecsEngine) {
        // create map state
        for (Entity gameObject : ecsEngine.getEntitiesFor(
                Family.all(GameObjectComponent.class,Box2DComponent.class,AnimationComponent.class).get())) {
            gameObjectsArray.add(gameObjCmpMapper.get(gameObject));
        }
    }

    @Override
    public void write(Json json) {
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    public Array<GameObjectComponent> getGameObjectsArray() {
        return gameObjectsArray;
    }
}
