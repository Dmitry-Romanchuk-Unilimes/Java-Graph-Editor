// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.nodeSplitTree;

import graphStructure.Location;
import graphStructure.Node;

import java.awt.*;
import java.util.Vector;

public class NodeSplitTree
{
    public static int count;
    private SplitNode root;
    private int radius;
    private int splitDepth;
    
    public NodeSplitTree(final Vector nodes) {
        this(nodes, 3, Node.RADIUS);
    }
    
    public NodeSplitTree(final Vector nodes, final int splitDepth, final int radius) {
        this.splitDepth = splitDepth;
        this.radius = radius;
        if (splitDepth > 0) {
            this.root = new InternalSplitNode(splitDepth, 0, nodes);
        }
        else {
            this.root = new ExternalSplitNode(0, nodes);
        }
    }
    
    public Node nodeAt(final Point point) {
        return this.root.nodeAt(point, this.radius);
    }
    
    public Node nodeAt(final Location location) {
        return this.root.nodeAt(new Point(location.intX(), location.intY()), this.radius);
    }
    
    public void printTree() {
        System.out.println("NodeSplitTree:");
        NodeSplitTree.count = 0;
        this.root.printNode();
        System.out.println("\nnum nodes: " + NodeSplitTree.count + "\n\n");
    }
}
