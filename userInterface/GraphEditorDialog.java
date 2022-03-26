// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

public abstract class GraphEditorDialog extends JGraphEdInternalFrame
{
    public GraphEditorDialog(final GraphController controller, final String title, final boolean resizable, final boolean closeable, final boolean maximizable, final boolean iconifiable) {
        super(controller, title, resizable, closeable, maximizable, iconifiable);
    }
    
    public abstract GraphEditorWindow getOwner();
    
    public abstract void setOwner(final GraphEditorWindow p0);
    
    public abstract void enableRunButton();
    
    public abstract void disableRunButton();
}
