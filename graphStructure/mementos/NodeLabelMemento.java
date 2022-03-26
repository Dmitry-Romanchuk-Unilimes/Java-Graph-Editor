// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Graph;
import graphStructure.Node;
import graphStructure.NodeInterface;

public class NodeLabelMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int LABEL_TYPE;
    private Node target;
    private String label;
    private int type;
    
    static {
        NodeLabelMemento.NO_TYPE = 0;
        NodeLabelMemento.LABEL_TYPE = 1;
    }
    
    private NodeLabelMemento(final Node target) {
        this.target = target;
        this.label = target.getLabel();
        this.type = NodeLabelMemento.NO_TYPE;
    }
    
    public static NodeLabelMemento createLabelMemento(final NodeInterface target) {
        final NodeLabelMemento toReturn = new NodeLabelMemento(target.getNode());
        toReturn.type = NodeLabelMemento.LABEL_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == NodeLabelMemento.NO_TYPE) {
            return;
        }
        if (this.type == NodeLabelMemento.LABEL_TYPE) {
            final String temp = this.target.getLabel();
            this.target.setLabel(this.label);
            this.label = temp;
        }
    }
    
    public String toString() {
        if (this.type == NodeLabelMemento.LABEL_TYPE) {
            return "ChangeNodeLabel: " + this.target + " " + this.label;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
