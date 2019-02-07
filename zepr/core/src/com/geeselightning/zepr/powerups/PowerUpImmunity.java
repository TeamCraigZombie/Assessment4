package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

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
