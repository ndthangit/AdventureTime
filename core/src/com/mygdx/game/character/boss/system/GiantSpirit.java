package com.mygdx.game.character.boss.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.ai.SteerableAgent;
import com.mygdx.game.character.boss.BossSkillType;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;

public class GiantSpirit {

    private static CoreGame thisGame;
    private static SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    static boolean isCharge;
    private static Vector2 position = new Vector2();

    public static void GS_movement(CoreGame game, Entity entity, Entity player, float deltaTime) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(entity);
        if (player == null) return;
        Box2DComponent b2dPlayer = ECSEngine.box2dCmpMapper.get(player);
        AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);

        SteerableAgent enemySteerable = new SteerableAgent(b2dCmp.body, 1.5f);
        SteerableAgent playerSteerable = new SteerableAgent(b2dPlayer.body, 1.5f);

        Vector2 playerPos = b2dPlayer.renderPosition;
        Vector2 bossPos = b2dCmp.body.getPosition();
        float distance = playerPos.dst(bossPos);


        if (bossCmp.isHit) { // bi danh trung
            bossCmp.resetState();

            if (aniCmp.isFinished && aniCmp.aniType == AnimationType.B_HIT) {
                bossCmp.isHit = false;
            }
        }
        else if (bossCmp.isCharge) { // nap don danh
            if (bossCmp.time >= 1) {
                isCharge = false;
                bossCmp.isSkill1 = true;
                bossCmp.isCharge = false;
                bossCmp.isAttack = true;
            }
        } else if (bossCmp.isAttack) {
            if (bossCmp.time >= 3) {
                bossCmp.isAttack = false;
            }
        }
        else {
            int dir = 1;
            if (distance <= 3) { // lui lai khi danh xong
                dir = -1;
            }

            if (bossCmp.time >= 8 && distance < 6) { // dieu kien danh thuong
                bossCmp.time -= bossCmp.reload;
                position.set(playerPos);
                b2dCmp.body.applyLinearImpulse(-b2dCmp.body.getLinearVelocity().x*b2dCmp.body.getMass(),
                        -b2dCmp.body.getLinearVelocity().y*b2dCmp.body.getMass(),
                        b2dCmp.body.getPosition().x,
                        b2dCmp.body.getPosition().y, true);
                b2dCmp.body.applyForceToCenter(Vector2.Zero, true);
                bossCmp.isCharge = true;
            }
            // di chuyen
            Arrive<Vector2> arriveBehavior = new Arrive<>(enemySteerable, playerSteerable)
                    .setArrivalTolerance(0.1f)
                    .setDecelerationRadius(3)
                    .setTimeToTarget(0.1f);
            arriveBehavior.calculateSteering(steeringOutput);
            Vector2 force = steeringOutput.linear.scl(deltaTime);
            force.set(force.x * dir, force.y * dir);
            b2dCmp.body.applyForceToCenter(force, true);

            // Limit the velocity to prevent the entity from moving too fast
            Vector2 velocity = b2dCmp.body.getLinearVelocity();
            float maxSpeed = 0.8f;
            if (velocity.len() > maxSpeed) {
                velocity = velocity.nor().scl(maxSpeed);
                b2dCmp.body.setLinearVelocity(velocity);
            }
        }

    }

    public static void GS_attack(CoreGame game, Entity entity, float deltatime) {
        if (thisGame == null) {
            thisGame = game;
        }
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(entity);

        if (bossCmp.isCharge) {
            if (!isCharge)
             chargeSkill(entity);

        }
        if (bossCmp.isSkill1 && bossCmp.isAttack) {
            createSkill1(entity);
        }
        if (bossCmp.isSkill2 && bossCmp.isAttack) {

        }
    }

    public static void GS_animation(BossComponent bossCmp, AnimationComponent animationCmp, Box2DComponent box2DCmp) {
        if (bossCmp.isHit) {
            animationCmp.aniType = AnimationType.B_HIT;
        } else {
            animationCmp.aniType = AnimationType.DOWN;
        }
    }
    private static void chargeSkill(Entity entity) {
        Box2DComponent box2DCmp = ECSEngine.box2dCmpMapper.get(entity);
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Vector2 cPosition = new Vector2(box2DCmp.body.getPosition());
        BossSkillType type = BossSkillType.BLAST;
        DamageArea area = new DamageArea(cPosition, CoreGame.BIT_BOSS, DirectionType.DOWN, type.getWidth(), type.getHeight(), type.getDamage(), EffectType.AURA, false, 0);
        thisGame.getEcsEngine().createDamageArea(area);
        isCharge = true;
    }

    private static void createSkill1(Entity entity) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        BossSkillType type = BossSkillType.BLAST;
        DamageArea area = new DamageArea(position, CoreGame.BIT_BOSS, DirectionType.DOWN, type.getWidth(), type.getHeight(), type.getDamage(), type.getEffectType(), false, 0);
        thisGame.getEcsEngine().createDamageArea(area);
        bossCmp.isSkill1 = false;
    }

    private static void createSkill2(Entity entity) {

    }

    private static Entity getPlayerEntity() {
        ImmutableArray<Entity> entities = thisGame.getEcsEngine().getEntitiesFor(Family.all(PlayerComponent.class).get());
        if (entities.size() > 0) {
            return entities.first();
        }
        return null;
    }
}

