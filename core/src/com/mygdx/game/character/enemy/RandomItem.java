package com.mygdx.game.character.enemy;


import com.mygdx.game.items.food.FoodType;

import java.util.Random;

public class RandomItem {
    public static FoodType randomFood() {
        FoodType listFood[] = FoodType.values();
        Random random = new Random();

        // Tạo số nguyên ngẫu nhiên
        int randomNumber = random.nextInt(1000);
        return listFood[randomNumber % listFood.length];
    }
}
