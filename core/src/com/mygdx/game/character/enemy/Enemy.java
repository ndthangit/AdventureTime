package com.mygdx.game.character.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.map.GameObjectType;

public class Enemy {
    private final EnemyType type;
    private final Vector2 position;
    private final float width;
    private final float height;

    public Enemy(EnemyType type, Vector2 position, float width, float height) {
        this.type = type;
        this.position = position;
        this.width = width;
        this.height = height;

    }

    public EnemyType getType() {
        return type;
    }
    public Vector2 getPosition() {
        return position;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
}
