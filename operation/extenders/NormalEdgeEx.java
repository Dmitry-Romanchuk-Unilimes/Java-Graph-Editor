// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.EdgeExtender;

public class NormalEdgeEx extends EdgeExtender
{
    protected int normalLabel;
    
    public NormalEdgeEx() {
    }
    
    public NormalEdgeEx(final NormalNodeEx start, final NormalNodeEx end) {
        super(start, end);
    }
    
    public void setNormalLabel(final int label) {
        this.normalLabel = label;
    }
    
    public int getNormalLabel() {
        return this.normalLabel;
    }
    
    public NormalNodeEx getNormalLabelSourceNode() {
        if (this.normalLabel == 1) {
            if (((NormalNodeEx)this.getStartNode()).getR1Parent() == this.getEndNode()) {
                return (NormalNodeEx)this.getStartNode();
            }
            if (((NormalNodeEx)this.getEndNode()).getR1Parent() == this.getStartNode()) {
                return (NormalNodeEx)this.getEndNode();
            }
            return null;
        }
        else if (this.normalLabel == 2) {
            if (((NormalNodeEx)this.getStartNode()).getR2Parent() == this.getEndNode()) {
                return (NormalNodeEx)this.getStartNode();
            }
            if (((NormalNodeEx)this.getEndNode()).getR2Parent() == this.getStartNode()) {
                return (NormalNodeEx)this.getEndNode();
            }
            return null;
        }
        else {
            if (this.normalLabel != 3) {
                return null;
            }
            if (((NormalNodeEx)this.getStartNode()).getR3Parent() == this.getEndNode()) {
                return (NormalNodeEx)this.getStartNode();
            }
            if (((NormalNodeEx)this.getEndNode()).getR3Parent() == this.getStartNode()) {
                return (NormalNodeEx)this.getEndNode();
            }
            return null;
        }
    }
}
