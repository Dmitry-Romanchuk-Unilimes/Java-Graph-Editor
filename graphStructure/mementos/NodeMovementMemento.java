// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Graph;
import graphStructure.Location;
import graphStructure.Node;
import graphStructure.NodeInterface;

public class NodeMovementMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int MOVE_TYPE;
    private Node target;
    private Location location;
    private int type;
    
    static {
        NodeMovementMemento.NO_TYPE = 0;
        NodeMovementMemento.MOVE_TYPE = 1;
    }
    
    private NodeMovementMemento(final Node target) {
        this.target = target;
        this.location = new Location(target.getLocation());
        this.type = NodeMovementMemento.NO_TYPE;
    }
    
    public static NodeMovementMemento createMoveMemento(final NodeInterface target) {
        final NodeMovementMemento toReturn = new NodeMovementMemento(target.getNode());
        toReturn.type = NodeMovementMemento.MOVE_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == NodeMovementMemento.NO_TYPE) {
            return;
        }
        if (this.type == NodeMovementMemento.MOVE_TYPE) {
            final Location aPoint = this.target.getLocation();
            this.target.setLocation(this.location);
            this.location = new Location(aPoint);
        }
    }
    
    public String toString() {
        if (this.type == NodeMovementMemento.MOVE_TYPE) {
            return "moveNode: " + this.target + " " + this.location;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return this.location.equals(this.target.getLocation());
    }
}
