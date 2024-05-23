package com.mygdx.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class UIComponent implements Component, Pool.Poolable{

    public boolean isShow;

    @Override
    public void reset() {

    }
}
