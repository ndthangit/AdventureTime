package com.mygdx.game.character.enemy;


import com.mygdx.game.items.food.FoodType;

import java.util.Random;

public class RandomItem {
    public static FoodType randomFood() {
        FoodType listFood[] = FoodType.values();
        Random random = new Random();

        // Tạo số nguyên ngẫu nhiên trong khoảng từ 0 đến 99
        int randomNumber = random.nextInt(listFood.length-1);
        return listFood[randomNumber];
    }
}
