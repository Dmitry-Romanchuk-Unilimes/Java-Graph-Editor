// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import dataStructure.binaryHeap.HeapNode;
import dataStructure.binaryHeap.HeapObject;
import graphStructure.NodeExtender;

public class SPNodeEx extends NodeExtender implements HeapObject
{
    private double cost;
    private boolean isDone;
    private SPEdgeEx traceBackEdge;
    private HeapNode heapNode;
    
    public SPNodeEx() {
        this.cost = 0.0;
        this.isDone = false;
        this.traceBackEdge = null;
        this.heapNode = null;
    }
    
    public double getCost() {
        return this.cost;
    }
    
    public void setCost(final double c) {
        this.cost = c;
    }
    
    public boolean isDone() {
        return this.isDone;
    }
    
    public void setIsDone(final boolean d) {
        this.isDone = d;
    }
    
    public SPEdgeEx getTraceBackEdge() {
        return this.traceBackEdge;
    }
    
    public void setTraceBackEdge(final SPEdgeEx t) {
        this.traceBackEdge = t;
    }
    
    public boolean isUsed() {
        return this.traceBackEdge != null;
    }
    
    public HeapNode getHeapNode() {
        return this.heapNode;
    }
    
    public void setHeapNode(final HeapNode hn) {
        this.heapNode = hn;
    }
}
