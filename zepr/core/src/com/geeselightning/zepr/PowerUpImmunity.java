package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpImmunity extends PowerUp {

    public float timeRemaining = Constant.IMMUNITYTIME;

    public PowerUpImmunity(Level currentLevel) {
        super(3, new Texture("immunity.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        Level.getPlayer().isImmune = true;
        this.getTexture().dispose();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Level.getPlayer().isImmune = false;
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
