// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.menuAndToolBar;

import javax.swing.*;
import java.awt.*;

public class CursorPositionThread extends Thread
{
    private final MenuAndToolBar menuAndToolbar;
    private Point lastCursorPoint;
    private boolean mouseIn;
    
    public CursorPositionThread(final MenuAndToolBar menuAndToolbar) {
        this.menuAndToolbar = menuAndToolbar;
        this.setPriority(2);
    }
    
    public void mouseIn(final boolean mouseIn) {
        this.mouseIn = mouseIn;
    }
    
    public void run() {
        while (this.mouseIn) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException ex) {}
            if (this.menuAndToolbar.getCursorPoint() != this.lastCursorPoint) {
                if (this.menuAndToolbar.getCursorPoint() == null) {
                    this.lastCursorPoint = null;
                    this.menuAndToolbar.setCursorLocationText("");
                }
                else {
                    this.lastCursorPoint = this.menuAndToolbar.getCursorPoint();
                    final Point cp = this.lastCursorPoint;
                    final Runnable updateController = new Runnable() {
                        public void run() {
                            CursorPositionThread.this.menuAndToolbar.setCursorLocationText("<html>x:" + cp.x + "<br>y:" + cp.y);
                        }
                    };
                    try {
                        SwingUtilities.invokeAndWait(updateController);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.menuAndToolbar.updateCursorLocation(null);
        this.menuAndToolbar.setCursorLocationText("");
    }
}
