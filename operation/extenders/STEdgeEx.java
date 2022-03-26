// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

public class STEdgeEx extends DFSEdgeEx
{
    protected boolean isOld;
    
    public void setIsOld(final boolean isOld) {
        this.isOld = isOld;
    }
    
    public boolean isOld() {
        return this.isOld;
    }
}
