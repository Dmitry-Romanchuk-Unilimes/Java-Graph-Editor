// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

public class STNodeEx extends DFSNodeEx
{
    protected int stNumber;
    protected boolean isOld;
    
    public void setStNumber(final int stNumber) {
        this.stNumber = stNumber;
    }
    
    public int getStNumber() {
        return this.stNumber;
    }
    
    public void setIsOld(final boolean isOld) {
        this.isOld = isOld;
    }
    
    public boolean isOld() {
        return this.isOld;
    }
}
