// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphStructure.Graph;
import graphStructure.LogEntry;
import graphStructure.Node;

import java.util.Vector;

public class TreeOperation
{
    public static boolean hasCycles(final Graph g) {
        final LogEntry logEntry = g.startLogEntry("Test For Cycles");
        boolean result = false;
        if (!PlanarityOperation.isPlanar(g)) {
            result = true;
        }
        else {
            final Vector graphs = ConnectivityOperation.getConnectedComponents(g);
            for (int j = 0; j < graphs.size(); ++j) {
                final Graph graph = graphs.elementAt(j);
                DepthFirstSearchOperation.depthFirstSearch(graph);
                final Vector edges = graph.getEdgeExtenders();
                for (int i = 0; i < edges.size(); ++i) {
                    if (edges.elementAt(i).isBackEdge()) {
                        result = true;
                        break;
                    }
                }
            }
        }
        g.stopLogEntry(logEntry);
        return result;
    }
    
    public static boolean isBinaryTree(final Graph g, final Node root) {
        if (!ConnectivityOperation.isConnected(g)) {
            return false;
        }
        if (hasCycles(g)) {
            return false;
        }
        if (root.getNumEdges() > 2) {
            return false;
        }
        final Vector nodes = g.getNodes();
        for (int i = 0; i < nodes.size(); ++i) {
            final Node currentNode = nodes.elementAt(i);
            if (currentNode != root && currentNode.getNumEdges() > 3) {
                return false;
            }
        }
        return true;
    }
}
