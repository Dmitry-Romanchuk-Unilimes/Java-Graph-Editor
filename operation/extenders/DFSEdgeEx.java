// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.EdgeExtender;
import graphStructure.NodeExtender;

public class DFSEdgeEx extends EdgeExtender
{
    protected boolean isBackEdge;
    protected boolean isUsed;
    
    public DFSEdgeEx() {
    }
    
    public DFSEdgeEx(final NodeExtender start, final NodeExtender end) {
        super(start, end);
    }
    
    public DFSEdgeEx(final DFSEdgeEx edgeEx, final NodeExtender start, final NodeExtender end) {
        super(edgeEx, null, start, end);
    }
    
    public void setIsBackEdge(final boolean isBackEdge) {
        this.isBackEdge = isBackEdge;
    }
    
    public boolean isBackEdge() {
        return this.isBackEdge;
    }
    
    public void setIsUsed(final boolean isUsed) {
        this.isUsed = isUsed;
    }
    
    public boolean isUsed() {
        return this.isUsed;
    }
}
