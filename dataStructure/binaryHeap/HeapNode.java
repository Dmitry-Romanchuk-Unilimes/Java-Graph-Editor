// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.binaryHeap;

public class HeapNode
{
    private HeapNode parent;
    private HeapNode left;
    private HeapNode right;
    private HeapObject element;
    
    public HeapNode(final HeapObject ho) {
        this.element = ho;
        this.parent = null;
        this.left = null;
        this.right = null;
    }
    
    public HeapObject getElement() {
        return this.element;
    }
    
    public void setElement(final HeapObject ho) {
        this.element = ho;
    }
    
    public HeapNode getParent() {
        return this.parent;
    }
    
    public void setParent(final HeapNode hn) {
        this.parent = hn;
    }
    
    public HeapNode getLeft() {
        return this.left;
    }
    
    public void setLeft(final HeapNode hn) {
        this.left = hn;
    }
    
    public HeapNode getRight() {
        return this.right;
    }
    
    public void setRight(final HeapNode hn) {
        this.right = hn;
    }
    
    public void deleteRecurse() {
        if (this.left != null) {
            this.left.deleteRecurse();
            this.left = null;
        }
        if (this.right != null) {
            this.right.deleteRecurse();
            this.right = null;
        }
    }
    
    public boolean isLeftChild() {
        return this.parent != null && this.parent.left == this;
    }
    
    public boolean isRightChild() {
        return this.parent != null && this.parent.right == this;
    }
    
    public HeapNode getSibling() {
        if (this.parent == null) {
            return null;
        }
        if (this.isLeftChild()) {
            return this.parent.right;
        }
        return this.parent.left;
    }
}
