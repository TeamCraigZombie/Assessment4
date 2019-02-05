package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

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
