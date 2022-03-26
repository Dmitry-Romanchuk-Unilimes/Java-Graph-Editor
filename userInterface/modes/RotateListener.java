// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.modes;

import graphStructure.Location;
import graphStructure.Node;
import userInterface.GraphEditor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class RotateListener extends GraphEditorListener
{
    private Location rotatePivotPoint;
    private Location rotateAxisPoint;
    private Location newRotateAxisPoint;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private double totalAngle;
    private boolean dragged;
    private boolean dragging;
    
    public RotateListener(final GraphEditorListener listener) {
        super(listener);
        this.totalAngle = 0.0;
        this.graph.setDrawSelected(false);
        this.editor.changeToRotateCursor();
        this.dragged = false;
        this.dragging = false;
    }
    
    public boolean isRotateListener() {
        return true;
    }
    
    public void mousePressed(final MouseEvent event) {
        if (this.graph.getNumNodes() > 1 && event.getPoint().x >= GraphEditor.DRAW_BUFFER && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER && event.getPoint().y >= GraphEditor.DRAW_BUFFER && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER && event.getButton() != 3) {
            this.rotatePivotPoint = this.graph.getCenterPointLocation();
            this.rotateAxisPoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
            this.editor.setPointToDraw(new Ellipse2D.Double(this.rotatePivotPoint.intX() - Node.RADIUS + GraphEditor.DRAW_BUFFER, this.rotatePivotPoint.intY() - Node.RADIUS + GraphEditor.DRAW_BUFFER, Node.RADIUS * 2, Node.RADIUS * 2));
            final Rectangle2D.Double bounds = this.graph.getBounds();
            this.minX = (int)bounds.getMinX();
            this.minY = (int)bounds.getMinY();
            this.graph.newMemento("Rotate Graph");
            this.graph.rotate(this.rotatePivotPoint, 0.0, true);
            this.editor.startRotate();
            this.dragging = true;
        }
    }
    
    public void mouseDragged(final MouseEvent event) {
        super.mouseMoved(event);
        if (this.dragging && this.graph.getNumNodes() > 1 && this.rotatePivotPoint != null) {
            this.newRotateAxisPoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
            final double angle = Node.angleBetween(this.rotateAxisPoint, this.rotatePivotPoint, this.newRotateAxisPoint);
            this.rotateAxisPoint = this.newRotateAxisPoint;
            this.editor.rotate(this.rotatePivotPoint, angle);
            this.dragged = true;
            final Vector nodes = this.graph.getNodes();
            if (!nodes.isEmpty()) {
                final Rectangle2D.Double bounds = this.graph.getBounds();
                this.minX = (int)bounds.getMinX();
                this.minY = (int)bounds.getMinY();
                this.maxX = (int)bounds.getMaxX();
                this.maxY = (int)bounds.getMaxY();
            }
            if (this.maxX > this.editor.getWidth() - 2 * GraphEditor.DRAW_BUFFER || this.minX <= 0 || this.maxY > this.editor.getHeight() - 2 * GraphEditor.DRAW_BUFFER || this.minY <= 0) {
                this.editor.rotate(this.rotatePivotPoint, -1.0 * angle);
            }
            this.editor.setPreferredSize();
            this.editor.repaint();
        }
    }
    
    public void mouseReleased(final MouseEvent event) {
        if (this.dragging) {
            if (this.dragged) {
                this.graph.doneMemento();
                this.controller.newUndo();
            }
            else {
                this.graph.abortMemento();
            }
            this.dragged = false;
            this.dragging = false;
            this.editor.stopRotate();
            if (this.graph.getNumNodes() > 1) {
                this.rotatePivotPoint = null;
                this.editor.repaint();
            }
        }
    }
    
    public void mouseClicked(final MouseEvent event) {
        super.mouseClicked(event);
    }
    
    public void keyPressed(final KeyEvent event) {
    }
    
    public void keyTyped(final KeyEvent event) {
    }
    
    public void keyReleased(final KeyEvent event) {
    }
}
