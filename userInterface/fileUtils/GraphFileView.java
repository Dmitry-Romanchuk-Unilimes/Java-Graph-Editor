// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.fileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

public class GraphFileView extends FileView
{
    public String getName(final File f) {
        return null;
    }
    
    public String getDescription(final File f) {
        return null;
    }
    
    public Boolean isTraversable(final File f) {
        return null;
    }
    
    public String getTypeDescription(final File f) {
        final String extension = Utils.getExtension(f);
        String type = null;
        if (extension != null && extension.equalsIgnoreCase("graph")) {
            type = "Graph File";
        }
        return type;
    }
    
    public Icon getIcon(final File f) {
        final String extension = Utils.getExtension(f);
        final Icon icon = null;
        return icon;
    }
}
