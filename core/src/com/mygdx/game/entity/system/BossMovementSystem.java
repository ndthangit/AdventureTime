package com.mygdx.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.ai.SteerableAgent;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.view.DirectionType;


public class BossMovementSystem extends IteratingSystem {

    private final CoreGame game;
    private SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());


    public BossMovementSystem(CoreGame game) {
        super(Family.all(AnimationComponent.class, BossComponent.class, Box2DComponent.class).get());
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dComponent = ECSEngine.box2dCmpMapper.get(entity);
        // Đuổi theo Player
        if (getPlayerEntity() == null) return;
        Box2DComponent b2dPlayer = ECSEngine.box2dCmpMapper.get(getPlayerEntity());
        SteerableAgent enemySteerable = new SteerableAgent(b2dComponent.body, 1.5f);
        SteerableAgent playerSteerable = new SteerableAgent(b2dPlayer.body, 1.5f);

        Vector2 playerPos = b2dPlayer.renderPosition;
        Vector2 bossPos = b2dComponent.body.getPosition();
        float distance = playerPos.dst(bossPos);
        bossCmp.time += deltaTime;
        if (playerPos.x < bossPos.x) {
            bossCmp.direction = DirectionType.LEFT;
        }
        else if (playerPos.x > bossPos.x) {
            bossCmp.direction = DirectionType.RIGHT;
        }
//        if (bossCmp.time >= bossCmp.reload && playerPos.y > bossPos.y - b2dComponent.height/2 && playerPos.y < bossPos.y + b2dComponent.height/2) {
//            bossCmp.time -= bossCmp.reload;
//
//        }
        if (bossCmp.isHit) { // bi danh trung
            bossCmp.resetState();
            bossCmp.timeHit += deltaTime;
            if (bossCmp.timeHit >= 0.4f) {
                bossCmp.isHit = false;
                bossCmp.timeHit = 0;
            }
        }
        else if (bossCmp.isCharge) { // nap don danh
            bossCmp.timeCharge += deltaTime;
            if (bossCmp.timeCharge >= 0.4f) {
                bossCmp.isCharge = false;
                bossCmp.timeCharge = 0;
                bossCmp.isAttack = true;
            }
        } else if (bossCmp.isAttack) { // danh thuong
            bossCmp.timeAttack += deltaTime;
            if (bossCmp.timeAttack >= 0.4f) {
                bossCmp.timeAttack = 0;
                bossCmp.isAttack = false;

            }
        }else {
            int dir = 1;
            if (bossCmp.time < 0.5f) { // lui lai khi danh xong
                dir = -1;
            }
            if (bossCmp.time >= 2 && distance < 3) { // dieu kien danh thuong
                bossCmp.isCharge = true;
                bossCmp.time -= bossCmp.reload;
                b2dComponent.body.applyLinearImpulse(-b2dComponent.body.getLinearVelocity().x*b2dComponent.body.getMass(),
                        -b2dComponent.body.getLinearVelocity().y*b2dComponent.body.getMass(),
                        b2dComponent.body.getPosition().x,
                        b2dComponent.body.getPosition().y, true);
                b2dComponent.body.applyForceToCenter(Vector2.Zero, true);
            }
            // dieu kien danh skill 1
            else if (bossCmp.time >= bossCmp.reload && playerPos.y - b2dPlayer.height/2 > bossPos.y - b2dComponent.height/2 && playerPos.y + b2dPlayer.height/2 < bossPos.y + b2dComponent.height/2) {
                bossCmp.isCharge = true;
                bossCmp.isSkill1 = true;
                bossCmp.time -= bossCmp.reload;
                b2dComponent.body.applyLinearImpulse(-b2dComponent.body.getLinearVelocity().x*b2dComponent.body.getMass(),
                        -b2dComponent.body.getLinearVelocity().y*b2dComponent.body.getMass(),
                        b2dComponent.body.getPosition().x,
                        b2dComponent.body.getPosition().y, true);
                b2dComponent.body.applyForceToCenter(Vector2.Zero, true);
            }
            else { // di chuyen
                bossCmp.resetState();
                Arrive<Vector2> arriveBehavior = new Arrive<>(enemySteerable, playerSteerable)
                        .setArrivalTolerance(0.1f)
                        .setDecelerationRadius(3)
                        .setTimeToTarget(0.1f);
                arriveBehavior.calculateSteering(steeringOutput);
                Vector2 force = steeringOutput.linear.scl(deltaTime);
                force.set(force.x*dir, force.y*dir);
                b2dComponent.body.applyForceToCenter(force, true);

                // Limit the velocity to prevent the entity from moving too fast
                Vector2 velocity = b2dComponent.body.getLinearVelocity();
                float maxSpeed = 0.8f;
                if (velocity.len() > maxSpeed) {
                    velocity = velocity.nor().scl(maxSpeed);
                    b2dComponent.body.setLinearVelocity(velocity);
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
