// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.fileUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class GraphFilter extends FileFilter
{
    public boolean accept(final File f) {
        if (f.isDirectory()) {
            return true;
        }
        final String extension = Utils.getExtension(f);
        return extension != null && extension.equalsIgnoreCase("graph");
    }
    
    public static boolean acceptForSave(final File f) {
        final String extension = Utils.getExtension(f);
        return extension != null && extension.equalsIgnoreCase("graph");
    }
    
    public String getDescription() {
        return "GRAPH files";
    }
}
