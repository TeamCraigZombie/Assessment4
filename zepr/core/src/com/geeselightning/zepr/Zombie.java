package com.geeselightning.zepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.geeselightning.zepr.pathfinding.GraphPathImp;
import com.geeselightning.zepr.pathfinding.HeuristicImp;
import com.geeselightning.zepr.pathfinding.Node;

public class Zombie extends Character {

    int attackDamage = Constant.ZOMBIEDMG;
    public int hitRange = Constant.ZOMBIERANGE;
    public final float hitCooldown = Constant.ZOMBIEHITCOOLDOWN;

    private IndexedAStarPathFinder<Node> pathFinder;
    private GraphPathImp resultPath = new GraphPathImp();

    public Zombie(Sprite sprite, Vector2 zombieSpawn, World world, float speed, int health) {
        super(sprite, zombieSpawn, world);
        this.speed = speed;
        maxhealth = this.health = health;

        pathFinder = new IndexedAStarPathFinder<>(Level.graph, false);

        int startX = (int) this.getPhysicsPosition().x;
        int startY = (int) this.getPhysicsPosition().y;

        int endX = (int) Level.getPlayer().getPhysicsPosition().x;
        int endY = (int) Level.getPlayer().getPhysicsPosition().y;

//        Gdx.app.log("start", "X " + startX + "Y " + startY);
//        Gdx.app.log("end", "X " + endX + "Y " + endY);

        Node startNode = Level.graph.getNodeByXY(startX, startY);
        Node endNode = Level.graph.getNodeByXY(endX, endY);

//        Gdx.app.log("start node", "" + startNode);
//        Gdx.app.log("end node", "" + endNode);

        pathFinder.searchNodePath(startNode, endNode, new HeuristicImp(), resultPath);
        Gdx.app.log("path", "" + resultPath.getCount());
    }

    public void attack(Player player, float delta) {
        if (canHitGlobal(player, hitRange) && hitRefresh > hitCooldown) {
            player.takeDamage(attackDamage);
            hitRefresh = 0;
        } else
            hitRefresh += delta;
    }

    @Override
    public void update() {
        //move according to velocity
        super.update();

        if (Level.getPlayer().canBeSeen) {
            // update velocity to move towards player
            // Vector2.scl scales the vector
            velocity = getDirNormVector(Level.getPlayer().getPixelPosition()).scl(speed);
            
            body.applyLinearImpulse(velocity, body.getPosition() , true);

            // update direction to face the player
            direction = getDirectionTo(Level.getPlayer().getCenter());
        }
    }
}
