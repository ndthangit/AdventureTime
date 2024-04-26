package com.mygdx.game.items.weapon;

import com.badlogic.gdx.math.Vector2;

import static com.mygdx.game.CoreGame.UNIT_SCALE;

public class Weapon {
    public WeaponType type;
    public Vector2 position;
    public int direction;
    public Vector2 posDirection[];
    public float width;
    public float height;
    public int getDirection() {
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

    

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Weapon(WeaponType type, Vector2 position, int direction, Vector2 speed, float width, float height) {
        this.position = position;
        this.direction = direction;
        this.posDirection = new Vector2[4];
        this.width = width;
        this.height = height;
        this.type = type;
        posDirection[0] = new Vector2(-UNIT_SCALE , -14 * UNIT_SCALE);
        posDirection[1] = new Vector2(-14 * UNIT_SCALE, -4 * UNIT_SCALE);
        posDirection[2] = new Vector2(-4 * UNIT_SCALE, 14 * UNIT_SCALE);
        posDirection[3] = new Vector2(14 * UNIT_SCALE, -4 * UNIT_SCALE);

    }

}
