// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

public class EdgeIterator
{
    private Node target;
    private Edge currentEdge;
    private Edge accessEdge;
    private boolean started;
    
    public EdgeIterator(final Node target, final Edge accessEdge) {
        this.target = target;
        this.accessEdge = accessEdge;
        if (accessEdge != null) {
            this.currentEdge = accessEdge;
        }
        else {
            this.currentEdge = null;
        }
        this.started = false;
    }
    
    public void reset() {
        if (this.accessEdge != null) {
            this.currentEdge = this.accessEdge;
        }
        else {
            this.currentEdge = null;
        }
        this.started = false;
    }
    
    public Edge nextEdge() {
        if (this.accessEdge == null) {
            return null;
        }
        if (!this.started) {
            this.started = true;
            return this.accessEdge;
        }
        this.currentEdge = (Edge)this.currentEdge.getNextInOrderFrom(this.target);
        if (this.currentEdge == this.accessEdge) {
            return null;
        }
        return this.currentEdge;
    }
    
    public Edge currentEdge() {
        if (this.accessEdge == null) {
            return null;
        }
        if (this.currentEdge != this.accessEdge) {
            return this.currentEdge;
        }
        if (this.started) {
            return null;
        }
        return this.accessEdge;
    }
    
    public boolean hasMoreEdges() {
        return this.accessEdge != null && (!this.started || this.currentEdge.getNextInOrderFrom(this.target) != this.accessEdge);
    }
}
