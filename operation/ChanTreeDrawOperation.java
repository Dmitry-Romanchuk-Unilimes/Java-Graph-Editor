// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphException.GraphException;
import graphStructure.Graph;
import graphStructure.Location;
import graphStructure.LogEntry;
import graphStructure.Node;
import operation.extenders.ChanEdgeEx;
import operation.extenders.ChanNodeEx;

import java.util.Vector;

public class ChanTreeDrawOperation
{
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    
    public static void displayChanTreeDrawing(final Graph g, final Node root, final int method, final int width, final int height) throws Exception {
        final LogEntry logEntry = g.startLogEntry("Chan Tree Drawing");
        if (!ConnectivityOperation.isConnected(g)) {
            logEntry.setData("Graph was not connected");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not connected!");
        }
        if (TreeOperation.hasCycles(g)) {
            logEntry.setData("Graph had cycles");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph has Cycles!");
        }
        if (method != 4 && !TreeOperation.isBinaryTree(g, root)) {
            logEntry.setData("Graph was not a Binary Tree");
            g.stopLogEntry(logEntry);
            throw new GraphException("Graph is not a Binary Tree!");
        }
        Class class$0;
        if ((class$0 = ChanTreeDrawOperation.class$0) == null) {
            try {
                class$0 = (ChanTreeDrawOperation.class$0 = Class.forName("operation.extenders.ChanNodeEx"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        final Vector nodes = g.createNodeExtenders(class$0);
        Class class$2;
        if ((class$2 = ChanTreeDrawOperation.class$1) == null) {
            try {
                class$2 = (ChanTreeDrawOperation.class$1 = Class.forName("operation.extenders.ChanEdgeEx"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        final Vector edges = g.createEdgeExtenders(class$2);
        final ChanNodeEx rootEx = (ChanNodeEx)root.getExtender();
        buildTree(rootEx);
        if (method == 1) {
            firstMethod(g, rootEx);
        }
        else if (method == 2) {
            secondMethod(g, rootEx);
        }
        else {
            if (method != 3) {
                return;
            }
            thirdMethod(g, rootEx);
        }
        correctGridCoordinates(rootEx, rootEx.getBoundX(), 0);
        final int gridWidth = rootEx.getBoundWidth();
        final int gridHeight = rootEx.getBoundHeight();
        g.setGridArea(rootEx.getBoundHeight() + 1, height, rootEx.getBoundWidth() + 1, width, true);
        final int widthIncrement = g.getGridColWidth();
        final int heightIncrement = g.getGridRowHeight();
        for (int i = 0; i < nodes.size(); ++i) {
            final ChanNodeEx aNode = nodes.elementAt(i);
            g.relocateNode(aNode.getRef(), new Location(aNode.getGridX() * widthIncrement, aNode.getGridY() * heightIncrement), true);
        }
        for (int i = 0; i < edges.size(); ++i) {
            final ChanEdgeEx anEdge = edges.elementAt(i);
            g.straightenEdge(anEdge.getRef(), true);
        }
        g.stopLogEntry(logEntry);
    }
    
    private static int buildTree(final ChanNodeEx root) {
        final Vector children = root.getChildren();
        int size = 1;
        for (int i = 0; i < children.size(); ++i) {
            final ChanNodeEx child = children.elementAt(i);
            if (child != root.getParent()) {
                child.setParent(root);
                size += buildTree(child);
            }
        }
        root.setSubTreeSize(size);
        return size;
    }
    
    private static void firstMethod(final Graph g, final ChanNodeEx root) {
        if (root.getSubTreeSize() == 1) {
            root.setGridX(0);
            root.setGridY(0);
            root.setBoundX(0);
            root.setBoundY(0);
            root.setBoundWidth(0);
            root.setBoundHeight(0);
        }
        else {
            firstMethod(g, root.getLeftChild());
            if (root.getRightChild() != null) {
                firstMethod(g, root.getRightChild());
                if (root.getLeftChild().getSubTreeSize() < root.getRightChild().getSubTreeSize()) {
                    leftRule(root, root.getLeftChild(), root.getRightChild());
                }
                else {
                    rightRule(root, root.getLeftChild(), root.getRightChild());
                }
            }
            else {
                otherRule(root, root.getLeftChild());
            }
        }
    }
    
    private static void secondMethod(final Graph g, final ChanNodeEx root) {
        secondMethod(g, root, 0, 0);
    }
    
    private static void secondMethod(final Graph g, final ChanNodeEx root, int biggestLeftSubTreeSize, int biggestRightSubTreeSize) {
        if (root.getSubTreeSize() == 1) {
            root.setGridX(0);
            root.setGridY(0);
            root.setBoundX(0);
            root.setBoundY(0);
            root.setBoundWidth(0);
            root.setBoundHeight(0);
        }
        else {
            if (root.getLeftChild().getSubTreeSize() > biggestLeftSubTreeSize) {
                biggestLeftSubTreeSize = root.getLeftChild().getSubTreeSize();
            }
            if (root.getRightChild() != null && root.getRightChild().getSubTreeSize() > biggestRightSubTreeSize) {
                biggestRightSubTreeSize = root.getRightChild().getSubTreeSize();
            }
            secondMethod(g, root.getLeftChild(), biggestLeftSubTreeSize, biggestRightSubTreeSize);
            if (root.getRightChild() != null) {
                secondMethod(g, root.getRightChild(), biggestLeftSubTreeSize, biggestRightSubTreeSize);
                if (root.getLeftChild().getSubTreeSize() + biggestRightSubTreeSize < root.getRightChild().getSubTreeSize() + biggestLeftSubTreeSize) {
                    leftRule(root, root.getLeftChild(), root.getRightChild());
                }
                else {
                    rightRule(root, root.getLeftChild(), root.getRightChild());
                }
            }
            else {
                otherRule(root, root.getLeftChild());
            }
        }
    }
    
    private static double log2(final double x) {
        return Math.log(x) / Math.log(2.0);
    }
    
    private static ChanNodeEx findLastNodeWithSizeGreaterThan(final ChanNodeEx root, final double num) {
        if (root.getSubTreeSize() < num) {
            if (root.getParent() != null) {
                return root.getParent();
            }
            return root;
        }
        else {
            if (root.getRightChild() == null || root.getLeftChild().getSubTreeSize() >= root.getRightChild().getSubTreeSize()) {
                return findLastNodeWithSizeGreaterThan(root.getLeftChild(), num);
            }
            return findLastNodeWithSizeGreaterThan(root.getRightChild(), num);
        }
    }
    
    private static void thirdMethod(final Graph g, final ChanNodeEx root) {
        final double a = g.getNumNodes() / Math.pow(2.0, Math.sqrt(2.0 * log2(g.getNumNodes())));
        ChanNodeEx kNode = findLastNodeWithSizeGreaterThan(root, g.getNumNodes() - a);
        boolean left = false;
        if (kNode == kNode.getParent().getLeftChild()) {
            left = true;
        }
        kNode = kNode.getParent();
        thirdMethod1(g, root, kNode);
        int shift;
        if (left) {
            shift = root.getBoundX();
        }
        else {
            shift = root.getBoundWidth() - root.getBoundX();
        }
        thirdMethod2(g, root, kNode, false, left, shift);
    }
    
    private static void thirdMethod1(final Graph g, final ChanNodeEx root, final ChanNodeEx kNode) {
        if (root.getSubTreeSize() == 1) {
            root.setGridX(0);
            root.setGridY(0);
            root.setBoundX(0);
            root.setBoundY(0);
            root.setBoundWidth(0);
            root.setBoundHeight(0);
        }
        else if (root != kNode) {
            thirdMethod1(g, root.getLeftChild(), kNode);
            if (root.getRightChild() != null) {
                thirdMethod1(g, root.getRightChild(), kNode);
                if (root.getLeftChild().getSubTreeSize() < root.getRightChild().getSubTreeSize()) {
                    leftRule(root, root.getLeftChild(), root.getRightChild());
                }
                else {
                    rightRule(root, root.getLeftChild(), root.getRightChild());
                }
            }
            else {
                otherRule(root, root.getLeftChild());
            }
        }
    }
    
    private static void thirdMethod2(final Graph g, final ChanNodeEx root, final ChanNodeEx kNode, final boolean passedKNode, final boolean left, final int shift) {
        if (root.getSubTreeSize() == 1) {
            root.setGridX(0);
            root.setGridY(0);
            root.setBoundX(0);
            root.setBoundY(0);
            root.setBoundWidth(0);
            root.setBoundHeight(0);
        }
        else if (root == kNode) {
            thirdMethod2(g, root.getLeftChild(), kNode, true, left, shift);
            if (root.getRightChild() != null) {
                thirdMethod2(g, root.getRightChild(), kNode, true, left, shift);
                if (left) {
                    extendedRightRule(root, root.getLeftChild(), root.getRightChild(), shift);
                }
                else {
                    extendedLeftRule(root, root.getLeftChild(), root.getRightChild(), shift);
                }
            }
            else {
                extendedOtherRule(root, root.getLeftChild(), shift);
            }
        }
        else if (passedKNode) {
            thirdMethod2(g, root.getLeftChild(), kNode, passedKNode, left, shift);
            if (root.getRightChild() != null) {
                thirdMethod2(g, root.getRightChild(), kNode, passedKNode, left, shift);
                if (left) {
                    rightRule(root, root.getLeftChild(), root.getRightChild());
                }
                else {
                    leftRule(root, root.getLeftChild(), root.getRightChild());
                }
            }
            else {
                otherRule(root, root.getLeftChild());
            }
        }
        else {
            thirdMethod2(g, root.getLeftChild(), kNode, passedKNode, left, shift);
            if (root.getRightChild() != null) {
                thirdMethod2(g, root.getRightChild(), kNode, passedKNode, left, shift);
                if (root.getLeftChild().getSubTreeSize() < root.getRightChild().getSubTreeSize()) {
                    leftRule(root, root.getLeftChild(), root.getRightChild());
                }
                else {
                    rightRule(root, root.getLeftChild(), root.getRightChild());
                }
            }
            else {
                otherRule(root, root.getLeftChild());
            }
        }
    }
    
    private static void leftRule(final ChanNodeEx root, final ChanNodeEx left, final ChanNodeEx right) {
        left.shiftX(-1 * (left.getBoundWidth() - left.getBoundX() + 1));
        left.shiftY(1);
        right.shiftY(1 + left.getBoundHeight());
        root.setGridX(0);
        root.setGridY(0);
        root.setBoundWidth(Math.max(right.getBoundX(), 1 + left.getBoundWidth()) + right.getBoundWidth() - right.getBoundX());
        root.setBoundHeight(left.getBoundHeight() + right.getBoundHeight() + 1);
        root.setBoundX(Math.max(right.getBoundX(), 1 + left.getBoundWidth()));
        root.setBoundY(0);
    }
    
    private static void rightRule(final ChanNodeEx root, final ChanNodeEx left, final ChanNodeEx right) {
        right.shiftX(1 + right.getBoundX());
        right.shiftY(1);
        left.shiftY(1 + right.getBoundHeight());
        root.setGridX(0);
        root.setGridY(0);
        root.setBoundWidth(left.getBoundX() + Math.max(1 + right.getBoundWidth(), left.getBoundWidth() - left.getBoundX()));
        root.setBoundHeight(left.getBoundHeight() + right.getBoundHeight() + 1);
        root.setBoundX(left.getBoundX());
        root.setBoundY(0);
    }
    
    private static void otherRule(final ChanNodeEx root, final ChanNodeEx child) {
        child.shiftY(1);
        root.setGridX(0);
        root.setGridY(0);
        root.setBoundWidth(child.getBoundWidth());
        root.setBoundHeight(child.getBoundHeight() + 1);
        root.setBoundX(child.getBoundX());
        root.setBoundY(0);
    }
    
    private static void extendedLeftRule(final ChanNodeEx root, final ChanNodeEx left, final ChanNodeEx right, final int shift) {
        left.shiftX(-1 * (left.getBoundWidth() - left.getBoundX() + 1));
        left.shiftY(1);
        right.shiftX(shift);
        right.shiftY(1 + left.getBoundHeight());
        root.setGridX(0);
        root.setGridY(0);
        root.setBoundWidth(shift + Math.max(right.getBoundWidth() - shift, 1 + left.getBoundWidth()));
        root.setBoundHeight(left.getBoundHeight() + right.getBoundHeight() + 1);
        root.setBoundX(Math.max(right.getBoundWidth() - shift, 1 + left.getBoundWidth()));
        root.setBoundY(0);
    }
    
    private static void extendedRightRule(final ChanNodeEx root, final ChanNodeEx left, final ChanNodeEx right, final int shift) {
        right.shiftX(1 + right.getBoundX());
        right.shiftY(1);
        left.shiftX(-1 * shift);
        left.shiftY(1 + right.getBoundHeight());
        root.setGridX(0);
        root.setGridY(0);
        root.setBoundWidth(shift + Math.max(1 + right.getBoundWidth(), left.getBoundWidth()) - shift);
        root.setBoundHeight(left.getBoundHeight() + right.getBoundHeight() + 1);
        root.setBoundX(shift);
        root.setBoundY(0);
    }
    
    private static void extendedOtherRule(final ChanNodeEx root, final ChanNodeEx child, final int shift) {
        child.shiftX(-1 * shift);
        child.shiftY(1);
        root.setGridX(0);
        root.setGridY(0);
        root.setBoundWidth(child.getBoundWidth());
        root.setBoundHeight(child.getBoundHeight() + 1);
        root.setBoundX(shift);
        root.setBoundY(0);
    }
    
    private static void correctGridCoordinates(final ChanNodeEx root, final int shiftX, final int shiftY) {
        root.shiftX(shiftX);
        root.shiftY(shiftY);
        if (root.getLeftChild() != null) {
            correctGridCoordinates(root.getLeftChild(), root.getGridX(), root.getGridY());
        }
        if (root.getRightChild() != null) {
            correctGridCoordinates(root.getRightChild(), root.getGridX(), root.getGridY());
        }
    }
}
