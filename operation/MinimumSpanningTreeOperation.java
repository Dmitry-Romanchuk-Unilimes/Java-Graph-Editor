// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import dataStructure.binaryHeap.BinaryHeap;
import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.LogEntry;
import graphStructure.Node;
import operation.extenders.MSTEdgeEx;
import operation.extenders.MSTNodeEx;

import java.awt.*;
import java.util.Vector;

public class MinimumSpanningTreeOperation
{
    public static Graph getMinimumSpanningTree(final Graph g) {
        return getMinimumSpanningTree(g, false);
    }
    
    public static Graph getMinimumSpanningTree(final Graph g, final boolean copyData) {
        final LogEntry logEntry = g.startLogEntry("Minimum Spanning Tree");
        if (g.getNumNodes() < 2) {
            logEntry.setData("Graph had less than 2 Nodes");
            g.stopLogEntry(logEntry);
            return null;
        }
        if (!ConnectivityOperation.isConnected(g)) {
            ConnectivityOperation.makeConnected(g);
        }
        g.clearNodeLabels();
        g.removeEdgeDirections();
        final Vector nodes = g.createNodeExtenders(new MSTNodeEx().getClass());
        g.createEdgeExtenders(new MSTEdgeEx().getClass());
        final Vector mstEdges = new Vector();
        final BinaryHeap bh = new BinaryHeap();
        MSTNodeEx currentNode = nodes.firstElement();
        currentNode.setCost(0.0);
        bh.insert(currentNode);
        for (int w = 1; w < nodes.size(); ++w) {
            currentNode = nodes.elementAt(w);
            currentNode.setCost(Double.MAX_VALUE);
            bh.insert(currentNode);
        }
        while (!bh.isEmpty()) {
            currentNode = (MSTNodeEx)bh.extractMin();
            if (currentNode.getLinkEdge() != null) {
                mstEdges.addElement(currentNode.getLinkEdge().getRef());
            }
            currentNode.setMarked(true);
            final Vector incidentEdges = currentNode.incidentEdges();
            for (int j = 0; j < incidentEdges.size(); ++j) {
                final MSTEdgeEx linkEdge = incidentEdges.elementAt(j);
                final MSTNodeEx otherNode = (MSTNodeEx)linkEdge.otherEndFrom(currentNode);
                final double length = linkEdge.getLength();
                if (!otherNode.isMarked() && otherNode.getCost() > length) {
                    otherNode.setLinkEdge(linkEdge);
                    otherNode.setCost(length);
                    bh.decreaseKey(otherNode);
                }
            }
        }
        g.stopLogEntry(logEntry);
        return g.copyEdges(mstEdges, copyData);
    }
    
    public static void drawMinimumSpanningTree(final Graph g) {
        final Graph gc = getMinimumSpanningTree(g, true);
        final Vector nodes = g.getNodes();
        for (int i = 0; i < nodes.size(); ++i) {
            final Node aNode = nodes.elementAt(i);
            g.changeNodeColor(aNode, Node.DEFAULT_COLOR, true);
            g.changeNodeLabel(aNode, "", true);
            g.changeNodeDrawX(aNode, false, true);
        }
        final Vector edges = g.getEdges();
        for (int j = 0; j < edges.size(); ++j) {
            final Edge anEdge = edges.elementAt(j);
            g.changeEdgeColor(anEdge, Edge.DEFAULT_COLOR, true);
        }
        if (gc != null) {
            final Vector mstEdges = gc.getEdges();
            for (int k = 0; k < mstEdges.size(); ++k) {
                final Edge anEdge = mstEdges.elementAt(k);
                g.changeEdgeColor(anEdge.getCopy(), Color.green, true);
            }
        }
        g.markForRepaint();
    }
}
