package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpInvisibility extends PowerUp {

    public float timeRemaining = Constant.INVISIBILITYTIME;

    public PowerUpInvisibility(Level currentLevel) {
        super(5, new Texture("invisibility.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        Level.getPlayer().canBeSeen = false; //player is undetectable for 5 seconds
        this.getTexture().dispose();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Level.getPlayer().canBeSeen = true;
    }

    @Override
    public void update(float delta) {
        if (active) {
            timeRemaining -= delta;
        }
        if (timeRemaining < 0) {
            deactivate();
        }
    }
}
