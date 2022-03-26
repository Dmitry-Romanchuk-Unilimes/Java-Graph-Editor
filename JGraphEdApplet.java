import userInterface.GraphController;

import javax.swing.*;
import java.awt.*;

// 
// Decompiled by Procyon v0.5.36
// 

public class JGraphEdApplet extends JApplet
{
    public static final int WIDTH = 700;
    public static final int HEIGHT = 550;
    private GraphController controller;
    
    public void init() {
        this.controller = new GraphController(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.controller.getToolBar(), "North");
        this.getContentPane().add(this.controller.getGraphWindow(), "Center");
        this.setJMenuBar(this.controller.getMenuBar());
        this.setVisible(true);
    }
}
