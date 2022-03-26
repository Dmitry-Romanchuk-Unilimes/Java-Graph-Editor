// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphException.GraphException;
import graphStructure.Graph;
import graphStructure.LogEntry;
import graphStructure.Node;
import graphStructure.NodeExtender;
import operation.extenders.STEdgeEx;
import operation.extenders.STNodeEx;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

public class STNumberOperation
{
    public static Vector stNumber(final Graph g) throws Exception {
        return stNumber(g, true);
    }
    
    public static Vector stNumber(final Graph g, final boolean check) throws Exception {
        final LogEntry logEntry = g.startLogEntry("ST Numbering");
        if (check && !ConnectivityOperation.isConnected(g)) {
            logEntry.setData("Graph was not Connected");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not connected!");
        }
        if (check && !BiconnectivityOperation.isBiconnected(g)) {
            logEntry.setData("Graph was not BiConnected");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not biconnected!");
        }
        final Vector nodes = g.createNodeExtenders(new STNodeEx().getClass());
        final Vector edges = g.createEdgeExtenders(new STEdgeEx().getClass());
        final Vector nodesInStOrder = new Vector();
        if (nodes.size() == 1) {
            nodes.firstElement().setStNumber(0);
            nodesInStOrder.addElement(nodes.firstElement());
            g.stopLogEntry(logEntry);
            return nodesInStOrder;
        }
        final Stack stack = new Stack();
        final Stack tempStack = new Stack();
        DepthFirstSearchOperation.depthFirstSearch(g, true);
        STNodeEx s = null;
        STNodeEx t = null;
        final Enumeration enumNodes = nodes.elements();
        while (enumNodes.hasMoreElements()) {
            final STNodeEx aNode = enumNodes.nextElement();
            if (aNode.getNumber() == 1) {
                t = aNode;
                aNode.setIsOld(true);
            }
            else if (aNode.getNumber() == 2) {
                s = aNode;
                aNode.setIsOld(true);
            }
            else {
                aNode.setIsOld(false);
            }
        }
        Enumeration enumEdges = edges.elements();
        while (enumEdges.hasMoreElements()) {
            final STEdgeEx anEdge = enumEdges.nextElement();
            if (anEdge.isBetween(s, t)) {
                anEdge.setIsOld(true);
            }
            else {
                anEdge.setIsOld(false);
            }
        }
        stack.push(t);
        stack.push(s);
        int i = 1;
        STNodeEx aNode;
        while (true) {
            aNode = stack.pop();
            if (aNode == t) {
                break;
            }
            final Vector path = pathFinder(aNode);
            if (path.isEmpty()) {
                aNode.setStNumber(i);
                nodesInStOrder.addElement(aNode);
                ++i;
            }
            else {
                enumEdges = path.elements();
                while (enumEdges.hasMoreElements()) {
                    tempStack.push(aNode);
                    final STEdgeEx anEdge = enumEdges.nextElement();
                    aNode = (STNodeEx)anEdge.otherEndFrom(aNode);
                }
                while (!tempStack.isEmpty()) {
                    stack.push(tempStack.pop());
                }
            }
        }
        aNode.setStNumber(i);
        nodesInStOrder.addElement(aNode);
        g.stopLogEntry(logEntry);
        return nodesInStOrder;
    }
    
    private static Vector pathFinder(final STNodeEx v) {
        final Vector path = new Vector();
        Enumeration enumEdges = v.incidentEdges().elements();
        while (enumEdges.hasMoreElements()) {
            final STEdgeEx anEdge = enumEdges.nextElement();
            if (!anEdge.isOld() && anEdge.isBackEdge() && ((STNodeEx)anEdge.otherEndFrom(v)).getNumber() < v.getNumber()) {
                anEdge.setIsOld(true);
                path.addElement(anEdge);
                return path;
            }
        }
        enumEdges = v.incidentEdges().elements();
        STEdgeEx tempEdge = null;
        while (enumEdges.hasMoreElements()) {
            final STEdgeEx anEdge = enumEdges.nextElement();
            if (!anEdge.isOld() && !anEdge.isBackEdge() && ((STNodeEx)anEdge.otherEndFrom(v)).getParent() == v && ((STNodeEx)anEdge.otherEndFrom(v)).getNumber() > v.getNumber()) {
                anEdge.setIsOld(true);
                path.addElement(anEdge);
                for (STNodeEx aNode = (STNodeEx)anEdge.otherEndFrom(v); !aNode.isOld(); aNode = (STNodeEx)tempEdge.otherEndFrom(aNode)) {
                    Enumeration enumEdges2 = aNode.incidentEdges().elements();
                    boolean found = false;
                    while (enumEdges2.hasMoreElements()) {
                        tempEdge = enumEdges2.nextElement();
                        if (!tempEdge.isOld() && ((STNodeEx)tempEdge.otherEndFrom(aNode)).getNumber() == aNode.getLowNumber()) {
                            aNode.setIsOld(true);
                            tempEdge.setIsOld(true);
                            path.addElement(tempEdge);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        enumEdges2 = aNode.incidentEdges().elements();
                        while (enumEdges2.hasMoreElements()) {
                            tempEdge = enumEdges2.nextElement();
                            if (!tempEdge.isOld() && ((STNodeEx)tempEdge.otherEndFrom(aNode)).getParent() == aNode && ((STNodeEx)tempEdge.otherEndFrom(aNode)).getLowNumber() == aNode.getLowNumber()) {
                                aNode.setIsOld(true);
                                tempEdge.setIsOld(true);
                                path.addElement(tempEdge);
                                break;
                            }
                        }
                    }
                }
                return path;
            }
        }
        enumEdges = v.incidentEdges().elements();
        while (enumEdges.hasMoreElements()) {
            final STEdgeEx anEdge = enumEdges.nextElement();
            if (!anEdge.isOld() && anEdge.isBackEdge() && ((STNodeEx)anEdge.otherEndFrom(v)).getNumber() > v.getNumber()) {
                anEdge.setIsOld(true);
                path.addElement(anEdge);
                for (STNodeEx aNode = (STNodeEx)anEdge.otherEndFrom(v); !aNode.isOld(); aNode = (STNodeEx)tempEdge.otherEndFrom(aNode)) {
                    final Enumeration enumEdges2 = aNode.incidentEdges().elements();
                    while (enumEdges2.hasMoreElements()) {
                        tempEdge = enumEdges2.nextElement();
                        if (!tempEdge.isOld() && tempEdge.otherEndFrom(aNode) == aNode.getParent()) {
                            aNode.setIsOld(true);
                            tempEdge.setIsOld(true);
                            path.addElement(tempEdge);
                            break;
                        }
                    }
                }
                return path;
            }
        }
        return path;
    }
    
    public static void displayStNumbering(final Graph g) throws Exception {
        final Vector graphs = BiconnectivityOperation.getBiconnectedComponents(g, true);
        BiconnectivityOperation.displayBiconnectedComponents(g);
        Vector nodes = g.getNodes();
        g.clearNodeLabels(true);
        g.removeEdgeDirections(true);
        for (int j = 0; j < graphs.size(); ++j) {
            nodes = stNumber(graphs.elementAt(j));
            for (int i = 0; i < nodes.size(); ++i) {
                final STNodeEx aNode = nodes.elementAt(i);
                final NodeExtender aNodeCopy = (NodeExtender)aNode.getMasterCopy();
                if (aNodeCopy.getLabel().length() > 0) {
                    aNodeCopy.appendLabel(",");
                }
                aNodeCopy.appendLabel(String.valueOf(aNode.getStNumber()));
            }
        }
        nodes = g.getNodes();
        for (int k = 0; k < nodes.size(); ++k) {
            final Node tempNode = nodes.elementAt(k);
            final String tempLabel = tempNode.getLabel();
            tempNode.setLabel("");
            g.changeNodeLabel(tempNode, tempLabel, true);
        }
        g.markForRepaint();
    }
}
