// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.NodeExtender;

public class DFSNodeEx extends NodeExtender
{
    protected int number;
    protected int lowNumber;
    protected DFSNodeEx parent;
    
    public void setNumber(final int number) {
        this.number = number;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public void setLowNumber(final int lowNumber) {
        this.lowNumber = lowNumber;
    }
    
    public int getLowNumber() {
        return this.lowNumber;
    }
    
    public void setParent(final DFSNodeEx parent) {
        this.parent = parent;
    }
    
    public DFSNodeEx getParent() {
        return this.parent;
    }
}
