package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpImmunity extends PowerUp {

    public PowerUpImmunity(Level currentLevel) {
        super(3, new Texture("immunity.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        Level.getPlayer().isImmune = true;
        timeRemaining = Constant.IMMUNITYTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Level.getPlayer().isImmune = false;
    }
}
