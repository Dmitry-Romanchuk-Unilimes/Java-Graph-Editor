// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import graphStructure.Graph;

import javax.swing.*;
import java.awt.*;

public class GraphEditorWindow extends JGraphEdInternalFrame
{
    public static int WIDTH;
    public static int HEIGHT;
    private GraphController graphController;
    private GraphEditor graphEditor;
    private static int newCount;
    private GraphEditorDialog ged;
    private GraphEditorInfoWindow infoWindow;
    private GraphEditorLogWindow logWindow;
    
    static {
        GraphEditorWindow.WIDTH = 400;
        GraphEditorWindow.HEIGHT = 400;
        GraphEditorWindow.newCount = 0;
    }
    
    public GraphEditorWindow(final GraphController graphController, final Graph graph) {
        super(graphController, graph.getFileName(), true, true, true, true);
        if (graph.getLabel().length() == 0) {
            this.setTitle(graph.getFileName());
        }
        this.init(graphController);
        this.graphEditor.setGraph(graph);
    }
    
    public GraphEditorWindow(final GraphController graphController) {
        super(graphController, "Untitled " + ++GraphEditorWindow.newCount, true, true, true, true);
        this.init(graphController);
    }
    
    private void init(final GraphController graphController) {
        this.graphController = graphController;
        this.infoWindow = new GraphEditorInfoWindow(graphController, this);
        this.logWindow = new GraphEditorLogWindow(graphController, this);
        this.graphEditor = new GraphEditor(graphController, this.infoWindow, this.logWindow);
        this.ged = null;
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.getContentPane().setLayout(layout);
        final JScrollPane scrollPane = new JScrollPane(this.graphEditor, 20, 30);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 0;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(scrollPane, layoutCons);
        this.getContentPane().add(scrollPane);
        this.setDefaultCloseOperation(0);
        this.addInternalFrameListener(graphController);
        this.setSize(GraphEditorWindow.WIDTH, GraphEditorWindow.HEIGHT);
        this.setVisible(true);
    }
    
    public void dispose() {
        this.graphEditor.prepareForClose();
        super.dispose();
    }
    
    public GraphEditor getGraphEditor() {
        return this.graphEditor;
    }
    
    public GraphEditorDialog getDialog() {
        return this.ged;
    }
    
    public void setDialog(final GraphEditorDialog d) {
        this.ged = d;
    }
    
    public GraphEditorInfoWindow getInfoWindow() {
        return this.infoWindow;
    }
    
    public GraphEditorLogWindow getLogWindow() {
        return this.logWindow;
    }
}
