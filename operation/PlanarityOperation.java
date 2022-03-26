// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import dataStructure.pqTree.PQNode;
import dataStructure.pqTree.PQTree;
import graphStructure.Graph;
import graphStructure.LogEntry;
import operation.extenders.PQEdgeEx;
import operation.extenders.PQNodeEx;
import operation.extenders.STNodeEx;

import java.util.Enumeration;
import java.util.Vector;

public class PlanarityOperation
{
    public static boolean isPlanar(final Graph g) {
        final LogEntry logEntry = g.startLogEntry("Test Planarity");
        if (!numEdgesLessThanOrEqualThreeTimesNumVerticesMinusSix(g)) {
            logEntry.setData("Edges were more than 3V-6");
            g.stopLogEntry(logEntry);
            return false;
        }
        final Vector graphs = BiconnectivityOperation.getBiconnectedComponents(g, false);
        for (int i = 0; i < graphs.size(); ++i) {
            final Graph aGraph = graphs.elementAt(i);
            if (!isPlanarHelper(aGraph)) {
                g.stopLogEntry(logEntry);
                return false;
            }
        }
        g.stopLogEntry(logEntry);
        return true;
    }
    
    private static boolean isPlanarHelper(final Graph g) {
        final LogEntry logEntry = g.startLogEntry("Test Planarity Helper");
        if (g.getNumNodes() <= 2) {
            g.stopLogEntry(logEntry);
            return true;
        }
        try {
            final PQTree pqTree = new PQTree();
            final Vector nodesInStOrder = STNumberOperation.stNumber(g, false);
            g.createNodeExtenders(new PQNodeEx().getClass());
            Vector edges = g.createEdgeExtenders(new PQEdgeEx().getClass());
            for (int i = 0; i < edges.size(); ++i) {
                final PQEdgeEx anEdge = edges.elementAt(i);
                anEdge.setPQNode(new PQNode(anEdge));
            }
            for (int j = 0; j < nodesInStOrder.size(); ++j) {
                final STNodeEx stNode = nodesInStOrder.elementAt(j);
                ((PQNodeEx)stNode.getRef().getExtender()).setStNumber(stNode.getStNumber());
                nodesInStOrder.setElementAt(stNode.getRef().getExtender(), j);
            }
            PQNodeEx currentNode = nodesInStOrder.firstElement();
            Enumeration enumEdges = currentNode.incidentEdges().elements();
            while (enumEdges.hasMoreElements()) {
                pqTree.getRoot().addChild(enumEdges.nextElement().getPQNode());
            }
            for (int k = 2; k < nodesInStOrder.size(); ++k) {
                edges = new Vector();
                currentNode = nodesInStOrder.elementAt(k - 1);
                enumEdges = currentNode.incidentEdges().elements();
                while (enumEdges.hasMoreElements()) {
                    final PQEdgeEx anEdge = enumEdges.nextElement();
                    if (((PQNodeEx)anEdge.otherEndFrom(currentNode)).getStNumber() < k) {
                        edges.addElement(anEdge.getPQNode());
                    }
                }
                final PQNode pertRoot = pqTree.reduction(edges);
                if (pqTree.isNullTree()) {
                    g.stopLogEntry(logEntry);
                    return false;
                }
                PQNode newRoot = new PQNode();
                edges = new Vector();
                enumEdges = currentNode.incidentEdges().elements();
                while (enumEdges.hasMoreElements()) {
                    final PQEdgeEx anEdge = enumEdges.nextElement();
                    if (((PQNodeEx)anEdge.otherEndFrom(currentNode)).getStNumber() > k) {
                        edges.addElement(anEdge.getPQNode());
                    }
                }
                if (edges.size() == 1) {
                    newRoot = edges.firstElement();
                }
                else {
                    enumEdges = edges.elements();
                    while (enumEdges.hasMoreElements()) {
                        newRoot.addChild(enumEdges.nextElement());
                    }
                }
                if (pertRoot.isQNode()) {
                    if (pertRoot.isFull()) {
                        System.out.println("isPseudo: " + pertRoot.isPseudoNode());
                    }
                    pertRoot.replaceFullChildrenWith(newRoot);
                    if (!pertRoot.isPseudoNode()) {
                        if (pertRoot.hasOnlyTwoChildren()) {
                            pertRoot.convertToPNode();
                        }
                        else if (pertRoot.hasOnlyOneChild() && !pertRoot.isPseudoNode()) {
                            if (pertRoot == pqTree.getRoot()) {
                                newRoot.becomeRoot();
                                pqTree.setRoot(newRoot);
                            }
                            else {
                                pertRoot.getParent().replaceChild(pertRoot, newRoot);
                            }
                        }
                    }
                }
                else if (pertRoot == pqTree.getRoot()) {
                    pqTree.setRoot(newRoot);
                }
                else {
                    pertRoot.getParent().replaceChild(pertRoot, newRoot);
                }
                pertRoot.clear();
            }
        }
        catch (Exception e) {
            System.out.println("PQTree error during planarity test");
            e.printStackTrace();
            g.stopLogEntry(logEntry);
            return false;
        }
        g.stopLogEntry(logEntry);
        return true;
    }
    
    public static boolean numEdgesLessThanOrEqualThreeTimesNumVerticesMinusSix(final Graph g) {
        if (g.getNumNodes() == 2) {
            return true;
        }
        final int threeTimeNumVerticesMinusSix = 3 * g.getNumNodes() - 6;
        return g.getNumEdges() <= threeTimeNumVerticesMinusSix;
    }
}
