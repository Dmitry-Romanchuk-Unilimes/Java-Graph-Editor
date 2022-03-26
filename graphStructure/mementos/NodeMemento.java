// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Graph;
import graphStructure.Node;

public class NodeMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int DELETE_TYPE;
    private static int CREATE_TYPE;
    private Node target;
    private int type;
    
    static {
        NodeMemento.NO_TYPE = 0;
        NodeMemento.DELETE_TYPE = 1;
        NodeMemento.CREATE_TYPE = 2;
    }
    
    private NodeMemento(final Node target) {
        this.target = target;
        this.type = NodeMemento.NO_TYPE;
    }
    
    public static NodeMemento createDeleteMemento(final Node target) {
        final NodeMemento toReturn = new NodeMemento(target);
        toReturn.type = NodeMemento.DELETE_TYPE;
        return toReturn;
    }
    
    public static NodeMemento createCreateMemento(final Node target) {
        final NodeMemento toReturn = new NodeMemento(target);
        toReturn.type = NodeMemento.CREATE_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == NodeMemento.NO_TYPE) {
            return;
        }
        if (this.type == NodeMemento.DELETE_TYPE) {
            graph.addNode(this.target, false);
            ++this.type;
        }
        else if (this.type == NodeMemento.CREATE_TYPE) {
            graph.deleteNode(this.target, false);
            --this.type;
        }
    }
    
    public String toString() {
        if (this.type == NodeMemento.DELETE_TYPE) {
            return "addNode: " + this.target;
        }
        if (this.type == NodeMemento.CREATE_TYPE) {
            return "deleteNode: " + this.target;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
