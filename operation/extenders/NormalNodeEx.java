// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.NodeExtender;

import java.util.Vector;

public class NormalNodeEx extends NodeExtender
{
    protected NormalNodeEx r1Parent;
    protected NormalNodeEx r2Parent;
    protected NormalNodeEx r3Parent;
    protected int canonicalNumber;
    
    public NormalNodeEx() {
    }
    
    public NormalNodeEx(final NormalNodeEx node) {
        super(node);
    }
    
    public void setR1Parent(final NormalNodeEx r1) {
        this.r1Parent = r1;
    }
    
    public NormalNodeEx getR1Parent() {
        return this.r1Parent;
    }
    
    public void setR2Parent(final NormalNodeEx r2) {
        this.r2Parent = r2;
    }
    
    public NormalNodeEx getR2Parent() {
        return this.r2Parent;
    }
    
    public void setR3Parent(final NormalNodeEx r3) {
        this.r3Parent = r3;
    }
    
    public NormalNodeEx getR3Parent() {
        return this.r3Parent;
    }
    
    public void setCanonicalNumber(final int canon) {
        this.canonicalNumber = canon;
    }
    
    public int getCanonicalNumber() {
        return this.canonicalNumber;
    }
    
    public Vector incidentEdgesToSmallerCanonicalNumber() {
        final Vector incidentEdges = this.incidentEdges();
        int startIndex = -1;
        NormalEdgeEx currentEdge = incidentEdges.firstElement();
        if (((NormalNodeEx)currentEdge.otherEndFrom(this)).getCanonicalNumber() < this.canonicalNumber) {
            int i = incidentEdges.size() - 1;
            while (i >= 0) {
                currentEdge = incidentEdges.elementAt(i);
                if (((NormalNodeEx)currentEdge.otherEndFrom(this)).getCanonicalNumber() > this.canonicalNumber) {
                    if (i < incidentEdges.size() - 1) {
                        startIndex = i + 1;
                        break;
                    }
                    startIndex = 0;
                    break;
                }
                else {
                    --i;
                }
            }
        }
        else {
            for (int i = 0; i < incidentEdges.size(); ++i) {
                currentEdge = incidentEdges.elementAt(i);
                if (((NormalNodeEx)currentEdge.otherEndFrom(this)).getCanonicalNumber() < this.canonicalNumber) {
                    startIndex = i;
                    break;
                }
            }
        }
        final Vector edgeVector = new Vector(incidentEdges.size());
        if (startIndex != -1) {
            for (int j = 0; j < incidentEdges.size(); ++j) {
                currentEdge = incidentEdges.elementAt((startIndex + j) % incidentEdges.size());
                if (((NormalNodeEx)currentEdge.otherEndFrom(this)).getCanonicalNumber() >= this.canonicalNumber) {
                    break;
                }
                edgeVector.addElement(currentEdge);
            }
        }
        return edgeVector;
    }
}
