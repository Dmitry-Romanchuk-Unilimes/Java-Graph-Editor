// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import java.awt.*;
import java.util.Vector;

public abstract class NodeExtender implements NodeInterface
{
    protected Node refNode;
    
    public NodeExtender() {
    }
    
    public NodeExtender(final NodeExtender node) {
        (this.refNode = new Node(node.getRef())).setExtender(this);
    }
    
    public void setRef(final Node refNode) {
        this.refNode = refNode;
    }
    
    public Node getRef() {
        return this.refNode;
    }
    
    public Node getNode() {
        return this.refNode;
    }
    
    public int getX() {
        return this.refNode.getX();
    }
    
    public int getY() {
        return this.refNode.getY();
    }
    
    public Vector incidentEdges() {
        final Vector incidentEdges = this.refNode.incidentEdges();
        final Vector toReturn = new Vector(incidentEdges.size());
        for (int i = 0; i < incidentEdges.size(); ++i) {
            toReturn.addElement(incidentEdges.elementAt(i).getExtender());
        }
        return toReturn;
    }
    
    public Vector incidentOutgoingEdges() {
        final Vector incidentEdges = this.refNode.incidentOutgoingEdges();
        final Vector toReturn = new Vector(incidentEdges.size());
        for (int i = 0; i < incidentEdges.size(); ++i) {
            toReturn.addElement(incidentEdges.elementAt(i).getExtender());
        }
        return toReturn;
    }
    
    public EdgeIterator incidentEdgesIterator() {
        return this.refNode.incidentEdgesIterator();
    }
    
    public EdgeInterface incidentEdgeWith(final NodeInterface aNode) {
        return ((Edge)this.refNode.incidentEdgeWith(((NodeExtender)aNode).getRef())).getExtender();
    }
    
    public boolean hasNoIncidentEdges() {
        return this.refNode.hasNoIncidentEdges();
    }
    
    public boolean hasEdge(final EdgeInterface edge) {
        return this.refNode.hasEdge(((EdgeExtender)edge).getRef());
    }
    
    public void draw(final Graphics2D g2, final boolean drawSelected, final boolean showCoord, final boolean showLabel) {
        this.refNode.draw(g2, drawSelected, showCoord, showLabel);
    }
    
    public NodeInterface getCopy() {
        if (this.refNode.getCopy() == null) {
            return null;
        }
        return ((Node)this.refNode.getCopy()).getExtender();
    }
    
    public NodeInterface getMasterCopy() {
        if (this.refNode.getMasterCopy() == null) {
            return null;
        }
        return ((Node)this.refNode.getMasterCopy()).getExtender();
    }
    
    public void setCopy(final NodeInterface aCopy) {
        if (aCopy == null) {
            this.refNode.setCopy(null);
        }
        else {
            this.refNode.setCopy(((NodeExtender)aCopy).getRef());
        }
    }
    
    public void addIncidentEdgeNoCheck(final EdgeInterface edge) {
        this.refNode.addIncidentEdgeNoCheck(((EdgeExtender)edge).getRef());
    }
    
    public boolean addIncidentEdge(final EdgeInterface edge) {
        return this.refNode.addIncidentEdge(((EdgeExtender)edge).getRef());
    }
    
    public void deleteIncidentEdge(final EdgeInterface edge) {
        this.refNode.deleteIncidentEdge(((EdgeExtender)edge).getRef());
    }
    
    public void resetIncidentEdges() {
        this.refNode.resetIncidentEdges();
    }
    
    public void addEdgeBetween(final EdgeInterface edge, final EdgeInterface prev, final EdgeInterface next) {
        this.refNode.addEdgeBetween(((EdgeExtender)edge).getRef(), ((EdgeExtender)prev).getRef(), ((EdgeExtender)next).getRef());
    }
    
    public Location getLocation() {
        return this.refNode.getLocation();
    }
    
    public void setLocation(final Location aLocation) {
        this.refNode.setLocation(aLocation);
    }
    
    public int getIndex() {
        return this.refNode.getIndex();
    }
    
    public void setIndex(final int index) {
        this.refNode.setIndex(index);
    }
    
    public void setColor(final Color aColor) {
        this.refNode.setColor(aColor);
    }
    
    public Color getColor() {
        return this.refNode.getColor();
    }
    
    public void setLabel(final String text) {
        this.refNode.setLabel(text);
    }
    
    public void appendLabel(final String newLabel) {
        this.refNode.appendLabel(newLabel);
    }
    
    public String getLabel() {
        return this.refNode.getLabel();
    }
    
    public void setDrawX(final boolean draw) {
        this.refNode.setDrawX(draw);
    }
    
    public boolean getDrawX() {
        return this.refNode.getDrawX();
    }
    
    public boolean equals(final Object o) {
        return o instanceof NodeExtender && this.refNode.equals(((NodeExtender)o).refNode);
    }
    
    public String toString() {
        return String.valueOf(this.getClass().getName()) + " - " + this.refNode;
    }
    
    public static Vector toNode(final Vector nodeExVector) {
        final Vector nodeVector = new Vector(nodeExVector.size());
        for (int i = 0; i < nodeExVector.size(); ++i) {
            nodeVector.addElement(nodeExVector.elementAt(i).getRef());
        }
        return nodeVector;
    }
}
