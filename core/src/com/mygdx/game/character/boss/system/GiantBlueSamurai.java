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
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.character.enemy.Enemy;
import com.mygdx.game.character.enemy.EnemyType;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.*;
import com.mygdx.game.view.AnimationType;
import com.mygdx.game.view.DirectionType;

public class GiantBlueSamurai {
    public static boolean attacking = false;
    public static boolean isUsedSkill2 = false;
    private static boolean isHitted = false;
    private static CoreGame thisGame;
    private static SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

    public static void GBS_movement(CoreGame game, Entity entity, Entity player, float deltaTime) {
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
            if (!isHitted) {
                aniCmp.isFinished = false;
                isHitted = true;
            }
            if (aniCmp.isFinished && aniCmp.aniType == AnimationType.B_HIT) {
                bossCmp.isHit = false;
                isHitted = false;
            }
        }
        else if (bossCmp.isCharge) { // nap don danh
            if (aniCmp.isFinished && (aniCmp.aniType == AnimationType.CHARGE_LEFT || aniCmp.aniType == AnimationType.CHARGE_RIGHT)) {
                bossCmp.isCharge = false;
                bossCmp.isAttack = true;
                aniCmp.isFinished = false;
            }
        } else if (bossCmp.isAttack) { // danh thuong

            bossCmp.timeAttack += deltaTime;
            if (aniCmp.isFinished && (aniCmp.aniType == AnimationType.B_ATTACK_LEFT || aniCmp.aniType == AnimationType.B_ATTACK_RIGHT)) {
                bossCmp.isAttack = false;
            }
        }
        else {
            int dir = 1;
            if (bossCmp.time < 0.5f) { // lui lai khi danh xong
                dir = -1;
            }

            if (bossCmp.life <= bossCmp.maxLife/2) {
                bossCmp.isSkill2 = true;
            }
            if (bossCmp.time >= 2 && distance < 3) { // dieu kien danh thuong
                bossCmp.isCharge = true;
                aniCmp.isFinished = false;
                bossCmp.time -= bossCmp.reload;
                b2dCmp.body.applyLinearImpulse(-b2dCmp.body.getLinearVelocity().x*b2dCmp.body.getMass(),
                        -b2dCmp.body.getLinearVelocity().y*b2dCmp.body.getMass(),
                        b2dCmp.body.getPosition().x,
                        b2dCmp.body.getPosition().y, true);
                b2dCmp.body.applyForceToCenter(Vector2.Zero, true);
            }
            // dieu kien danh skill 1
            else if (bossCmp.time >= bossCmp.reload && playerPos.y - b2dPlayer.height/2 > bossPos.y - b2dCmp.height/2 && playerPos.y + b2dPlayer.height/2 < bossPos.y + b2dCmp.height/2) {
                bossCmp.isCharge = true;
                bossCmp.isSkill1 = true;
                aniCmp.isFinished = false;
                bossCmp.time -= bossCmp.reload;
                b2dCmp.body.applyLinearImpulse(-b2dCmp.body.getLinearVelocity().x*b2dCmp.body.getMass(),
                        -b2dCmp.body.getLinearVelocity().y*b2dCmp.body.getMass(),
                        b2dCmp.body.getPosition().x,
                        b2dCmp.body.getPosition().y, true);
                b2dCmp.body.applyForceToCenter(Vector2.Zero, true);
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
    }

    public static void GBS_attack(CoreGame game, Entity entity, float deltatime) {
        if (thisGame == null) {
            thisGame = game;
        }
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(entity);
        if (bossCmp.isSkill2 && !isUsedSkill2) {
            isUsedSkill2 = true;
            createSkill2(entity);
        }
        if (bossCmp.isSkill1 && bossCmp.isAttack) {
            createSkill1(entity);
            bossCmp.isSkill1 = false;
        }

        if (bossCmp.isAttack) {
            if (!attacking) {
                float localPositionX = bossCmp.direction == DirectionType.LEFT ? -b2dCmp.width/2: b2dCmp.width/2;
                Vector2 position = new Vector2(b2dCmp.renderPosition.x + localPositionX, b2dCmp.renderPosition.y);
                DamageArea area = new DamageArea(position, CoreGame.BIT_BOSS, bossCmp.direction,bossCmp.type.getWidth(), 3*bossCmp.type.getHeight()/2, bossCmp.attack, EffectType.NONE, false, 0, false);
                game.getEcsEngine().createDamageArea(area);
                attacking = true;
            }
        }
    }

    public static void GBS_animation(BossComponent bossCmp, AnimationComponent animationCmp, Box2DComponent box2DCmp) {
        if (bossCmp.isHit) {
            animationCmp.aniType = AnimationType.B_HIT;
        } else if (bossCmp.isCharge) {
            Gdx.app.debug("Boss", " why");
            if (bossCmp.direction == DirectionType.LEFT) {
                animationCmp.aniType = AnimationType.CHARGE_LEFT;
            }
            else {
                animationCmp.aniType = AnimationType.CHARGE_RIGHT;
            }
            Gdx.app.debug("Boss", animationCmp.aniType.toString());
        } else if (bossCmp.isAttack) {
            if (bossCmp.direction == DirectionType.LEFT) {
                animationCmp.aniType = AnimationType.B_ATTACK_LEFT;
            }
            else {
                animationCmp.aniType = AnimationType.B_ATTACK_RIGHT;
            }
        } else if (bossCmp.isSkill1) {

        } else if (bossCmp.isSkill2) {

        } else {
            if (box2DCmp.body.getLinearVelocity().equals(Vector2.Zero)) {
                animationCmp.aniType = AnimationType.IDLE_DOWN;
            }
            else {
                animationCmp.aniType = AnimationType.DOWN;
            }
        }
    }

    private static void createSkill1(Entity entity) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(entity);
        float localPositionX = bossCmp.direction == DirectionType.LEFT ? -b2dCmp.width/2: + b2dCmp.width/2;
        Vector2 position = new Vector2(b2dCmp.renderPosition.x + localPositionX, b2dCmp.renderPosition.y);
        BossSkillType type = bossCmp.type.getSkill1();
        DamageArea area = new DamageArea(position, CoreGame.BIT_BOSS, bossCmp.direction,bossCmp.type.getWidth(), bossCmp.type.getHeight(), type.getDamage() , type.getEffectType(), true, type.getSpeed(), false);
        thisGame.getEcsEngine().createDamageArea(area);
        thisGame.getAudioManager().playAudio(AudioType.FIREBALL);
    }
    private static void createSkill2(Entity entity) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(entity);
        int dir[][] = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        EnemyType enemyType[] = {EnemyType.REDSAMURAI, EnemyType.SAMURAI, EnemyType.REDNINJA, EnemyType.ORANGESORCERER};
        for (int i=0; i<4; i++) {
            Vector2 position = new Vector2(b2dCmp.body.getPosition().x + dir[i][0] * b2dCmp.width , b2dCmp.body.getPosition().y + dir[i][1] * b2dCmp.height);
            Enemy enemy = new Enemy(enemyType[i], position, 16, 16);
            thisGame.getEcsEngine().createEnemy(enemy);
        }
        bossCmp.isSkill2 = false;
        isUsedSkill2 = true;
    }
}
