package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.character.enemy.EnemyType;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.view.DirectionType;

public class EnemyComponent extends EntityComponent {
    public int attack;
    public EnemyType type;
    public Vector2 startPosition = new Vector2();
    public Vector2 wanderingDirection = new Vector2();
    public float timeSinceLastDirectionChange = 0;
    public float directionChangeInterval = 5; // giây
    public float wanderDir;
    public boolean stop;
    public boolean focus;

    public boolean isAttack = false;
    public boolean isAttacking = false;
    public float time;
    public DirectionType diection;
    public float reload = 0.5f;

    public float shootDelay = 1f;
    public float timeSinceLastShot = 0f;
    public float timeDelayForBullet = 0f;
    @Override
    public void reset() {
        super.reset();
        attack = 0;
        type = null;
    }

    public void resetState() {
        isAttack = false;
        isAttacking = false;
    }
}
