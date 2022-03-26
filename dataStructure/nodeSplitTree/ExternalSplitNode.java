// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.nodeSplitTree;

import graphStructure.Graph;
import graphStructure.Node;

import java.awt.*;
import java.util.Vector;

public class ExternalSplitNode extends SplitNode
{
    private Vector leftChildren;
    private Vector rightChildren;
    
    public ExternalSplitNode(final int splitCount, final Vector nodes) {
        this.splitCount = splitCount;
        this.leftChildren = new Vector();
        this.rightChildren = new Vector();
        if (splitCount % 2 == 0) {
            this.splitNode = Graph.partitionAroundMedianX(nodes, this.leftChildren, this.rightChildren);
        }
        else {
            this.splitNode = Graph.partitionAroundMedianY(nodes, this.leftChildren, this.rightChildren);
        }
    }
    
    public Node nodeAt(final Point aPoint, final int radius) {
        if (this.splitNode.contains(aPoint, radius)) {
            return this.splitNode;
        }
        if (this.splitCount % 2 == 0) {
            if (aPoint.x < this.splitNode.getX()) {
                for (int i = 0; i < this.leftChildren.size(); ++i) {
                    if (this.leftChildren.elementAt(i).contains(aPoint, radius)) {
                        return this.leftChildren.elementAt(i);
                    }
                }
            }
            else if (aPoint.x > this.splitNode.getX()) {
                for (int i = 0; i < this.rightChildren.size(); ++i) {
                    if (this.rightChildren.elementAt(i).contains(aPoint, radius)) {
                        return this.rightChildren.elementAt(i);
                    }
                }
            }
            else {
                for (int i = 0; i < this.leftChildren.size(); ++i) {
                    if (this.leftChildren.elementAt(i).contains(aPoint, radius)) {
                        return this.leftChildren.elementAt(i);
                    }
                }
                for (int i = 0; i < this.rightChildren.size(); ++i) {
                    if (this.rightChildren.elementAt(i).contains(aPoint, radius)) {
                        return this.rightChildren.elementAt(i);
                    }
                }
            }
        }
        else if (aPoint.y < this.splitNode.getY()) {
            for (int i = 0; i < this.leftChildren.size(); ++i) {
                if (this.leftChildren.elementAt(i).contains(aPoint, radius)) {
                    return this.leftChildren.elementAt(i);
                }
            }
        }
        else if (aPoint.y > this.splitNode.getY()) {
            for (int i = 0; i < this.rightChildren.size(); ++i) {
                if (this.rightChildren.elementAt(i).contains(aPoint, radius)) {
                    return this.rightChildren.elementAt(i);
                }
            }
        }
        else {
            for (int i = 0; i < this.leftChildren.size(); ++i) {
                if (this.leftChildren.elementAt(i).contains(aPoint, radius)) {
                    return this.leftChildren.elementAt(i);
                }
            }
            for (int i = 0; i < this.rightChildren.size(); ++i) {
                if (this.rightChildren.elementAt(i).contains(aPoint, radius)) {
                    return this.rightChildren.elementAt(i);
                }
            }
        }
        return null;
    }
    
    public void printNode() {
        ++NodeSplitTree.count;
        System.out.println(this + " L: " + this.leftChildren + " R: " + this.rightChildren);
    }
    
    public String toString() {
        if (this.splitNode == null) {
            return "null";
        }
        return this.splitNode.toString();
    }
}
