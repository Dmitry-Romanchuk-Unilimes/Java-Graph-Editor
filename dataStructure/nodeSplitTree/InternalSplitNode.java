// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.nodeSplitTree;

import graphStructure.Graph;
import graphStructure.Node;

import java.awt.*;
import java.util.Vector;

public class InternalSplitNode extends SplitNode
{
    private SplitNode leftChild;
    private SplitNode rightChild;
    
    public InternalSplitNode(final int splitDepth, final int splitCount, final Vector nodes) {
        final Vector lesser = new Vector();
        final Vector greater = new Vector();
        this.splitCount = splitCount;
        if (splitCount % 2 == 0) {
            this.splitNode = Graph.partitionAroundMedianX(nodes, lesser, greater);
        }
        else {
            this.splitNode = Graph.partitionAroundMedianY(nodes, lesser, greater);
        }
        if (lesser.size() == 0) {
            final SplitNode splitNode = null;
            this.rightChild = splitNode;
            this.leftChild = splitNode;
        }
        else if (lesser.size() == 1) {
            this.leftChild = new InternalSplitNode(splitDepth, splitCount + 1, lesser);
            if (greater.size() == 1) {
                this.rightChild = new InternalSplitNode(splitDepth, splitCount + 1, greater);
            }
            else {
                this.rightChild = null;
            }
        }
        else if (lesser.size() == 2) {
            this.leftChild = new InternalSplitNode(splitDepth, splitCount + 1, lesser);
            this.rightChild = new InternalSplitNode(splitDepth, splitCount + 1, greater);
        }
        else if (splitCount < splitDepth) {
            this.leftChild = new InternalSplitNode(splitDepth, splitCount + 1, lesser);
            this.rightChild = new InternalSplitNode(splitDepth, splitCount + 1, greater);
        }
        else {
            this.leftChild = new ExternalSplitNode(splitCount, lesser);
            this.rightChild = new ExternalSplitNode(splitCount, greater);
        }
    }
    
    public Node nodeAt(final Point aPoint, final int radius) {
        if (this.splitNode.contains(aPoint, radius)) {
            return this.splitNode;
        }
        if (this.splitCount % 2 == 0) {
            if (aPoint.x < this.splitNode.getX()) {
                if (this.leftChild == null) {
                    return null;
                }
                return this.leftChild.nodeAt(aPoint, radius);
            }
            else {
                if (aPoint.x <= this.splitNode.getX()) {
                    Node ret = null;
                    if (this.leftChild != null) {
                        ret = this.leftChild.nodeAt(aPoint, radius);
                    }
                    if (ret == null && this.rightChild != null) {
                        ret = this.rightChild.nodeAt(aPoint, radius);
                    }
                    return ret;
                }
                if (this.rightChild == null) {
                    return null;
                }
                return this.rightChild.nodeAt(aPoint, radius);
            }
        }
        else if (aPoint.y < this.splitNode.getY()) {
            if (this.leftChild == null) {
                return null;
            }
            return this.leftChild.nodeAt(aPoint, radius);
        }
        else {
            if (aPoint.y <= this.splitNode.getY()) {
                Node ret = null;
                if (this.leftChild != null) {
                    ret = this.leftChild.nodeAt(aPoint, radius);
                }
                if (ret == null && this.rightChild != null) {
                    ret = this.rightChild.nodeAt(aPoint, radius);
                }
                return ret;
            }
            if (this.rightChild == null) {
                return null;
            }
            return this.rightChild.nodeAt(aPoint, radius);
        }
    }
    
    public void printNode() {
        ++NodeSplitTree.count;
        System.out.println(this + " L: " + this.leftChild + " R: " + this.rightChild);
        if (this.leftChild != null) {
            this.leftChild.printNode();
        }
        if (this.rightChild != null) {
            this.rightChild.printNode();
        }
    }
    
    public String toString() {
        if (this.splitNode == null) {
            return "null";
        }
        return this.splitNode.toString();
    }
}
