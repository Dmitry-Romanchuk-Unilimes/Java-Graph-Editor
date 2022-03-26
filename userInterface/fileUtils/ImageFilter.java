// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.fileUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImageFilter extends FileFilter
{
    public boolean accept(final File f) {
        if (f.isDirectory()) {
            return true;
        }
        final String extension = Utils.getExtension(f);
        return extension != null && (extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg"));
    }
    
    public String getDescription() {
        return "Image Files (GIF & JPG)";
    }
}
