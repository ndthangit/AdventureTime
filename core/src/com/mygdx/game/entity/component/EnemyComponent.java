//EneCom
package com.mygdx.game.entity.component;


import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.enemy.EnemyType;

public class EnemyComponent extends EntityComponent{
    public int attack;
    public EnemyType type;
    //
    public Vector2 startPosition = new Vector2();
    //
    public Vector2 wanderingDirection = new Vector2();
    public float timeSinceLastDirectionChange = 0;
    public float directionChangeInterval = 5; // giây
<<<<<<< Updated upstream
    public float wanderingRadius = 10; // giới hạn bán kính lang thang
=======
    public float wanderingRadius = 10;
    public boolean stop = false;

>>>>>>> Stashed changes
    @Override
    public void reset() {
        super.reset();
        attack = 0;
        type = null;
    }
}