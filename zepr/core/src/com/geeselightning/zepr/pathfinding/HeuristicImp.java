package com.geeselightning.zepr.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.geeselightning.zepr.Level;

public class HeuristicImp implements Heuristic<Node> {
    @Override
    public float estimate(Node startNode, Node endNode) {
        int startIndex = startNode.getIndex();
        int endIndex = endNode.getIndex();

        int startX = startIndex % Level.lvlTileWidth;
        int startY = startIndex / Level.lvlTileWidth;
        int endX = endIndex % Level.lvlTileWidth;
        int endY = endIndex / Level.lvlTileWidth;

        // straight line distance heuristic
        float distance = (float) Math.sqrt(Math.pow(startX-endX,2) + Math.pow(startY-endY,2));

        return distance;
    }
}
