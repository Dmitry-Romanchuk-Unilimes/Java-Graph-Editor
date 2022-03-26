// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import graphStructure.LogEntry;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.Vector;

public class GraphEditorLogWindow extends JGraphEdInternalFrame
{
    public static int WIDTH;
    public static int HEIGHT;
    private GraphController controller;
    private GraphEditorWindow editorWindow;
    private JTree logEntryTree;
    
    static {
        GraphEditorLogWindow.WIDTH = 400;
        GraphEditorLogWindow.HEIGHT = 400;
    }
    
    public GraphEditorLogWindow(final GraphController controller, final GraphEditorWindow editorWindow) {
        super(controller, String.valueOf(editorWindow.getTitle()) + " - Log", true, true, true, true);
        this.controller = controller;
        this.editorWindow = editorWindow;
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.getContentPane().setLayout(layout);
        (this.logEntryTree = new JTree()).setShowsRootHandles(true);
        final JScrollPane scrollPane = new JScrollPane(this.logEntryTree, 20, 30);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(scrollPane, layoutCons);
        this.getContentPane().add(scrollPane);
        this.update();
        this.addInternalFrameListener(controller);
        this.setSize(GraphEditorLogWindow.WIDTH, GraphEditorLogWindow.HEIGHT);
    }
    
    public void update() {
        if (this.isVisible()) {
            final Vector logEntries = this.editorWindow.getGraphEditor().getGraph().getLogEntries();
            if (logEntries.size() == 0) {
                this.logEntryTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("No Log Entries")));
            }
            else {
                final LogEntry root = new LogEntry();
                root.setSubEntries(logEntries);
                this.logEntryTree.setModel(new DefaultTreeModel(root));
                this.logEntryTree.setRootVisible(false);
            }
            this.logEntryTree.repaint();
            this.logEntryTree.validate();
        }
    }
}
