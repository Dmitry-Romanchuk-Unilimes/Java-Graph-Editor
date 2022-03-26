// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphException.GraphException;
import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.LogEntry;
import graphStructure.Node;
import operation.extenders.CanEdgeEx;
import operation.extenders.CanNodeEx;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

public class CanonicalOrderOperation
{
    private static CanNodeEx candidateAccess;
    
    static {
        CanonicalOrderOperation.candidateAccess = null;
    }
    
    public static Vector canonicalOrder(final Graph g) throws Exception {
        return canonicalOrder(g, true);
    }
    
    public static Vector canonicalOrder(final Graph g, final boolean check) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Canonical Ordering");
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
        if (!MakeMaximalOperation.makeMaximal(g, false)) {
            EmbedOperation.embed(g, false);
        }
        final Node[] outerFaceTriangle = g.getRandomTriangularFace();
        if (outerFaceTriangle == null) {
            logEntry.setData("Could not find third outer face Node");
            g.stopLogEntry(logEntry);
            throw new Exception("Could not find third node for canonical!");
        }
        return canonicalOrder(g, outerFaceTriangle[0], outerFaceTriangle[1], outerFaceTriangle[2], logEntry);
    }
    
    public static Vector canonicalOrder(final Graph g, final Node fNode, final Node sNode, final Node tNode) throws Exception {
        return canonicalOrder(g, fNode, sNode, tNode, null);
    }
    
    private static Vector canonicalOrder(final Graph g, final Node fNode, final Node sNode, final Node tNode, LogEntry logEntry) throws Exception {
        if (logEntry == null) {
            logEntry = g.startLogEntry("Canonical Ordering");
        }
        final Vector nodesInCanonicalOrder = new Vector();
        final Vector nodes = g.createNodeExtenders(new CanNodeEx().getClass());
        g.createEdgeExtenders(new CanEdgeEx().getClass());
        final CanNodeEx firstNode = (CanNodeEx)fNode.getExtender();
        final CanNodeEx secondNode = (CanNodeEx)sNode.getExtender();
        final CanNodeEx thirdNode = (CanNodeEx)tNode.getExtender();
        for (int i = 0; i < nodes.size(); ++i) {
            nodes.elementAt(i).setOuterFaceEdgeCount(0);
            nodes.elementAt(i).setIsOnOuterFace(false);
            nodes.elementAt(i).setCanonicalNumber(-1);
            nodes.elementAt(i).setCandidateLeft(null);
            nodes.elementAt(i).setCandidateRight(null);
        }
        if (thirdNode == null) {
            logEntry.setData("Could not find third outer face Node");
            g.stopLogEntry(logEntry);
            throw new Exception("*** ERROR the third node was not found in canonical!");
        }
        firstNode.setCanonicalNumber(1);
        firstNode.setIsOnOuterFace(true);
        secondNode.setCanonicalNumber(2);
        secondNode.setIsOnOuterFace(true);
        thirdNode.setCanonicalNumber(nodes.size());
        thirdNode.setIsOnOuterFace(true);
        Enumeration enumEdges = firstNode.incidentEdges().elements();
        while (enumEdges.hasMoreElements()) {
            ((CanNodeEx)enumEdges.nextElement().otherEndFrom(firstNode)).incrementOuterFaceEdgeCount();
        }
        enumEdges = secondNode.incidentEdges().elements();
        while (enumEdges.hasMoreElements()) {
            ((CanNodeEx)enumEdges.nextElement().otherEndFrom(secondNode)).incrementOuterFaceEdgeCount();
        }
        enumEdges = thirdNode.incidentEdges().elements();
        while (enumEdges.hasMoreElements()) {
            ((CanNodeEx)enumEdges.nextElement().otherEndFrom(thirdNode)).incrementOuterFaceEdgeCount();
        }
        (CanonicalOrderOperation.candidateAccess = thirdNode).setCandidateRight(CanonicalOrderOperation.candidateAccess);
        CanonicalOrderOperation.candidateAccess.setCandidateLeft(CanonicalOrderOperation.candidateAccess);
        for (int canonicalNumber = nodes.size(); canonicalNumber > 2; --canonicalNumber) {
            final CanNodeEx currentNode = CanonicalOrderOperation.candidateAccess;
            if (CanonicalOrderOperation.candidateAccess == null) {
                logEntry.setData("Ran out of Nodes to process");
                g.stopLogEntry(logEntry);
                throw new Exception("Ran out of candidates! (" + canonicalNumber + ")");
            }
            currentNode.setCanonicalNumber(canonicalNumber);
            nodesInCanonicalOrder.addElement(currentNode);
            if (CanonicalOrderOperation.candidateAccess.getCandidateLeft() == CanonicalOrderOperation.candidateAccess) {
                CanonicalOrderOperation.candidateAccess = null;
            }
            else if (CanonicalOrderOperation.candidateAccess.getCandidateLeft() == CanonicalOrderOperation.candidateAccess.getCandidateRight()) {
                (CanonicalOrderOperation.candidateAccess = CanonicalOrderOperation.candidateAccess.getCandidateLeft()).setCandidateRight(CanonicalOrderOperation.candidateAccess);
                CanonicalOrderOperation.candidateAccess.setCandidateLeft(CanonicalOrderOperation.candidateAccess);
            }
            else {
                currentNode.getCandidateRight().setCandidateLeft(currentNode.getCandidateLeft());
                currentNode.getCandidateLeft().setCandidateRight(currentNode.getCandidateRight());
                CanonicalOrderOperation.candidateAccess = currentNode.getCandidateLeft();
            }
            currentNode.setCandidateLeft(null);
            currentNode.setCandidateRight(null);
            final Vector edges = currentNode.incidentEdges();
            for (int j = 0; j < edges.size(); ++j) {
                final CanEdgeEx tempEdge = edges.elementAt(j);
                final CanNodeEx otherNode = (CanNodeEx)tempEdge.otherEndFrom(currentNode);
                if (otherNode.getCanonicalNumber() == -1) {
                    if (!otherNode.isOnOuterFace()) {
                        otherNode.setIsOnOuterFace(true);
                        final Vector incidentEdges = otherNode.incidentEdges();
                        for (int m = 0; m < incidentEdges.size(); ++m) {
                            final CanNodeEx otherNode2 = (CanNodeEx)incidentEdges.elementAt(m).otherEndFrom(otherNode);
                            if (otherNode2.getCanonicalNumber() == -1) {
                                otherNode2.incrementOuterFaceEdgeCount();
                                verifyCandidate(otherNode2, firstNode, secondNode);
                            }
                        }
                    }
                    otherNode.decrementOuterFaceEdgeCount();
                    verifyCandidate(otherNode, firstNode, secondNode);
                }
            }
        }
        nodesInCanonicalOrder.addElement(secondNode);
        nodesInCanonicalOrder.addElement(firstNode);
        g.stopLogEntry(logEntry);
        return nodesInCanonicalOrder;
    }
    
    private static void verifyCandidate(final CanNodeEx potentialCandidate, final CanNodeEx firstNode, final CanNodeEx secondNode) {
        if (potentialCandidate.getCandidateLeft() != null && potentialCandidate.getOuterFaceEdgeCount() != 2) {
            if (potentialCandidate.getCandidateLeft() == potentialCandidate) {
                potentialCandidate.setCandidateLeft(CanonicalOrderOperation.candidateAccess = null);
                potentialCandidate.setCandidateRight(null);
            }
            else if (potentialCandidate.getCandidateLeft() == potentialCandidate.getCandidateRight()) {
                (CanonicalOrderOperation.candidateAccess = potentialCandidate.getCandidateLeft()).setCandidateLeft(CanonicalOrderOperation.candidateAccess);
                CanonicalOrderOperation.candidateAccess.setCandidateRight(CanonicalOrderOperation.candidateAccess);
                potentialCandidate.setCandidateLeft(null);
                potentialCandidate.setCandidateRight(null);
            }
            else {
                potentialCandidate.getCandidateLeft().setCandidateRight(potentialCandidate.getCandidateRight());
                potentialCandidate.getCandidateRight().setCandidateLeft(potentialCandidate.getCandidateLeft());
                if (potentialCandidate == CanonicalOrderOperation.candidateAccess) {
                    CanonicalOrderOperation.candidateAccess = potentialCandidate.getCandidateLeft();
                }
                potentialCandidate.setCandidateLeft(null);
                potentialCandidate.setCandidateRight(null);
            }
        }
        else if (potentialCandidate.getCandidateLeft() == null && potentialCandidate.getOuterFaceEdgeCount() == 2 && potentialCandidate.isOnOuterFace() && potentialCandidate != firstNode && potentialCandidate != secondNode) {
            if (CanonicalOrderOperation.candidateAccess == null) {
                (CanonicalOrderOperation.candidateAccess = potentialCandidate).setCandidateRight(CanonicalOrderOperation.candidateAccess);
                CanonicalOrderOperation.candidateAccess.setCandidateLeft(CanonicalOrderOperation.candidateAccess);
            }
            else if (CanonicalOrderOperation.candidateAccess.getCandidateLeft() == CanonicalOrderOperation.candidateAccess) {
                CanonicalOrderOperation.candidateAccess.setCandidateLeft(potentialCandidate);
                CanonicalOrderOperation.candidateAccess.setCandidateRight(potentialCandidate);
                potentialCandidate.setCandidateLeft(CanonicalOrderOperation.candidateAccess);
                potentialCandidate.setCandidateRight(CanonicalOrderOperation.candidateAccess);
            }
            else {
                potentialCandidate.setCandidateRight(CanonicalOrderOperation.candidateAccess);
                potentialCandidate.setCandidateLeft(CanonicalOrderOperation.candidateAccess.getCandidateLeft());
                potentialCandidate.getCandidateRight().setCandidateLeft(potentialCandidate);
                potentialCandidate.getCandidateLeft().setCandidateRight(potentialCandidate);
            }
        }
    }
    
    public static void displayCanonicalOrdering(final Graph g) throws Exception {
        displayCanonicalOrdering(g, canonicalOrder(g));
    }
    
    public static void displayCanonicalOrdering(final Graph g, final Node firstNode, final Node secondNode, final Node thirdNode) throws Exception {
        displayCanonicalOrdering(g, canonicalOrder(g, firstNode, secondNode, thirdNode));
    }
    
    public static void displayCanonicalOrdering(final Graph g, final Vector nodes) {
        for (int i = 0; i < nodes.size(); ++i) {
            final CanNodeEx currentNode = nodes.elementAt(i);
            g.changeNodeLabel(currentNode, String.valueOf(currentNode.getCanonicalNumber()), true);
            g.changeNodeDrawX(currentNode, false, true);
            if (currentNode.getCanonicalNumber() == 1 || currentNode.getCanonicalNumber() == 2 || currentNode.getCanonicalNumber() == nodes.size()) {
                g.changeNodeColor(currentNode, Color.green, true);
            }
            else {
                g.changeNodeColor(currentNode, Node.DEFAULT_COLOR, true);
            }
        }
        final Vector edges = g.getEdgeExtenders();
        for (int j = 0; j < edges.size(); ++j) {
            final CanEdgeEx currentEdge = edges.elementAt(j);
            g.changeEdgeColor(currentEdge, Edge.DEFAULT_COLOR, true);
            g.changeEdgeDirection(currentEdge, null, true);
        }
        g.markForRepaint();
    }
}
