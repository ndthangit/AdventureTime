package com.mygdx.game.effect;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.view.DirectionType;

public class Effect {
    private final EffectType type;
    private Vector2 position;
    private short owner;
    private DirectionType direction;

    public EffectType getType() {
        return type;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Effect(EffectType type, short owner, Vector2 position, DirectionType direction) {
        this.type = type;
        this.position = position;
        this.owner = owner;
        this.direction = direction;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public short getOwner() {
        return owner;
    }
}
