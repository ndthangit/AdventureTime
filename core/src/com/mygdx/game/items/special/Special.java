package com.mygdx.game.items.special;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.food.FoodType;

import static com.mygdx.game.CoreGame.UNIT_SCALE;

public class Special extends Item {

    public SpecialType type;

    public Special(SpecialType type, Vector2 position) {
        super(position, type.getAtlasPath(), type.getAtlasName(), type.getWidth()*UNIT_SCALE, type.getHeight()*UNIT_SCALE);
        this.type = type;
    }
}
