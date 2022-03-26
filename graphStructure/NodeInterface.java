// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import java.awt.*;
import java.util.Vector;

public interface NodeInterface
{
    Vector incidentEdges();
    
    Vector incidentOutgoingEdges();
    
    EdgeIterator incidentEdgesIterator();
    
    EdgeInterface incidentEdgeWith(final NodeInterface p0);
    
    boolean hasNoIncidentEdges();
    
    boolean hasEdge(final EdgeInterface p0);
    
    void draw(final Graphics2D p0, final boolean p1, final boolean p2, final boolean p3);
    
    NodeInterface getCopy();
    
    NodeInterface getMasterCopy();
    
    void setCopy(final NodeInterface p0);
    
    void addIncidentEdgeNoCheck(final EdgeInterface p0);
    
    boolean addIncidentEdge(final EdgeInterface p0);
    
    void deleteIncidentEdge(final EdgeInterface p0);
    
    void resetIncidentEdges();
    
    void addEdgeBetween(final EdgeInterface p0, final EdgeInterface p1, final EdgeInterface p2);
    
    Location getLocation();
    
    void setLocation(final Location p0);
    
    int getX();
    
    int getY();
    
    int getIndex();
    
    void setIndex(final int p0);
    
    void setColor(final Color p0);
    
    Color getColor();
    
    void setLabel(final String p0);
    
    void appendLabel(final String p0);
    
    String getLabel();
    
    void setDrawX(final boolean p0);
    
    Node getNode();
    
    boolean getDrawX();
}
