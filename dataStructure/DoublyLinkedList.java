// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure;

public class DoublyLinkedList
{
    private DoublyLinkedListNode head;
    private DoublyLinkedListNode tail;
    private int size;
    private DoublyLinkedListNode current;
    
    public DoublyLinkedList() {
        this.head = new DoublyLinkedListNode();
        this.tail = new DoublyLinkedListNode();
        this.current = this.tail;
        this.size = 0;
    }
    
    public Object dequeue() {
        if (this.size == 0) {
            return null;
        }
        final Object obj = this.head.getNext().getElement();
        this.head.setNext(this.head.getNext().getNext());
        this.head.getNext().setPrev(this.head);
        --this.size;
        return obj;
    }
    
    public void enqueue(final Object obj) {
        final DoublyLinkedListNode node = new DoublyLinkedListNode(obj);
        if (this.size == 0) {
            this.head.setNext(node);
            node.setPrev(this.head);
            this.tail.setPrev(node);
            node.setNext(this.tail);
        }
        else {
            node.setPrev(this.tail.getPrev());
            this.tail.getPrev().setNext(node);
            node.setNext(this.tail);
            this.tail.setPrev(node);
        }
        this.current = node;
        ++this.size;
    }
    
    public void enqueueAfterCurrent(final Object obj) {
        final DoublyLinkedListNode node = new DoublyLinkedListNode(obj);
        if (this.size == 0) {
            this.head.setNext(node);
            node.setPrev(this.head);
            this.tail.setPrev(node);
            node.setNext(this.tail);
        }
        else if (this.current != this.tail) {
            for (DoublyLinkedListNode next = this.current.getNext(); next != this.tail; next = next.getNext()) {
                --this.size;
            }
            this.current.setNext(node);
            node.setPrev(this.current);
            node.setNext(this.tail);
            this.tail.setPrev(node);
        }
        else {
            node.setPrev(this.tail.getPrev());
            this.tail.getPrev().setNext(node);
            node.setNext(this.tail);
            this.tail.setPrev(node);
        }
        this.current = node;
        ++this.size;
    }
    
    public Object getCurrent() {
        if (this.current == this.head || this.current == this.tail) {
            return null;
        }
        return this.current.getElement();
    }
    
    public Object removeCurrent() {
        if (this.current == this.head || this.current == this.tail) {
            return null;
        }
        this.current.getNext().setPrev(this.current.getPrev());
        this.current.getPrev().setNext(this.current.getNext());
        this.current = this.current.getNext();
        --this.size;
        return this.current.getElement();
    }
    
    public void toHead() {
        this.current = this.head.getNext();
    }
    
    public void toTail() {
        this.current = this.tail.getPrev();
    }
    
    public void toNext() {
        if (this.current != this.tail) {
            this.current = this.current.getNext();
        }
    }
    
    public void toPrev() {
        if (this.current != this.head) {
            this.current = this.current.getPrev();
        }
    }
    
    public boolean hasNext() {
        return this.current != this.tail && this.current.getNext() != this.tail;
    }
    
    public boolean hasPrev() {
        return this.current.getPrev() != this.head;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public int size() {
        return this.size;
    }
}
