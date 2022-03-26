// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Edge;
import graphStructure.EdgeInterface;
import graphStructure.Graph;
import graphStructure.Location;

public class EdgeMovementMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int MOVE_TYPE;
    private Edge target;
    private Location location;
    private boolean curved;
    private boolean orthogonal;
    private int type;
    
    static {
        EdgeMovementMemento.NO_TYPE = 0;
        EdgeMovementMemento.MOVE_TYPE = 1;
    }
    
    private EdgeMovementMemento(final Edge target) {
        this.target = target;
        this.location = new Location(target.getCenterLocation());
        this.curved = target.isCurved();
        this.orthogonal = target.isOrthogonal();
        this.type = EdgeMovementMemento.NO_TYPE;
    }
    
    public static EdgeMovementMemento createMoveMemento(final EdgeInterface target) {
        final EdgeMovementMemento toReturn = new EdgeMovementMemento(target.getEdge());
        toReturn.type = EdgeMovementMemento.MOVE_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == EdgeMovementMemento.NO_TYPE) {
            return;
        }
        if (this.type == EdgeMovementMemento.MOVE_TYPE) {
            final Location aPoint = this.target.getCenterLocation();
            this.target.setCenterLocation(this.location);
            this.location = new Location(aPoint);
            final boolean curve = this.target.isCurved();
            this.target.setIsCurved(this.curved);
            this.curved = curve;
            final boolean orth = this.target.isOrthogonal();
            this.target.setIsOrthogonal(this.orthogonal);
            this.orthogonal = orth;
        }
    }
    
    public String toString() {
        if (this.type == EdgeMovementMemento.MOVE_TYPE) {
            return "moveEdge: " + this.target + " " + this.location;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return this.location.equals(this.target.getCenterLocation());
    }
}
