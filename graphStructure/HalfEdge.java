// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

public class HalfEdge
{
    private Node sourceNode;
    private HalfEdge twinEdge;
    private HalfEdge nextEdge;
    private HalfEdge previousEdge;
    private Edge parentEdge;
    
    public HalfEdge(final Node aSourceNode, final Edge aParentEdge, final HalfEdge aTwin) {
        this.sourceNode = aSourceNode;
        this.parentEdge = aParentEdge;
        this.twinEdge = aTwin;
    }
    
    public boolean equals(final Object o) {
        return o instanceof HalfEdge && (((HalfEdge)o).getSourceNode().equals(this.getSourceNode()) && ((HalfEdge)o).getDestNode().equals(this.getDestNode()));
    }
    
    public void setTwinEdge(final HalfEdge aTwin) {
        this.twinEdge = aTwin;
    }
    
    public HalfEdge getTwinEdge() {
        return this.twinEdge;
    }
    
    public Node getSourceNode() {
        return this.sourceNode;
    }
    
    public Node getDestNode() {
        return this.twinEdge.sourceNode;
    }
    
    public Edge getParentEdge() {
        return this.parentEdge;
    }
    
    public void setNext(final HalfEdge next) {
        this.nextEdge = next;
    }
    
    public HalfEdge getNext() {
        return this.nextEdge;
    }
    
    public void setPrevious(final HalfEdge previous) {
        this.previousEdge = previous;
    }
    
    public HalfEdge getPrevious() {
        return this.previousEdge;
    }
    
    public String infoString() {
        return "h: " + this.hashCode() + " sn: " + this.sourceNode + " te: " + this.twinEdge.hashCode() + " ne: " + this.nextEdge.hashCode() + " pe: " + this.previousEdge.hashCode() + " par: " + this.parentEdge;
    }
}
