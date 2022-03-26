// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.fileUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class ImagePreview extends JComponent implements PropertyChangeListener
{
    ImageIcon thumbnail;
    File file;
    
    public ImagePreview(final JFileChooser fc) {
        this.thumbnail = null;
        this.file = null;
        this.setPreferredSize(new Dimension(100, 50));
        fc.addPropertyChangeListener(this);
    }
    
    public void loadImage() {
        if (this.file == null) {
            return;
        }
        final ImageIcon tmpIcon = new ImageIcon(this.file.getPath());
        if (tmpIcon.getIconWidth() > 90) {
            this.thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, 1));
        }
        else {
            this.thumbnail = tmpIcon;
        }
    }
    
    public void propertyChange(final PropertyChangeEvent e) {
        final String prop = e.getPropertyName();
        if (prop.equals("SelectedFileChangedProperty")) {
            this.file = (File)e.getNewValue();
            if (this.isShowing()) {
                this.loadImage();
                this.repaint();
            }
        }
    }
    
    public void paintComponent(final Graphics g) {
        if (this.thumbnail == null) {
            this.loadImage();
        }
        if (this.thumbnail != null) {
            int x = this.getWidth() / 2 - this.thumbnail.getIconWidth() / 2;
            int y = this.getHeight() / 2 - this.thumbnail.getIconHeight() / 2;
            if (y < 0) {
                y = 0;
            }
            if (x < 5) {
                x = 5;
            }
            this.thumbnail.paintIcon(this, g, x, y);
        }
    }
}
