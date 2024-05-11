package com.mygdx.game.items.weapon;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.effect.Effect;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.EffectComponent;
import com.mygdx.game.items.Item;
import com.mygdx.game.view.DirectionType;

import java.util.EnumMap;

import static com.mygdx.game.CoreGame.UNIT_SCALE;

public class Weapon extends Item {
    public WeaponType type;
    public DirectionType direction;
    public Vector2 posDirection[];
    public Vector2 effDirection[];
    public Effect effect;
    public float width;
    public float height;
    public DirectionType getDirection() {
        return direction;
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

    public Weapon(WeaponType type, Effect effect, Vector2 position, DirectionType direction) {
        super(position, type.getAtlasPath(), type.getAtlasKeyIcon(), type.getWidth()*UNIT_SCALE, type.getHeight()*UNIT_SCALE);
        this.direction = direction;
        this.posDirection = new Vector2[4];
        this.effDirection = new Vector2[4];
        this.width = type.getWidth() * UNIT_SCALE;
        this.height = type.getHeight() * UNIT_SCALE;
        this.type = type;
        this.effect = effect;
        posDirection[0] = new Vector2(-UNIT_SCALE , -14 * UNIT_SCALE);
        posDirection[1] = new Vector2(-14 * UNIT_SCALE, -4 * UNIT_SCALE);
        posDirection[2] = new Vector2(-4 * UNIT_SCALE, 14 * UNIT_SCALE);
        posDirection[3] = new Vector2(14 * UNIT_SCALE, -4 * UNIT_SCALE);


        effDirection[0] = new Vector2(-4* UNIT_SCALE , -14 * UNIT_SCALE);
        effDirection[1] = new Vector2(-14 * UNIT_SCALE, -1 * UNIT_SCALE);
        effDirection[2] = new Vector2( - UNIT_SCALE, 14 * UNIT_SCALE);
        effDirection[3] = new Vector2(14 * UNIT_SCALE, -7 * UNIT_SCALE);
    }

}
