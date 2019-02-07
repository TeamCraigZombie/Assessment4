package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class PowerUpSpeed extends PowerUp {

    public PowerUpSpeed(Level currentLevel, Player player) {
        super(2, new Texture("speed.png"), currentLevel, player);
    }

    @Override
    public void activate() {
        super.activate();
        player.speed += Constant.SPEEDUP;
        timeRemaining = Constant.SPEEDUPTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        player.speed -= Constant.SPEEDUP;
    }
}
