package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;

public class PowerUpSpeed extends PowerUp {

    public PowerUpSpeed(Level currentLevel) {
        super(2, new Texture("speed.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        Level.getPlayer().speed += Constant.SPEEDUP;
        timeRemaining = Constant.SPEEDUPTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Level.getPlayer().speed -= Constant.SPEEDUP;
    }
}
