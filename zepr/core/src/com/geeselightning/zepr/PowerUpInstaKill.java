package com.geeselightning.zepr;

import com.badlogic.gdx.graphics.Texture;

public class PowerUpInstaKill extends PowerUp {

    public float timeRemaining = Constant.INSTAKILLTIME;

    public PowerUpInstaKill(Level currentLevel) {
        super(4, new Texture("instakill.png"), currentLevel);
    }

    @Override
    public void activate() {
        super.activate();
        Level.getPlayer().attackDamage = Constant.ZOMBIEMAXHP; //make zombies one hit kill
        this.getTexture().dispose();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Level.getPlayer().attackDamage = Constant.PLAYERDMG;
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
