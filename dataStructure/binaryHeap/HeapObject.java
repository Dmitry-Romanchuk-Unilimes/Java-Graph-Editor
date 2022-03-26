// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.binaryHeap;

public interface HeapObject
{
    HeapNode getHeapNode();
    
    void setHeapNode(final HeapNode p0);
    
    double getCost();
    
    void setCost(final double p0);
}
