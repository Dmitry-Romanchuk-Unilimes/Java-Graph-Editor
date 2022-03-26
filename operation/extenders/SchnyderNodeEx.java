// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.NodeExtender;

public class SchnyderNodeEx extends NodeExtender
{
    protected int canonicalNumber;
    protected SchnyderNodeEx r1Parent;
    protected SchnyderNodeEx r2Parent;
    protected SchnyderNodeEx r3Parent;
    protected int[] px;
    protected int[] tx;
    protected int[] rx;
    protected int[] temp;
    
    public SchnyderNodeEx() {
        this.px = new int[3];
        this.tx = new int[3];
        this.rx = new int[3];
        this.temp = new int[6];
    }
    
    public void setCanonicalNumber(final int canon) {
        this.canonicalNumber = canon;
    }
    
    public int getCanonicalNumber() {
        return this.canonicalNumber;
    }
    
    public void setR1Parent(final SchnyderNodeEx r1) {
        this.r1Parent = r1;
    }
    
    public SchnyderNodeEx getR1Parent() {
        return this.r1Parent;
    }
    
    public void setR2Parent(final SchnyderNodeEx r2) {
        this.r2Parent = r2;
    }
    
    public SchnyderNodeEx getR2Parent() {
        return this.r2Parent;
    }
    
    public void setR3Parent(final SchnyderNodeEx r3) {
        this.r3Parent = r3;
    }
    
    public SchnyderNodeEx getR3Parent() {
        return this.r3Parent;
    }
    
    public SchnyderNodeEx getRXParent(final int x) {
        if (x == 1) {
            return this.getR1Parent();
        }
        if (x == 2) {
            return this.getR2Parent();
        }
        if (x == 3) {
            return this.getR3Parent();
        }
        return null;
    }
    
    public void setPX(final int treeNumber, final int aPX) {
        this.px[treeNumber - 1] = aPX;
    }
    
    public int getPX(final int treeNumber) {
        return this.px[treeNumber - 1];
    }
    
    public void setTX(final int treeNumber, final int aTX) {
        this.tx[treeNumber - 1] = aTX;
    }
    
    public int getTX(final int treeNumber) {
        return this.tx[treeNumber - 1];
    }
    
    public void setRX(final int treeNumber, final int aRX) {
        this.rx[treeNumber - 1] = aRX;
    }
    
    public int getRX(final int treeNumber) {
        return this.rx[treeNumber - 1];
    }
    
    public void setTemp(final int treeNumber, final int aTemp) {
        this.temp[treeNumber - 1] = aTemp;
    }
    
    public int getTemp(final int treeNumber) {
        return this.temp[treeNumber - 1];
    }
}
