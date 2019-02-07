package com.geeselightning.zepr.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Level;
import com.geeselightning.zepr.Player;

public class PowerUpInstaKill extends PowerUp {

    public PowerUpInstaKill(Level currentLevel, Player player) {
        super(4, new Texture("instakill.png"), currentLevel, player);
    }

    @Override
    public void activate() {
        super.activate();
        player.attackDamage = Constant.ZOMBIEMAXHP; //make zombies one hit kill
        timeRemaining = Constant.INSTAKILLTIME;
    }

    @Override
    public void deactivate() {
        super.deactivate();
        player.attackDamage = Constant.PLAYERDMG;
    }
}
