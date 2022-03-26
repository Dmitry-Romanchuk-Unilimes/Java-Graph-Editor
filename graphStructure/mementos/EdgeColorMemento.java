// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Edge;
import graphStructure.EdgeInterface;
import graphStructure.Graph;

import java.awt.*;

public class EdgeColorMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int COLOR_TYPE;
    private Edge target;
    private Color color;
    private int type;
    
    static {
        EdgeColorMemento.NO_TYPE = 0;
        EdgeColorMemento.COLOR_TYPE = 1;
    }
    
    private EdgeColorMemento(final Edge target) {
        this.target = target;
        this.color = target.getColor();
        this.type = EdgeColorMemento.NO_TYPE;
    }
    
    public static EdgeColorMemento createColorMemento(final EdgeInterface target) {
        final EdgeColorMemento toReturn = new EdgeColorMemento(target.getEdge());
        toReturn.type = EdgeColorMemento.COLOR_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == EdgeColorMemento.NO_TYPE) {
            return;
        }
        if (this.type == EdgeColorMemento.COLOR_TYPE) {
            final Color temp = this.target.getColor();
            this.target.setColor(this.color);
            this.color = temp;
        }
    }
    
    public String toString() {
        if (this.type == EdgeColorMemento.COLOR_TYPE) {
            return "ChangeEdgeColor: " + this.target + " " + this.color;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
