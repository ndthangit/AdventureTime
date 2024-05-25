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
    //
    public Vector2 startPosition = new Vector2();
    public boolean stop;
    public boolean focus;

    public boolean isAttack = false;
    public boolean isAttacking = false;
    public float time;
    public DirectionType direction;
    public float reload = 0.5f;
    //Bullet
    public float timeSinceLastShot = 0f;
    public float timeDelayForBullet = 0f;
    @Override
    public void reset() {
        super.reset();
        attack = 0;
        type = null;
    }
}
