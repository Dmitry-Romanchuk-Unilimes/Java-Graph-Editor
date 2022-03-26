// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.fileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

public class ImageFileView extends FileView
{
    ImageIcon jpgIcon;
    ImageIcon gifIcon;
    ImageIcon tiffIcon;
    
    public ImageFileView() {
        this.jpgIcon = new ImageIcon("ajpgIcon.gif");
        this.gifIcon = new ImageIcon("agifIcon.gif");
        this.tiffIcon = new ImageIcon("atiffIcon.gif");
    }
    
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
        if (extension != null) {
            if (extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg")) {
                type = "JPEG Image";
            }
            else if (extension.equalsIgnoreCase("gif")) {
                type = "GIF Image";
            }
            else if (extension.equalsIgnoreCase("tiff") || extension.equalsIgnoreCase("tif")) {
                type = "TIFF Image";
            }
        }
        return type;
    }
    
    public Icon getIcon(final File f) {
        final String extension = Utils.getExtension(f);
        Icon icon = null;
        if (extension != null) {
            if (extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg")) {
                icon = this.jpgIcon;
            }
            else if (extension.equalsIgnoreCase("gif")) {
                icon = this.gifIcon;
            }
            else if (extension.equalsIgnoreCase("tiff") || extension.equalsIgnoreCase("tif")) {
                icon = this.tiffIcon;
            }
        }
        return icon;
    }
}
