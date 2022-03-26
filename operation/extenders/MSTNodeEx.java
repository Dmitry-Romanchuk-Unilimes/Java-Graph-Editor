// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import dataStructure.binaryHeap.HeapNode;
import dataStructure.binaryHeap.HeapObject;
import graphStructure.NodeExtender;

public class MSTNodeEx extends NodeExtender implements HeapObject
{
    private HeapNode heapNode;
    private MSTEdgeEx linkEdge;
    private double cost;
    private boolean marked;
    
    public MSTNodeEx() {
        this.heapNode = null;
        this.linkEdge = null;
        this.cost = 0.0;
        this.marked = false;
    }
    
    public HeapNode getHeapNode() {
        return this.heapNode;
    }
    
    public void setHeapNode(final HeapNode hn) {
        this.heapNode = hn;
    }
    
    public MSTEdgeEx getLinkEdge() {
        return this.linkEdge;
    }
    
    public void setLinkEdge(final MSTEdgeEx le) {
        this.linkEdge = le;
    }
    
    public double getCost() {
        return this.cost;
    }
    
    public void setCost(final double c) {
        this.cost = c;
    }
    
    public boolean isMarked() {
        return this.marked;
    }
    
    public void setMarked(final boolean m) {
        this.marked = m;
    }
}
