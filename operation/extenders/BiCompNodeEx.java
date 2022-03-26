// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

public class BiCompNodeEx extends DFSNodeEx
{
    protected int subGraphNumber;
    protected boolean isOld;
    
    public void setSubGraphNumber(final int subGraphNumber) {
        this.subGraphNumber = subGraphNumber;
    }
    
    public int getSubGraphNumber() {
        return this.subGraphNumber;
    }
    
    public void setIsOld(final boolean isOld) {
        this.isOld = isOld;
    }
    
    public boolean isOld() {
        return this.isOld;
    }
}
