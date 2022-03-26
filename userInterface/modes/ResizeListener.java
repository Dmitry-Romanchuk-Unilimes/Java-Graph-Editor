// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.modes;

import userInterface.GraphEditor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class ResizeListener extends GraphEditorListener
{
    private boolean horizontalResize;
    private boolean verticalResize;
    private boolean dragging;
    private boolean dragged;
    private Point lastPoint;
    
    public ResizeListener(final GraphEditorListener listener) {
        super(listener);
        this.graph.setDrawSelected(false);
        final boolean b = false;
        this.verticalResize = b;
        this.horizontalResize = b;
        this.editor.changeToNormalCursor();
        this.dragging = false;
        this.dragged = false;
    }
    
    public boolean isResizeListener() {
        return true;
    }
    
    public void mousePressed(final MouseEvent event) {
        if (this.graph.getNumNodes() > 1 && event.getButton() != 3) {
            this.dragging = true;
            this.graph.newMemento("Resize Graph");
            this.graph.scaleTo(this.graph.getBounds(), true);
            this.editor.startScaleTo();
            this.lastPoint = event.getPoint();
        }
    }
    
    public void mouseDragged(final MouseEvent event) {
        super.mouseMoved(event);
        if (this.dragging && this.graph.getNumNodes() > 1) {
            final Rectangle2D.Double bounds = this.graph.getBounds();
            double width = bounds.width;
            double height = bounds.height;
            if (this.horizontalResize) {
                width -= this.lastPoint.x - event.getPoint().x;
            }
            if (this.verticalResize) {
                height -= this.lastPoint.y - event.getPoint().y;
            }
            final Rectangle2D.Double rect = new Rectangle2D.Double(bounds.getMinX(), bounds.getMinY(), width, height);
            this.lastPoint = event.getPoint();
            this.editor.scaleTo(rect);
            this.editor.updateShapes();
            this.editor.setPreferredSize();
            this.editor.repaint();
            this.dragged = true;
        }
    }
    
    public void mouseReleased(final MouseEvent event) {
        if (this.dragging) {
            if (this.graph.getNumNodes() > 1) {
                this.dragging = false;
                this.graph.updateEdgeCurveAngles();
                this.mouseMoved(event);
                this.editor.updateShapes();
                this.editor.setPreferredSize();
                this.editor.repaint();
            }
            if (this.dragged) {
                this.graph.doneMemento();
                this.controller.newUndo();
            }
            else {
                this.graph.abortMemento();
            }
            this.dragged = false;
            this.editor.stopScaleTo();
        }
    }
    
    public void mouseMoved(final MouseEvent event) {
        super.mouseMoved(event);
        if (!this.dragging && this.graph.getNumNodes() > 1) {
            final Rectangle2D.Double bounds = this.graph.getBounds();
            if (Math.abs((int)bounds.getMaxX() + GraphEditor.DRAW_BUFFER - event.getPoint().x) <= 1) {
                this.horizontalResize = true;
                if (Math.abs((int)bounds.getMaxY() + GraphEditor.DRAW_BUFFER - event.getPoint().y) <= 1) {
                    this.verticalResize = true;
                    this.editor.changeToDiagonalResizeCursor();
                }
                else {
                    this.verticalResize = false;
                    this.editor.changeToHorizontalResizeCursor();
                }
            }
            else {
                this.horizontalResize = false;
                if (Math.abs((int)bounds.getMaxY() + GraphEditor.DRAW_BUFFER - event.getPoint().y) <= 1) {
                    this.verticalResize = true;
                    this.editor.changeToVerticalResizeCursor();
                }
                else {
                    this.verticalResize = false;
                    this.editor.changeToNormalCursor();
                }
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
