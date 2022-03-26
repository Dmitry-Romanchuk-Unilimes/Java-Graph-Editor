// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphException.GraphException;
import graphStructure.EdgeInterface;
import graphStructure.Graph;
import graphStructure.LogEntry;
import operation.extenders.MakeMaxEdgeEx;
import operation.extenders.MakeMaxNodeEx;

import java.util.Enumeration;
import java.util.Vector;

public class MakeMaximalOperation
{
    public static boolean makeMaximal(final Graph g) throws Exception {
        return makeMaximal(g, true);
    }
    
    public static boolean makeMaximal(final Graph g, final boolean check) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Make Maximal Planar");
        if (check && g.getNumNodes() < 3) {
            logEntry.setData("Graph had less than 3 Nodes");
            g.stopLogEntry(logEntry);
            throw new GraphException("3 or more nodes required!");
        }
        if (check && !PlanarityOperation.isPlanar(g)) {
            logEntry.setData("Graph was not Planar");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not planar!");
        }
        int counter = 0;
        if (g.getNumEdges() < g.getNumNodes() * 3 - 6) {
            if (!BiconnectivityOperation.makeBiconnected(g, false)) {
                EmbedOperation.embed(g, false);
            }
            final Vector nodes = g.createNodeExtenders(new MakeMaxNodeEx().getClass());
            g.createEdgeExtenders(new MakeMaxEdgeEx().getClass());
            final Enumeration enumNodes = nodes.elements();
            while (enumNodes.hasMoreElements()) {
                final MakeMaxNodeEx currentNode = enumNodes.nextElement();
                final Enumeration enumEdges = currentNode.incidentEdges().elements();
                while (enumEdges.hasMoreElements()) {
                    final MakeMaxEdgeEx currentEdge = enumEdges.nextElement();
                    final MakeMaxEdgeEx secondEdge = (MakeMaxEdgeEx)currentEdge.getNextInOrderFrom(currentNode);
                    final MakeMaxNodeEx firstNode = (MakeMaxNodeEx)currentEdge.otherEndFrom(currentNode);
                    final MakeMaxNodeEx secondNode = (MakeMaxNodeEx)secondEdge.otherEndFrom(currentNode);
                    if (currentEdge.getPreviousInOrderFrom(firstNode).otherEndFrom(firstNode) != secondNode) {
                        final MakeMaxEdgeEx newEdge = new MakeMaxEdgeEx(firstNode, secondNode);
                        newEdge.setIsGenerated(true);
                        newEdge.setIsOld(true);
                        g.addEdge(newEdge, currentEdge.getPreviousInOrderFrom(firstNode), secondEdge);
                        ++counter;
                    }
                }
            }
            final EdgeInterface[] sortedEdges = g.sortEdges(g.getEdgeExtenders());
            final Vector duplicateEdgeRunIndices = new Vector();
            boolean run = false;
            for (int i = 0; i < sortedEdges.length - 1; ++i) {
                if (sortedEdges[i].equals(sortedEdges[i + 1])) {
                    if (!run) {
                        duplicateEdgeRunIndices.addElement(new Integer(i));
                    }
                    run = true;
                }
                else {
                    run = false;
                }
            }
            for (int j = 0; j < duplicateEdgeRunIndices.size(); ++j) {
                int index = duplicateEdgeRunIndices.elementAt(j);
                boolean original = false;
                do {
                    final MakeMaxEdgeEx currentEdge = (MakeMaxEdgeEx)sortedEdges[index];
                    if (!currentEdge.isOld()) {
                        original = true;
                    }
                    ++index;
                } while (sortedEdges[index].equals(sortedEdges[index - 1]));
                if (!original) {
                    ((MakeMaxEdgeEx)sortedEdges[index - 1]).setIsOld(false);
                }
                index = duplicateEdgeRunIndices.elementAt(j);
                do {
                    final MakeMaxEdgeEx currentEdge = (MakeMaxEdgeEx)sortedEdges[index];
                    if (currentEdge.isOld() && g.isInQuadrilateral(currentEdge.getRef())) {
                        g.flip(currentEdge.getRef());
                    }
                    ++index;
                } while (sortedEdges[index].equals(sortedEdges[index - 1]));
            }
            logEntry.setData(String.valueOf(counter) + " edges added");
            g.stopLogEntry(logEntry);
            return true;
        }
        logEntry.setData(String.valueOf(counter) + " edges added");
        g.stopLogEntry(logEntry);
        return false;
    }
}
