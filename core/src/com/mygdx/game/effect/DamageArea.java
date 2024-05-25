package com.mygdx.game.effect;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.view.DirectionType;

public class DamageArea {
    public Vector2 position;
    public DirectionType direction;
    public int damage;
    public float speed;
    public short owner;
    public EffectType type;
    public boolean isBullet;
    public int width;
    public int height;
    public boolean upgrade;
    public DamageArea(Vector2 position,short owner, DirectionType direction, int width, int height, int damage, EffectType type, boolean isBullet, float speed, boolean upgrade) {
        this.position = position;
        this.owner = owner;
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.type = type;
        this.isBullet = isBullet;
        this.speed = speed;
        this.upgrade = upgrade;
    }
}
