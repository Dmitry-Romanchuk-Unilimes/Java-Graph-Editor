// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.modes;

import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.Node;
import userInterface.GraphController;
import userInterface.GraphEditor;
import userInterface.menuAndToolBar.CursorPositionThread;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public abstract class GraphEditorListener implements MouseListener, MouseMotionListener, KeyListener
{
    public static boolean SINGLE_CLICK_ADD_NODE;
    public static boolean ADD_NODE_ON_EDGE_DROP;
    protected Graph graph;
    protected GraphEditor editor;
    protected GraphController controller;
    private CursorPositionThread cursorThread;
    private Vector specialSelectedNodes;
    protected int numSpecialSelectionsAllowed;
    protected int numSpecialSelections;
    private boolean triangleSelection;
    
    static {
        GraphEditorListener.SINGLE_CLICK_ADD_NODE = true;
        GraphEditorListener.ADD_NODE_ON_EDGE_DROP = true;
    }
    
    public GraphEditorListener(final GraphEditorListener listener) {
        this.graph = listener.graph;
        this.editor = listener.editor;
        this.controller = listener.controller;
        this.cursorThread = listener.cursorThread;
        this.graph.markForRepaint();
    }
    
    public GraphEditorListener(final Graph graph, final GraphEditor editor, final GraphController controller) {
        this.graph = graph;
        this.editor = editor;
        this.controller = controller;
        this.cursorThread = null;
        this.graph.markForRepaint();
    }
    
    public void setGraph(final Graph g) {
        this.graph = g;
    }
    
    public Graph getGraph() {
        return this.graph;
    }
    
    public GraphController getGraphController() {
        return this.controller;
    }
    
    public boolean isEditListener() {
        return false;
    }
    
    public boolean isMoveListener() {
        return false;
    }
    
    public boolean isRotateListener() {
        return false;
    }
    
    public boolean isResizeListener() {
        return false;
    }
    
    public boolean isGridListener() {
        return false;
    }
    
    public String getModeString() {
        if (this.isEditListener()) {
            return "Edit";
        }
        if (this.isMoveListener()) {
            return "Move";
        }
        if (this.isRotateListener()) {
            return "Rotate";
        }
        if (this.isResizeListener()) {
            return "Resize";
        }
        if (this.isGridListener()) {
            return "Grid";
        }
        return "";
    }
    
    public void prepareForClose() {
    }
    
    private boolean checkOnSameFace(final Node aNode) {
        if (this.specialSelectedNodes.isEmpty()) {
            return true;
        }
        if (this.specialSelectedNodes.size() == 1) {
            return aNode.neighbours().contains(this.specialSelectedNodes.elementAt(0));
        }
        if (this.specialSelectedNodes.size() == 2) {
            final Node firstNode = this.specialSelectedNodes.elementAt(0);
            final Edge tempEdge = (Edge)firstNode.incidentEdgeWith(this.specialSelectedNodes.elementAt(1));
            return tempEdge.getNextInOrderFrom(firstNode).otherEndFrom(firstNode) == aNode || tempEdge.getPreviousInOrderFrom(firstNode).otherEndFrom(firstNode) == aNode;
        }
        return false;
    }
    
    public void allowNodeSelection(final int numSelectionsAllowed) {
        this.numSpecialSelectionsAllowed = numSelectionsAllowed;
        this.numSpecialSelections = 0;
        if (this.specialSelectedNodes != null && this.specialSelectedNodes.size() > 0) {
            for (int i = 0; i < this.specialSelectedNodes.size(); ++i) {
                this.specialSelectedNodes.elementAt(i).setSpecialSelected(false);
            }
        }
        this.specialSelectedNodes = new Vector(numSelectionsAllowed);
        this.triangleSelection = false;
    }
    
    public void allowTriangleSelection() {
        this.numSpecialSelectionsAllowed = 3;
        this.numSpecialSelections = 0;
        if (this.specialSelectedNodes != null && this.specialSelectedNodes.size() > 0) {
            for (int i = 0; i < this.specialSelectedNodes.size(); ++i) {
                this.specialSelectedNodes.elementAt(i).setSpecialSelected(false);
            }
        }
        this.specialSelectedNodes = new Vector(this.numSpecialSelectionsAllowed);
        this.triangleSelection = true;
    }
    
    public Vector getSpecialNodeSelections() {
        return this.specialSelectedNodes;
    }
    
    public void mouseClicked(final MouseEvent event) {
        if (this.numSpecialSelectionsAllowed != 0 && event.getPoint().x >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER + Node.RADIUS && event.getPoint().y >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER + Node.RADIUS) {
            final Point ePoint = new Point(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
            final Node aNode = this.graph.nodeAt(ePoint);
            if (event.getClickCount() == 1 && aNode != null) {
                if (this.specialSelectedNodes.contains(aNode)) {
                    this.specialSelectedNodes.removeElement(aNode);
                    aNode.setSpecialSelected(false);
                    this.graph.markForRepaint();
                    --this.numSpecialSelections;
                    this.controller.nodesSelectedByEditor(this.numSpecialSelections, this.numSpecialSelectionsAllowed);
                }
                else if (this.numSpecialSelections < this.numSpecialSelectionsAllowed && (!this.triangleSelection || this.checkOnSameFace(aNode))) {
                    this.specialSelectedNodes.addElement(aNode);
                    aNode.setSpecialSelected(true);
                    this.graph.markForRepaint();
                    ++this.numSpecialSelections;
                    if (this.triangleSelection && this.numSpecialSelections == 3) {
                        Edge edgeOne = null;
                        Edge edgeTwo = null;
                        final Node nodeOne = this.specialSelectedNodes.elementAt(0);
                        final Node nodeTwo = this.specialSelectedNodes.elementAt(1);
                        final Node nodeThree = this.specialSelectedNodes.elementAt(2);
                        final Vector incidentEdges = nodeOne.incidentEdges();
                        for (int i = 0; i < incidentEdges.size(); ++i) {
                            final Edge tempEdge = incidentEdges.elementAt(i);
                            if (tempEdge.otherEndFrom(nodeOne) == nodeTwo) {
                                edgeOne = tempEdge;
                            }
                            else if (tempEdge.otherEndFrom(nodeOne) == nodeThree) {
                                edgeTwo = tempEdge;
                            }
                        }
                        if (edgeOne.getNextInOrderFrom(nodeOne) != edgeTwo) {
                            this.specialSelectedNodes.setElementAt(nodeThree, 1);
                            this.specialSelectedNodes.setElementAt(nodeTwo, 2);
                        }
                    }
                    this.controller.nodesSelectedByEditor(this.numSpecialSelections, this.numSpecialSelectionsAllowed);
                }
                this.editor.repaint();
            }
        }
    }
    
    public void mouseMoved(final MouseEvent event) {
        final Point cursorPoint = event.getPoint();
        cursorPoint.translate(-1 * GraphEditor.DRAW_BUFFER, -1 * GraphEditor.DRAW_BUFFER);
        this.controller.updateCursorLocation(cursorPoint);
    }
    
    public void mouseEntered(final MouseEvent event) {
        this.editor.requestFocus();
        this.mouseMoved(event);
        (this.cursorThread = new CursorPositionThread(this.controller.getMenuAndToolBar())).mouseIn(true);
        this.cursorThread.start();
    }
    
    public void mouseExited(final MouseEvent event) {
        this.editor.repaint();
        this.cursorThread.mouseIn(false);
    }
    
    public void resetGraph() {
        this.graph.setDrawSelected(false);
        this.editor.changeToNormalCursor();
        this.numSpecialSelectionsAllowed = 0;
        this.specialSelectedNodes = null;
        this.numSpecialSelections = 0;
        this.triangleSelection = false;
    }
}
