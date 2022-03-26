// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.pqTree;

public class PQDNode extends PQNode
{
    private boolean readInReverseDirection;
    
    public PQDNode(final Object data) {
        super(data);
        this.siblings = new PQNodePairDirected(-1);
        this.type = 3;
        this.readInReverseDirection = false;
    }
    
    public void setReadInReverseDirection(final boolean readInReverse) {
        this.readInReverseDirection = readInReverse;
    }
    
    public boolean readInReverseDirection() {
        return this.readInReverseDirection;
    }
    
    public void toggleReadInReverseDirection() {
        this.readInReverseDirection = !this.readInReverseDirection;
    }
    
    public void setDirection(final PQNode directedNode) throws Exception {
        if (this.siblings.contains(directedNode)) {
            ((PQNodePairDirected)this.siblings).setDirectedTowards(directedNode);
        }
    }
    
    public PQNode getDirection() {
        return ((PQNodePairDirected)this.siblings).getDirectedTowards();
    }
    
    public String infoString() {
        return String.valueOf(super.infoString()) + " ==> " + ((PQNodePairDirected)this.siblings).getDirectedTowards().infoString();
    }
    
    public String toString() {
        return "D" + super.toString() + " ==> " + ((PQNodePairDirected)this.siblings).getDirectedTowards();
    }
}
