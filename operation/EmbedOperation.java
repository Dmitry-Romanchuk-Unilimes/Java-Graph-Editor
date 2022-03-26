// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import dataStructure.pqTree.PQDNode;
import dataStructure.pqTree.PQNode;
import dataStructure.pqTree.PQTree;
import graphException.GraphException;
import graphStructure.*;
import operation.extenders.PQEdgeEx;
import operation.extenders.PQNodeEx;
import operation.extenders.STNodeEx;

import java.util.Enumeration;
import java.util.Vector;

public class EmbedOperation
{
    public static void embed(final Graph g) throws Exception {
        embed(g, true);
    }
    
    public static void embed(final Graph g, final boolean check) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Embedding");
        if (check && !PlanarityOperation.isPlanar(g)) {
            logEntry.setData("Graph was not Planar");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not planar!");
        }
        final Vector graphs = BiconnectivityOperation.getBiconnectedComponents(g, true);
        g.deleteAllEdges();
        for (int i = 0; i < graphs.size(); ++i) {
            final Graph aGraph = graphs.elementAt(i);
            if (aGraph.getNumNodes() > 2) {
                final PQNodeEx tNode = upwardEmbed(aGraph);
                if (tNode != null) {
                    entireEmbed(aGraph, tNode);
                }
            }
            final Vector edges = aGraph.getEdges();
            for (int h = 0; h < edges.size(); ++h) {
                final Edge anEdge = edges.elementAt(h);
                final Node sNode = (Node)anEdge.getStartNode();
                final Node dNode = (Node)anEdge.getEndNode();
                if (anEdge.getDirectedSourceNode() != null) {
                    anEdge.setCopy(new Edge((Edge)anEdge.getMasterCopy(), anEdge.getDirectedSourceNode().getMasterCopy(), sNode.getMasterCopy(), dNode.getMasterCopy()));
                }
                else {
                    anEdge.setCopy(new Edge((Edge)anEdge.getMasterCopy(), null, sNode.getMasterCopy(), dNode.getMasterCopy()));
                }
            }
            final Vector nodes = aGraph.getNodes();
            for (int j = 0; j < nodes.size(); ++j) {
                final Node aNode = nodes.elementAt(j);
                for (int k = 0; k < aNode.incidentEdges().size(); ++k) {
                    final Edge anEdge = aNode.incidentEdges().elementAt(k);
                    Node sNode;
                    for (sNode = aNode; sNode.getCopy() != null; sNode = (Node)sNode.getCopy()) {}
                    g.addEdgeNoCheck(sNode, anEdge.getCopy());
                }
            }
        }
        g.stopLogEntry(logEntry);
    }
    
    private static PQNodeEx upwardEmbed(final Graph g) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Upward Embedding");
        if (g.getNumNodes() == 2) {
            g.stopLogEntry(logEntry);
            return null;
        }
        PQNodeEx tNode = null;
        final Vector upwardEdges = new Vector();
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
            tNode = nodesInStOrder.lastElement();
            PQNodeEx currentNode = nodesInStOrder.firstElement();
            upwardEdges.addElement(new Vector<Vector>());
            Enumeration enumEdges = currentNode.incidentEdges().elements();
            while (enumEdges.hasMoreElements()) {
                pqTree.getRoot().addChild(enumEdges.nextElement().getPQNode());
            }
            for (int k = 2; k <= nodesInStOrder.size(); ++k) {
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
                    throw new Exception("A PQ-Tree reduction returned a null tree during upwardEmbed!");
                }
                final Vector fullLeaves = pertRoot.getFullLeaves();
                if (fullLeaves == null) {
                    throw new Exception("*** ERROR no full leaves were returned during embedding!");
                }
                upwardEdges.addElement(fullLeaves);
                if (k < nodesInStOrder.size()) {
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
                    if (pertRoot.isQNode() && (!pertRoot.isFull() || pertRoot.isPseudoNode())) {
                        final PQNode to = pertRoot.getFullLeavesTo();
                        final PQNode from = pertRoot.getFullLeavesFrom();
                        pertRoot.replaceFullChildrenWith(newRoot);
                        final PQDNode dNode = new PQDNode(currentNode);
                        dNode.setParent(newRoot.getParent());
                        if (from != null) {
                            from.getSiblings().replacePQNode(newRoot, dNode);
                            dNode.getSiblings().addPQNode(from);
                            newRoot.getSiblings().replacePQNode(from, dNode);
                            dNode.getSiblings().addPQNode(newRoot);
                            dNode.setDirection(newRoot);
                        }
                        else {
                            if (to == null) {
                                throw new Exception("*** ERROR neither from or to existed when adding dNode!");
                            }
                            to.getSiblings().replacePQNode(newRoot, dNode);
                            dNode.getSiblings().addPQNode(to);
                            newRoot.getSiblings().replacePQNode(to, dNode);
                            dNode.getSiblings().addPQNode(newRoot);
                            dNode.setDirection(to);
                        }
                        if (pertRoot.hasOnlyTwoChildren() && !pertRoot.isPseudoNode()) {
                            if (pertRoot.getEndMostChildren().size() != 2) {
                                throw new Exception("*** ERROR endMostChildren did not have size 2!");
                            }
                            if (pertRoot.getEndMostChildren().PQNodeAt(0).isDNode() || pertRoot.getEndMostChildren().PQNodeAt(1).isDNode()) {
                                throw new Exception("*** ERROR pNode was created with dNode children!");
                            }
                            pertRoot.convertToPNode();
                        }
                        else if (pertRoot.hasOnlyOneChild() && !pertRoot.isPseudoNode()) {
                            if (pertRoot.getEndMostChildren().size() != 1) {
                                throw new Exception("*** ERROR endMostChildren did not have size 1!");
                            }
                            if (pertRoot.getEndMostChildren().PQNodeAt(0).isDNode()) {
                                throw new Exception("*** ERROR pNode was created with dNode child!");
                            }
                            if (pertRoot == pqTree.getRoot()) {
                                newRoot.becomeRoot();
                                pqTree.setRoot(newRoot);
                            }
                            else {
                                pertRoot.getParent().replaceChild(pertRoot, newRoot);
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
            boolean reverse = false;
            int indexOfNodeToReverse = 0;
            for (int h = nodesInStOrder.size() - 1; h >= 0; --h) {
                final Vector fullLeaves = upwardEdges.elementAt(h);
                for (int l = 0; l < fullLeaves.size(); ++l) {
                    final PQNode pqNode = fullLeaves.elementAt(l);
                    if (pqNode.isDNode()) {
                        reverse = ((PQDNode)pqNode).readInReverseDirection();
                        if (reverse) {
                            indexOfNodeToReverse = ((PQNodeEx)pqNode.getData()).getStNumber() - 1;
                            final Vector edgesOfNodeToReverse = upwardEdges.elementAt(indexOfNodeToReverse);
                            final Vector edgesOfNodeReversed = new Vector();
                            for (int x = edgesOfNodeToReverse.size() - 1; x >= 0; --x) {
                                if (edgesOfNodeToReverse.elementAt(x).isDNode()) {
                                    edgesOfNodeToReverse.elementAt(x).toggleReadInReverseDirection();
                                }
                                edgesOfNodeReversed.addElement(edgesOfNodeToReverse.elementAt(x));
                            }
                            upwardEdges.setElementAt(edgesOfNodeReversed, indexOfNodeToReverse);
                        }
                    }
                }
            }
            for (int h = 0; h < nodesInStOrder.size(); ++h) {
                currentNode = nodesInStOrder.elementAt(h);
                final Vector fullLeaves = upwardEdges.elementAt(h);
                currentNode.resetIncidentEdges();
                for (int l = 0; l < fullLeaves.size(); ++l) {
                    final PQNode pqNode = fullLeaves.elementAt(l);
                    if (!pqNode.isDNode()) {
                        currentNode.addIncidentEdgeNoCheck((EdgeInterface)pqNode.getData());
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("PQTree error during embedding test");
            g.stopLogEntry(logEntry);
            throw e;
        }
        g.stopLogEntry(logEntry);
        return tNode;
    }
    
    private static void entireEmbed(final Graph g, final PQNodeEx tNode) {
        final LogEntry logEntry = g.startLogEntry("Entire Embedding");
        final Vector nodes = g.getNodeExtenders();
        for (int i = 0; i < nodes.size(); ++i) {
            nodes.elementAt(i).setIsOld(false);
        }
        entireEmbedHelper(g, tNode);
        for (int i = 0; i < nodes.size(); ++i) {
            nodes.elementAt(i).setIsOld(false);
        }
        g.stopLogEntry(logEntry);
    }
    
    private static void entireEmbedHelper(final Graph g, final PQNodeEx aNode) {
        aNode.setIsOld(true);
        final Vector incidentEdges = aNode.incidentEdges();
        for (int i = incidentEdges.size() - 1; i >= 0; --i) {
            final PQEdgeEx currentEdge = incidentEdges.elementAt(i);
            final PQNodeEx otherNode = (PQNodeEx)currentEdge.otherEndFrom(aNode);
            if (otherNode.getStNumber() < aNode.getStNumber()) {
                otherNode.addIncidentEdgeNoCheck(currentEdge);
                if (!otherNode.isOld()) {
                    entireEmbedHelper(g, otherNode);
                }
            }
        }
    }
}
