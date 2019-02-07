package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;

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
