// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import javax.swing.tree.TreeNode;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;

public class LogEntry implements TreeNode
{
    private static DecimalFormat decimalFormat;
    private static String PREFIX;
    private String operationName;
    private int numNodes;
    private int numEdges;
    private long timeTaken;
    private Vector subEntries;
    private LogEntry parent;
    private String data;
    
    static {
        LogEntry.decimalFormat = new DecimalFormat("000");
        LogEntry.PREFIX = "  ";
    }
    
    public LogEntry(final String opName, final Graph g, final long startTime) {
        this.operationName = opName;
        this.numNodes = g.getNumNodes();
        this.numEdges = g.getNumEdges();
        this.timeTaken = startTime;
        this.subEntries = new Vector();
        this.parent = null;
        this.data = null;
    }
    
    public LogEntry() {
        this.operationName = "None";
        this.numNodes = -1;
        this.numEdges = -1;
        this.timeTaken = 0L;
        this.subEntries = new Vector();
        this.parent = null;
        this.data = null;
    }
    
    public LogEntry getParentEntry() {
        return this.parent;
    }
    
    public void updateTimeTaken(final long endTime) {
        this.timeTaken = endTime - this.timeTaken;
    }
    
    public void addSubEntry(final LogEntry logEntry) {
        logEntry.parent = this;
        this.subEntries.addElement(logEntry);
    }
    
    public void setSubEntries(final Vector subEntries) {
        this.subEntries = subEntries;
    }
    
    public void setData(final String newData) {
        this.data = newData;
    }
    
    public String toString() {
        String toReturn = String.valueOf(this.operationName) + " run on graph with " + this.numNodes + " nodes and " + this.numEdges + " edges in " + formatTime(this.timeTaken);
        if (this.data != null) {
            toReturn = String.valueOf(toReturn) + " (" + this.data + ")";
        }
        return toReturn;
    }
    
    public String infoString() {
        return this.infoString("");
    }
    
    public String infoString(final String prefix) {
        String toReturn = String.valueOf(prefix) + this.operationName + " run on graph with " + this.numNodes + " nodes and " + this.numEdges + " edges in " + formatTime(this.timeTaken);
        if (this.data == null) {
            toReturn = String.valueOf(toReturn) + "\n";
        }
        else {
            toReturn = String.valueOf(toReturn) + " (" + this.data + ")\n";
        }
        for (int i = 0; i < this.subEntries.size(); ++i) {
            toReturn = String.valueOf(toReturn) + this.subEntries.elementAt(i).infoString(String.valueOf(prefix) + LogEntry.PREFIX);
        }
        return toReturn;
    }
    
    private static String formatTime(long time) {
        final int millis = (int)(time % 1000L);
        time = (time - millis) / 1000L;
        return String.valueOf(time) + "." + LogEntry.decimalFormat.format(millis) + " s";
    }
    
    public Enumeration children() {
        return this.subEntries.elements();
    }
    
    public boolean getAllowsChildren() {
        return true;
    }
    
    public TreeNode getChildAt(final int index) {
        return this.subEntries.elementAt(index);
    }
    
    public int getChildCount() {
        return this.subEntries.size();
    }
    
    public int getIndex(final TreeNode node) {
        return this.subEntries.indexOf(node);
    }
    
    public TreeNode getParent() {
        return this.parent;
    }
    
    public boolean isLeaf() {
        return this.subEntries.size() == 0;
    }
}
