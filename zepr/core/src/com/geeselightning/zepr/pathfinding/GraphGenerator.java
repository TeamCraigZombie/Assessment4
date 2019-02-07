package com.geeselightning.zepr.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.geeselightning.zepr.Level;

import java.util.Iterator;

import static com.geeselightning.zepr.MapBodyBuilder.getRectangle;

public class GraphGenerator {
    public static GraphImp generateGraph(TiledMap map) {
        Array<Node> nodes = new Array<>();

        TiledMapTileLayer tiles = (TiledMapTileLayer) map.getLayers().get("Background");
        // cannot use object layer, so make sure all tiles with objects in them are in own "Walls" layer
        TiledMapTileLayer collisionTiles = (TiledMapTileLayer) map.getLayers().get("Walls");
        int mapHeight = Level.lvlTileHeight;
        int mapWidth = Level.lvlTileWidth;

        // add every tile as a node
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                // generate a node for each tile
                Node node = new Node();
                node.type = Node.Type.REGULAR;
                nodes.add(node);
            }
        }

        Gdx.app.log("Size", ""+nodes.size);
        // add connections between every node
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                TiledMapTileLayer.Cell target = tiles.getCell(x, y);
                TiledMapTileLayer.Cell up = tiles.getCell(x, y+1);
                TiledMapTileLayer.Cell upLeft = tiles.getCell(x-1, y+1);
                TiledMapTileLayer.Cell upRight = tiles.getCell(x+1, y+1);
                TiledMapTileLayer.Cell left = tiles.getCell(x-1, y);
                TiledMapTileLayer.Cell right = tiles.getCell(x+1, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y-1);
                TiledMapTileLayer.Cell downLeft = tiles.getCell(x-1, y-1);
                TiledMapTileLayer.Cell downRight = tiles.getCell(x+1, y-1);

                TiledMapTileLayer.Cell collisionTarget = collisionTiles.getCell(x, y);
                TiledMapTileLayer.Cell collisionUp = collisionTiles.getCell(x, y+1);
                TiledMapTileLayer.Cell collisionUpLeft = collisionTiles.getCell(x-1, y+1);
                TiledMapTileLayer.Cell collisionUpRight = collisionTiles.getCell(x+1, y+1);
                TiledMapTileLayer.Cell collisionLeft = collisionTiles.getCell(x-1, y);
                TiledMapTileLayer.Cell collisionRight = collisionTiles.getCell(x+1, y);
                TiledMapTileLayer.Cell collisionDown = collisionTiles.getCell(x, y-1);
                TiledMapTileLayer.Cell collisionDownLeft = collisionTiles.getCell(x-1, y-1);
                TiledMapTileLayer.Cell collisionDownRight = collisionTiles.getCell(x+1, y-1);

                Node targetNode = nodes.get(mapWidth * y + x);  //same as nodes[x][y], but nodes is 1D
                if (target != null && collisionTarget == null) {
                    if (y != 0 && down != null && collisionDown == null) {
                        Node downNode = nodes.get(mapWidth * (y - 1) + x);
                        targetNode.createConnection(downNode, 1);
                    }
                    if (x != 0 && y != 0 && downLeft != null && collisionDownLeft == null) {
                        Node downLeftNode = nodes.get(mapWidth * (y - 1) + (x - 1));
                        targetNode.createConnection(downLeftNode, (float) Math.sqrt(2));
                    }
                    if (x != mapWidth - 1 && y != 0 && downRight != null && collisionDownRight == null) {
                        Node downRightNode = nodes.get(mapWidth * (y - 1) + (x + 1));
                        targetNode.createConnection(downRightNode, (float) Math.sqrt(2));
                    }
                    if (x != 0 && left != null && collisionLeft == null) {
                        Node leftNode = nodes.get(mapWidth * y + (x - 1));
                        targetNode.createConnection(leftNode, 1);
                    }
                    if (x != mapWidth - 1 && right != null && collisionRight == null) {
                        Node rightNode = nodes.get(mapWidth * y + (x + 1));
                        targetNode.createConnection(rightNode, 1);
                    }
                    if (y != mapHeight - 1 && up != null && collisionUp == null) {
                        Node upNode = nodes.get(mapWidth * (y + 1) - x);
                        targetNode.createConnection(upNode, 1);
                    }
                    if (x != 0 && y != mapHeight - 1 && upLeft != null && collisionUpLeft == null) {
                        Node upLeftNode = nodes.get(mapWidth * (y + 1) + (x - 1));
                        targetNode.createConnection(upLeftNode, (float) Math.sqrt(2));
                    }
                    if (x != mapWidth - 1 && y != mapHeight - 1 && upRight != null && collisionUpRight == null) {
                        Node upRightNode = nodes.get(mapWidth * (y + 1) + (x + 1));
                        targetNode.createConnection(upRightNode, (float) Math.sqrt(2));
                    }
                }
            }
        }

        return new GraphImp(nodes);
    }
}
