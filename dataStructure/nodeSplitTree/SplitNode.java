// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.nodeSplitTree;

import graphStructure.Node;

import java.awt.*;

public abstract class SplitNode
{
    protected int splitCount;
    protected Node splitNode;
    
    public Node getSplitNode() {
        return this.splitNode;
    }
    
    public abstract Node nodeAt(final Point p0, final int p1);
    
    public abstract void printNode();
}
