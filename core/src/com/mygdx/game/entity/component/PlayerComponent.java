package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.mygdx.game.character.player.PlayerType;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.food.Food;
import com.mygdx.game.items.food.FoodType;
import com.mygdx.game.items.special.Special;
import com.mygdx.game.items.weapon.Weapon;
import com.mygdx.game.view.AnimationType;

public class PlayerComponent extends EntityComponent {
	public PlayerType aniType;
	// chỗ này sẽ cho thêm vi tri der them inventory de sau co the su dung

	public Item[] inventory = new Item[4]; // su dung de chua do an
	// số lượng vat pham co trong item, thuoc tinh quantity

	//tam tao vu khi tai day
	public Array<Weapon> weaponList; // danh sah cai vk trong nguoi
	public Weapon weapon; // vu khi tren tay
	public int indWeapon; // chi so tro den vu khi trong danh sach

	public boolean isAttack;
	public boolean isSkill;
	public boolean stop;
	public boolean vincible;

	public float reloadSkill1;
	public float reloadSkill2;
	public float durationSkill2;

	public float timeBuff;

	public int damageBuff;
	public float speedBuff;
	public float speedBuffSkill2 = 0;

	public boolean upgradeSkill1 = false;


	@Override
	public void reset() {
		super.reset();
		aniType = null;
		weapon = null;
		isSkill = false;
		isAttack = false;
	}

	public boolean useItem(int i) {
		if (inventory[i] != null) {
			Item item = inventory[i];
			if (item instanceof Food){
				Food food = (Food) item;
				life += food.type.getHeal();
				if (life > maxLife) {
					life = maxLife;
				}
				powerUp(food.type);
				inventory[i].quatity -= 1;
				if (inventory[i].quatity == 0) {
					inventory[i] = null;
				}
			}
			return true;
		}
		return false;
	}

	public boolean addItem(Item newItem) {
		if (newItem instanceof Food) {
			for (int i=0; i<4; i++) {
				if (inventory[i] != null && inventory[i].key.equals(newItem.key) && inventory[i].quatity < 5) {
					inventory[i].quatity += 1;
					return true;
				}
			}
			for (int i=0; i<4; i++) {
				if (inventory[i] == null) {
					inventory[i] = newItem;
					newItem.quatity = 1;
					return true;
				}
			}
			return false;
		}
		else if (newItem instanceof Weapon){
			weaponList.add((Weapon) newItem);
			return true;
		}
		else if (newItem instanceof Special) {
			Special special = (Special) newItem;
			switch (special.type) {
				case SCROLLFIRE:
					upgradeSkill1 = true;
					break;
				case LIFEPOT:
					maxLife += 4;
					life = maxLife;
					break;
			}
			return true;
		}
		return false;
	}

	private void powerUp(FoodType type) {
		resetBuff();
		damageBuff = type.getStrength();
		speedBuff = type.getSpeed();
		timeBuff = type.getTime();
	}
	public void resetBuff() {
		timeBuff = 0;
		damageBuff = 0;
		speedBuff = 0;
	}
}
