// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure;

class QueueNode
{
    private Object element;
    private QueueNode next;
    
    QueueNode() {
        this(null, null);
    }
    
    public QueueNode(final Object e, final QueueNode n) {
        this.element = e;
        this.next = n;
    }
    
    void setElement(final Object newElem) {
        this.element = newElem;
    }
    
    void setNext(final QueueNode newNext) {
        this.next = newNext;
    }
    
    Object getElement() {
        return this.element;
    }
    
    QueueNode getNext() {
        return this.next;
    }
}
