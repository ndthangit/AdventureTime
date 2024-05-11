//Ene movement
package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.ai.SteerableAgent;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EnemyComponent;
import com.mygdx.game.entity.component.PlayerComponent;

import java.util.Random;

public class EnemyMovementSystem extends IteratingSystem {
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<Box2DComponent> bodm;
    private CoreGame game;
    private Vector2 originalPosition; // Thêm biến này để lưu vị trí ban đầu của Enemy
    private SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    private Random random;
    @SuppressWarnings("unchecked")
    public EnemyMovementSystem(CoreGame game) {
        super(Family.all(EnemyComponent.class, Box2DComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        bodm = ComponentMapper.getFor(Box2DComponent.class);
        this.game = game;
        originalPosition = new Vector2(); // Khởi tạo vị trí ban đầu
        random = new Random();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Box2DComponent playerCom = ECSEngine.box2dCmpMapper.get(getPlayerEntity());
        EnemyComponent enemyCom = em.get(entity);
        Box2DComponent b2dComponent = bodm.get(entity);

        Vector2 playerPos = playerCom.renderPosition;
        Vector2 enemyPos = b2dComponent.body.getPosition();
        float distance = playerPos.dst(enemyPos);
        final Vector2 speed = new Vector2(enemyCom.speed.x * 0.3f, enemyCom.speed.y * 0.3f);
        if (enemyCom.stop == false) {
            if (distance < 3) {
                // Đuổi theo Player
                Box2DComponent b2dPlayer = bodm.get(getPlayerEntity());
                SteerableAgent enemySteerable = new SteerableAgent(b2dComponent.body, 1.5f);
                SteerableAgent playerSteerable = new SteerableAgent(b2dPlayer.body, 1.5f);

                Arrive<Vector2> arriveBehavior = new Arrive<>(enemySteerable, playerSteerable)
                        .setArrivalTolerance(0.1f)
                        .setDecelerationRadius(3)
                        .setTimeToTarget(0.1f);
                arriveBehavior.calculateSteering(steeringOutput);
                Vector2 force = steeringOutput.linear.scl(deltaTime);
                b2dComponent.body.applyForceToCenter(force, true);

                // Limit the velocity to prevent the entity from moving too fast
                Vector2 velocity = b2dComponent.body.getLinearVelocity();
                float maxSpeed = 0.8f;
                if (velocity.len() > maxSpeed) {
                    velocity = velocity.nor().scl(maxSpeed);
                    b2dComponent.body.setLinearVelocity(velocity);
                }
            } else {
                // Quay về vị trí ban đầu nếu không đúng tại startPosition
                if (!enemyPos.epsilonEquals(enemyCom.startPosition, 0.1f)) {
                    Vector2 dir = new Vector2(enemyCom.startPosition.x - enemyPos.x, enemyCom.startPosition.y - enemyPos.y);
                    dir.nor();
                    b2dComponent.body.setTransform(b2dComponent.body.getPosition().x + dir.x * deltaTime * 0.5f,
                            b2dComponent.body.getPosition().y + dir.y * deltaTime * 0.5f,
                            b2dComponent.body.getAngle());
                } else {
                    // Di chuyển ngẫu nhiên xung quanh startPosition
                    SteerableAgent enemySteerable = new SteerableAgent(b2dComponent.body, 1.5f);
                    enemySteerable.setMaxLinearSpeed(5000); // Tăng tốc độ tối đa
                    enemySteerable.setMaxLinearAcceleration(3000); // Tăng gia tốc tối đa

                    Wander<Vector2> wanderSB = new Wander<>(enemySteerable) //
                            .setFaceEnabled(false) // We want to use Face internally (independent facing is on)
                            .setAlignTolerance(0.01f) // Used by Face
                            .setDecelerationRadius(5) // Used by Face
                            .setTimeToTarget(0.1f) // Used by Face and Arrive
                            .setWanderOffset(0) //
                            .setWanderOrientation(random.nextFloat() * 360) //
                            .setWanderRadius(2) //
                            .setWanderRate(MathUtils.PI2 * 4);

                    wanderSB.calculateSteering(steeringOutput);
                    Vector2 force = steeringOutput.linear.scl(deltaTime);
                    b2dComponent.body.applyForceToCenter(force.scl(1), true); // Tăng lực được áp dụng

                    // Limit the velocity to prevent the entity from moving too fast
                    Vector2 velocity = b2dComponent.body.getLinearVelocity();
                    float maxSpeed = 1.5f; // Tăng tốc độ tối đa
                    if (velocity.len() > maxSpeed) {
                        velocity = velocity.nor().scl(maxSpeed);
                        b2dComponent.body.setLinearVelocity(velocity);
                    }
                }
            }
        }
    }

    public Entity getPlayerEntity() {
        ImmutableArray<Entity> entities = game.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
        if (entities.size() > 0) {
            return entities.first();
        }
        return null;
    }
}