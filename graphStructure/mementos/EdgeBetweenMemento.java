// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Edge;
import graphStructure.Graph;

public class EdgeBetweenMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int DELETE_TYPE;
    private static int CREATE_TYPE;
    private static int CHANGE_TYPE;
    private Edge target;
    private Edge startPrevious;
    private Edge endPrevious;
    private int type;
    
    static {
        EdgeBetweenMemento.NO_TYPE = 0;
        EdgeBetweenMemento.DELETE_TYPE = 1;
        EdgeBetweenMemento.CREATE_TYPE = 2;
        EdgeBetweenMemento.CHANGE_TYPE = 3;
    }
    
    private EdgeBetweenMemento(final Edge target, final Edge sourcePrevious, final Edge endPrevious) {
        this.target = target;
        this.startPrevious = sourcePrevious;
        this.endPrevious = endPrevious;
        this.type = EdgeBetweenMemento.NO_TYPE;
    }
    
    public static EdgeBetweenMemento createCreateMemento(final Edge target, final Edge previous, final Edge next) {
        final EdgeBetweenMemento toReturn = new EdgeBetweenMemento(target, previous, next);
        toReturn.type = EdgeBetweenMemento.CREATE_TYPE;
        return toReturn;
    }
    
    public static EdgeBetweenMemento createDeleteMemento(final Edge target, final Edge previous, final Edge next) {
        final EdgeBetweenMemento toReturn = new EdgeBetweenMemento(target, previous, next);
        toReturn.type = EdgeBetweenMemento.DELETE_TYPE;
        return toReturn;
    }
    
    public static EdgeBetweenMemento createChangeMemento(final Edge target) {
        final Edge previous = (Edge)target.getPreviousInOrderFrom(target.getStartNode());
        final Edge next = (Edge)target.getPreviousInOrderFrom(target.getEndNode());
        final EdgeBetweenMemento toReturn = new EdgeBetweenMemento(target, previous, next);
        toReturn.type = EdgeBetweenMemento.CHANGE_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == EdgeBetweenMemento.NO_TYPE) {
            return;
        }
        if (this.type == EdgeBetweenMemento.DELETE_TYPE) {
            graph.addEdge(this.target, this.startPrevious, this.endPrevious, false);
            ++this.type;
        }
        else if (this.type == EdgeBetweenMemento.CREATE_TYPE) {
            graph.deleteEdge(this.target, false);
            --this.type;
        }
        else if (this.type == EdgeBetweenMemento.CHANGE_TYPE) {
            Edge temp = (Edge)this.target.getPreviousInOrderFrom(this.target.getStartNode());
            this.target.setPreviousInOrderFrom(this.target.getStartNode(), this.startPrevious);
            this.startPrevious.setNextInOrderFrom(this.target.getStartNode(), this.target);
            this.startPrevious = temp;
            temp = (Edge)this.target.getPreviousInOrderFrom(this.target.getEndNode());
            this.target.setPreviousInOrderFrom(this.target.getEndNode(), this.endPrevious);
            this.endPrevious.setNextInOrderFrom(this.target.getEndNode(), this.target);
            this.endPrevious = temp;
        }
    }
    
    public String toString() {
        if (this.type == EdgeBetweenMemento.DELETE_TYPE) {
            return "addEdge: " + this.target + " Between: " + this.startPrevious + ", " + this.endPrevious;
        }
        if (this.type == EdgeBetweenMemento.CREATE_TYPE) {
            return "deleteEdge: " + this.target + " Between: " + this.startPrevious + ", " + this.endPrevious;
        }
        if (this.type == EdgeBetweenMemento.CHANGE_TYPE) {
            return "changeOrder: " + this.target + " Start Prev: " + this.startPrevious + ", End Prev: " + this.endPrevious;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
