// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.modes;

import graphStructure.Location;
import userInterface.GraphEditor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class MoveListener extends GraphEditorListener
{
    private Location dragStartLocation;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private boolean dragged;
    private boolean dragging;
    
    public MoveListener(final GraphEditorListener listener) {
        super(listener);
        this.graph.setDrawSelected(false);
        this.editor.changeToMoveCursor();
        this.dragging = false;
        this.dragged = false;
    }
    
    public boolean isMoveListener() {
        return true;
    }
    
    public void mousePressed(final MouseEvent event) {
        if (event.getPoint().x >= GraphEditor.DRAW_BUFFER && event.getPoint().x <= this.editor.getWidth() - GraphEditor.DRAW_BUFFER && event.getPoint().y >= GraphEditor.DRAW_BUFFER && event.getPoint().y <= this.editor.getHeight() - GraphEditor.DRAW_BUFFER && event.getButton() != 3) {
            this.dragStartLocation = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
            final Rectangle2D.Double bounds = this.graph.getBounds();
            this.maxX = (int)bounds.getMaxX();
            this.minX = (int)bounds.getMinX();
            this.maxY = (int)bounds.getMaxY();
            this.minY = (int)bounds.getMinY();
            this.graph.newMemento("Move Graph");
            this.graph.translate(0, 0, true);
            this.editor.startTranslate();
            this.dragging = true;
        }
    }
    
    public void mouseDragged(final MouseEvent event) {
        super.mouseMoved(event);
        if (this.dragging) {
            final Location ePoint = new Location(event.getPoint().x - GraphEditor.DRAW_BUFFER, event.getPoint().y - GraphEditor.DRAW_BUFFER);
            int transX = ePoint.intX() - this.dragStartLocation.intX();
            int transY = ePoint.intY() - this.dragStartLocation.intY();
            this.dragStartLocation = ePoint;
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
                this.dragged = true;
                this.controller.newUndo();
                this.editor.translate(transX, transY);
                this.editor.setPreferredSize();
                this.editor.repaint();
            }
        }
    }
    
    public void mouseReleased(final MouseEvent event) {
        this.dragStartLocation = null;
        if (this.dragging) {
            if (this.dragged) {
                this.graph.doneMemento();
                this.controller.newUndo();
            }
            else {
                this.graph.abortMemento();
            }
            this.dragged = false;
            this.editor.stopTranslate();
            this.editor.repaint();
            this.dragging = false;
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
