// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.NodeExtender;

import java.util.Vector;

public class ChanNodeEx extends NodeExtender
{
    protected ChanNodeEx parent;
    protected int subTreeSize;
    protected int gridX;
    protected int gridY;
    protected int boundX;
    protected int boundY;
    protected int boundWidth;
    protected int boundHeight;
    
    public ChanNodeEx() {
        this.parent = null;
    }
    
    public void setParent(final ChanNodeEx parent) {
        this.parent = parent;
    }
    
    public ChanNodeEx getParent() {
        return this.parent;
    }
    
    public void setSubTreeSize(final int subTreeSize) {
        this.subTreeSize = subTreeSize;
    }
    
    public int getSubTreeSize() {
        return this.subTreeSize;
    }
    
    public Vector getChildren() {
        final Vector edges = this.incidentEdges();
        final Vector children = new Vector(edges.size());
        for (int i = 0; i < edges.size(); ++i) {
            final ChanNodeEx node = (ChanNodeEx)edges.elementAt(i).otherEndFrom(this);
            if (node != this.parent) {
                children.addElement(node);
            }
        }
        return children;
    }
    
    public ChanNodeEx getLeftChild() {
        if ((this.parent == null && (this.refNode.getNumEdges() > 2 || this.refNode.getNumEdges() < 1)) || (this.parent != null && (this.refNode.getNumEdges() > 3 || this.refNode.getNumEdges() < 2))) {
            return null;
        }
        return this.getChildren().elementAt(0);
    }
    
    public ChanNodeEx getRightChild() {
        if ((this.parent == null && this.refNode.getNumEdges() != 2) || (this.parent != null && this.refNode.getNumEdges() != 3)) {
            return null;
        }
        return this.getChildren().elementAt(1);
    }
    
    public int getGridX() {
        return this.gridX;
    }
    
    public void setGridX(final int gridX) {
        this.gridX = gridX;
    }
    
    public int getGridY() {
        return this.gridY;
    }
    
    public void setGridY(final int gridY) {
        this.gridY = gridY;
    }
    
    public void shiftX(final int shiftX) {
        this.gridX += shiftX;
    }
    
    public void shiftY(final int shiftY) {
        this.gridY += shiftY;
    }
    
    public int getBoundX() {
        return this.boundX;
    }
    
    public void setBoundX(final int boundX) {
        this.boundX = boundX;
    }
    
    public int getBoundY() {
        return this.boundY;
    }
    
    public void setBoundY(final int boundY) {
        this.boundY = boundY;
    }
    
    public int getBoundWidth() {
        return this.boundWidth;
    }
    
    public void setBoundWidth(final int boundWidth) {
        this.boundWidth = boundWidth;
    }
    
    public int getBoundHeight() {
        return this.boundHeight;
    }
    
    public void setBoundHeight(final int boundHeight) {
        this.boundHeight = boundHeight;
    }
    
    public String toString() {
        return this.refNode.getLocation() + " " + this.gridX + " " + this.gridY + " " + this.boundX + " " + this.boundWidth + " " + this.boundY + " " + this.boundHeight + " " + this.subTreeSize;
    }
}
