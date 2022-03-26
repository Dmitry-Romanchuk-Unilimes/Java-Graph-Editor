import userInterface.GraphController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

// 
// Decompiled by Procyon v0.5.36
// 

public class JGraphEdFrame extends JFrame implements WindowListener
{
    public static final int WIDTH = 865;
    public static final int HEIGHT = 600;
    private GraphController controller;
    
    public JGraphEdFrame() throws Exception {
        super("JGraphEd");
        this.controller = new GraphController(true);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.controller.getToolBar(), "North");
        this.getContentPane().add(this.controller.getGraphWindow(), "Center");
        this.setDefaultCloseOperation(0);
        this.setJMenuBar(this.controller.getMenuBar());
        this.addWindowListener(this);
        this.setSize(865, 600);
        this.setVisible(true);
    }
    
    public void windowClosing(final WindowEvent e) {
        if (this.controller.hasUnsavedGraphs()) {
            final int returnInt = JOptionPane.showConfirmDialog(this.controller.getGraphWindow(), "Are you sure you want to discard all changes to open graph editor windows ?", "Exit JGraphEd", 0);
            if (returnInt == 0) {
                System.exit(0);
            }
        }
        else {
            System.exit(0);
        }
    }
    
    public void windowActivated(final WindowEvent e) {
    }
    
    public void windowClosed(final WindowEvent e) {
    }
    
    public void windowDeactivated(final WindowEvent e) {
    }
    
    public void windowDeiconified(final WindowEvent e) {
    }
    
    public void windowIconified(final WindowEvent e) {
    }
    
    public void windowOpened(final WindowEvent e) {
    }
    
    public GraphController getController() {
        return this.controller;
    }
    
    public static void main(final String[] args) throws Exception {
        new JGraphEdFrame();
    }
}
