// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import java.awt.*;

public interface EdgeInterface
{
    NodeInterface otherEndFrom(final NodeInterface p0);
    
    boolean isBetween(final NodeInterface p0, final NodeInterface p1);
    
    NodeInterface getStartNode();
    
    NodeInterface getEndNode();
    
    boolean isGenerated();
    
    EdgeInterface getCopy();
    
    EdgeInterface getMasterCopy();
    
    void setCopy(final EdgeInterface p0);
    
    EdgeInterface getPreviousInOrderFrom(final NodeInterface p0);
    
    EdgeInterface getNextInOrderFrom(final NodeInterface p0);
    
    void setNextInOrderFrom(final NodeInterface p0, final EdgeInterface p1);
    
    void setPreviousInOrderFrom(final NodeInterface p0, final EdgeInterface p1);
    
    int getHigherIndex();
    
    int getLowerIndex();
    
    void setColor(final Color p0);
    
    Color getColor();
    
    Node getDirectedSourceNode();
    
    void setDirectedFrom(final NodeInterface p0);
    
    double getStraightLength();
    
    double getLength();
    
    void makeStraight();
    
    void update();
    
    Edge getEdge();
}
