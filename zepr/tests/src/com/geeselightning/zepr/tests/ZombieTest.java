package com.geeselightning.zepr.tests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.geeselightning.zepr.Constant;
import com.geeselightning.zepr.Player;
import com.geeselightning.zepr.Zombie;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ZombieTest {



    @Test
    // Test 3.1
    public void zombieDoesNoDamageToPlayerWhenAtMaxRange() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), Constant.ORIGIN, world);

        Zombie zombie = new Zombie(new Vector2(player.getCenter().x, player.getCenter().y - Constant.ZOMBIERANGE), world, Zombie.Type.ZOMBIE1);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertEquals("Player on the edge of range should not take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 3.2
    public void zombieDoesDamageToPlayerWhenInRange() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), Constant.ORIGIN, world);

        Zombie zombie = new Zombie(new Vector2(player.getCenter().x, player.getCenter().y - Constant.ZOMBIERANGE + 5), world, Zombie.Type.ZOMBIE1);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertNotEquals("Player within range should take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
        player.dispose();
        world.dispose();
    }


    @Test
    // Test 3.3
    public void zombieDoesNoDamageToPlayerOutOfRange() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), Constant.ORIGIN, world);

        Zombie zombie = new Zombie(new Vector2(player.getCenter().x, player.getCenter().y - 100), world, Zombie.Type.ZOMBIE1);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);

        assertEquals("Player outside of range should not take damage when the zombie attacks.",
                player.getHealth(), originalHealth, 0.1);
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 3.4
    public void zombieCannotAttackBeforeCooldownComplete() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), Constant.ORIGIN, world);

        Zombie zombie = new Zombie(new Vector2(player.getCenter().x, player.getCenter().y ), world, Zombie.Type.ZOMBIE1);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);
        zombie.attack(player, 0);

        assertEquals("Player should only have taken one hit if attacked again before cooldown complete.",
                originalHealth - Constant.ZOMBIEDMG, player.getHealth(), 0.1);
        player.dispose();
        world.dispose();
    }

    @Test
    // Test 3.5
    public void zombieCanAttackAfterCooldownComplete() {
        World world = new World(new Vector2(0, 0), true);
        Player player = new Player(new Texture("player01.png"), Constant.ORIGIN, world);

        Zombie zombie = new Zombie(new Vector2(player.getCenter().x, player.getCenter().y ), world, Zombie.Type.ZOMBIE1);
        double originalHealth = player.getHealth();
        zombie.attack(player, 0);
        // zombie will not attack this go so has to be called a third time
        zombie.attack(player, Constant.ZOMBIEHITCOOLDOWN + 1);
        zombie.attack(player, 0);

        assertEquals("Player should have taken two hits if attacked again after cooldown complete.",
                originalHealth - (2 * Constant.ZOMBIEDMG), player.getHealth(), 0.1);
        player.dispose();
        world.dispose();
    }

}
