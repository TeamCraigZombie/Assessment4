package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class PowerUpInvisibility extends PowerUp {

    public PowerUpInvisibility(Level currentLevel, Player player) {
        super(5, new Texture("invisibility.png"), currentLevel, player);
    }

    @Override
    public void activate() {
        super.activate();
        player.canBeSeen = false; //player is undetectable for 5 seconds
        timeRemaining = Constant.INVISIBILITYTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        player.canBeSeen = true;
    }
}