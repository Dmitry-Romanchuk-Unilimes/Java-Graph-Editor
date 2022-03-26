// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.pqTree;

public class PQNodePair
{
    protected static int MAX_SIZE;
    protected int size;
    protected PQNode firstPQNode;
    protected PQNode secondPQNode;
    
    static {
        PQNodePair.MAX_SIZE = 2;
    }
    
    public PQNodePair() {
        this.size = 0;
        this.firstPQNode = null;
        this.secondPQNode = null;
    }
    
    public int size() {
        return this.size;
    }
    
    public void addPQNode(final PQNode pqNode) throws Exception {
        if (this.size < PQNodePair.MAX_SIZE) {
            if (this.size == 0) {
                this.firstPQNode = pqNode;
            }
            else {
                this.secondPQNode = pqNode;
            }
            ++this.size;
            return;
        }
        throw new Exception("*** ERROR cannot add more than " + PQNodePair.MAX_SIZE + " PQNodes to a pair!");
    }
    
    public boolean removePQNode(final PQNode pqNode) {
        if (pqNode == this.firstPQNode) {
            this.firstPQNode = this.secondPQNode;
            this.secondPQNode = null;
            --this.size;
            return true;
        }
        if (pqNode == this.secondPQNode) {
            this.secondPQNode = null;
            --this.size;
            return true;
        }
        return false;
    }
    
    public boolean removePQNodeAt(final int index) {
        return this.removePQNode(this.PQNodeAt(index));
    }
    
    public PQNode PQNodeAt(final int index) {
        if (index >= this.size) {
            return null;
        }
        if (index == 0) {
            return this.firstPQNode;
        }
        return this.secondPQNode;
    }
    
    public PQNode otherPQNode(final PQNode pqNode) throws Exception {
        if (this.size == 1) {
            if (pqNode == null) {
                return this.firstPQNode;
            }
            if (pqNode == this.firstPQNode) {
                return null;
            }
            throw new Exception("*** ERROR cannot return other PQNode of " + pqNode);
        }
        else {
            if (this.size != 2) {
                throw new Exception("*** ERROR cannot return other PQNode of " + pqNode);
            }
            if (pqNode == this.firstPQNode) {
                return this.secondPQNode;
            }
            if (pqNode == this.secondPQNode) {
                return this.firstPQNode;
            }
            throw new Exception("*** ERROR cannot return other PQNode of " + pqNode);
        }
    }
    
    public void replacePQNode(final PQNode oldPQNode, final PQNode newPQNode) throws Exception {
        if (this.firstPQNode == oldPQNode) {
            this.firstPQNode = newPQNode;
        }
        else {
            if (this.secondPQNode != oldPQNode) {
                throw new Exception("*** ERROR cannot replace nonexistant PQNode: " + oldPQNode);
            }
            this.secondPQNode = newPQNode;
        }
    }
    
    public int indexOf(final PQNode pqNode) {
        if (pqNode == this.firstPQNode) {
            return 0;
        }
        if (pqNode == this.secondPQNode) {
            return 1;
        }
        return -1;
    }
    
    public boolean contains(final PQNode pqNode) {
        return this.indexOf(pqNode) != -1;
    }
}
