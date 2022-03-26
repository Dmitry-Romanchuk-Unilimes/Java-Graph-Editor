// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Graph;
import graphStructure.Node;
import graphStructure.NodeInterface;

public class NodeDrawXMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int DRAWX_TYPE;
    private Node target;
    private boolean drawX;
    private int type;
    
    static {
        NodeDrawXMemento.NO_TYPE = 0;
        NodeDrawXMemento.DRAWX_TYPE = 1;
    }
    
    private NodeDrawXMemento(final Node target) {
        this.target = target;
        this.drawX = target.getDrawX();
        this.type = NodeDrawXMemento.NO_TYPE;
    }
    
    public static NodeDrawXMemento createDrawXMemento(final NodeInterface target) {
        final NodeDrawXMemento toReturn = new NodeDrawXMemento(target.getNode());
        toReturn.type = NodeDrawXMemento.DRAWX_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == NodeDrawXMemento.NO_TYPE) {
            return;
        }
        if (this.type == NodeDrawXMemento.DRAWX_TYPE) {
            final boolean temp = this.target.getDrawX();
            this.target.setDrawX(this.drawX);
            this.drawX = temp;
        }
    }
    
    public String toString() {
        if (this.type == NodeDrawXMemento.DRAWX_TYPE) {
            return "ChangeNodeDrawX: " + this.target + " " + this.drawX;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
