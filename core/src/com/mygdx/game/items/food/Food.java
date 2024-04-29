package com.mygdx.game.items.food;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.items.Item;

public class Food extends Item {
    public FoodType type;
    public Food(FoodType type, Vector2 position) {
        super(position, type.getAtlasPath(), type.getKey());
        this.type = type;
    }
}
