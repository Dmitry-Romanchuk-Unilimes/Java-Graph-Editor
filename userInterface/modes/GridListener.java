// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.modes;

import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.Location;
import graphStructure.Node;
import userInterface.GraphController;
import userInterface.GraphEditor;

import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class GridListener extends EditListener
{
    private boolean undoOrthogonal;
    
    public GridListener(final GraphEditorListener listener) {
        super(listener);
        this.init();
    }
    
    public GridListener(final Graph graph, final GraphEditor editor, final GraphController controller) {
        super(graph, editor, controller);
        this.init();
    }
    
    private void init() {
        this.editor.changeToNormalCursor();
        this.undoOrthogonal = false;
    }
    
    public boolean isEditListener() {
        return false;
    }
    
    public boolean isGridListener() {
        return true;
    }
    
    public void mouseClicked(final MouseEvent event) {
        final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
        final Edge anEdge = this.graph.edgeAt(ePoint);
        final Node aNode = this.graph.nodeAt(ePoint);
        if (this.numSpecialSelectionsAllowed == 0 && event.getButton() != 3 && event.getPoint().x >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER + Node.RADIUS && event.getPoint().y >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER + Node.RADIUS && aNode == null && anEdge == null) {
            final Node gridNode = this.graph.nodeAt(this.graph.getClosestGridLocation(ePoint));
            if (gridNode == null) {
                this.graph.newMemento("Create New Node");
                this.graph.createNode(this.graph.getClosestGridLocation(ePoint));
                this.graph.doneMemento();
                this.controller.newUndo();
                this.editor.setPreferredSize();
                this.editor.update();
            }
            else {
                this.graph.toggleNodeSelection(gridNode);
                this.editor.repaint();
            }
        }
        else {
            super.mouseClicked(event);
        }
    }
    
    public void mousePressed(final MouseEvent event) {
        final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
        final Edge anEdge = this.graph.edgeAt(ePoint);
        final Node aNode = this.graph.nodeAt(ePoint);
        if (this.numSpecialSelectionsAllowed == 0 && event.getButton() != 3 && event.getPoint().x >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER + Node.RADIUS && event.getPoint().y >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER + Node.RADIUS && aNode == null && anEdge != null && anEdge.isSelected()) {
            this.graph.newMemento("Orthogonalize Selected Edge");
            this.undoOrthogonal = !anEdge.isOrthogonal();
            this.graph.orthogonalizeEdge(anEdge, true);
            this.editor.startTranslateEdge(anEdge);
            this.dragStartLocation = new Location(event.getPoint());
            this.dragEdge = anEdge;
        }
        else {
            super.mousePressed(event);
        }
    }
    
    public void mouseDragged(final MouseEvent event) {
        if (this.numSpecialSelectionsAllowed == 0 && (this.dragNode != null || this.dragEdge != null)) {
            super.mouseMoved(event);
            this.dragged = true;
            final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
            if (this.dragNode != null && this.dragNode.isSelected()) {
                if (ePoint.intX() < this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER && ePoint.intY() < this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER) {
                    this.graph.relocateNode(this.dragNode, this.graph.getClosestGridLocation(ePoint), false);
                    this.graph.refreshOrthogonalEdges(this.dragNode.incidentEdges());
                    this.editor.setPreferredSize();
                    this.editor.repaint();
                }
            }
            else if (this.dragNode != null && !this.dragNode.isSelected()) {
                this.dragStartLocation = new Location(event.getPoint());
                final Location gridLocation = this.graph.getClosestGridLocation(ePoint);
                this.editor.setLineToDraw(new Line2D.Double(this.dragNode.getLocation().intX() + GraphEditor.DRAW_BUFFER, this.dragNode.getLocation().intY() + GraphEditor.DRAW_BUFFER, gridLocation.intX() + GraphEditor.DRAW_BUFFER, gridLocation.intY() + GraphEditor.DRAW_BUFFER));
                this.dragStartLocation.translate(-1 * GraphEditor.DRAW_BUFFER, -1 * GraphEditor.DRAW_BUFFER);
                Node dragToNode = null;
                if (event.isShiftDown()) {
                    dragToNode = this.graph.nodeAt(this.dragStartLocation);
                }
                else {
                    dragToNode = this.nodeSplitTree.nodeAt(this.dragStartLocation);
                }
                if (dragToNode == null) {
                    if (event.isShiftDown()) {
                        dragToNode = this.graph.nodeAt(this.graph.getClosestGridLocation(this.dragStartLocation));
                    }
                    else {
                        dragToNode = this.nodeSplitTree.nodeAt(this.graph.getClosestGridLocation(this.dragStartLocation));
                    }
                }
                if (dragToNode != null) {
                    this.editor.setLineToDrawColor(Edge.DEFAULT_COLOR);
                    if (!this.graph.isOnGrid(dragToNode.getLocation())) {
                        this.editor.setLineToDraw(new Line2D.Double(this.dragNode.getLocation().intX() + GraphEditor.DRAW_BUFFER, this.dragNode.getLocation().intY() + GraphEditor.DRAW_BUFFER, ePoint.intX() + GraphEditor.DRAW_BUFFER, ePoint.intY() + GraphEditor.DRAW_BUFFER));
                    }
                    final Edge dragEdge = (Edge)this.dragNode.incidentEdgeWith(dragToNode);
                    if (dragEdge != null) {
                        this.editor.setCurveToDrawColor(Color.green);
                        if (dragEdge.isCurved()) {
                            this.editor.setCurveToDraw(dragEdge.getCurve(GraphEditor.DRAW_BUFFER, GraphEditor.DRAW_BUFFER));
                        }
                        if (dragEdge.isDirected()) {
                            if (this.dragNode == dragEdge.getDirectedSourceNode()) {
                                this.editor.setLineToDrawColor(new Color(155, 155, 255));
                                this.editor.setPolygonToDraw(null);
                            }
                            else {
                                this.editor.setPolygonToDrawColor(GraphEditor.backgroundColor);
                                this.editor.setPolygonToDraw(dragEdge.getDirectionArrow(dragToNode, GraphEditor.DRAW_BUFFER, GraphEditor.DRAW_BUFFER, 1, 1));
                                this.editor.setLineToDrawColor(Color.green);
                            }
                        }
                        else {
                            this.editor.setPolygonToDrawColor(Color.green);
                            this.editor.setPolygonToDraw(dragEdge.getDirectionArrow(this.dragNode, GraphEditor.DRAW_BUFFER, GraphEditor.DRAW_BUFFER, 1, 1));
                            this.editor.setLineToDrawColor(Color.green);
                        }
                    }
                    else {
                        this.editor.setCurveToDraw(null);
                    }
                }
                else {
                    this.editor.setLineToDrawColor(new Color(155, 155, 255));
                    this.editor.setPolygonToDraw(null);
                    this.editor.setCurveToDraw(null);
                }
                this.editor.repaint();
            }
            else if (ePoint.intX() < this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER && ePoint.intY() < this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER) {
                this.graph.relocateEdge(this.dragEdge, ePoint, false);
                this.graph.orthogonalizeEdge(this.dragEdge, false);
                this.editor.setPreferredSize();
                this.editor.update();
            }
        }
        else {
            super.mouseDragged(event);
        }
    }
    
    public void mouseReleased(final MouseEvent event) {
        if (this.numSpecialSelectionsAllowed == 0 && (this.dragNode != null || this.dragEdge != null)) {
            if (this.dragNode != null) {
                if (this.dragged) {
                    if (this.dragNode.isSelected()) {
                        if (this.dragged) {
                            this.graph.doneMemento();
                            this.controller.newUndo();
                            this.graph.markForRepaint();
                            this.editor.update();
                        }
                        else {
                            this.graph.abortMemento();
                        }
                        this.editor.stopTranslateNodes();
                    }
                    else {
                        final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
                        Node aNode = this.graph.nodeAt(ePoint);
                        if (aNode == null) {
                            aNode = this.graph.nodeAt(this.graph.getClosestGridLocation(ePoint));
                        }
                        if (aNode != this.dragNode) {
                            if (aNode != null) {
                                final Edge tempEdge = (Edge)this.dragNode.incidentEdgeWith(aNode);
                                if (tempEdge != null) {
                                    this.graph.newMemento("Direct Edge");
                                    if (tempEdge.getDirectedSourceNode() == aNode) {
                                        this.graph.changeEdgeDirection(tempEdge, null, true);
                                    }
                                    else {
                                        this.graph.changeEdgeDirection(tempEdge, this.dragNode, true);
                                    }
                                    this.graph.doneMemento();
                                    this.controller.newUndo();
                                    this.editor.update();
                                }
                                else {
                                    this.graph.newMemento("Create New Edge");
                                    this.graph.addEdge(this.dragNode, aNode);
                                    this.graph.doneMemento();
                                    this.controller.newUndo();
                                    this.editor.update();
                                }
                                this.editor.setPolygonToDraw(null);
                                this.editor.setCurveToDraw(null);
                            }
                            else if (this.createNodeOnEdgeDrop && ePoint.intX() >= 0 && ePoint.intX() <= this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER && ePoint.intY() >= 0 && ePoint.intY() <= this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER) {
                                this.graph.newMemento("Create New Edge and End Node");
                                aNode = this.graph.createNode(this.graph.getClosestGridLocation(ePoint));
                                this.graph.addEdge(this.dragNode, aNode);
                                this.graph.doneMemento();
                                this.controller.newUndo();
                                this.editor.setPreferredSize();
                                this.editor.update();
                            }
                        }
                    }
                }
                this.nodeSplitTree = null;
                this.editor.setLineToDraw(null);
                this.dragNode = null;
                this.editor.repaint();
            }
            else if (this.dragEdge != null) {
                if (this.dragged) {
                    this.graph.doneMemento();
                    this.controller.newUndo();
                    this.graph.markForRepaint();
                    this.editor.update();
                }
                else if (this.undoOrthogonal) {
                    this.undoOrthogonal = false;
                    this.graph.undoMemento();
                    this.graph.abortMemento();
                }
                this.editor.stopTranslateEdge();
                this.dragEdge = null;
            }
            this.dragStartLocation = null;
            this.dragged = false;
        }
        else {
            super.mouseReleased(event);
        }
    }
    
    public void keyPressed(final KeyEvent event) {
        if (this.numSpecialSelectionsAllowed == 0 && (event.getKeyCode() == 127 || event.getKeyCode() == 110)) {
            this.controller.removeSelected();
            this.editor.update();
        }
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.gridButton) {
            if (this.aNode != null) {
                if (!this.graph.isOnGrid(this.aNode.getLocation())) {
                    this.graph.newMemento("Snap Node To Grid");
                    this.graph.relocateNode(this.aNode, this.graph.getClosestGridLocation(this.aNode.getLocation()), true);
                    this.graph.updateEdges(this.aNode.incidentEdges(), true);
                    this.graph.doneMemento();
                    this.controller.newUndo();
                    this.editor.repaint();
                    this.popupLocation = new Point(this.aNode.getLocation().intX() + GraphEditor.DRAW_BUFFER, this.aNode.getLocation().intY() + GraphEditor.DRAW_BUFFER);
                    this.showPopup(this.aNode, this.popupLocation);
                }
            }
            else if (this.anEdge != null) {
                this.graph.newMemento("Make Edge Orthogonal");
                this.graph.orthogonalizeEdge(this.anEdge, true);
                this.graph.doneMemento();
                this.controller.newUndo();
                this.editor.repaint();
                this.popupLocation = new Point(this.anEdge.getCenterLocation().intX() + GraphEditor.DRAW_BUFFER, this.anEdge.getCenterLocation().intY() + GraphEditor.DRAW_BUFFER);
                this.showPopup(this.anEdge, this.popupLocation);
            }
        }
        else {
            super.actionPerformed(e);
        }
    }
    
    public void focusGained(final FocusEvent e) {
    }
    
    public void focusLost(final FocusEvent e) {
    }
    
    public void ancestorMoved(final AncestorEvent event) {
    }
    
    public void ancestorAdded(final AncestorEvent event) {
    }
    
    public void ancestorRemoved(final AncestorEvent event) {
    }
    
    public void keyTyped(final KeyEvent event) {
    }
    
    public void keyReleased(final KeyEvent event) {
    }
}
