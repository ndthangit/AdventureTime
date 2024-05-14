package com.mygdx.game.character.boss.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CoreGame;
import com.mygdx.game.character.boss.BossSkillType;
import com.mygdx.game.character.boss.BossType;
import com.mygdx.game.effect.DamageArea;
import com.mygdx.game.effect.EffectType;
import com.mygdx.game.entity.ECSEngine;
import com.mygdx.game.entity.component.BossComponent;
import com.mygdx.game.entity.component.Box2DComponent;
import com.mygdx.game.entity.component.DamageAreaComponent;
import com.mygdx.game.entity.component.RemoveComponent;
import com.mygdx.game.view.DirectionType;

public class GiantBlueSamurai {
    public static boolean attacking = false;
    public static boolean isUsedSkill2 = false;
    private static CoreGame thisGame;
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
        if (bossCmp.isSkill1) {
            createSkill1(entity);
            bossCmp.isSkill1 = false;
        }

        if (bossCmp.isAttack) {
            if (!attacking) {
                float localPositionX = bossCmp.direction == DirectionType.LEFT ? -b2dCmp.width/2: 0;
                Vector2 position = new Vector2(b2dCmp.renderPosition.x + localPositionX, b2dCmp.renderPosition.y - 3*b2dCmp.height/8);
                DamageArea area = new DamageArea(position, CoreGame.BIT_BOSS, bossCmp.direction,bossCmp.type.getWidth()/2, 3*bossCmp.type.getHeight()/4, bossCmp.attack, EffectType.NONE, false, 0);
                game.getEcsEngine().createDamageArea(area);
                attacking = true;
                Gdx.app.debug("TAG", "OK");
            }
        }
    }

    private static void createSkill1(Entity entity) {
        BossComponent bossCmp = ECSEngine.bossCmpMapper.get(entity);
        Box2DComponent b2dCmp = ECSEngine.box2dCmpMapper.get(entity);
        float localPositionX = bossCmp.direction == DirectionType.LEFT ? -b2dCmp.width/2: 0;
        Vector2 position = new Vector2(b2dCmp.renderPosition.x + localPositionX, b2dCmp.renderPosition.y - b2dCmp.height/4);
        BossSkillType type = bossCmp.type.getSkill1();
        DamageArea area = new DamageArea(position, CoreGame.BIT_BOSS, bossCmp.direction,bossCmp.type.getWidth()/2, bossCmp.type.getHeight()/2, type.getDamage() , type.getEffectType(), true, type.getSpeed());
        thisGame.getEcsEngine().createDamageArea(area);
    }
    private static void createSkill2(Entity entity) {

    }
}
