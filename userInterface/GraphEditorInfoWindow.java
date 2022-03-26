// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import graphStructure.Graph;
import operation.BiconnectivityOperation;
import operation.ConnectivityOperation;
import operation.PlanarityOperation;

import javax.swing.*;
import java.awt.*;

public class GraphEditorInfoWindow extends JGraphEdInternalFrame
{
    public static int WIDTH;
    public static int HEIGHT;
    private GraphController controller;
    private GraphEditorWindow editorWindow;
    private JLabel totalNodesLabel;
    private JLabel totalEdgesLabel;
    private JLabel generatedEdgesLabel;
    private JLabel curvedEdgesLabel;
    private JLabel planarLabel;
    private JLabel maximalPlanarLabel;
    private JLabel connectedCountLabel;
    private JLabel biconnectedCountLabel;
    
    static {
        GraphEditorInfoWindow.WIDTH = 400;
        GraphEditorInfoWindow.HEIGHT = 400;
    }
    
    public GraphEditorInfoWindow(final GraphController controller, final GraphEditorWindow editorWindow) {
        super(controller, String.valueOf(editorWindow.getTitle()) + " - Info", true, true, true, true);
        this.controller = controller;
        this.editorWindow = editorWindow;
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.getContentPane().setLayout(layout);
        this.totalNodesLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.totalNodesLabel, layoutCons);
        this.getContentPane().add(this.totalNodesLabel);
        this.totalEdgesLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.totalEdgesLabel, layoutCons);
        this.getContentPane().add(this.totalEdgesLabel);
        this.generatedEdgesLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.generatedEdgesLabel, layoutCons);
        this.getContentPane().add(this.generatedEdgesLabel);
        this.curvedEdgesLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.curvedEdgesLabel, layoutCons);
        this.getContentPane().add(this.curvedEdgesLabel);
        this.planarLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.planarLabel, layoutCons);
        this.getContentPane().add(this.planarLabel);
        this.maximalPlanarLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.maximalPlanarLabel, layoutCons);
        this.getContentPane().add(this.maximalPlanarLabel);
        this.connectedCountLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.connectedCountLabel, layoutCons);
        this.getContentPane().add(this.connectedCountLabel);
        this.biconnectedCountLabel = new JLabel();
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.biconnectedCountLabel, layoutCons);
        this.getContentPane().add(this.biconnectedCountLabel);
        this.update();
        this.addInternalFrameListener(controller);
        this.setSize(GraphEditorInfoWindow.WIDTH, GraphEditorInfoWindow.HEIGHT);
    }
    
    public void update() {
        if (this.isVisible()) {
            final Graph g = this.editorWindow.getGraphEditor().getGraph();
            this.totalNodesLabel.setText("Total Nodes: " + g.getNumNodes());
            this.totalEdgesLabel.setText("Total Edges: " + g.getNumEdges());
            this.generatedEdgesLabel.setText("Generated Edges: " + g.getNumGeneratedEdges());
            this.curvedEdgesLabel.setText("Curved Edges: " + g.getNumCurvedEdges());
            final boolean planar = PlanarityOperation.isPlanar(g);
            this.planarLabel.setText("Planar?: " + planar);
            this.maximalPlanarLabel.setText("Maximal Planar?: " + (planar && g.getNumEdges() == g.getNumNodes() * 3 - 6));
            this.connectedCountLabel.setText("Num Connected Components: " + ConnectivityOperation.getConnectedComponents(g).size());
            this.biconnectedCountLabel.setText("Num Biconnected Components: " + BiconnectivityOperation.getBiconnectedComponents(g).size());
        }
    }
}
