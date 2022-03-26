// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure;

public class Queue
{
    private QueueNode head;
    private QueueNode tail;
    private int size;
    
    public Queue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    public void enqueue(final Object obj) {
        final QueueNode node = new QueueNode(obj, null);
        if (this.size == 0) {
            this.head = node;
        }
        else {
            this.tail.setNext(node);
        }
        this.tail = node;
        ++this.size;
    }
    
    public Object dequeue() {
        if (this.size == 0) {
            return null;
        }
        final Object obj = this.head.getElement();
        this.head = this.head.getNext();
        --this.size;
        if (this.size == 0) {
            this.tail = null;
        }
        return obj;
    }
    
    public int size() {
        return this.size;
    }
}
