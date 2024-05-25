package com.mygdx.game.character.enemy.system;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.EnemyComponent;
import com.mygdx.game.view.DirectionType;

public class AttackSystem {
    private static CoreGame thisGame;
    public static void attackSystem(CoreGame game, EnemyComponent enemyCom, Box2DComponent b2dComponent) {
        if (thisGame == null) thisGame = game;

        if(enemyCom.isAttacking) {
            b2dComponent.body.applyLinearImpulse(-b2dComponent.body.getLinearVelocity().x*b2dComponent.body.getMass(),
                    -b2dComponent.body.getLinearVelocity().y*b2dComponent.body.getMass(),
                    b2dComponent.body.getPosition().x,
                    b2dComponent.body.getPosition().y, true);
            b2dComponent.body.applyForceToCenter(Vector2.Zero, true);
        }

        if(enemyCom.isAttack)
            if(!enemyCom.isAttacking) {//Đánh thường
                b2dComponent.body.applyLinearImpulse(-b2dComponent.body.getLinearVelocity().x*b2dComponent.body.getMass(),
                        -b2dComponent.body.getLinearVelocity().y*b2dComponent.body.getMass(),
                        b2dComponent.body.getPosition().x,
                        b2dComponent.body.getPosition().y, true);
                b2dComponent.body.applyForceToCenter(Vector2.Zero, true);
                float localPositionX = 0;
                float localPositionY = 0;
                switch (enemyCom.direction) {
                    case LEFT:
                        localPositionX = -b2dComponent.width/2;
                        break;
                    case RIGHT:
                        localPositionX = +b2dComponent.width/2;
                        break;
                    case UP:
                        localPositionY = +b2dComponent.height/2;
                        break;
                    case DOWN:
                        localPositionY = -b2dComponent.height/2;
                        break;
                }
                Vector2 position = new Vector2(b2dComponent.renderPosition.x + localPositionX, b2dComponent.renderPosition.y + localPositionY);
                DamageArea area = new DamageArea(position, CoreGame.BIT_ENEMY, enemyCom.direction, 16, 16, enemyCom.attack, EffectType.SLASHCURVED, false, 0, false);
                game.getEcsEngine().createDamageArea(area);
                enemyCom.isAttacking=true;
            }
        if(enemyCom.isAttack && enemyCom.time>=enemyCom.reload){
            enemyCom.isAttack = false;
            enemyCom.isAttacking = false;
            enemyCom.time = 0;
        }
    }
}
