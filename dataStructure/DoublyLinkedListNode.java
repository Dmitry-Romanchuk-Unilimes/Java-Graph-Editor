// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure;

class DoublyLinkedListNode
{
    private Object element;
    private DoublyLinkedListNode prev;
    private DoublyLinkedListNode next;
    
    DoublyLinkedListNode() {
        this(null, null, null);
    }
    
    public DoublyLinkedListNode(final Object e) {
        this(e, null, null);
    }
    
    public DoublyLinkedListNode(final Object e, final DoublyLinkedListNode p, final DoublyLinkedListNode n) {
        this.element = e;
        this.prev = p;
        this.next = n;
    }
    
    void setElement(final Object newElem) {
        this.element = newElem;
    }
    
    void setPrev(final DoublyLinkedListNode newPrev) {
        this.prev = newPrev;
    }
    
    void setNext(final DoublyLinkedListNode newNext) {
        this.next = newNext;
    }
    
    Object getElement() {
        return this.element;
    }
    
    DoublyLinkedListNode getPrev() {
        return this.prev;
    }
    
    DoublyLinkedListNode getNext() {
        return this.next;
    }
}
