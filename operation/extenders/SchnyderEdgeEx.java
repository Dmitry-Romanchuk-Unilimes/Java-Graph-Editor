// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.EdgeExtender;

public class SchnyderEdgeEx extends EdgeExtender
{
    protected int normalLabel;
    
    public void setNormalLabel(final int label) {
        this.normalLabel = label;
    }
    
    public int getNormalLabel() {
        return this.normalLabel;
    }
    
    public SchnyderNodeEx getNormalLabelSourceNode() {
        if (((SchnyderNodeEx)this.getStartNode()).getRXParent(this.normalLabel) == this.getEndNode()) {
            return (SchnyderNodeEx)this.getStartNode();
        }
        if (((SchnyderNodeEx)this.getEndNode()).getRXParent(this.normalLabel) == this.getStartNode()) {
            return (SchnyderNodeEx)this.getEndNode();
        }
        return null;
    }
}
