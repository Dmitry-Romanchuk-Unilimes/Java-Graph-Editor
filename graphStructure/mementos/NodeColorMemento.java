// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Graph;
import graphStructure.Node;
import graphStructure.NodeInterface;

import java.awt.*;

public class NodeColorMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int COLOR_TYPE;
    private Node target;
    private Color color;
    private int type;
    
    static {
        NodeColorMemento.NO_TYPE = 0;
        NodeColorMemento.COLOR_TYPE = 1;
    }
    
    private NodeColorMemento(final Node target) {
        this.target = target;
        this.color = target.getColor();
        this.type = NodeColorMemento.NO_TYPE;
    }
    
    public static NodeColorMemento createColorMemento(final NodeInterface target) {
        final NodeColorMemento toReturn = new NodeColorMemento(target.getNode());
        toReturn.type = NodeColorMemento.COLOR_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == NodeColorMemento.NO_TYPE) {
            return;
        }
        if (this.type == NodeColorMemento.COLOR_TYPE) {
            final Color temp = this.target.getColor();
            this.target.setColor(this.color);
            this.color = temp;
        }
    }
    
    public String toString() {
        if (this.type == NodeColorMemento.COLOR_TYPE) {
            return "ChangeNodeColor: " + this.target + " " + this.color;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
