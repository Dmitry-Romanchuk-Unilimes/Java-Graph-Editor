// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphException.GraphException;
import graphStructure.Graph;
import graphStructure.LogEntry;
import graphStructure.Node;
import operation.extenders.CanNodeEx;
import operation.extenders.NormalEdgeEx;
import operation.extenders.NormalNodeEx;

import java.awt.*;
import java.util.Vector;

public class NormalLabelOperation
{
    public static Vector normalLabel(final Graph g) throws Exception {
        return normalLabel(g, true);
    }
    
    public static Vector normalLabel(final Graph g, final boolean check) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Normal Labeling");
        if (check && g.getNumNodes() <= 2) {
            logEntry.setData("Graph did not have 3 Nodes");
            g.stopLogEntry(logEntry);
            throw new GraphException("3 or more nodes required!");
        }
        if (check && !PlanarityOperation.isPlanar(g)) {
            logEntry.setData("Graph was not Planar");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not planar!");
        }
        return normalLabel(g, CanonicalOrderOperation.canonicalOrder(g, false), logEntry);
    }
    
    public static Vector normalLabel(final Graph g, final Node fNode, final Node sNode, final Node tNode) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Normal Labeling");
        return normalLabel(g, CanonicalOrderOperation.canonicalOrder(g, fNode, sNode, tNode), logEntry);
    }
    
    private static Vector normalLabel(final Graph g, Vector nodesInCanonicalOrder, final LogEntry logEntry) throws Exception {
        final Vector rootNodes = new Vector(3);
        g.createNodeExtenders(new NormalNodeEx().getClass());
        g.createEdgeExtenders(new NormalEdgeEx().getClass());
        final Vector tempVector = new Vector(nodesInCanonicalOrder.size());
        for (int i = 0; i < nodesInCanonicalOrder.size(); ++i) {
            final CanNodeEx canNode = nodesInCanonicalOrder.elementAt(i);
            ((NormalNodeEx)canNode.getRef().getExtender()).setCanonicalNumber(canNode.getCanonicalNumber());
            tempVector.addElement(canNode.getRef().getExtender());
        }
        nodesInCanonicalOrder.removeAllElements();
        nodesInCanonicalOrder = tempVector;
        final int numNodes = nodesInCanonicalOrder.size();
        final NormalNodeEx firstNode = (NormalNodeEx)nodesInCanonicalOrder.elementAt(numNodes - 1);
        final NormalNodeEx secondNode = (NormalNodeEx)nodesInCanonicalOrder.elementAt(numNodes - 2);
        final NormalNodeEx thirdNode = (NormalNodeEx)nodesInCanonicalOrder.elementAt(0);
        firstNode.setR1Parent(firstNode);
        firstNode.setR2Parent(firstNode);
        firstNode.setR3Parent(firstNode);
        secondNode.setR1Parent(secondNode);
        secondNode.setR2Parent(secondNode);
        secondNode.setR3Parent(secondNode);
        thirdNode.setR1Parent(thirdNode);
        thirdNode.setR2Parent(thirdNode);
        thirdNode.setR3Parent(thirdNode);
        rootNodes.addElement(firstNode);
        rootNodes.addElement(secondNode);
        rootNodes.addElement(thirdNode);
        if (numNodes > 3) {
            for (int j = numNodes - 3; j > 0; --j) {
                final NormalNodeEx currentNode = (NormalNodeEx)nodesInCanonicalOrder.elementAt(j);
                final Vector incidentEdges = currentNode.incidentEdgesToSmallerCanonicalNumber();
                NormalEdgeEx currentEdge = incidentEdges.elementAt(0);
                currentEdge.setNormalLabel(2);
                currentNode.setR2Parent((NormalNodeEx)currentEdge.otherEndFrom(currentNode));
                for (int k = 1; k < incidentEdges.size() - 1; ++k) {
                    currentEdge = incidentEdges.elementAt(k);
                    currentEdge.setNormalLabel(1);
                    ((NormalNodeEx)currentEdge.otherEndFrom(currentNode)).setR1Parent(currentNode);
                }
                currentEdge = incidentEdges.elementAt(incidentEdges.size() - 1);
                currentEdge.setNormalLabel(3);
                currentNode.setR3Parent((NormalNodeEx)currentEdge.otherEndFrom(currentNode));
            }
            final Vector incidentEdges = thirdNode.incidentEdges();
            for (int l = 0; l < incidentEdges.size(); ++l) {
                final NormalEdgeEx currentEdge = incidentEdges.elementAt(l);
                final NormalNodeEx otherNode = (NormalNodeEx)currentEdge.otherEndFrom(thirdNode);
                if (otherNode.getCanonicalNumber() > 2) {
                    currentEdge.setNormalLabel(1);
                    otherNode.setR1Parent(thirdNode);
                }
            }
        }
        g.stopLogEntry(logEntry);
        return rootNodes;
    }
    
    public static void displayNormalLabeling(final Graph g, final Node fNode, final Node sNode, final Node tNode) throws Exception {
        normalLabel(g, fNode, sNode, tNode);
        displayNormalLabeling(g, g.getNodeExtenders(), g.getEdgeExtenders());
        g.markForRepaint();
    }
    
    public static void displayNormalLabeling(final Graph g) throws Exception {
        normalLabel(g);
        displayNormalLabeling(g, g.getNodeExtenders(), g.getEdgeExtenders());
        g.markForRepaint();
    }
    
    private static void displayNormalLabeling(final Graph g, final Vector nodes, final Vector edges) throws Exception {
        for (int j = 0; j < nodes.size(); ++j) {
            final NormalNodeEx currentNode = nodes.elementAt(j);
            g.changeNodeDrawX(currentNode, false, true);
            if (currentNode.getCanonicalNumber() == 1) {
                g.changeNodeColor(currentNode, Color.blue, true);
            }
            else if (currentNode.getCanonicalNumber() == 2) {
                g.changeNodeColor(currentNode, Color.green, true);
            }
            else if (currentNode.getCanonicalNumber() == nodes.size()) {
                g.changeNodeColor(currentNode, Color.red, true);
            }
            else {
                g.changeNodeColor(currentNode, Color.darkGray, true);
            }
        }
        for (int i = 0; i < edges.size(); ++i) {
            final NormalEdgeEx currentEdge = edges.elementAt(i);
            if (currentEdge.getNormalLabel() == 1) {
                g.changeEdgeColor(currentEdge, Color.red, true);
                g.changeEdgeDirection(currentEdge, currentEdge.getNormalLabelSourceNode(), true);
            }
            else if (currentEdge.getNormalLabel() == 2) {
                g.changeEdgeColor(currentEdge, Color.green, true);
                g.changeEdgeDirection(currentEdge, currentEdge.getNormalLabelSourceNode(), true);
            }
            else if (currentEdge.getNormalLabel() == 3) {
                g.changeEdgeColor(currentEdge, Color.blue, true);
                g.changeEdgeDirection(currentEdge, currentEdge.getNormalLabelSourceNode(), true);
            }
            else {
                g.changeEdgeColor(currentEdge, Color.black, true);
                currentEdge.setDirectedFrom(null);
            }
        }
    }
}
