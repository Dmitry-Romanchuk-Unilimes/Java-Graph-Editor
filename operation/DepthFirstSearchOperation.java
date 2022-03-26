// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphStructure.Graph;
import graphStructure.LogEntry;
import operation.extenders.DFSEdgeEx;
import operation.extenders.DFSNodeEx;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

public class DepthFirstSearchOperation
{
    public static void depthFirstSearch(final Graph g) {
        depthFirstSearch(g, false);
    }
    
    public static void depthFirstSearch(final Graph g, final boolean reuseExtenders) {
        if (!reuseExtenders) {
            g.createNodeExtenders(new DFSNodeEx().getClass());
            g.createEdgeExtenders(new DFSEdgeEx().getClass());
        }
        final Vector nodes = g.getNodeExtenders();
        depthFirstSearch(g, nodes.firstElement(), true);
    }
    
    public static Vector depthFirstSearch(final Graph g, final DFSNodeEx startNode, final boolean reset) {
        final LogEntry logEntry = g.startLogEntry("Depth First Search");
        final Vector nodes = g.getNodeExtenders();
        final Vector edges = g.getEdgeExtenders();
        if (reset) {
            resetDFSData(nodes, edges);
        }
        final Vector returnVector = new Vector();
        int i = 0;
        boolean alreadyVisited = false;
        DFSNodeEx tempNode = startNode;
        while (true) {
            if (!alreadyVisited) {
                ++i;
                tempNode.setNumber(i);
                tempNode.setLowNumber(i);
                returnVector.addElement(tempNode);
            }
            final Enumeration enum1 = tempNode.incidentEdges().elements();
            boolean flag = false;
            boolean hasUnusedEdges = false;
            alreadyVisited = false;
            while (enum1.hasMoreElements()) {
                final DFSEdgeEx tempEdge = enum1.nextElement();
                if (!tempEdge.isUsed()) {
                    tempEdge.setIsUsed(true);
                    hasUnusedEdges = true;
                    final DFSNodeEx otherNode = (DFSNodeEx)tempEdge.otherEndFrom(tempNode);
                    if (otherNode.getNumber() == 0) {
                        tempEdge.setIsBackEdge(false);
                        otherNode.setParent(tempNode);
                        tempNode = otherNode;
                        break;
                    }
                    tempEdge.setIsBackEdge(true);
                    tempNode.setLowNumber(Math.min(tempNode.getLowNumber(), otherNode.getNumber()));
                    alreadyVisited = true;
                    break;
                }
            }
            if (!hasUnusedEdges && tempNode.getParent() != null) {
                if (tempNode.getParent().getNumber() != 1) {
                    if (tempNode.getLowNumber() < tempNode.getParent().getNumber()) {
                        tempNode.getParent().setLowNumber(Math.min(tempNode.getParent().getLowNumber(), tempNode.getLowNumber()));
                    }
                    tempNode = tempNode.getParent();
                    alreadyVisited = true;
                    flag = true;
                }
                if (flag) {
                    continue;
                }
                final Enumeration enum2 = startNode.incidentEdges().elements();
                boolean flag2 = false;
                while (enum2.hasMoreElements()) {
                    if (!enum2.nextElement().isUsed()) {
                        flag2 = true;
                    }
                }
                if (!flag2) {
                    break;
                }
                tempNode = startNode;
                alreadyVisited = true;
            }
        }
        logEntry.setData(String.valueOf(returnVector.size()) + " Nodes Visited");
        g.stopLogEntry(logEntry);
        return returnVector;
    }
    
    public static void displayDepthFirstSearch(final Graph g) {
        final Vector graphs = ConnectivityOperation.getConnectedComponents(g, true);
        for (int i = 0; i < graphs.size(); ++i) {
            final Graph currentGraph = graphs.elementAt(i);
            if (currentGraph.getNumNodes() == 1) {
                g.changeNodeColor(currentGraph.getNodes().firstElement().getCopy(), Color.green, true);
                g.changeNodeDrawX(currentGraph.getNodes().firstElement().getCopy(), false, true);
            }
            else {
                depthFirstSearch(currentGraph, false);
                final Vector nodes = currentGraph.getNodeExtenders();
                for (int j = 0; j < nodes.size(); ++j) {
                    final DFSNodeEx currentNode = nodes.elementAt(j);
                    g.changeNodeColor(currentNode.getCopy(), Color.gray, true);
                    g.changeNodeDrawX(currentNode.getCopy(), false, true);
                    if (currentNode.getParent() != null) {
                        g.changeEdgeDirection(currentNode.incidentEdgeWith(currentNode.getParent()).getCopy(), currentNode.getParent().getCopy(), true);
                    }
                }
                g.changeNodeColor(nodes.firstElement().getCopy(), Color.green, true);
                final Vector edges = currentGraph.getEdgeExtenders();
                for (int j = 0; j < edges.size(); ++j) {
                    final DFSEdgeEx currentEdge = edges.elementAt(j);
                    if (currentEdge.isBackEdge()) {
                        g.changeEdgeColor(currentEdge.getCopy(), Color.blue, true);
                        g.changeEdgeDirection(currentEdge.getCopy(), null, true);
                    }
                    else {
                        g.changeEdgeColor(currentEdge.getCopy(), Color.green, true);
                    }
                }
            }
            currentGraph.resetCopyData();
        }
        g.markForRepaint();
    }
    
    private static void resetDFSData(final Vector nodes, final Vector edges) {
        Enumeration enum1 = nodes.elements();
        while (enum1.hasMoreElements()) {
            final DFSNodeEx tempNode = enum1.nextElement();
            tempNode.setNumber(0);
            tempNode.setLowNumber(0);
            tempNode.setParent(null);
        }
        enum1 = edges.elements();
        while (enum1.hasMoreElements()) {
            final DFSEdgeEx tempEdge = enum1.nextElement();
            tempEdge.setIsUsed(false);
            tempEdge.setIsBackEdge(false);
        }
    }
}
