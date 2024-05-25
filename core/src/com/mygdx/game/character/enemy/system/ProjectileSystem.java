package com.mygdx.game.character.enemy.system;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.character.enemy.EnemyDetailSkillType;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EnemyComponent;

import javax.swing.text.html.parser.Entity;

public class ProjectileSystem {
    public static void Projectile(CoreGame game, EnemyComponent enemyCom,Vector2 enemyPos, Vector2 playerPos, Box2DComponent box2DCmp) {
        EnemyDetailSkillType type = enemyCom.type.getDetailSkillType();

        if (Math.abs(playerPos.y - enemyPos.y) < 0.5f && Math.abs(playerPos.x - enemyPos.x) < 3.5f) {
            // Nếu có, Boss sẽ bắn đạn theo phương ngang đó
            Vector2 dir = new Vector2(playerPos.x > enemyPos.x ? 1 : -1, 0);
            if (enemyCom.timeSinceLastShot >= 2) {
                Vector2 bulletStart = new Vector2(enemyPos.x - box2DCmp.width/2, enemyPos.y - box2DCmp.height/2);// Mục tiêu là vị trí ngang của Player và dọc của Boss
                DamageArea area = new DamageArea(enemyPos, CoreGame.BIT_ENEMY, enemyCom.direction, type.getWidth(), type.getHeight(), type.getDamage(), type.getEffectType(), true, type.getSpeed(), false);
                game.getEcsEngine().createDamageArea(area); // Tạo đạn với tốc độ 1.0f
                switch (type) {
                    case FIRE_BALL:
                        game.getAudioManager().playAudio(AudioType.FIREBALL);
                        break;
                    case SHURIKEN:
                        game.getAudioManager().playAudio(AudioType.POWERUP);
                        break;

                }

                enemyCom.timeSinceLastShot = 0;
                enemyCom.timeDelayForBullet = 1f;
            }
        } else if (Math.abs(playerPos.x - enemyPos.x) < 0.5f && Math.abs(playerPos.y - enemyPos.y) < 3.5f) {
            // Nếu có, Boss sẽ bắn đạn theo phương ngang đó
            Vector2 dir = new Vector2(0, playerPos.y > enemyPos.y ? 1 : -1);
            if (enemyCom.timeSinceLastShot >= 2) {
                DamageArea area = new DamageArea(enemyPos, CoreGame.BIT_ENEMY, enemyCom.direction, type.getWidth(), type.getHeight(), type.getDamage(), type.getEffectType(), true, type.getSpeed(),false);
                game.getEcsEngine().createDamageArea(area);
                enemyCom.timeSinceLastShot = 0;
                enemyCom.timeDelayForBullet = 1f;
            }
        }
    }
}
