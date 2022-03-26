// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Edge;
import graphStructure.EdgeInterface;
import graphStructure.Graph;
import graphStructure.Node;

public class EdgeDirectionMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int DIRECTION_TYPE;
    private Edge target;
    private Node directedSource;
    private int type;
    
    static {
        EdgeDirectionMemento.NO_TYPE = 0;
        EdgeDirectionMemento.DIRECTION_TYPE = 1;
    }
    
    private EdgeDirectionMemento(final Edge target) {
        this.target = target;
        this.directedSource = target.getDirectedSourceNode();
        this.type = EdgeDirectionMemento.NO_TYPE;
    }
    
    public static EdgeDirectionMemento createDirectionMemento(final EdgeInterface target) {
        final EdgeDirectionMemento toReturn = new EdgeDirectionMemento(target.getEdge());
        toReturn.type = EdgeDirectionMemento.DIRECTION_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == EdgeDirectionMemento.NO_TYPE) {
            return;
        }
        if (this.type == EdgeDirectionMemento.DIRECTION_TYPE) {
            final Node temp = this.target.getDirectedSourceNode();
            this.target.setDirectedFrom(this.directedSource);
            this.directedSource = temp;
        }
    }
    
    public String toString() {
        if (this.type == EdgeDirectionMemento.DIRECTION_TYPE) {
            return "ChangeEdgeDirection: " + this.target + " " + this.directedSource;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
