package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpInvisibility extends PowerUp {

    public PowerUpInvisibility(Level currentLevel) {
        super(5, new Texture("invisibility.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        Level.getPlayer().canBeSeen = false; //player is undetectable for 5 seconds
        timeRemaining = Constant.INVISIBILITYTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Level.getPlayer().canBeSeen = true;
    }
}
