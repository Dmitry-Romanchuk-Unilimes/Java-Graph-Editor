// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.NodeExtender;

public class BiCompEdgeEx extends DFSEdgeEx
{
    protected boolean wasAdded;
    protected int subGraphNumber;
    
    public BiCompEdgeEx() {
    }
    
    public BiCompEdgeEx(final NodeExtender start, final NodeExtender end) {
        super(start, end);
    }
    
    public BiCompEdgeEx(final BiCompEdgeEx edgeEx, final NodeExtender start, final NodeExtender end) {
        super(edgeEx, start, end);
    }
    
    public void setWasAdded(final boolean isAdded) {
        this.wasAdded = isAdded;
    }
    
    public boolean wasAdded() {
        return this.wasAdded;
    }
    
    public void setSubGraphNumber(final int subGraphNumber) {
        this.subGraphNumber = subGraphNumber;
    }
    
    public int getSubGraphNumber() {
        return this.subGraphNumber;
    }
}
