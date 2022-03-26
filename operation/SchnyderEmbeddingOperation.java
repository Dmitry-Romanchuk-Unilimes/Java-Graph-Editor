// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphException.GraphException;
import graphStructure.*;
import operation.extenders.NormalEdgeEx;
import operation.extenders.NormalNodeEx;
import operation.extenders.SchnyderEdgeEx;
import operation.extenders.SchnyderNodeEx;

import java.awt.*;
import java.util.Vector;

public class SchnyderEmbeddingOperation
{
    public static void straightLineGridEmbed(final Graph g, final int width, final int height) throws Exception {
        straightLineGridEmbed(g, true, width, height);
    }
    
    public static void straightLineGridEmbed(final Graph g, final boolean check, final int width, final int height) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Schnyder Straight Line Grid Embedding");
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
        straightLineGridEmbed(g, NormalLabelOperation.normalLabel(g, false), width, height, logEntry);
    }
    
    public static void straightLineGridEmbed(final Graph g, final Node fNode, final Node sNode, final Node tNode, final int width, final int height) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Schnyder Straight Line Grid Embedding");
        straightLineGridEmbed(g, NormalLabelOperation.normalLabel(g, fNode, sNode, tNode), width, height, logEntry);
    }
    
    private static void straightLineGridEmbed(final Graph g, final Vector rootNodes, final int width, final int height, final LogEntry logEntry) throws Exception {
        final Vector oldNodes = g.getNodeExtenders();
        final Vector oldEdges = g.getEdgeExtenders();
        g.createNodeExtenders(new SchnyderNodeEx().getClass());
        g.createEdgeExtenders(new SchnyderEdgeEx().getClass());
        final Vector nodes = g.getNodeExtenders();
        final Vector edges = g.getEdgeExtenders();
        for (int i = 0; i < oldNodes.size(); ++i) {
            final NormalNodeEx oldNode = oldNodes.elementAt(i);
            final SchnyderNodeEx newNode = nodes.elementAt(i);
            newNode.setR1Parent((SchnyderNodeEx)oldNode.getR1Parent().getRef().getExtender());
            newNode.setR2Parent((SchnyderNodeEx)oldNode.getR2Parent().getRef().getExtender());
            newNode.setR3Parent((SchnyderNodeEx)oldNode.getR3Parent().getRef().getExtender());
            newNode.setCanonicalNumber(oldNode.getCanonicalNumber());
        }
        for (int j = 0; j < oldEdges.size(); ++j) {
            final NormalEdgeEx oldEdge = oldEdges.elementAt(j);
            final SchnyderEdgeEx newEdge = edges.elementAt(j);
            newEdge.setNormalLabel(oldEdge.getNormalLabel());
        }
        final SchnyderNodeEx firstNode = (SchnyderNodeEx)rootNodes.elementAt(2).getRef().getExtender();
        final SchnyderNodeEx secondNode = (SchnyderNodeEx)rootNodes.elementAt(1).getRef().getExtender();
        final SchnyderNodeEx thirdNode = (SchnyderNodeEx)rootNodes.elementAt(0).getRef().getExtender();
        traverseTree(1, firstNode);
        traverseTree(2, secondNode);
        traverseTree(3, thirdNode);
        firstNode.setTX(2, 1);
        firstNode.setTX(3, 1);
        secondNode.setTX(1, 1);
        secondNode.setTX(3, 1);
        thirdNode.setTX(1, 1);
        thirdNode.setTX(2, 1);
        traverseTree2(1, firstNode);
        traverseTree2(2, secondNode);
        traverseTree2(3, thirdNode);
        traverseTree3(1, firstNode);
        traverseTree3(2, secondNode);
        traverseTree3(3, thirdNode);
        firstNode.setRX(1, g.getNumNodes() - 2);
        firstNode.setRX(2, 1);
        firstNode.setRX(3, 0);
        firstNode.setPX(1, 0);
        secondNode.setRX(1, 0);
        secondNode.setRX(2, g.getNumNodes() - 2);
        secondNode.setRX(3, 1);
        secondNode.setPX(2, 0);
        thirdNode.setRX(1, 1);
        thirdNode.setRX(2, 0);
        thirdNode.setRX(3, g.getNumNodes() - 2);
        thirdNode.setPX(3, 0);
        g.setGridArea(g.getNumNodes() - 1, height, g.getNumNodes() - 1, width, true);
        final int widthIncrement = g.getGridColWidth();
        final int heightIncrement = g.getGridRowHeight();
        for (int k = 0; k < nodes.size(); ++k) {
            final SchnyderNodeEx newNode = nodes.elementAt(k);
            g.relocateNode(newNode.getRef(), new Location((newNode.getRX(1) - newNode.getPX(3)) * widthIncrement, (newNode.getRX(2) - newNode.getPX(1)) * heightIncrement), true);
        }
        for (int k = 0; k < edges.size(); ++k) {
            final SchnyderEdgeEx newEdge = edges.elementAt(k);
            g.straightenEdge(newEdge.getRef(), true);
        }
        g.stopLogEntry(logEntry);
    }
    
    private static void traverseTree(final int treeNumber, final SchnyderNodeEx currentNode) throws Exception {
        final Vector incidentEdges = currentNode.incidentEdges();
        if (currentNode.getRXParent(treeNumber) != currentNode) {
            currentNode.setPX(treeNumber, currentNode.getRXParent(treeNumber).getPX(treeNumber) + 1);
        }
        else {
            currentNode.setPX(treeNumber, 1);
        }
        int count = 0;
        for (int i = 0; i < incidentEdges.size(); ++i) {
            final SchnyderEdgeEx currentEdge = incidentEdges.elementAt(i);
            if (currentEdge.getNormalLabel() == treeNumber && currentEdge.getNormalLabelSourceNode() == currentEdge.otherEndFrom(currentNode)) {
                traverseTree(treeNumber, (SchnyderNodeEx)currentEdge.otherEndFrom(currentNode));
                count += ((SchnyderNodeEx)currentEdge.otherEndFrom(currentNode)).getTX(treeNumber);
            }
        }
        currentNode.setTX(treeNumber, 1 + count);
    }
    
    private static void traverseTree2(final int treeNumber, final SchnyderNodeEx currentNode) throws Exception {
        final Vector incidentEdges = currentNode.incidentEdges();
        if (treeNumber == 1) {
            currentNode.setTemp(1, currentNode.getTX(2) + currentNode.getRXParent(treeNumber).getTemp(1));
            currentNode.setTemp(2, currentNode.getTX(3) + currentNode.getRXParent(treeNumber).getTemp(2));
        }
        else if (treeNumber == 2) {
            currentNode.setTemp(3, currentNode.getTX(1) + currentNode.getRXParent(treeNumber).getTemp(3));
            currentNode.setTemp(4, currentNode.getTX(3) + currentNode.getRXParent(treeNumber).getTemp(4));
        }
        else if (treeNumber == 3) {
            currentNode.setTemp(5, currentNode.getTX(1) + currentNode.getRXParent(treeNumber).getTemp(5));
            currentNode.setTemp(6, currentNode.getTX(2) + currentNode.getRXParent(treeNumber).getTemp(6));
        }
        for (int i = 0; i < incidentEdges.size(); ++i) {
            final SchnyderEdgeEx currentEdge = incidentEdges.elementAt(i);
            if (currentEdge.getNormalLabel() == treeNumber && currentEdge.getNormalLabelSourceNode() == currentEdge.otherEndFrom(currentNode)) {
                traverseTree2(treeNumber, (SchnyderNodeEx)currentEdge.otherEndFrom(currentNode));
            }
        }
    }
    
    private static void traverseTree3(final int treeNumber, final SchnyderNodeEx currentNode) throws Exception {
        final Vector incidentEdges = currentNode.incidentEdges();
        if (treeNumber == 1) {
            currentNode.setRX(treeNumber, currentNode.getTemp(3) + currentNode.getTemp(5) - currentNode.getTX(treeNumber));
        }
        else if (treeNumber == 2) {
            currentNode.setRX(treeNumber, currentNode.getTemp(1) + currentNode.getTemp(6) - currentNode.getTX(treeNumber));
        }
        else if (treeNumber == 3) {
            currentNode.setRX(treeNumber, currentNode.getTemp(2) + currentNode.getTemp(4) - currentNode.getTX(treeNumber));
        }
        for (int i = 0; i < incidentEdges.size(); ++i) {
            final SchnyderEdgeEx currentEdge = incidentEdges.elementAt(i);
            if (currentEdge.getNormalLabel() == treeNumber && currentEdge.getNormalLabelSourceNode() == currentEdge.otherEndFrom(currentNode)) {
                traverseTree3(treeNumber, (SchnyderNodeEx)currentEdge.otherEndFrom(currentNode));
            }
        }
    }
    
    public static void displayStraightLineGridEmbedding(final Graph g, final Node fNode, final Node sNode, final Node tNode, final int width, final int height) throws Exception {
        straightLineGridEmbed(g, fNode, sNode, tNode, width, height);
        g.markForRepaint();
    }
    
    public static void displayStraightLineGridEmbedding(final Graph g, final int width, final int height) throws Exception {
        straightLineGridEmbed(g, width, height);
        g.markForRepaint();
    }
    
    public static void displayNormalLabeling(final Graph g, final Node fNode, final Node sNode, final Node tNode, final int width, final int height) throws Exception {
        displayStraightLineGridEmbedding(g, fNode, sNode, tNode, width, height);
        displayNormalLabeling(g);
    }
    
    public static void displayNormalLabeling(final Graph g, final int width, final int height) throws Exception {
        displayStraightLineGridEmbedding(g, width, height);
        displayNormalLabeling(g);
    }
    
    private static void displayNormalLabeling(final Graph g) throws Exception {
        final Vector nodes = g.getNodeExtenders();
        final Vector edges = g.getEdgeExtenders();
        for (int j = 0; j < nodes.size(); ++j) {
            final SchnyderNodeEx currentNode = nodes.elementAt(j);
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
            final SchnyderEdgeEx currentEdge = edges.elementAt(i);
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
                g.changeEdgeDirection(currentEdge, null, true);
            }
        }
        g.markForRepaint();
    }
    
    public static void displayCanonicalOrdering(final Graph g, final Node fNode, final Node sNode, final Node tNode, final int width, final int height) throws Exception {
        displayStraightLineGridEmbedding(g, fNode, sNode, tNode, width, height);
        displayCanonicalOrdering(g);
    }
    
    public static void displayCanonicalOrdering(final Graph g, final int width, final int height) throws Exception {
        displayStraightLineGridEmbedding(g, width, height);
        displayCanonicalOrdering(g);
    }
    
    private static void displayCanonicalOrdering(final Graph g) throws Exception {
        final Vector nodes = g.getNodeExtenders();
        for (int i = 0; i < nodes.size(); ++i) {
            final SchnyderNodeEx currentNode = nodes.elementAt(i);
            g.changeNodeDrawX(currentNode, false, true);
            g.changeNodeLabel(currentNode, String.valueOf(currentNode.getCanonicalNumber()), true);
            if (currentNode.getCanonicalNumber() == 1 || currentNode.getCanonicalNumber() == 2 || currentNode.getCanonicalNumber() == nodes.size()) {
                g.changeNodeColor(currentNode, Color.green, true);
            }
            else {
                g.changeNodeColor(currentNode, Node.DEFAULT_COLOR, true);
            }
        }
        final Vector edges = g.getEdgeExtenders();
        for (int j = 0; j < edges.size(); ++j) {
            final SchnyderEdgeEx currentEdge = edges.elementAt(j);
            g.changeEdgeColor(currentEdge, Edge.DEFAULT_COLOR, true);
            g.changeEdgeDirection(currentEdge, null, true);
        }
        g.markForRepaint();
    }
}
