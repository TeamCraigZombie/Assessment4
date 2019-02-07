package com.geeselightning.zepr.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

public class ConnectionImp implements Connection<Node> {
    private Node toNode;
    private Node fromNode;
    private float cost;

    public ConnectionImp(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public float getCost() {
        return cost;
    }
}
