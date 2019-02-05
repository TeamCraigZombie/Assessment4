package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpImmunity extends PowerUp {

    public PowerUpImmunity(Level currentLevel, Player player) {
        super(3, new Texture("immunity.png"), currentLevel, player);
    }

    @Override
    public void activate() {
        super.activate();
        player.isImmune = true;
        timeRemaining = Constant.IMMUNITYTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        player.isImmune = false;
    }
}
