// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import dataStructure.binaryHeap.BinaryHeap;
import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.LogEntry;
import graphStructure.Node;
import operation.extenders.SPEdgeEx;
import operation.extenders.SPNodeEx;

import java.awt.*;
import java.util.Vector;

public class DijkstraShortestPathOperation
{
    public static Graph getShortestPath(final Graph g, final Node source, final Node dest) {
        return getShortestPath(g, source, dest, false);
    }
    
    public static Graph getShortestPath(final Graph g, final Node source, final Node dest, final boolean copyData) {
        final LogEntry logEntry = g.startLogEntry("Dijkstra Shortest Path");
        if (g.getNumNodes() < 2) {
            logEntry.setData("Graph had less than 2 Nodes");
            g.stopLogEntry(logEntry);
            return null;
        }
        final BinaryHeap heap = new BinaryHeap();
        boolean destReached = false;
        final SPEdgeEx dummyEdge = new SPEdgeEx();
        final Vector spEdges = new Vector();
        final Vector nodes = g.createNodeExtenders(new SPNodeEx().getClass());
        g.createEdgeExtenders(new SPEdgeEx().getClass());
        for (int i = 0; i < nodes.size(); ++i) {
            final SPNodeEx currentNode = nodes.elementAt(i);
            currentNode.setCost(Double.MAX_VALUE);
            currentNode.setTraceBackEdge(null);
            currentNode.setIsDone(false);
        }
        final SPNodeEx sourceNode = (SPNodeEx)source.getExtender();
        final SPNodeEx destNode = (SPNodeEx)dest.getExtender();
        sourceNode.setCost(0.0);
        sourceNode.setTraceBackEdge(dummyEdge);
        sourceNode.setIsDone(true);
        heap.insert(sourceNode);
        do {
            final SPNodeEx currentNode = (SPNodeEx)heap.extractMin();
            if (currentNode.getCost() == Double.MAX_VALUE) {
                logEntry.setData("No Path Found");
                g.stopLogEntry(logEntry);
                return null;
            }
            currentNode.setIsDone(true);
            if (currentNode == destNode) {
                traceBack(destNode, dummyEdge, spEdges);
                destReached = true;
                break;
            }
            final Vector incidentEdges = currentNode.incidentOutgoingEdges();
            for (int i = 0; i < incidentEdges.size(); ++i) {
                final SPEdgeEx currentEdge = incidentEdges.elementAt(i);
                final double length = currentEdge.getLength();
                final SPNodeEx otherNode = (SPNodeEx)currentEdge.otherEndFrom(currentNode);
                if (!otherNode.isDone() && currentNode.getCost() + length < otherNode.getCost()) {
                    otherNode.setCost(currentNode.getCost() + length);
                    if (!otherNode.isUsed()) {
                        heap.insert(otherNode);
                    }
                    else {
                        heap.decreaseKey(otherNode);
                    }
                    otherNode.setTraceBackEdge(currentEdge);
                }
            }
        } while (!heap.isEmpty());
        if (destReached) {
            logEntry.setData("Path Length: " + destNode.getCost());
            g.stopLogEntry(logEntry);
            return g.copyEdges(spEdges, copyData);
        }
        logEntry.setData("No Path Found");
        g.stopLogEntry(logEntry);
        return null;
    }
    
    private static void traceBack(final SPNodeEx node, final SPEdgeEx dummyEdge, final Vector spEdges) {
        if (node.getTraceBackEdge() != null && node.getTraceBackEdge() != dummyEdge) {
            spEdges.addElement(node.getTraceBackEdge().getRef());
            traceBack((SPNodeEx)node.getTraceBackEdge().otherEndFrom(node), dummyEdge, spEdges);
        }
    }
    
    public static double drawShortestPath(final Graph g, final Node source, final Node dest) {
        final Graph gc = getShortestPath(g, source, dest, true);
        Vector nodes = g.getNodes();
        for (int i = 0; i < nodes.size(); ++i) {
            final Node aNode = nodes.elementAt(i);
            g.changeNodeColor(aNode, Node.DEFAULT_COLOR, true);
            g.changeNodeDrawX(aNode, false, true);
        }
        Vector edges = g.getEdges();
        for (int j = 0; j < edges.size(); ++j) {
            final Edge anEdge = edges.elementAt(j);
            g.changeEdgeColor(anEdge, Edge.DEFAULT_COLOR, true);
        }
        if (gc == null) {
            g.markForRepaint();
            return -1.0;
        }
        double pathLength = 0.0;
        nodes = gc.getNodes();
        for (int k = 0; k < nodes.size(); ++k) {
            final Node aNode = nodes.elementAt(k);
            g.changeNodeColor(aNode.getCopy(), Color.green, true);
        }
        edges = gc.getEdges();
        for (int k = 0; k < edges.size(); ++k) {
            final Edge anEdge = edges.elementAt(k);
            g.changeEdgeColor(anEdge.getCopy(), Color.green, true);
            pathLength += anEdge.getLength();
        }
        g.markForRepaint();
        return pathLength;
    }
}
