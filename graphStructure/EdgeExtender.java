// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import java.awt.*;
import java.util.Vector;

public abstract class EdgeExtender implements EdgeInterface
{
    protected Edge refEdge;
    
    public EdgeExtender() {
    }
    
    public EdgeExtender(final NodeExtender start, final NodeExtender end) {
        (this.refEdge = new Edge(start.getRef(), end.getRef())).setExtender(this);
    }
    
    public EdgeExtender(final EdgeExtender anEdgeEx, final NodeExtender dNode, final NodeExtender start, final NodeExtender end) {
        (this.refEdge = new Edge(anEdgeEx.getRef(), dNode.getRef(), start.getRef(), end.getRef())).setExtender(this);
    }
    
    public void setRef(final Edge refEdge) {
        this.refEdge = refEdge;
    }
    
    public Edge getRef() {
        return this.refEdge;
    }
    
    public Edge getEdge() {
        return this.refEdge;
    }
    
    public NodeInterface otherEndFrom(final NodeInterface aNode) {
        return ((Node)this.refEdge.otherEndFrom(((NodeExtender)aNode).getRef())).getExtender();
    }
    
    public boolean isBetween(final NodeInterface firstNode, final NodeInterface secondNode) {
        return this.refEdge.isBetween(((NodeExtender)firstNode).getRef(), ((NodeExtender)secondNode).getRef());
    }
    
    public NodeInterface getStartNode() {
        return ((Node)this.refEdge.getStartNode()).getExtender();
    }
    
    public NodeInterface getEndNode() {
        return ((Node)this.refEdge.getEndNode()).getExtender();
    }
    
    public boolean isGenerated() {
        return this.refEdge.isGenerated();
    }
    
    public void setIsGenerated(final boolean isGenerated) {
        this.refEdge.setIsGenerated(isGenerated);
    }
    
    public EdgeInterface getCopy() {
        if (this.refEdge.getCopy() == null) {
            return null;
        }
        return ((Edge)this.refEdge.getCopy()).getExtender();
    }
    
    public EdgeInterface getMasterCopy() {
        if (this.refEdge.getMasterCopy() == null) {
            return null;
        }
        return ((Edge)this.refEdge.getMasterCopy()).getExtender();
    }
    
    public void setCopy(final EdgeInterface aCopy) {
        if (aCopy == null) {
            this.refEdge.setCopy(null);
        }
        else {
            this.refEdge.setCopy(((EdgeExtender)aCopy).getRef());
        }
    }
    
    public EdgeInterface getPreviousInOrderFrom(final NodeInterface sourceNode) {
        return ((Edge)this.refEdge.getPreviousInOrderFrom(((NodeExtender)sourceNode).getRef())).getExtender();
    }
    
    public EdgeInterface getNextInOrderFrom(final NodeInterface sourceNode) {
        return ((Edge)this.refEdge.getNextInOrderFrom(((NodeExtender)sourceNode).getRef())).getExtender();
    }
    
    public void setNextInOrderFrom(final NodeInterface sourceNode, final EdgeInterface nextEdge) {
        this.refEdge.setNextInOrderFrom(((NodeExtender)sourceNode).getRef(), ((EdgeExtender)nextEdge).getRef());
    }
    
    public void setPreviousInOrderFrom(final NodeInterface sourceNode, final EdgeInterface prevEdge) {
        this.refEdge.setPreviousInOrderFrom(((NodeExtender)sourceNode).getRef(), ((EdgeExtender)prevEdge).getRef());
    }
    
    public int getHigherIndex() {
        return this.refEdge.getHigherIndex();
    }
    
    public int getLowerIndex() {
        return this.refEdge.getLowerIndex();
    }
    
    public void setColor(final Color aColor) {
        this.refEdge.setColor(aColor);
    }
    
    public Color getColor() {
        return this.refEdge.getColor();
    }
    
    public Node getDirectedSourceNode() {
        return this.refEdge.getDirectedSourceNode();
    }
    
    public void setDirectedFrom(final NodeInterface directedSourceNode) {
        if (directedSourceNode == null) {
            this.refEdge.setDirectedFrom(null);
        }
        else {
            this.refEdge.setDirectedFrom(((NodeExtender)directedSourceNode).getRef());
        }
    }
    
    public boolean equals(final Object o) {
        return o instanceof EdgeExtender && this.refEdge.equals(((EdgeExtender)o).refEdge);
    }
    
    public String toString() {
        return String.valueOf(this.getClass().getName()) + " - " + this.refEdge;
    }
    
    public static Vector toEdge(final Vector edgeExVector) {
        final Vector edgeVector = new Vector(edgeExVector.size());
        for (int i = 0; i < edgeExVector.size(); ++i) {
            edgeVector.addElement(edgeExVector.elementAt(i).getRef());
        }
        return edgeVector;
    }
    
    public double getStraightLength() {
        return this.refEdge.getStraightLength();
    }
    
    public double getLength() {
        return this.refEdge.getLength();
    }
    
    public void makeStraight() {
        this.refEdge.makeStraight();
    }
    
    public void update() {
        this.refEdge.update();
    }
}
