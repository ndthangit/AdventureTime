package com.mygdx.game.character.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.Charater;
import com.mygdx.game.map.GameObject;
import com.mygdx.game.map.GameObjectType;

public class Enemy extends Charater {
    private final EnemyType type;


    public Enemy(EnemyType type, Vector2 position, float width, float height) {
        super(position, width, height);
        this.type = type;
    }

    public EnemyType getType() {
        return type;
    }
}
