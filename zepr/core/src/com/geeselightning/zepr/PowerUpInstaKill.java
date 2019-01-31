package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpInstaKill extends PowerUp {

    public PowerUpInstaKill(Level currentLevel) {
        super(4, new Texture("instakill.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        Level.getPlayer().attackDamage = Constant.ZOMBIEMAXHP; //make zombies one hit kill
        timeRemaining = Constant.INSTAKILLTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Level.getPlayer().attackDamage = Constant.PLAYERDMG;
    }
}
