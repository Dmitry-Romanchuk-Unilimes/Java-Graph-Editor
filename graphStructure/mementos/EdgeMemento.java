// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Edge;
import graphStructure.Graph;

public class EdgeMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int PRESERVE_GENERATED_TYPE;
    private static int BECOME_GENERATED_TYPE;
    private Edge target;
    private int type;
    
    static {
        EdgeMemento.NO_TYPE = 0;
        EdgeMemento.PRESERVE_GENERATED_TYPE = 7;
        EdgeMemento.BECOME_GENERATED_TYPE = 8;
    }
    
    private EdgeMemento(final Edge target) {
        this.target = target;
        this.type = EdgeMemento.NO_TYPE;
    }
    
    public static EdgeMemento createPreserveGeneratedMemento(final Edge target) {
        final EdgeMemento toReturn = new EdgeMemento(target);
        toReturn.type = EdgeMemento.PRESERVE_GENERATED_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == EdgeMemento.NO_TYPE) {
            return;
        }
        if (this.type == EdgeMemento.PRESERVE_GENERATED_TYPE) {
            this.target.setIsGenerated(true);
            ++this.type;
        }
        else if (this.type == EdgeMemento.BECOME_GENERATED_TYPE) {
            this.target.setIsGenerated(false);
            --this.type;
        }
    }
    
    public String toString() {
        if (this.type == EdgeMemento.PRESERVE_GENERATED_TYPE) {
            return "makeGenerated: " + this.target;
        }
        if (this.type == EdgeMemento.BECOME_GENERATED_TYPE) {
            return "preserveGenerated: " + this.target;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
