// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphException.GraphException;
import graphStructure.*;
import operation.extenders.BiCompEdgeEx;
import operation.extenders.BiCompNodeEx;

import java.awt.*;
import java.util.Enumeration;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

public class BiconnectivityOperation
{
    public static Vector getBiconnectedComponents(final Graph g) {
        return getBiconnectedComponents(g, false);
    }
    
    public static Vector getBiconnectedComponents(final Graph g, final boolean copyData) {
        final LogEntry logEntry = g.startLogEntry("Get Biconnected Components");
        final Vector graphs = new Vector();
        final Vector oldGraphs = ConnectivityOperation.getConnectedComponents(g, copyData);
        for (int j = 0; j < oldGraphs.size(); ++j) {
            final Graph oldGraph = oldGraphs.elementAt(j);
            if (oldGraph.getNumNodes() <= 2) {
                graphs.addElement(oldGraph.copy(copyData));
            }
            else {
                final Vector newEdges = new Vector();
                final Graph graph = oldGraph.copy(copyData);
                int i = 0;
                boolean alreadyVisited = false;
                final Stack nodeStack = new Stack();
                final Stack edgeStack = new Stack();
                final Vector nodes = graph.createNodeExtenders(new BiCompNodeEx().getClass());
                Enumeration enum1 = nodes.elements();
                while (enum1.hasMoreElements()) {
                    final BiCompNodeEx tempNode = enum1.nextElement();
                    tempNode.setNumber(0);
                    tempNode.setLowNumber(0);
                    tempNode.setParent(null);
                }
                final Vector edges = graph.createEdgeExtenders(new BiCompEdgeEx().getClass());
                enum1 = edges.elements();
                while (enum1.hasMoreElements()) {
                    final BiCompEdgeEx tempEdge = enum1.nextElement();
                    tempEdge.setIsUsed(false);
                }
                BiCompNodeEx tempNode = nodes.firstElement();
                while (true) {
                    if (!alreadyVisited) {
                        ++i;
                        tempNode.setNumber(i);
                        tempNode.setLowNumber(i);
                        nodeStack.push(tempNode);
                    }
                    enum1 = tempNode.incidentEdges().elements();
                    boolean flag = false;
                    boolean hasUnusedEdges = false;
                    alreadyVisited = false;
                    while (enum1.hasMoreElements()) {
                        final BiCompEdgeEx tempEdge = enum1.nextElement();
                        if (!tempEdge.isUsed()) {
                            tempEdge.setIsUsed(true);
                            tempEdge.setWasAdded(true);
                            edgeStack.push(tempEdge);
                            hasUnusedEdges = true;
                            final BiCompNodeEx otherNode = (BiCompNodeEx)tempEdge.otherEndFrom(tempNode);
                            if (otherNode.getNumber() == 0) {
                                otherNode.setParent(tempNode);
                                tempNode = otherNode;
                                break;
                            }
                            tempNode.setLowNumber(Math.min(tempNode.getLowNumber(), otherNode.getNumber()));
                            alreadyVisited = true;
                            break;
                        }
                    }
                    if (!hasUnusedEdges) {
                        if (tempNode.getParent().getNumber() != 1) {
                            if (tempNode.getLowNumber() < tempNode.getParent().getNumber()) {
                                tempNode.getParent().setLowNumber(Math.min(tempNode.getParent().getLowNumber(), tempNode.getLowNumber()));
                            }
                            else {
                                final Graph newGraph = new Graph(oldGraph);
                                while (nodeStack.peek() != tempNode) {
                                    newGraph.addNode(nodeStack.pop().getRef());
                                }
                                if (nodeStack.peek() == tempNode) {
                                    newGraph.addNode(nodeStack.pop().getRef());
                                }
                                newGraph.addNode(((BiCompNodeEx)tempNode.getParent()).getRef());
                                newEdges.removeAllElements();
                                while (!edgeStack.peek().isBetween(tempNode, tempNode.getParent())) {
                                    final BiCompEdgeEx tempEdge = edgeStack.pop();
                                    tempEdge.setWasAdded(false);
                                    newEdges.addElement(tempEdge);
                                }
                                if (edgeStack.peek().isBetween(tempNode, tempNode.getParent())) {
                                    final BiCompEdgeEx tempEdge = edgeStack.pop();
                                    tempEdge.setWasAdded(false);
                                    newEdges.addElement(tempEdge);
                                }
                                graphs.addElement(newGraph.copyEdges(EdgeExtender.toEdge(newEdges), copyData));
                            }
                            tempNode = (BiCompNodeEx)tempNode.getParent();
                            alreadyVisited = true;
                            flag = true;
                        }
                        if (flag) {
                            continue;
                        }
                        final Graph newGraph = new Graph(oldGraph);
                        while (nodeStack.peek() != tempNode) {
                            newGraph.addNode(nodeStack.pop().getRef());
                        }
                        if (nodeStack.peek() == tempNode) {
                            newGraph.addNode(nodeStack.pop().getRef());
                        }
                        newGraph.addNode(nodes.firstElement().getRef());
                        newEdges.removeAllElements();
                        while (!edgeStack.peek().isBetween(tempNode, nodes.firstElement())) {
                            final BiCompEdgeEx tempEdge = edgeStack.pop();
                            tempEdge.setWasAdded(false);
                            newEdges.addElement(tempEdge);
                        }
                        if (edgeStack.peek().isBetween(tempNode, nodes.firstElement())) {
                            final BiCompEdgeEx tempEdge = edgeStack.pop();
                            tempEdge.setWasAdded(false);
                            newEdges.addElement(tempEdge);
                        }
                        graphs.addElement(newGraph.copyEdges(EdgeExtender.toEdge(newEdges), copyData));
                        final Enumeration enum2 = nodes.firstElement().incidentEdges().elements();
                        boolean flag2 = false;
                        while (enum2.hasMoreElements()) {
                            if (!enum2.nextElement().isUsed()) {
                                flag2 = true;
                            }
                        }
                        if (!flag2) {
                            break;
                        }
                        tempNode = nodes.firstElement();
                        alreadyVisited = true;
                    }
                }
            }
        }
        logEntry.setData(String.valueOf(graphs.size()) + " Biconnected Components found");
        g.stopLogEntry(logEntry);
        return graphs;
    }
    
    public static Vector findSeparatingNodes(final Graph g) {
        final LogEntry logEntry = g.startLogEntry("Find Separator Nodes");
        g.createNodeExtenders(new BiCompNodeEx().getClass());
        g.createEdgeExtenders(new BiCompEdgeEx().getClass());
        final Vector separatingNodes = new Vector();
        final Vector nodesFromEachComponent = ConnectivityOperation.getNodeFromEachConnectedComponent(g, true);
        for (int j = 0; j < nodesFromEachComponent.size(); ++j) {
            final BiCompNodeEx startNode = nodesFromEachComponent.elementAt(j);
            final Vector connectedNodes = ConnectivityOperation.getConnectedNodes(g, startNode);
            if (!startNode.hasNoIncidentEdges()) {
                int i = 0;
                boolean alreadyVisited = false;
                int subGraphNumber = 1;
                final Stack nodeStack = new Stack();
                final Stack edgeStack = new Stack();
                Enumeration enum1 = connectedNodes.elements();
                while (enum1.hasMoreElements()) {
                    final BiCompNodeEx tempNode = enum1.nextElement();
                    tempNode.setNumber(0);
                    tempNode.setLowNumber(0);
                    tempNode.setParent(null);
                    tempNode.setSubGraphNumber(0);
                }
                final Vector edges = g.getEdgeExtenders(NodeExtender.toNode(connectedNodes));
                enum1 = edges.elements();
                while (enum1.hasMoreElements()) {
                    final BiCompEdgeEx tempEdge = enum1.nextElement();
                    tempEdge.setIsUsed(false);
                    tempEdge.setSubGraphNumber(0);
                    tempEdge.setWasAdded(false);
                }
                BiCompNodeEx tempNode = connectedNodes.firstElement();
                while (true) {
                    if (!alreadyVisited) {
                        ++i;
                        tempNode.setNumber(i);
                        tempNode.setLowNumber(i);
                        nodeStack.push(tempNode);
                    }
                    enum1 = tempNode.incidentEdges().elements();
                    boolean flag = false;
                    boolean hasUnusedEdges = false;
                    alreadyVisited = false;
                    while (enum1.hasMoreElements()) {
                        final BiCompEdgeEx tempEdge = enum1.nextElement();
                        if (!tempEdge.isUsed()) {
                            tempEdge.setIsUsed(true);
                            tempEdge.setWasAdded(true);
                            edgeStack.push(tempEdge);
                            hasUnusedEdges = true;
                            final BiCompNodeEx otherNode = (BiCompNodeEx)tempEdge.otherEndFrom(tempNode);
                            if (otherNode.getNumber() == 0) {
                                otherNode.setParent(tempNode);
                                tempNode = otherNode;
                                break;
                            }
                            tempNode.setLowNumber(Math.min(tempNode.getLowNumber(), otherNode.getNumber()));
                            alreadyVisited = true;
                            break;
                        }
                    }
                    if (!hasUnusedEdges) {
                        if (tempNode.getParent().getNumber() != 1) {
                            if (tempNode.getLowNumber() < tempNode.getParent().getNumber()) {
                                tempNode.getParent().setLowNumber(Math.min(tempNode.getParent().getLowNumber(), tempNode.getLowNumber()));
                            }
                            else {
                                while (nodeStack.peek() != tempNode) {
                                    nodeStack.pop().setSubGraphNumber(subGraphNumber);
                                }
                                if (nodeStack.peek() == tempNode) {
                                    nodeStack.pop().setSubGraphNumber(subGraphNumber);
                                }
                                while (!edgeStack.peek().isBetween(tempNode, tempNode.getParent())) {
                                    final BiCompEdgeEx tempEdge = edgeStack.pop();
                                    tempEdge.setWasAdded(false);
                                    tempEdge.setSubGraphNumber(subGraphNumber);
                                }
                                if (edgeStack.peek().isBetween(tempNode, tempNode.getParent())) {
                                    final BiCompEdgeEx tempEdge = edgeStack.pop();
                                    tempEdge.setWasAdded(false);
                                    tempEdge.setSubGraphNumber(subGraphNumber);
                                }
                                ++subGraphNumber;
                                if (!((BiCompNodeEx)tempNode.getParent()).isOld()) {
                                    ((BiCompNodeEx)tempNode.getParent()).setSubGraphNumber(0);
                                    ((BiCompNodeEx)tempNode.getParent()).setIsOld(true);
                                    separatingNodes.addElement(tempNode.getParent());
                                }
                            }
                            tempNode = (BiCompNodeEx)tempNode.getParent();
                            alreadyVisited = true;
                            flag = true;
                        }
                        if (flag) {
                            continue;
                        }
                        while (nodeStack.peek() != tempNode) {
                            nodeStack.pop().setSubGraphNumber(subGraphNumber);
                        }
                        if (nodeStack.peek() == tempNode) {
                            nodeStack.pop().setSubGraphNumber(subGraphNumber);
                        }
                        connectedNodes.firstElement().setSubGraphNumber(subGraphNumber);
                        while (!edgeStack.peek().isBetween(tempNode, connectedNodes.firstElement())) {
                            final BiCompEdgeEx tempEdge = edgeStack.pop();
                            tempEdge.setWasAdded(false);
                            tempEdge.setSubGraphNumber(subGraphNumber);
                        }
                        if (edgeStack.peek().isBetween(tempNode, connectedNodes.firstElement())) {
                            final BiCompEdgeEx tempEdge = edgeStack.pop();
                            tempEdge.setWasAdded(false);
                            tempEdge.setSubGraphNumber(subGraphNumber);
                        }
                        ++subGraphNumber;
                        final Enumeration enum2 = connectedNodes.firstElement().incidentEdges().elements();
                        boolean flag2 = false;
                        while (enum2.hasMoreElements()) {
                            if (!enum2.nextElement().isUsed()) {
                                flag2 = true;
                            }
                        }
                        if (!flag2) {
                            break;
                        }
                        tempNode = connectedNodes.firstElement();
                        if (!tempNode.isOld()) {
                            tempNode.setSubGraphNumber(0);
                            tempNode.setIsOld(true);
                            separatingNodes.addElement(tempNode);
                        }
                        alreadyVisited = true;
                    }
                }
                enum1 = separatingNodes.elements();
                while (enum1.hasMoreElements()) {
                    tempNode = enum1.nextElement();
                    tempNode.setParent(null);
                    tempNode.setIsOld(false);
                }
                enum1 = edges.elements();
                while (enum1.hasMoreElements()) {
                    final BiCompEdgeEx tempEdge = enum1.nextElement();
                    tempEdge.setIsUsed(false);
                    tempEdge.setWasAdded(false);
                }
            }
        }
        logEntry.setData(String.valueOf(separatingNodes.size()) + " nodes found");
        g.stopLogEntry(logEntry);
        return separatingNodes;
    }
    
    public static boolean makeBiconnected(final Graph g) throws Exception {
        return makeBiconnected(g, true);
    }
    
    public static boolean makeBiconnected(final Graph g, final boolean check) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Make Biconnected");
        if (check && !PlanarityOperation.isPlanar(g)) {
            logEntry.setData("Graph was not Planar");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not planar!");
        }
        if (!isBiconnected(g)) {
            int counter = 0;
            ConnectivityOperation.makeConnected(g);
            EmbedOperation.embed(g, false);
            final Vector separators = findSeparatingNodes(g);
            final Enumeration enumSeparators = separators.elements();
            while (enumSeparators.hasMoreElements()) {
                final BiCompNodeEx separatorNode = enumSeparators.nextElement();
                final Vector edges = separatorNode.incidentEdges();
                final Enumeration enumEdges = edges.elements();
                while (enumEdges.hasMoreElements()) {
                    final BiCompEdgeEx currentEdge = enumEdges.nextElement();
                    final BiCompEdgeEx nextEdge = (BiCompEdgeEx)currentEdge.getNextInOrderFrom(separatorNode);
                    if (!g.isTriangle(separatorNode.getRef(), currentEdge.getRef(), nextEdge.getRef()) && (currentEdge.getSubGraphNumber() != nextEdge.getSubGraphNumber() || currentEdge.getSubGraphNumber() == 0)) {
                        final BiCompNodeEx firstNode = (BiCompNodeEx)currentEdge.otherEndFrom(separatorNode);
                        final BiCompNodeEx secondNode = (BiCompNodeEx)nextEdge.otherEndFrom(separatorNode);
                        final BiCompEdgeEx newEdge = new BiCompEdgeEx(firstNode, secondNode);
                        newEdge.setIsGenerated(true);
                        g.addEdge(newEdge, currentEdge.getPreviousInOrderFrom(firstNode), nextEdge);
                        ++counter;
                        if (edges.size() == 2) {
                            break;
                        }
                        continue;
                    }
                }
            }
            logEntry.setData(String.valueOf(counter) + " edges added");
            g.stopLogEntry(logEntry);
            return true;
        }
        g.stopLogEntry(logEntry);
        return false;
    }
    
    public static boolean isBiconnected(final Graph g) {
        final LogEntry logEntry = g.startLogEntry("Test Biconnectivity");
        final boolean isBiconnected = getBiconnectedComponents(g, false).size() <= 1;
        g.stopLogEntry(logEntry);
        return isBiconnected;
    }
    
    public static void displayBiconnectedComponents(final Graph g) {
        final Vector graphs = getBiconnectedComponents(g, true);
        final Random rand = new Random();
        for (int i = 0; i < graphs.size(); ++i) {
            final Color aColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            final Vector nodes = graphs.elementAt(i).getNodes();
            final Vector edges = graphs.elementAt(i).getEdges();
            for (int j = 0; j < nodes.size(); ++j) {
                final Node aNode = (Node)nodes.elementAt(j).getMasterCopy();
                g.changeNodeColor(aNode, aColor, true);
                g.changeNodeDrawX(aNode, false, true);
                g.changeNodeLabel(aNode, "", true);
            }
            for (int j = 0; j < edges.size(); ++j) {
                final Edge anEdge = (Edge)edges.elementAt(j).getMasterCopy();
                g.changeEdgeColor(anEdge, aColor, true);
                g.changeEdgeDirection(anEdge, null, true);
            }
        }
        final Vector nodes = NodeExtender.toNode(findSeparatingNodes(g));
        for (int i = 0; i < nodes.size(); ++i) {
            final Node aNode = nodes.elementAt(i);
            g.changeNodeColor(aNode, Color.red, true);
            g.changeNodeDrawX(aNode, true, true);
        }
        g.markForRepaint();
    }
}
