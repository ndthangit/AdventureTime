package com.mygdx.game;

import com.mygdx.game.map.MapType;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private Map<MapType, MapState> mapState;

    public GameState() {

        mapState = new HashMap<>();
        mapState.put(MapType.BEGIN_FOREST, new MapState());
        mapState.put(MapType.DOJO, new MapState());
        mapState.put(MapType.TOWN, new MapState());
        mapState.put(MapType.SAMU_BOSS, new MapState());
        mapState.put(MapType.ABANDONVIL, new MapState());

    }

    public MapState getMapState(MapType currentMapType) {
        return mapState.get(currentMapType);
    }

}
