// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.binaryHeap;

public class BinaryHeap
{
    private HeapNode min;
    private HeapNode last;
    private int size;
    
    public BinaryHeap() {
        this.min = null;
        this.last = null;
        this.size = 0;
    }
    
    public void finalize() {
        if (!this.isEmpty()) {
            this.min.deleteRecurse();
            this.min = null;
        }
    }
    
    public void insert(final HeapObject ho) {
        ++this.size;
        if (this.isEmpty()) {
            ho.setHeapNode(this.min = new HeapNode(ho));
            this.last = this.min;
        }
        else {
            final HeapNode newNode = new HeapNode(ho);
            ho.setHeapNode(newNode);
            this.findLastParentForInsert();
            newNode.setParent(this.last);
            if (this.last.getLeft() != null) {
                this.last.setRight(newNode);
            }
            else {
                this.last.setLeft(newNode);
            }
            this.upHeap(this.last = newNode);
        }
    }
    
    public HeapObject extractMin() {
        if (!this.isEmpty()) {
            --this.size;
            final HeapObject minNode = this.min.getElement();
            minNode.setHeapNode(null);
            this.min.setElement(this.last.getElement());
            this.min.getElement().setHeapNode(this.min);
            this.downHeap(this.min);
            if (this.last.isLeftChild()) {
                (this.last = this.last.getParent()).setLeft(null);
            }
            else if (this.last.isRightChild()) {
                (this.last = this.last.getParent()).setRight(null);
            }
            else {
                this.last = null;
                this.min = null;
            }
            if (this.last != null) {
                this.updateLastAfterDelete();
            }
            return minNode;
        }
        return null;
    }
    
    public void decreaseKey(final HeapObject ho) {
        this.upHeap(ho.getHeapNode());
    }
    
    public boolean isEmpty() {
        return this.min == null;
    }
    
    public int size() {
        return this.size;
    }
    
    public void printHeap() {
        System.out.println("PRINTHEAP");
        if (this.isEmpty()) {
            System.out.println("  EMPTY HEAP");
        }
        else {
            this.printHeap(this.min);
            System.out.println("\nmin is: " + this.min.getElement());
            System.out.println("\nlast is: " + this.last.getElement());
        }
        System.out.println("\nEND PRINTHEAP");
    }
    
    private void upHeap(final HeapNode aNode) {
        if (aNode.getParent() != null && aNode.getElement().getCost() < aNode.getParent().getElement().getCost()) {
            final HeapObject temp = aNode.getElement();
            aNode.setElement(aNode.getParent().getElement());
            aNode.getParent().setElement(temp);
            aNode.getElement().setHeapNode(aNode);
            aNode.getParent().getElement().setHeapNode(aNode.getParent());
            this.upHeap(aNode.getParent());
        }
    }
    
    private void downHeap(final HeapNode aNode) {
        HeapNode minNode = null;
        if (aNode.getLeft() != null) {
            minNode = aNode.getLeft();
        }
        if (aNode.getRight() != null && (minNode == null || aNode.getRight().getElement().getCost() < minNode.getElement().getCost())) {
            minNode = aNode.getRight();
        }
        if (minNode != null && minNode.getElement().getCost() < aNode.getElement().getCost()) {
            final HeapObject temp = aNode.getElement();
            aNode.setElement(minNode.getElement());
            minNode.setElement(temp);
            aNode.getElement().setHeapNode(aNode);
            minNode.getElement().setHeapNode(minNode);
            this.downHeap(minNode);
        }
    }
    
    private void findLastParentForInsert() {
        if (this.last.isLeftChild()) {
            this.last = this.last.getParent();
        }
        else {
            while (this.last.isRightChild()) {
                this.last = this.last.getParent();
            }
            if (this.last.getSibling() != null) {
                this.last = this.last.getSibling();
            }
            while (this.last.getLeft() != null) {
                this.last = this.last.getLeft();
            }
        }
    }
    
    private void updateLastAfterDelete() {
        if (this.last.getLeft() != null) {
            this.last = this.last.getLeft();
        }
        else {
            while (this.last.isLeftChild()) {
                this.last = this.last.getParent();
            }
            if (this.last.getSibling() != null) {
                this.last = this.last.getSibling();
            }
            while (this.last.getRight() != null) {
                this.last = this.last.getRight();
            }
        }
    }
    
    private void printHeap(final HeapNode node) {
        if (node != null) {
            System.out.println(node.getElement() + " " + (node.getLeft() == null) + " " + (node.getRight() == null) + " " + (node.getParent() == null));
            this.printHeap(node.getLeft());
            this.printHeap(node.getRight());
        }
    }
}
