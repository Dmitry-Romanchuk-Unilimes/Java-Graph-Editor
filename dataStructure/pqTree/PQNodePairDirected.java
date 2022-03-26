// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.pqTree;

public class PQNodePairDirected extends PQNodePair
{
    private int directedTowardsIndex;
    
    public PQNodePairDirected(final int directionIndex) {
        this.directedTowardsIndex = directionIndex;
    }
    
    public void setDirectedTowards(final PQNode directedTowards) {
        if (directedTowards == null) {
            this.directedTowardsIndex = -1;
        }
        else if (this.contains(directedTowards)) {
            this.directedTowardsIndex = this.indexOf(directedTowards);
        }
        else {
            this.directedTowardsIndex = -1;
        }
    }
    
    public PQNode getDirectedTowards() {
        if (this.directedTowardsIndex == -1) {
            return null;
        }
        return this.PQNodeAt(this.directedTowardsIndex);
    }
    
    public void addPQNode(final PQNode pqNode) throws Exception {
        super.addPQNode(pqNode);
        if (this.directedTowardsIndex == -1) {
            this.directedTowardsIndex = this.indexOf(pqNode);
        }
    }
    
    public boolean removePQNode(final PQNode pqNode) {
        if (this.directedTowardsIndex == 1 && this.indexOf(pqNode) == 0) {
            this.directedTowardsIndex = 0;
        }
        else if (this.indexOf(pqNode) == this.directedTowardsIndex) {
            this.directedTowardsIndex = -1;
        }
        return super.removePQNode(pqNode);
    }
}
