package com.mygdx.game.character.boss.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.character.ai.SteerableAgent;
import com.mygdx.game.character.boss.BossSkillType;
import com.mygdx.game.character.enemy.Enemy;
import com.mygdx.game.character.enemy.EnemyType;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.AnimationComponent;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.PlayerComponent;
import com.mygdx.game.entity.system.PlayerCameraSystem;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;

public class GiantSpirit {

    private static CoreGame thisGame;
    private static SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(Vector2.Zero);
    static boolean isCharge;
    public static boolean isUsedSkill2 = false;
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

        if (bossCmp.isCharge) { // nap don danh
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

        if (bossCmp.life <= bossCmp.maxLife/2) {
            bossCmp.isSkill2 = true;
        }
        if (bossCmp.isHit) { // bi danh trung

            if (aniCmp.isFinished && aniCmp.aniType == AnimationType.B_HIT) {
                bossCmp.isHit = false;
            }
        } else if (bossCmp.isReady(deltaTime) && !bossCmp.isCharge && !bossCmp.isAttack) {
            Arrive<Vector2> arriveBehavior = new Arrive<>(enemySteerable, playerSteerable)
                    .setArrivalTolerance(0.1f)
                    .setDecelerationRadius(3)
                    .setTimeToTarget(0.1f);
            arriveBehavior.calculateSteering(steeringOutput);
            Vector2 force = steeringOutput.linear.scl(deltaTime);
            force.set(force.x, force.y);
            b2dCmp.body.applyForceToCenter(force, true);
            if (bossCmp.time >= bossCmp.reload && distance < 6) { // dieu kien danh thuong
                bossCmp.time = 0;
                position.set(playerPos);
                b2dCmp.body.applyLinearImpulse(-b2dCmp.body.getLinearVelocity().x*b2dCmp.body.getMass(),
                        -b2dCmp.body.getLinearVelocity().y*b2dCmp.body.getMass(),
                        b2dCmp.body.getPosition().x,
                        b2dCmp.body.getPosition().y, true);
                b2dCmp.body.applyForceToCenter(Vector2.Zero, true);
                bossCmp.isCharge = true;
            }
            // Limit the velocity to prevent the entity from moving too fast
            Vector2 velocity = b2dCmp.body.getLinearVelocity();
            float maxSpeed = 12f;
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
        if (bossCmp.isSkill2 && !bossCmp.isUsedSkill2) {
            createSkill2(entity);
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
        BossSkillType type = BossSkillType.BLAST;
        DamageArea area = new DamageArea(position, CoreGame.BIT_BOSS, DirectionType.DOWN, type.getWidth(), type.getHeight(), 0, EffectType.AURA, false, 0, false);
        thisGame.getEcsEngine().createDamageArea(area);
        PlayerCameraSystem.cameraShake(thisGame, 1f, 1f, Gdx.graphics.getDeltaTime());
        isCharge = true;
        thisGame.getAudioManager().playAudio(AudioType.ALERT);
    }

    private static void createSkill1(Entity entity) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        BossSkillType type = BossSkillType.BLAST;
        DamageArea area = new DamageArea(position, CoreGame.BIT_BOSS, DirectionType.DOWN, type.getWidth(), type.getHeight(), type.getDamage(), type.getEffectType(), false, 0, false);
        thisGame.getEcsEngine().createDamageArea(area);
        bossCmp.isSkill1 = false;
        thisGame.getAudioManager().playAudio(AudioType.EXPLOSION);
    }

    private static void createSkill2(Entity entity) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(entity);
        int dir[][] = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        EnemyType enemyType[] = {EnemyType.SPIRIT, EnemyType.SPIRIT, EnemyType.SKULLBLUE, EnemyType.SKULL};
        for (int i=0; i<4; i++) {
            Vector2 position = new Vector2(b2dCmp.body.getPosition().x + dir[i][0] * b2dCmp.width , b2dCmp.body.getPosition().y + dir[i][1] * b2dCmp.height);
            Enemy enemy = new Enemy(enemyType[i], position, 16, 16);
            thisGame.getEcsEngine().createEnemy(enemy);
        }
        bossCmp.isSkill2 = false;
        bossCmp.isUsedSkill2 = true;
    }
}

