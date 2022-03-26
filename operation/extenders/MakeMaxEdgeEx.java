// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.EdgeExtender;
import graphStructure.NodeExtender;

public class MakeMaxEdgeEx extends EdgeExtender
{
    protected boolean isOld;
    
    public MakeMaxEdgeEx() {
    }
    
    public MakeMaxEdgeEx(final NodeExtender start, final NodeExtender end) {
        super(start, end);
    }
    
    public MakeMaxEdgeEx(final MakeMaxEdgeEx edgeEx, final NodeExtender start, final NodeExtender end) {
        super(edgeEx, null, start, end);
    }
    
    public void setIsOld(final boolean isOld) {
        this.isOld = isOld;
    }
    
    public boolean isOld() {
        return this.isOld;
    }
}
