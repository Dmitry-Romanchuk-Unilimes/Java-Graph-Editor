// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class JGraphEdInternalFrame extends JInternalFrame
{
    protected JMenuItem menuItem;
    
    public JGraphEdInternalFrame(final GraphController controller, final String title, final boolean resizable, final boolean closable, final boolean maximizable, final boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        final JGraphEdInternalFrame thisCopy = this;
        this.menuItem = new JMenuItem(new JGraphEdInternalFrameAction(title, (ImageIcon)null, title, (Integer)null, true) {
            public void actionPerformed(final ActionEvent event) {
                controller.showWindow(thisCopy);
            }
        });
    }
    
    public JMenuItem getMenuItem() {
        return this.menuItem;
    }
}
