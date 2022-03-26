// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.Node;

public class NodeChangeMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int CHANGE_TYPE;
    private Node target;
    private Edge accessEdge;
    private int type;
    
    static {
        NodeChangeMemento.NO_TYPE = 0;
        NodeChangeMemento.CHANGE_TYPE = 1;
    }
    
    private NodeChangeMemento(final Node target) {
        this.target = target;
        this.accessEdge = target.getAccessEdge();
        this.type = NodeChangeMemento.NO_TYPE;
    }
    
    public static NodeChangeMemento createChangeMemento(final Node target) {
        final NodeChangeMemento toReturn = new NodeChangeMemento(target);
        toReturn.type = NodeChangeMemento.CHANGE_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == NodeChangeMemento.NO_TYPE) {
            return;
        }
        if (this.type == NodeChangeMemento.CHANGE_TYPE) {
            final Edge temp = this.target.getAccessEdge();
            this.target.setAccessEdge(this.accessEdge);
            this.accessEdge = temp;
        }
    }
    
    public String toString() {
        if (this.type == NodeChangeMemento.CHANGE_TYPE) {
            return "changeNode: " + this.target + " " + this.accessEdge;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
