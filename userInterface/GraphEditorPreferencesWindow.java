// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import graphStructure.Node;
import userInterface.modes.EditListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphEditorPreferencesWindow extends JGraphEdInternalFrame implements ActionListener
{
    public static int WIDTH;
    public static int HEIGHT;
    private GraphController controller;
    private JCheckBox singleClickAddNodeBox;
    private JCheckBox addNodeOnEdgeDropBox;
    private JCheckBox drawOnEmbeddingBox;
    private JCheckBox clearGeneratedBox;
    private JCheckBox opaqueTextBox;
    
    static {
        GraphEditorPreferencesWindow.WIDTH = 400;
        GraphEditorPreferencesWindow.HEIGHT = 400;
    }
    
    public GraphEditorPreferencesWindow(final GraphController controller) {
        super(controller, "JGraphEd Preferences", true, true, true, false);
        this.controller = controller;
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.getContentPane().setLayout(layout);
        (this.singleClickAddNodeBox = new JCheckBox("Allow Single (as well as Double) Click To Add Nodes")).setSelected(EditListener.SINGLE_CLICK_ADD_NODE);
        this.singleClickAddNodeBox.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 2;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 0.0;
        layout.setConstraints(this.singleClickAddNodeBox, layoutCons);
        this.getContentPane().add(this.singleClickAddNodeBox);
        (this.addNodeOnEdgeDropBox = new JCheckBox("Create a New End Node if a new Edge has only one Node")).setSelected(EditListener.ADD_NODE_ON_EDGE_DROP);
        this.addNodeOnEdgeDropBox.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 2;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 0.0;
        layout.setConstraints(this.addNodeOnEdgeDropBox, layoutCons);
        this.getContentPane().add(this.addNodeOnEdgeDropBox);
        (this.drawOnEmbeddingBox = new JCheckBox("Default for Draw Canonical Order and Normal Label on Embedding")).setSelected(controller.getDrawOnEmbedding());
        this.drawOnEmbeddingBox.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 2;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 0.0;
        layout.setConstraints(this.drawOnEmbeddingBox, layoutCons);
        this.getContentPane().add(this.drawOnEmbeddingBox);
        (this.clearGeneratedBox = new JCheckBox("Clear Generated Edges after Straight Line Embedding")).setSelected(controller.getClearGenerated());
        this.clearGeneratedBox.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 2;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 0.0;
        layout.setConstraints(this.clearGeneratedBox, layoutCons);
        this.getContentPane().add(this.clearGeneratedBox);
        (this.opaqueTextBox = new JCheckBox("Draw Text Background on Top of Edges")).setSelected(Node.OPAQUE_TEXT);
        this.opaqueTextBox.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 2;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 0.0;
        layout.setConstraints(this.opaqueTextBox, layoutCons);
        this.getContentPane().add(this.opaqueTextBox);
        this.addInternalFrameListener(controller);
        this.setSize(GraphEditorPreferencesWindow.WIDTH, GraphEditorPreferencesWindow.HEIGHT);
        this.setVisible(true);
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.singleClickAddNodeBox) {
            EditListener.SINGLE_CLICK_ADD_NODE = !EditListener.SINGLE_CLICK_ADD_NODE;
        }
        else if (e.getSource() == this.addNodeOnEdgeDropBox) {
            EditListener.ADD_NODE_ON_EDGE_DROP = !EditListener.ADD_NODE_ON_EDGE_DROP;
        }
        else if (e.getSource() == this.drawOnEmbeddingBox) {
            this.controller.toggleDrawOnEmbedding();
        }
        else if (e.getSource() == this.clearGeneratedBox) {
            this.controller.toggleClearGenerated();
        }
        else if (e.getSource() == this.opaqueTextBox) {
            Node.OPAQUE_TEXT = !Node.OPAQUE_TEXT;
            this.controller.getGraphWindow().forceGraphRepaints();
        }
    }
}
