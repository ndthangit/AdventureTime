package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.items.food.FoodType;

public class FoodComponent implements Component, Pool.Poolable {
    public FoodType foodType;
    public float width;
    public float height;
    public float timeRemain;

    @Override
    public void reset() {
        foodType = null;
        width = 0;
        height = 0;
        timeRemain = 0;
    }
}
