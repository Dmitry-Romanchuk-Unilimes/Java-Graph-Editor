// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import dataStructure.pqTree.PQNode;
import graphStructure.EdgeExtender;

public class PQEdgeEx extends EdgeExtender
{
    protected PQNode pqNode;
    
    public void setPQNode(final PQNode pq) {
        this.pqNode = pq;
    }
    
    public PQNode getPQNode() {
        return this.pqNode;
    }
}
