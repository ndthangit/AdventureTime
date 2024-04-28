package com.mygdx.game.items.food;

import com.badlogic.gdx.math.Vector2;

public class Food {
    public FoodType type;
    public Vector2 position;
    public Food(FoodType type, Vector2 position) {
        this.type = type;
        this.position = position;
    }
}
