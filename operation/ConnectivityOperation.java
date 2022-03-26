// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphStructure.Graph;
import graphStructure.LogEntry;
import graphStructure.Node;
import graphStructure.NodeExtender;
import operation.extenders.DFSEdgeEx;
import operation.extenders.DFSNodeEx;

import java.util.Enumeration;
import java.util.Vector;

public class ConnectivityOperation
{
    public static Vector getConnectedComponents(final Graph g) {
        return getConnectedComponents(g, false);
    }
    
    public static Vector getConnectedComponents(final Graph g, final boolean copyData) {
        final LogEntry logEntry = g.startLogEntry("Get Connected Components");
        final Vector graphs = new Vector();
        final Vector nodes = g.createNodeExtenders(new DFSNodeEx().getClass());
        g.createEdgeExtenders(new DFSEdgeEx().getClass());
        for (int i = 0; i < nodes.size(); ++i) {
            if (nodes.elementAt(i).hasNoIncidentEdges()) {
                final Graph newGraph = g.copyNode(nodes.elementAt(i).getRef(), copyData);
                graphs.addElement(newGraph);
            }
            else if (nodes.elementAt(i).getNumber() == 0) {
                final Graph newGraph = g.copyNodes(NodeExtender.toNode(DepthFirstSearchOperation.depthFirstSearch(g, nodes.elementAt(i), false)), copyData);
                graphs.addElement(newGraph);
            }
        }
        logEntry.setData(String.valueOf(graphs.size()) + " Connected Components found");
        g.stopLogEntry(logEntry);
        return graphs;
    }
    
    public static Vector getConnectedNodes(final Graph g, final DFSNodeEx aNode) {
        final LogEntry logEntry = g.startLogEntry("Get Connected Nodes");
        Vector returnVector = null;
        if (aNode.hasNoIncidentEdges()) {
            returnVector = new Vector();
            returnVector.addElement(aNode);
        }
        else {
            returnVector = DepthFirstSearchOperation.depthFirstSearch(g, aNode, true);
        }
        logEntry.setData(String.valueOf(returnVector.size()) + " Connected Nodes found");
        return returnVector;
    }
    
    public static Vector getNodeFromEachConnectedComponent(final Graph g) {
        return getNodeFromEachConnectedComponent(g, false);
    }
    
    public static Vector getNodeFromEachConnectedComponent(final Graph g, final boolean reuseExtenders) {
        final LogEntry logEntry = g.startLogEntry("Get Node from each Connected Component");
        final Vector nodesFromEachComponent = new Vector();
        Vector nodes;
        if (reuseExtenders) {
            nodes = g.getNodeExtenders();
        }
        else {
            nodes = g.createNodeExtenders(new DFSNodeEx().getClass());
            g.createEdgeExtenders(new DFSEdgeEx().getClass());
        }
        for (int i = 0; i < nodes.size(); ++i) {
            if (nodes.elementAt(i).hasNoIncidentEdges()) {
                nodesFromEachComponent.addElement(nodes.elementAt(i));
            }
            else if (nodes.elementAt(i).getNumber() == 0) {
                nodesFromEachComponent.addElement(nodes.elementAt(i));
                DepthFirstSearchOperation.depthFirstSearch(g, nodes.elementAt(i), false);
            }
        }
        logEntry.setData(String.valueOf(nodesFromEachComponent.size()) + " Nodes found");
        g.stopLogEntry(logEntry);
        return nodesFromEachComponent;
    }
    
    public static boolean isConnected(final Graph g) {
        final LogEntry logEntry = g.startLogEntry("Test Connectivity");
        if (g.getNumNodes() <= 1) {
            g.stopLogEntry(logEntry);
            return true;
        }
        Enumeration enumNodes = g.getNodes().elements();
        while (enumNodes.hasMoreElements()) {
            if (enumNodes.nextElement().hasNoIncidentEdges()) {
                g.stopLogEntry(logEntry);
                return false;
            }
        }
        DepthFirstSearchOperation.depthFirstSearch(g);
        final Vector nodes = g.getNodeExtenders();
        enumNodes = nodes.elements();
        while (enumNodes.hasMoreElements()) {
            if (enumNodes.nextElement().getNumber() == 0) {
                g.stopLogEntry(logEntry);
                return false;
            }
        }
        g.stopLogEntry(logEntry);
        return true;
    }
    
    public static void makeConnected(final Graph g) {
        final LogEntry logEntry = g.startLogEntry("Make Connected");
        final Vector nodesToConnect = getNodeFromEachConnectedComponent(g);
        int counter = 0;
        if (nodesToConnect.size() > 1) {
            Node startNode = nodesToConnect.elementAt(0).getRef();
            for (int i = 1; i < nodesToConnect.size(); ++i) {
                final Node endNode = nodesToConnect.elementAt(i).getRef();
                g.addGeneratedEdgeNoCheck(startNode, endNode, true);
                startNode = endNode;
                ++counter;
            }
        }
        logEntry.setData(String.valueOf(counter) + " edges added");
        g.stopLogEntry(logEntry);
    }
}
