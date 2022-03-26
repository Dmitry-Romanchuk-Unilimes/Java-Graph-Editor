// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.modes;

import dataStructure.nodeSplitTree.NodeSplitTree;
import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.Location;
import graphStructure.Node;
import userInterface.GraphController;
import userInterface.GraphEditor;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class EditListener extends GraphEditorListener implements ActionListener, FocusListener, AncestorListener
{
    protected Timer counter;
    protected int animationDelay;
    protected Vector edgesToAnimateVector;
    protected Vector oldEdgeColorVector;
    protected Node animateSourceNode;
    protected int sourceEdgeIndex;
    protected int animateEdgeIndex;
    protected Color oldNodeColor;
    protected Location dragStartLocation;
    protected Node dragNode;
    protected Edge dragEdge;
    protected Vector selectedNodeVector;
    protected Vector selectedEdgeVector;
    protected NodeSplitTree nodeSplitTree;
    protected boolean createNodeOnEdgeDrop;
    protected int minX;
    protected int maxX;
    protected int minY;
    protected int maxY;
    protected boolean dragged;
    protected boolean undoCurve;
    protected JButton closeButton;
    protected JButton selectButton;
    protected JButton labelButton;
    protected JButton colorButton;
    protected JButton makeStraightButton;
    protected JButton undirectButton;
    protected JButton makePermanentButton;
    protected JButton gridButton;
    protected Node aNode;
    protected Edge anEdge;
    protected JPanel popupPanel;
    protected JLayeredPane layeredPane;
    protected JRootPane rootPane;
    protected JWindow popupWindow;
    protected JLayeredPane wLayeredPane;
    protected JRootPane wRootPane;
    protected boolean layerInited;
    protected Point popupLocation;
    protected boolean windowPopup;
    protected boolean applyToSelected;
    
    public EditListener(final GraphEditorListener listener) {
        super(listener);
        this.animationDelay = 700;
        this.init();
    }
    
    public EditListener(final Graph graph, final GraphEditor editor, final GraphController controller) {
        super(graph, editor, controller);
        this.animationDelay = 700;
        this.init();
    }
    
    private void init() {
        this.graph.setDrawSelected(true);
        this.editor.changeToNormalCursor();
        this.nodeSplitTree = null;
        this.dragged = false;
        this.undoCurve = false;
        this.aNode = null;
        this.anEdge = null;
        this.layerInited = false;
        this.windowPopup = false;
        this.applyToSelected = false;
    }
    
    public boolean isEditListener() {
        return true;
    }
    
    public void mouseClicked(final MouseEvent event) {
        if (this.numSpecialSelectionsAllowed != 0) {
            super.mouseClicked(event);
        }
        else if (event.getButton() != 3 && event.getPoint().x >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER + Node.RADIUS && event.getPoint().y >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER + Node.RADIUS) {
            final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
            if (event.isControlDown()) {
                final Node aNode = this.graph.nodeAt(ePoint);
                if (aNode != null) {
                    if (event.isShiftDown()) {
                        this.drawIncidentEdgesInOrder(aNode, true);
                    }
                    else {
                        this.drawIncidentEdgesInOrder(aNode, false);
                    }
                }
                final Edge anEdge = this.graph.edgeAt(ePoint);
                if (anEdge != null) {
                    this.drawIncidentEdgesInOrder(anEdge);
                }
            }
            else {
                final Node aNode = this.graph.nodeAt(ePoint);
                final Edge anEdge = this.graph.edgeAt(ePoint);
                if (aNode != null || anEdge != null) {
                    if (aNode != null) {
                        this.graph.toggleNodeSelection(aNode);
                        this.editor.repaint();
                    }
                    else if (anEdge != null) {
                        this.graph.toggleEdgeSelection(anEdge);
                        this.editor.repaint();
                    }
                }
                else if (aNode == null && (event.getClickCount() == 2 || GraphEditorListener.SINGLE_CLICK_ADD_NODE) && event.getPoint().x >= GraphEditor.DRAW_BUFFER && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER && event.getPoint().y >= GraphEditor.DRAW_BUFFER && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER) {
                    this.graph.newMemento("Create New Node");
                    this.graph.createNode(ePoint);
                    this.graph.doneMemento();
                    this.controller.newUndo();
                    this.editor.setPreferredSize();
                    this.editor.update();
                }
            }
        }
    }
    
    public void mousePressed(final MouseEvent event) {
        if (this.numSpecialSelectionsAllowed == 0) {
            if (event.getButton() != 3) {
                if (event.getPoint().x >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER + Node.RADIUS && event.getPoint().y >= GraphEditor.DRAW_BUFFER - Node.RADIUS && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER + Node.RADIUS) {
                    final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
                    final Node aNode = this.graph.nodeAt(ePoint);
                    if (aNode != null) {
                        this.dragStartLocation = new Location(event.getPoint());
                        this.dragNode = aNode;
                        if (aNode.isSelected()) {
                            if (event.isControlDown()) {
                                this.selectedNodeVector = this.graph.selectedNodes();
                                if (!this.selectedNodeVector.isEmpty()) {
                                    this.graph.newMemento("Move Group of Selected Nodes");
                                    final Rectangle2D.Double bounds = this.graph.getBounds(this.selectedNodeVector);
                                    this.maxX = (int)bounds.getMaxX();
                                    this.minX = (int)bounds.getMinX();
                                    this.maxY = (int)bounds.getMaxY();
                                    this.minY = (int)bounds.getMinY();
                                    this.graph.translateNodes(this.selectedNodeVector, 0, 0, true);
                                    this.editor.startTranslateNodes(this.selectedNodeVector);
                                }
                            }
                            else {
                                this.graph.newMemento("Move Selected Node");
                                this.graph.translateNode(aNode, 0, 0, true);
                                this.editor.startTranslateNode(aNode);
                                this.maxX = aNode.getX();
                                this.minX = aNode.getX();
                                this.maxY = aNode.getY();
                                this.minY = aNode.getY();
                            }
                        }
                        else if (event.isControlDown() || GraphEditorListener.ADD_NODE_ON_EDGE_DROP) {
                            this.createNodeOnEdgeDrop = true;
                            final int x = aNode.getX();
                            this.minX = x;
                            this.maxX = x;
                            final int y = aNode.getY();
                            this.minY = y;
                            this.maxY = y;
                            this.nodeSplitTree = new NodeSplitTree(this.graph.getNodes());
                        }
                        else {
                            this.createNodeOnEdgeDrop = false;
                            final int x2 = aNode.getX();
                            this.minX = x2;
                            this.maxX = x2;
                            final int y2 = aNode.getY();
                            this.minY = y2;
                            this.maxY = y2;
                            this.nodeSplitTree = new NodeSplitTree(this.graph.getNodes());
                        }
                    }
                    else {
                        final Edge anEdge = this.graph.edgeAt(ePoint);
                        if (anEdge != null) {
                            if (anEdge.isSelected()) {
                                this.graph.newMemento("Curve Selected Edge");
                                this.undoCurve = !anEdge.isCurved();
                                this.graph.curveEdge(anEdge, 0, 0, true);
                                this.editor.startTranslateEdge(anEdge);
                                this.dragStartLocation = new Location(event.getPoint());
                                this.dragEdge = anEdge;
                                this.maxX = anEdge.getCenterLocation().intX();
                                this.minX = anEdge.getCenterLocation().intX();
                                this.maxY = anEdge.getCenterLocation().intY();
                                this.minY = anEdge.getCenterLocation().intY();
                            }
                        }
                        else {
                            this.dragStartLocation = new Location(event.getPoint());
                        }
                    }
                }
                this.hidePopup();
            }
            else {
                final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
                this.aNode = this.graph.nodeAt(ePoint);
                if (this.aNode != null) {
                    this.graph.enumerateNodeAndEdgeIndices();
                    this.popupLocation = event.getPoint();
                    this.applyToSelected = event.isControlDown();
                    this.showPopup(this.aNode, event.getPoint());
                }
                else {
                    this.anEdge = this.graph.edgeAt(ePoint);
                    if (this.anEdge != null) {
                        this.graph.enumerateNodeAndEdgeIndices();
                        this.popupLocation = event.getPoint();
                        this.applyToSelected = event.isControlDown();
                        this.showPopup(this.anEdge, event.getPoint());
                    }
                    else {
                        this.hidePopup();
                    }
                }
            }
        }
    }
    
    public void mouseDragged(final MouseEvent event) {
        super.mouseMoved(event);
        if (this.numSpecialSelectionsAllowed == 0) {
            if (this.dragNode != null) {
                this.dragged = true;
                final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
                if (this.dragNode.isSelected()) {
                    int transX = ePoint.intX() - this.dragNode.getLocation().intX();
                    int transY = ePoint.intY() - this.dragNode.getLocation().intY();
                    if (this.maxX + transX > this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER) {
                        transX = this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER - this.maxX;
                    }
                    else if (this.minX + transX < 0) {
                        transX = 0 - this.minX;
                    }
                    if (this.maxY + transY > this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER) {
                        transY = this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER - this.maxY;
                    }
                    else if (this.minY + transY < 0) {
                        transY = 0 - this.minY;
                    }
                    this.maxX += transX;
                    this.minX += transX;
                    this.maxY += transY;
                    this.minY += transY;
                    if (transX != 0 || transY != 0) {
                        if (event.isControlDown()) {
                            this.graph.translateNodes(this.selectedNodeVector, transX, transY, false);
                        }
                        else {
                            this.graph.translateNode(this.dragNode, transX, transY, false);
                        }
                        this.editor.setPreferredSize();
                        this.editor.repaint();
                    }
                }
                else {
                    this.dragStartLocation = new Location(event.getPoint());
                    this.editor.setLineToDraw(new Line2D.Double(this.dragNode.getLocation().intX() + GraphEditor.DRAW_BUFFER, this.dragNode.getLocation().intY() + GraphEditor.DRAW_BUFFER, this.dragStartLocation.intX(), this.dragStartLocation.intY()));
                    this.dragStartLocation.translate(-1 * GraphEditor.DRAW_BUFFER, -1 * GraphEditor.DRAW_BUFFER);
                    Node dragToNode = null;
                    if (event.isShiftDown()) {
                        dragToNode = this.graph.nodeAt(this.dragStartLocation);
                    }
                    else {
                        dragToNode = this.nodeSplitTree.nodeAt(this.dragStartLocation);
                    }
                    if (dragToNode != null) {
                        this.editor.setLineToDrawColor(Edge.DEFAULT_COLOR);
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
            }
            else if (this.dragEdge != null) {
                this.dragged = true;
                final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
                final Location edgeLocation = this.dragEdge.getCenterLocation();
                int transX2 = ePoint.intX() - edgeLocation.intX();
                int transY2 = ePoint.intY() - edgeLocation.intY();
                if (this.maxX + transX2 > this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER) {
                    transX2 = this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER - this.maxX;
                }
                else if (this.minX + transX2 < 0) {
                    transX2 = 0 - this.minX;
                }
                if (this.maxY + transY2 > this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER) {
                    transY2 = this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER - this.maxY;
                }
                else if (this.minY + transY2 < 0) {
                    transY2 = 0 - this.minY;
                }
                this.maxX += transX2;
                this.minX += transX2;
                this.maxY += transY2;
                this.minY += transY2;
                if (transX2 != 0 || transY2 != 0) {
                    this.graph.curveEdge(this.dragEdge, transX2, transY2, false);
                    this.editor.setPreferredSize();
                    this.editor.update();
                }
            }
            else if (this.dragStartLocation != null) {
                this.dragged = true;
                double startX = this.dragStartLocation.doubleX();
                double startY = this.dragStartLocation.doubleY();
                if (event.getPoint().x < this.dragStartLocation.intX()) {
                    startX = event.getPoint().x;
                }
                if (event.getPoint().y < this.dragStartLocation.intY()) {
                    startY = event.getPoint().y;
                }
                final double width = Math.abs(event.getPoint().x - this.dragStartLocation.intX());
                final double height = Math.abs(event.getPoint().y - this.dragStartLocation.intY());
                this.editor.setRectangleToDraw(new Rectangle2D.Double(startX, startY, width, height));
                this.editor.repaint();
            }
        }
    }
    
    public void mouseReleased(final MouseEvent event) {
        if (this.dragNode != null) {
            if (!this.dragNode.isSelected()) {
                final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
                Node aNode = this.graph.nodeAt(ePoint);
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
                        aNode = this.graph.createNode(ePoint);
                        this.graph.addEdge(this.dragNode, aNode);
                        this.graph.doneMemento();
                        this.controller.newUndo();
                        this.editor.setPreferredSize();
                        this.editor.update();
                    }
                }
                this.nodeSplitTree = null;
                this.editor.setLineToDraw(null);
                this.editor.repaint();
            }
            else if (this.dragged) {
                this.graph.doneMemento();
                this.controller.newUndo();
                this.graph.markForRepaint();
                this.editor.update();
            }
            else {
                this.graph.abortMemento();
            }
            this.dragNode = null;
            this.editor.stopTranslateNodes();
        }
        else if (this.dragEdge != null) {
            if (this.dragged) {
                this.graph.doneMemento();
                this.controller.newUndo();
                this.graph.markForRepaint();
                this.editor.update();
            }
            else {
                if (this.undoCurve) {
                    this.undoCurve = false;
                    this.dragEdge.makeStraight();
                }
                this.graph.abortMemento();
            }
            this.editor.stopTranslateEdge();
            this.dragEdge = null;
        }
        else if (this.dragged) {
            final Rectangle2D.Double rect = this.editor.getRectangleToDraw();
            rect.setRect(rect.x - GraphEditor.DRAW_BUFFER, rect.y - GraphEditor.DRAW_BUFFER, rect.width, rect.height);
            final Vector nodes = this.graph.getNodesInRectangle(rect);
            final Vector edges = this.graph.getEdgesInRectangle(rect);
            this.graph.selectNodes(nodes);
            this.graph.selectEdges(edges);
            this.editor.setRectangleToDraw(null);
            this.editor.repaint();
        }
        this.dragStartLocation = null;
        this.dragged = false;
    }
    
    public void keyPressed(final KeyEvent event) {
        if (this.numSpecialSelectionsAllowed == 0 && (event.getKeyCode() == 127 || event.getKeyCode() == 110)) {
            this.controller.removeSelected();
            this.editor.update();
        }
    }
    
    public void drawIncidentEdgesInOrder(final Node aNode, final boolean reverse) {
        if (this.counter == null || !this.counter.isRunning()) {
            this.animateSourceNode = aNode;
            this.oldEdgeColorVector = new Vector();
            if (reverse) {
                this.edgesToAnimateVector = aNode.incidentEdgesInReverse();
            }
            else {
                this.edgesToAnimateVector = aNode.incidentEdges();
            }
            this.oldNodeColor = aNode.getColor();
            for (int i = 0; i < this.edgesToAnimateVector.size(); ++i) {
                this.oldEdgeColorVector.addElement(this.edgesToAnimateVector.elementAt(i).getColor());
            }
            this.animateEdgeIndex = 0;
            (this.counter = new Timer(this.animationDelay, this)).start();
        }
    }
    
    public void drawIncidentEdgesInOrder(final Edge anEdge) {
        if (this.counter == null || !this.counter.isRunning()) {
            this.edgesToAnimateVector = anEdge.edgesFromSameCycle();
            this.sourceEdgeIndex = this.edgesToAnimateVector.size();
            this.edgesToAnimateVector.addAll(anEdge.edgesFromSameCycleOnOtherSide());
            this.animateSourceNode = null;
            this.oldEdgeColorVector = new Vector();
            for (int i = 0; i < this.edgesToAnimateVector.size(); ++i) {
                this.oldEdgeColorVector.addElement(this.edgesToAnimateVector.elementAt(i).getColor());
            }
            this.animateEdgeIndex = 0;
            (this.counter = new Timer(this.animationDelay, this)).start();
        }
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.counter) {
            if (this.animateSourceNode != null) {
                if (this.animateEdgeIndex < this.edgesToAnimateVector.size()) {
                    final Edge tempEdge = this.edgesToAnimateVector.elementAt(this.animateEdgeIndex);
                    tempEdge.setColor(Color.cyan);
                    ++this.animateEdgeIndex;
                }
                else if (this.animateEdgeIndex == this.edgesToAnimateVector.size()) {
                    this.animateSourceNode.setColor(Color.cyan);
                    ++this.animateEdgeIndex;
                }
                else {
                    this.counter.stop();
                    for (int i = 0; i < this.edgesToAnimateVector.size(); ++i) {
                        this.edgesToAnimateVector.elementAt(i).setColor(this.oldEdgeColorVector.elementAt(i));
                    }
                    this.animateSourceNode.setColor(this.oldNodeColor);
                }
            }
            else if (this.animateEdgeIndex < this.edgesToAnimateVector.size()) {
                final Edge tempEdge = this.edgesToAnimateVector.elementAt(this.animateEdgeIndex);
                if (this.animateEdgeIndex < this.sourceEdgeIndex) {
                    tempEdge.setColor(Color.cyan);
                }
                else {
                    tempEdge.setColor(Color.orange);
                }
                ++this.animateEdgeIndex;
            }
            else {
                this.counter.stop();
                for (int i = 0; i < this.edgesToAnimateVector.size(); ++i) {
                    this.edgesToAnimateVector.elementAt(i).setColor(this.oldEdgeColorVector.elementAt(i));
                }
            }
            this.graph.markForRepaint();
            this.editor.repaint();
        }
        else if (e.getSource() == this.labelButton) {
            if (this.aNode != null) {
                final String newLabel = (String)JOptionPane.showInputDialog(null, "Please Enter the Text for the New Label", "Set Node Label", -1, null, null, this.aNode.getLabel());
                if (newLabel != null) {
                    if (this.applyToSelected && this.selectedNodeVector.size() + this.selectedEdgeVector.size() > 1) {
                        this.graph.newMemento("Change Selected Node Labels");
                        for (int i = 0; i < this.selectedNodeVector.size(); ++i) {
                            this.graph.changeNodeLabel(this.selectedNodeVector.elementAt(i), newLabel, true);
                        }
                        this.graph.doneMemento();
                    }
                    else {
                        this.graph.newMemento("Change Node Label");
                        this.graph.changeNodeLabel(this.aNode, newLabel, true);
                        this.graph.doneMemento();
                    }
                    this.controller.newUndo();
                    this.editor.repaint();
                    this.showPopup(this.aNode, this.popupLocation);
                }
            }
        }
        else if (e.getSource() == this.colorButton) {
            if (this.aNode != null) {
                final Color newColor = JColorChooser.showDialog(null, "Set Node Color", this.aNode.getColor());
                if (newColor != null) {
                    if (this.applyToSelected && this.selectedNodeVector.size() > 1) {
                        this.graph.newMemento("Change Selected Node Colours");
                        for (int i = 0; i < this.selectedNodeVector.size(); ++i) {
                            this.graph.changeNodeColor(this.selectedNodeVector.elementAt(i), newColor, true);
                        }
                        this.graph.doneMemento();
                    }
                    else {
                        this.graph.newMemento("Change Node Color");
                        this.graph.changeNodeColor(this.aNode, newColor, true);
                        this.graph.doneMemento();
                    }
                    this.controller.newUndo();
                    this.editor.repaint();
                    this.showPopup(this.aNode, this.popupLocation);
                }
            }
            else if (this.anEdge != null) {
                final Color newColor = JColorChooser.showDialog(null, "Set Edge Color", this.anEdge.getColor());
                if (newColor != null) {
                    if (this.applyToSelected && this.selectedEdgeVector.size() > 1) {
                        this.graph.newMemento("Change Selected Edge Colours");
                        for (int i = 0; i < this.selectedEdgeVector.size(); ++i) {
                            this.graph.changeEdgeColor(this.selectedEdgeVector.elementAt(i), newColor, true);
                        }
                        this.graph.doneMemento();
                    }
                    else {
                        this.graph.newMemento("Change Edge Color");
                        this.graph.changeEdgeColor(this.anEdge, newColor, true);
                        this.graph.doneMemento();
                    }
                    this.controller.newUndo();
                    this.editor.repaint();
                    this.showPopup(this.anEdge, this.popupLocation);
                }
            }
        }
        else if (e.getSource() == this.makeStraightButton) {
            final Location diff = this.anEdge.getCenterLocation();
            if (this.applyToSelected && this.selectedNodeVector.size() + this.selectedEdgeVector.size() > 1) {
                this.graph.newMemento("Make Selected Edges Straight");
                for (int i = 0; i < this.selectedEdgeVector.size(); ++i) {
                    this.graph.straightenEdge(this.selectedEdgeVector.elementAt(i), true);
                }
                this.graph.doneMemento();
            }
            else {
                this.graph.newMemento("Straighten Edge");
                this.graph.straightenEdge(this.anEdge, true);
                this.graph.doneMemento();
            }
            this.controller.newUndo();
            this.editor.update();
            final int dx = this.anEdge.getCenterLocation().intX() - diff.intX();
            final int dy = this.anEdge.getCenterLocation().intY() - diff.intY();
            this.popupLocation.translate(dx, dy);
            this.showPopup(this.anEdge, this.popupLocation);
        }
        else if (e.getSource() == this.undirectButton) {
            if (this.applyToSelected && this.selectedNodeVector.size() + this.selectedEdgeVector.size() > 1) {
                this.graph.newMemento("Make Selected Edges Undirected");
                for (int j = 0; j < this.selectedEdgeVector.size(); ++j) {
                    this.graph.changeEdgeDirection(this.selectedEdgeVector.elementAt(j), null, true);
                }
                this.graph.doneMemento();
            }
            else {
                this.graph.newMemento("Make Edge Undirected");
                this.graph.changeEdgeDirection(this.anEdge, null, true);
                this.graph.doneMemento();
            }
            this.controller.newUndo();
            this.editor.update();
            this.showPopup(this.anEdge, this.popupLocation);
        }
        else if (e.getSource() == this.makePermanentButton) {
            if (this.applyToSelected && this.selectedNodeVector.size() + this.selectedEdgeVector.size() > 1) {
                this.graph.newMemento("Make Generated Edges Permanent");
                for (int j = 0; j < this.selectedEdgeVector.size(); ++j) {
                    this.graph.makeGeneratedEdgePermanent(this.selectedEdgeVector.elementAt(j));
                }
                this.graph.doneMemento();
            }
            else {
                this.graph.newMemento("Make Generated Edge Permanent");
                this.graph.makeGeneratedEdgePermanent(this.anEdge);
                this.graph.doneMemento();
            }
            this.controller.newUndo();
            this.editor.update();
            this.showPopup(this.anEdge, this.popupLocation);
        }
        else if (e.getSource() == this.selectButton) {
            if (this.aNode != null) {
                this.aNode.toggleSelected();
                if (this.aNode.isSelected()) {
                    this.selectButton.setText("Toggle Select (T)");
                }
                else {
                    this.selectButton.setText("Toggle Select (F)");
                }
                this.graph.markForRepaint();
                this.editor.repaint();
            }
            else if (this.anEdge != null) {
                this.anEdge.toggleSelected();
                if (this.anEdge.isSelected()) {
                    this.selectButton.setText("Toggle Select (T)");
                }
                else {
                    this.selectButton.setText("Toggle Select (F)");
                }
                this.graph.markForRepaint();
                this.editor.repaint();
            }
        }
        else if (e.getSource() == this.closeButton) {
            this.hidePopup();
        }
    }
    
    public void focusGained(final FocusEvent e) {
    }
    
    public void focusLost(final FocusEvent e) {
        if (e.getOppositeComponent() != this.popupWindow && e.getOppositeComponent() != this.labelButton && e.getOppositeComponent() != this.colorButton && e.getOppositeComponent() != this.makeStraightButton && e.getOppositeComponent() != this.closeButton) {
            this.hidePopup();
        }
    }
    
    public void ancestorMoved(final AncestorEvent event) {
        this.hidePopup();
    }
    
    public void ancestorAdded(final AncestorEvent event) {
    }
    
    public void ancestorRemoved(final AncestorEvent event) {
    }
    
    protected void showPopup(final Node aNode, final Point location) {
        this.hidePopup();
        (this.popupPanel = new JPanel()).setBorder(BorderFactory.createEtchedBorder());
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.popupPanel.setLayout(layout);
        this.selectedNodeVector = this.graph.selectedNodes();
        this.selectedEdgeVector = this.graph.selectedEdges();
        JLabel label;
        if (this.applyToSelected && this.selectedNodeVector.size() > 1) {
            label = new JLabel("Edit All Selected");
        }
        else {
            label = new JLabel("Index: " + aNode.getIndex());
        }
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(label, layoutCons);
        this.popupPanel.add(label);
        label = new JLabel("X, Y: " + aNode.getX() + ", " + aNode.getY());
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(label, layoutCons);
        this.popupPanel.add(label);
        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        this.labelButton = new JButton("Set Label: ");
        if (this.applyToSelected && this.selectedNodeVector.size() > 1) {
            this.labelButton.setToolTipText("Set all Selected Nodes' Label");
        }
        else {
            this.labelButton.setToolTipText("Set this Node's Label");
        }
        this.labelButton.addActionListener(this);
        labelPanel.add(this.labelButton, "West");
        label = new JLabel("\"" + aNode.getLabel() + "\"");
        labelPanel.add(label, "Center");
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(labelPanel, layoutCons);
        this.popupPanel.add(labelPanel);
        final JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BorderLayout());
        this.colorButton = new JButton("Set Color:");
        if (this.applyToSelected && this.selectedNodeVector.size() > 1) {
            this.colorButton.setToolTipText("Set all Selected Nodes' Color");
        }
        else {
            this.colorButton.setToolTipText("Set this Node's Color");
        }
        this.colorButton.addActionListener(this);
        colorPanel.add(this.colorButton, "West");
        final JPanel subColorPanel = new JPanel();
        subColorPanel.setBackground(aNode.getColor());
        colorPanel.add(subColorPanel, "Center");
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(colorPanel, layoutCons);
        this.popupPanel.add(colorPanel);
        if (aNode.isSelected()) {
            this.selectButton = new JButton("Toggle Select (T) ");
        }
        else {
            this.selectButton = new JButton("Toggle Select (F) ");
        }
        this.selectButton.setToolTipText("Toggle Node Selection");
        this.selectButton.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.selectButton, layoutCons);
        this.popupPanel.add(this.selectButton);
        if (this.isGridListener() && !this.graph.isOnGrid(aNode.getLocation())) {
            (this.gridButton = new JButton("Snap To Grid")).addActionListener(this);
            layoutCons.gridx = -1;
            layoutCons.gridy = -1;
            layoutCons.gridwidth = 0;
            layoutCons.gridheight = 1;
            layoutCons.fill = 1;
            layoutCons.insets = new Insets(1, 1, 1, 1);
            layoutCons.anchor = 11;
            layoutCons.weightx = 1.0;
            layoutCons.weighty = 1.0;
            layout.setConstraints(this.gridButton, layoutCons);
            this.popupPanel.add(this.gridButton);
        }
        (this.closeButton = new JButton("Close")).setToolTipText("Close this Window");
        this.closeButton.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.closeButton, layoutCons);
        this.popupPanel.add(this.closeButton);
        this.showPopup(location);
    }
    
    protected void showPopup(final Edge anEdge, final Point location) {
        this.hidePopup();
        (this.popupPanel = new JPanel()).setBorder(BorderFactory.createEtchedBorder());
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.popupPanel.setLayout(layout);
        this.selectedNodeVector = this.graph.selectedNodes();
        this.selectedEdgeVector = this.graph.selectedEdges();
        JLabel label;
        if (this.applyToSelected && this.selectedEdgeVector.size() > 1) {
            label = new JLabel("Edit All Selected");
        }
        else {
            label = new JLabel("Index: " + anEdge.getIndex());
        }
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(label, layoutCons);
        this.popupPanel.add(label);
        label = new JLabel("s.X, s.Y: " + anEdge.getStartNode().getX() + ", " + anEdge.getStartNode().getY());
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(label, layoutCons);
        this.popupPanel.add(label);
        label = new JLabel("e.X, e.Y: " + anEdge.getEndNode().getX() + ", " + anEdge.getEndNode().getY());
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(label, layoutCons);
        this.popupPanel.add(label);
        final JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BorderLayout());
        this.colorButton = new JButton("Color: ");
        if (this.applyToSelected && this.selectedEdgeVector.size() > 1) {
            this.colorButton.setToolTipText("Set all Selected Edges' Color");
        }
        else {
            this.colorButton.setToolTipText("Set this Edge's Color");
        }
        this.colorButton.addActionListener(this);
        colorPanel.add(this.colorButton, "West");
        final JPanel subColorPanel = new JPanel();
        subColorPanel.setBackground(anEdge.getColor());
        colorPanel.add(subColorPanel, "Center");
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(colorPanel, layoutCons);
        this.popupPanel.add(colorPanel);
        if (this.applyToSelected && this.selectedEdgeVector.size() > 1) {
            boolean anyIsCurved = false;
            for (int x = 0; x < this.selectedEdgeVector.size(); ++x) {
                if (this.selectedEdgeVector.elementAt(x).isCurved() || this.selectedEdgeVector.elementAt(x).isOrthogonal()) {
                    anyIsCurved = true;
                }
            }
            if (anyIsCurved) {
                (this.makeStraightButton = new JButton("Make Straight")).setToolTipText("Make all Selected Edges Straight");
                this.makeStraightButton.addActionListener(this);
                layoutCons.gridx = -1;
                layoutCons.gridy = -1;
                layoutCons.gridwidth = 0;
                layoutCons.gridheight = 1;
                layoutCons.fill = 1;
                layoutCons.insets = new Insets(1, 1, 1, 1);
                layoutCons.anchor = 11;
                layoutCons.weightx = 1.0;
                layoutCons.weighty = 1.0;
                layout.setConstraints(this.makeStraightButton, layoutCons);
                this.popupPanel.add(this.makeStraightButton);
            }
        }
        else if (anEdge.isCurved() || anEdge.isOrthogonal()) {
            (this.makeStraightButton = new JButton("Make Straight")).setToolTipText("Make this Edge Straight");
            this.makeStraightButton.addActionListener(this);
            layoutCons.gridx = -1;
            layoutCons.gridy = -1;
            layoutCons.gridwidth = 0;
            layoutCons.gridheight = 1;
            layoutCons.fill = 1;
            layoutCons.insets = new Insets(1, 1, 1, 1);
            layoutCons.anchor = 11;
            layoutCons.weightx = 1.0;
            layoutCons.weighty = 1.0;
            layout.setConstraints(this.makeStraightButton, layoutCons);
            this.popupPanel.add(this.makeStraightButton);
        }
        if (this.isGridListener() && !anEdge.isOrthogonal() && anEdge.isCurved()) {
            (this.gridButton = new JButton("Make Orthogonal")).addActionListener(this);
            layoutCons.gridx = -1;
            layoutCons.gridy = -1;
            layoutCons.gridwidth = 0;
            layoutCons.gridheight = 1;
            layoutCons.fill = 1;
            layoutCons.insets = new Insets(1, 1, 1, 1);
            layoutCons.anchor = 11;
            layoutCons.weightx = 1.0;
            layoutCons.weighty = 1.0;
            layout.setConstraints(this.gridButton, layoutCons);
            this.popupPanel.add(this.gridButton);
        }
        if (this.applyToSelected && this.selectedEdgeVector.size() > 1) {
            boolean anyIsDirected = false;
            for (int x = 0; x < this.selectedEdgeVector.size(); ++x) {
                if (this.selectedEdgeVector.elementAt(x).isDirected()) {
                    anyIsDirected = true;
                }
            }
            if (anyIsDirected) {
                (this.undirectButton = new JButton("Remove Directions")).setToolTipText("Make All Selected Edges Undirected");
                this.undirectButton.addActionListener(this);
                layoutCons.gridx = -1;
                layoutCons.gridy = -1;
                layoutCons.gridwidth = 0;
                layoutCons.gridheight = 1;
                layoutCons.fill = 1;
                layoutCons.insets = new Insets(1, 1, 1, 1);
                layoutCons.anchor = 11;
                layoutCons.weightx = 1.0;
                layoutCons.weighty = 1.0;
                layout.setConstraints(this.undirectButton, layoutCons);
                this.popupPanel.add(this.undirectButton);
            }
        }
        else if (anEdge.isDirected()) {
            (this.undirectButton = new JButton("Remove Direction")).setToolTipText("Make this Edge Undirected");
            this.undirectButton.addActionListener(this);
            layoutCons.gridx = -1;
            layoutCons.gridy = -1;
            layoutCons.gridwidth = 0;
            layoutCons.gridheight = 1;
            layoutCons.fill = 1;
            layoutCons.insets = new Insets(1, 1, 1, 1);
            layoutCons.anchor = 11;
            layoutCons.weightx = 1.0;
            layoutCons.weighty = 1.0;
            layout.setConstraints(this.undirectButton, layoutCons);
            this.popupPanel.add(this.undirectButton);
        }
        if (this.applyToSelected && this.selectedEdgeVector.size() > 1) {
            boolean anyIsGenerated = false;
            for (int x = 0; x < this.selectedEdgeVector.size(); ++x) {
                if (this.selectedEdgeVector.elementAt(x).isGenerated()) {
                    anyIsGenerated = true;
                }
            }
            if (anyIsGenerated) {
                (this.makePermanentButton = new JButton("Make Permanent")).setToolTipText("Make All Generated Edges Permanent");
                this.makePermanentButton.addActionListener(this);
                layoutCons.gridx = -1;
                layoutCons.gridy = -1;
                layoutCons.gridwidth = 0;
                layoutCons.gridheight = 1;
                layoutCons.fill = 1;
                layoutCons.insets = new Insets(1, 1, 1, 1);
                layoutCons.anchor = 11;
                layoutCons.weightx = 1.0;
                layoutCons.weighty = 1.0;
                layout.setConstraints(this.makePermanentButton, layoutCons);
                this.popupPanel.add(this.makePermanentButton);
            }
        }
        else if (anEdge.isGenerated()) {
            (this.makePermanentButton = new JButton("Make Permanent")).setToolTipText("Make this Generated Edge Permanent");
            this.makePermanentButton.addActionListener(this);
            layoutCons.gridx = -1;
            layoutCons.gridy = -1;
            layoutCons.gridwidth = 0;
            layoutCons.gridheight = 1;
            layoutCons.fill = 1;
            layoutCons.insets = new Insets(1, 1, 1, 1);
            layoutCons.anchor = 11;
            layoutCons.weightx = 1.0;
            layoutCons.weighty = 1.0;
            layout.setConstraints(this.makePermanentButton, layoutCons);
            this.popupPanel.add(this.makePermanentButton);
        }
        if (anEdge.isSelected()) {
            this.selectButton = new JButton("Toggle Select (T) ");
        }
        else {
            this.selectButton = new JButton("Toggle Select (F) ");
        }
        this.selectButton.setToolTipText("Toggle Node Selection");
        this.selectButton.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.selectButton, layoutCons);
        this.popupPanel.add(this.selectButton);
        (this.closeButton = new JButton("Close")).setToolTipText("Close this Window");
        this.closeButton.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.closeButton, layoutCons);
        this.popupPanel.add(this.closeButton);
        this.showPopup(location);
    }
    
    private void showPopup(Point location) {
        if (!this.layerInited) {
            this.layerInited = true;
            final Container container = this.editor.getTopLevelAncestor();
            if (container instanceof JFrame) {
                this.layeredPane = ((JFrame)container).getLayeredPane();
                this.rootPane = ((JFrame)container).getRootPane();
            }
            else if (container instanceof JApplet) {
                this.layeredPane = ((JApplet)container).getLayeredPane();
                this.rootPane = ((JApplet)container).getRootPane();
            }
            else {
                this.layeredPane = null;
                this.rootPane = null;
            }
            (this.popupWindow = new JWindow(this.getParentWindow(this.editor))).setFocusableWindowState(false);
            this.popupWindow.addFocusListener(this);
            this.wLayeredPane = this.popupWindow.getLayeredPane();
            this.wRootPane = this.popupWindow.getRootPane();
        }
        location = SwingUtilities.convertPoint(this.editor, location, this.rootPane);
        this.popupPanel.setSize(this.popupPanel.getPreferredSize());
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = null;
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice[] gd = ge.getScreenDevices();
        for (int i = 0; i < gd.length; ++i) {
            if (gd[i].getType() == 0) {
                final GraphicsConfiguration dgc = gd[i].getDefaultConfiguration();
                if (dgc.getBounds().contains(location)) {
                    gc = dgc;
                    break;
                }
            }
        }
        Insets screenInsets;
        Rectangle screenBounds;
        if (gc != null) {
            screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
        }
        else {
            screenInsets = new Insets(0, 0, 0, 0);
            screenBounds = new Rectangle(toolkit.getScreenSize());
        }
        final int scrWidth = screenBounds.width - Math.abs(screenInsets.left + screenInsets.right);
        final int scrHeight = screenBounds.height - Math.abs(screenInsets.top + screenInsets.bottom);
        final Dimension size = this.popupPanel.getPreferredSize();
        final Point screenLocation = new Point(location);
        SwingUtilities.convertPointToScreen(screenLocation, this.rootPane);
        this.windowPopup = true;
        if (screenLocation.x + size.width > screenBounds.x + scrWidth) {
            screenLocation.x = screenBounds.x + scrWidth - size.width;
            this.windowPopup = true;
        }
        if (screenLocation.y + size.height > screenBounds.y + scrHeight) {
            screenLocation.y = screenBounds.y + scrHeight - size.height;
            this.windowPopup = true;
        }
        if (screenLocation.x < screenBounds.x) {
            screenLocation.x = screenBounds.x;
            this.windowPopup = true;
        }
        if (screenLocation.y < screenBounds.y) {
            screenLocation.y = screenBounds.y;
            this.windowPopup = true;
        }
        if (location.y + this.popupPanel.getHeight() > this.rootPane.getHeight() || location.x + this.popupPanel.getWidth() > this.rootPane.getWidth()) {
            this.windowPopup = true;
        }
        if (this.windowPopup) {
            this.popupWindow.setLocation(screenLocation.x, screenLocation.y);
            this.popupPanel.setLocation(0, 0);
            this.popupPanel.setVisible(true);
            this.popupWindow.setSize(this.popupPanel.getSize());
            this.popupWindow.setVisible(true);
            this.wLayeredPane.add(this.popupPanel, JLayeredPane.POPUP_LAYER);
        }
        else {
            this.popupPanel.setLocation(location.x, location.y);
            this.popupPanel.setVisible(true);
            this.layeredPane.add(this.popupPanel, JLayeredPane.POPUP_LAYER);
        }
    }
    
    public void hidePopup() {
        if (this.windowPopup) {
            if (this.popupPanel != null) {
                this.popupPanel.setVisible(false);
                this.wLayeredPane.remove(this.popupPanel);
                this.popupPanel = null;
                this.popupWindow.setVisible(false);
            }
        }
        else if (this.popupPanel != null) {
            this.popupPanel.setVisible(false);
            this.layeredPane.remove(this.popupPanel);
            this.popupPanel = null;
        }
    }
    
    public void keyTyped(final KeyEvent event) {
    }
    
    public void keyReleased(final KeyEvent event) {
    }
    
    private Window getParentWindow(final Component owner) {
        Window window = null;
        if (owner instanceof Window) {
            window = (Window)owner;
        }
        else if (owner != null) {
            window = SwingUtilities.getWindowAncestor(owner);
        }
        if (window == null) {
            window = new Frame();
        }
        return window;
    }
    
    public void prepareForClose() {
        this.hidePopup();
    }
}
