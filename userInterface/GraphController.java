// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import graphException.GraphException;
import graphStructure.Graph;
import graphStructure.Node;
import operation.*;
import userInterface.fileUtils.GraphFileView;
import userInterface.fileUtils.GraphFilter;
import userInterface.fileUtils.ImageFilter;
import userInterface.fileUtils.Utils;
import userInterface.menuAndToolBar.MenuAndToolBar;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Vector;

public class GraphController implements InternalFrameListener
{
    private static String DEFAULT_RANDOM_NODES;
    private GraphWindow graphWindow;
    private GraphEditorWindow activeGraphEditorWindow;
    private GraphEditorDialog activeGraphEditorDialog;
    private MenuAndToolBar menuAndToolBar;
    private boolean isApplication;
    private JInternalFrame lastWindow;
    private int lastShownIndex;
    private boolean drawOnEmbedding;
    private boolean clearGenerated;
    
    static {
        GraphController.DEFAULT_RANDOM_NODES = "10";
    }
    
    public GraphController(final boolean isApplication) {
        this.isApplication = isApplication;
        this.activeGraphEditorWindow = null;
        this.activeGraphEditorDialog = null;
        this.lastWindow = null;
        this.lastShownIndex = 0;
        this.drawOnEmbedding = true;
        this.clearGenerated = false;
        this.menuAndToolBar = new MenuAndToolBar(this);
        this.graphWindow = new GraphWindow();
    }
    
    public void setDrawOnEmbedding(final boolean drawEmbed) {
        this.drawOnEmbedding = drawEmbed;
    }
    
    public boolean getDrawOnEmbedding() {
        return this.drawOnEmbedding;
    }
    
    public void toggleDrawOnEmbedding() {
        this.drawOnEmbedding = !this.drawOnEmbedding;
    }
    
    public void setClearGenerated(final boolean clearGen) {
        this.clearGenerated = clearGen;
    }
    
    public boolean getClearGenerated() {
        return this.clearGenerated;
    }
    
    public void toggleClearGenerated() {
        this.clearGenerated = !this.clearGenerated;
    }
    
    public GraphWindow getGraphWindow() {
        return this.graphWindow;
    }
    
    public GraphEditorWindow getActiveGraphEditor() {
        return this.activeGraphEditorWindow;
    }
    
    public boolean isApplication() {
        return this.isApplication;
    }
    
    public void updateCursorLocation(final Point cursorPoint) {
        this.menuAndToolBar.updateCursorLocation(cursorPoint);
    }
    
    public JToolBar getToolBar() {
        return this.menuAndToolBar.getToolBar();
    }
    
    public JMenuBar getMenuBar() {
        return this.menuAndToolBar.getMenuBar();
    }
    
    public void internalFrameOpened(final InternalFrameEvent e) {
        this.menuAndToolBar.addWindow(((JGraphEdInternalFrame)e.getSource()).getMenuItem());
    }
    
    public void internalFrameClosed(final InternalFrameEvent e) {
        this.menuAndToolBar.removeWindow(((JGraphEdInternalFrame)e.getSource()).getMenuItem());
        this.lastWindow = null;
        if (e.getSource() instanceof GraphEditorWindow) {
            final GraphEditorWindow gew = (GraphEditorWindow)e.getSource();
            if (gew.getDialog() != null) {
                final GraphEditorDialog ged = gew.getDialog();
                gew.setDialog(null);
                ged.setOwner(null);
                this.graphWindow.close(ged);
            }
            if (gew.getInfoWindow().isVisible()) {
                this.graphWindow.closeInfo(gew.getInfoWindow());
            }
            if (gew.getLogWindow().isVisible()) {
                this.graphWindow.closeLog(gew.getLogWindow());
            }
        }
        else if (e.getSource() instanceof GraphEditorDialog) {
            final GraphEditorDialog ged2 = (GraphEditorDialog)e.getSource();
            if (ged2.getOwner() != null) {
                ged2.getOwner().getGraphEditor().allowNodeSelection(0);
                ged2.getOwner().setDialog(null);
                this.graphWindow.activate(ged2.getOwner());
            }
        }
    }
    
    public void internalFrameActivated(final InternalFrameEvent e) {
        if (e.getSource() instanceof GraphEditorWindow) {
            this.activeGraphEditorWindow = (GraphEditorWindow)e.getSource();
            if (this.activeGraphEditorWindow.getDialog() != null && this.activeGraphEditorWindow.getDialog() != this.lastWindow) {
                this.lastWindow = this.activeGraphEditorWindow;
                this.graphWindow.activateDialog(this.activeGraphEditorWindow.getDialog());
            }
            else {
                this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
                this.lastWindow = this.activeGraphEditorWindow;
            }
        }
        else {
            if (e.getSource() instanceof GraphEditorDialog) {
                this.activeGraphEditorDialog = (GraphEditorDialog)e.getSource();
                if (this.lastWindow != this.activeGraphEditorDialog.getOwner()) {
                    this.graphWindow.activate(this.activeGraphEditorDialog.getOwner());
                    this.activeGraphEditorDialog = (GraphEditorDialog)e.getSource();
                    this.graphWindow.activateDialog(this.activeGraphEditorDialog);
                }
                this.lastWindow = this.activeGraphEditorDialog;
            }
            this.menuAndToolBar.hideControls();
        }
    }
    
    public void internalFrameDeactivated(final InternalFrameEvent e) {
        this.menuAndToolBar.hideControls();
        if (e.getSource() instanceof GraphEditorWindow && this.activeGraphEditorWindow == e.getSource()) {
            this.activeGraphEditorWindow = null;
        }
        if (e.getSource() instanceof GraphEditorDialog && this.activeGraphEditorDialog == e.getSource()) {
            this.activeGraphEditorDialog = null;
        }
    }
    
    public void internalFrameClosing(final InternalFrameEvent e) {
        if (e.getSource() instanceof GraphEditorWindow) {
            final GraphEditorWindow editorWindow = (GraphEditorWindow)e.getSource();
            if (editorWindow != null) {
                if (editorWindow.getGraphEditor().getGraph().hasChangedSinceLastSave()) {
                    final int returnInt = JOptionPane.showConfirmDialog(this.graphWindow, "Are you sure you want to discard all changes to " + editorWindow.getTitle() + "?", "Discard Changes", 0);
                    if (returnInt == 0) {
                        editorWindow.dispose();
                    }
                }
                else {
                    editorWindow.dispose();
                }
            }
        }
    }
    
    public void internalFrameDeiconified(final InternalFrameEvent e) {
    }
    
    public void internalFrameIconified(final InternalFrameEvent e) {
    }
    
    public MenuAndToolBar getMenuAndToolBar() {
        return this.menuAndToolBar;
    }
    
    public void showWindow(final JGraphEdInternalFrame intFrame) {
        this.graphWindow.activate(intFrame);
    }
    
    public boolean hasUnsavedGraphs() {
        final Vector graphEditorWindows = this.graphWindow.getAllGraphEditorWindows();
        for (int i = 0; i < graphEditorWindows.size(); ++i) {
            if (graphEditorWindows.elementAt(i).getGraphEditor().getGraph().hasChangedSinceLastSave()) {
                return true;
            }
        }
        return false;
    }
    
    public void nodesSelectedByEditor(final int numNodes, final int maxNodes) {
        if (this.activeGraphEditorWindow != null && this.activeGraphEditorWindow.getDialog() != null) {
            final GraphEditorDialog ged = this.activeGraphEditorWindow.getDialog();
            if (numNodes == maxNodes) {
                ged.enableRunButton();
                this.activeGraphEditorWindow.getGraphEditor().repaint();
                this.graphWindow.activateDialog(ged);
            }
            else {
                ged.disableRunButton();
                this.activeGraphEditorWindow.getGraphEditor().repaint();
            }
        }
    }
    
    public void update() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().updateShapes();
        }
        else if (this.activeGraphEditorDialog != null) {
            this.activeGraphEditorDialog.getOwner().getGraphEditor().updateShapes();
        }
        this.graphWindow.repaint();
    }
    
    public void newGraph() {
        this.graphWindow.addGraphEditorWindow(this);
    }
    
    public void loadGraph() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new GraphFilter());
        fc.setFileView(new GraphFileView());
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        final int returnVal = fc.showDialog(this.graphWindow, "Load GRAPH File");
        if (returnVal == 0) {
            String loadGraphPath = "";
            try {
                loadGraphPath = fc.getSelectedFile().getCanonicalPath();
                final BufferedReader aReader = new BufferedReader(new FileReader(loadGraphPath));
                final Graph graph = Graph.loadFrom(aReader);
                graph.setFilePath(loadGraphPath);
                aReader.close();
                this.graphWindow.addGraphEditorWindow(this, graph);
            }
            catch (IOException ioe) {
                JOptionPane.showMessageDialog(this.graphWindow, "The file you selected does not appear to be a valid GRAPH file.", "Unable to load graph file", 0);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this.graphWindow, String.valueOf(loadGraphPath) + " does not appear to be a valid GRAPH file.", "Unable to load graph file", 0);
                ex.printStackTrace();
            }
        }
    }
    
    public void saveGraph() {
        if (this.activeGraphEditorWindow != null) {
            final JFileChooser fc = new JFileChooser();
            final GraphFilter graphFilter = new GraphFilter();
            final ImageFilter imageFilter = new ImageFilter();
            fc.addChoosableFileFilter(graphFilter);
            fc.addChoosableFileFilter(imageFilter);
            fc.setFileFilter(graphFilter);
            fc.setFileView(new GraphFileView());
            fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
            final int returnVal = fc.showDialog(this.graphWindow, "Save Current Graph to File");
            if (returnVal == 0) {
                String savePath = "";
                try {
                    savePath = fc.getSelectedFile().getCanonicalPath();
                    final String fileExt = Utils.getExtension(fc.getSelectedFile());
                    if (fc.getFileFilter() == graphFilter) {
                        if (fileExt != null && fileExt.equalsIgnoreCase("graph")) {
                            final File aFile = new File(savePath);
                            if (aFile.exists()) {
                                final int returnInt = JOptionPane.showConfirmDialog(this.graphWindow, "Do you wish to Overwrite the file " + savePath + "?", "File Already Exists", 0);
                                if (returnInt == 0) {
                                    final PrintWriter aWriter = new PrintWriter(new FileWriter(savePath));
                                    this.activeGraphEditorWindow.getGraphEditor().getGraph().saveTo(aWriter);
                                    aWriter.close();
                                }
                            }
                            else {
                                final PrintWriter aWriter2 = new PrintWriter(new FileWriter(savePath));
                                this.activeGraphEditorWindow.getGraphEditor().getGraph().saveTo(aWriter2);
                                aWriter2.close();
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(this.graphWindow, "Graphs may only be saved as .graph files.", "Unable to Save Graph", 0);
                        }
                    }
                    else if (fc.getFileFilter() == imageFilter) {
                        if (fileExt != null && (fileExt.equalsIgnoreCase("gif") || fileExt.equalsIgnoreCase("jpg") || fileExt.equalsIgnoreCase("jpeg"))) {
                            final File aFile = new File(savePath);
                            if (aFile.exists()) {
                                final int returnInt = JOptionPane.showConfirmDialog(this.graphWindow, "Do you wish to Overwrite the file " + savePath + "?", "File Already Exists", 0);
                                if (returnInt == 0) {
                                    this.activeGraphEditorWindow.getGraphEditor().saveImage(savePath);
                                }
                            }
                            else {
                                this.activeGraphEditorWindow.getGraphEditor().saveImage(savePath);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(this.graphWindow, "Images may only be saved as .gif or .jpg or .jpeg files.", "Unable to Save Graph Image", 0);
                        }
                    }
                }
                catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this.graphWindow, "Unable to write to the selected file.", "Unable to Save Graph", 0);
                }
            }
        }
    }
    
    public void closeGraph() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.doDefaultCloseAction();
        }
    }
    
    public void preferences() {
        this.graphWindow.show(new GraphEditorPreferencesWindow(this));
    }
    
    public void help() {
        this.graphWindow.show(new GraphEditorHelpWindow(this));
    }
    
    public void info() {
        this.graphWindow.showInfo(this.activeGraphEditorWindow);
    }
    
    public void log() {
        this.graphWindow.showLog(this.activeGraphEditorWindow);
    }
    
    public void editMode() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().changeToEditMode();
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
        }
    }
    
    public void gridMode() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().changeToGridMode();
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
        }
    }
    
    public void moveMode() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().changeToMoveMode();
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
            this.graphWindow.repaint();
        }
    }
    
    public void rotateMode() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().changeToRotateMode();
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
        }
    }
    
    public void resizeMode() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().changeToResizeMode();
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
        }
    }
    
    public void toggleUndo() {
        if (this.activeGraphEditorWindow != null) {
            final Graph graph = this.activeGraphEditorWindow.getGraphEditor().getGraph();
            if (graph.getTrackUndos()) {
                graph.setTrackUndos(false);
            }
            else {
                graph.setTrackUndos(true);
            }
            this.menuAndToolBar.updateUndo(this.activeGraphEditorWindow.getGraphEditor().getGraph());
        }
    }
    
    public void undo() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().undo();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void redo() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().redo();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void newUndo() {
        if (this.activeGraphEditorWindow != null) {
            this.menuAndToolBar.updateUndo(this.activeGraphEditorWindow.getGraphEditor().getGraph());
        }
    }
    
    public void unselectAll() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().unselectAll();
            this.activeGraphEditorWindow.getGraphEditor().repaint();
        }
    }
    
    public void removeSelected() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Remove Selected");
            this.activeGraphEditorWindow.getGraphEditor().getGraph().deleteSelected();
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void removeAll() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Remove All");
            this.activeGraphEditorWindow.getGraphEditor().getGraph().deleteAll();
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void removeGenerated() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Remove Generated Edges");
            this.activeGraphEditorWindow.getGraphEditor().getGraph().deleteGeneratedEdges();
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void preserveGenerated() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Make Generated Edges Permanent");
            this.activeGraphEditorWindow.getGraphEditor().getGraph().makeGeneratedEdgesPermanent();
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void testConnectivity() {
        if (this.activeGraphEditorWindow != null) {
            if (!this.activeGraphEditorWindow.getGraphEditor().getGraph().hasNodes()) {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Has No Nodes", "Connectivity Test Results", 1);
            }
            else if (ConnectivityOperation.isConnected(this.activeGraphEditorWindow.getGraphEditor().getGraph())) {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Is Connected", "Connectivity Test Results", 1);
            }
            else {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Is Not Connected", "Connectivity Test Results", 1);
            }
        }
    }
    
    public void testBiconnectivity() {
        if (this.activeGraphEditorWindow != null) {
            if (!this.activeGraphEditorWindow.getGraphEditor().getGraph().hasNodes()) {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Has No Nodes", "Biconnectivity Test Results", 1);
            }
            else if (BiconnectivityOperation.isBiconnected(this.activeGraphEditorWindow.getGraphEditor().getGraph())) {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Is Biconnected", "Biconnectivity Test Results", 1);
            }
            else {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Is Not Biconnected", "Biconnectivity Test Results", 1);
            }
        }
    }
    
    public void testPlanarity() {
        if (this.activeGraphEditorWindow != null) {
            if (!this.activeGraphEditorWindow.getGraphEditor().getGraph().hasNodes()) {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Has No Nodes.", "Planarity Test Results", 1);
            }
            else if (PlanarityOperation.isPlanar(this.activeGraphEditorWindow.getGraphEditor().getGraph())) {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Is Planar", "Planarity Test Results", 1);
            }
            else {
                JOptionPane.showMessageDialog(this.graphWindow, "The Graph Is Not Planar", "Planarity Test Results", 1);
            }
        }
    }
    
    public void createRandom() {
        if (this.activeGraphEditorWindow != null) {
            try {
                final String numberString = (String)JOptionPane.showInputDialog(this.graphWindow, "Enter the number of random nodes to create", "Create X Random Nodes", -1, null, null, GraphController.DEFAULT_RANDOM_NODES);
                try {
                    if (numberString != null) {
                        final int number = Integer.parseInt(numberString);
                        if (number < 0) {
                            throw new NumberFormatException("Value must be positive");
                        }
                        this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Create Random");
                        CreateRandomGraphOperation.createRandomNodes(this.activeGraphEditorWindow.getGraphEditor().getGraph(), number, this.activeGraphEditorWindow.getGraphEditor().getDrawWidth(), this.activeGraphEditorWindow.getGraphEditor().getDrawHeight());
                        this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
                        this.newUndo();
                        this.activeGraphEditorWindow.getGraphEditor().update();
                    }
                }
                catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this.graphWindow, "Please enter a positive whole number for the number of random nodes to create", "Invalid Input", 0);
                }
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Create Random Operation", 0);
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void embedding() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Embed");
                EmbedOperation.embed(this.activeGraphEditorWindow.getGraphEditor().getGraph());
                this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
                this.newUndo();
                this.activeGraphEditorWindow.getGraphEditor().repaint();
                JOptionPane.showMessageDialog(this.graphWindow, "Hold down Control (or Control-Shift) and click on a Node or Edge", "Embedding", 1);
            }
            catch (Exception e) {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Embedding Operation", 0);
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void makeConnected() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Make Connected");
                ConnectivityOperation.makeConnected(this.activeGraphEditorWindow.getGraphEditor().getGraph());
                this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
                this.newUndo();
                this.activeGraphEditorWindow.getGraphEditor().update();
            }
            catch (Exception e) {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Make Connected Operation", 0);
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void makeBiconnected() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Make Biconnected");
                BiconnectivityOperation.makeBiconnected(this.activeGraphEditorWindow.getGraphEditor().getGraph());
                this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
                this.newUndo();
                this.activeGraphEditorWindow.getGraphEditor().update();
            }
            catch (Exception e) {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Make Biconnected Operation", 0);
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void makeMaximal() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Make Maximal");
                MakeMaximalOperation.makeMaximal(this.activeGraphEditorWindow.getGraphEditor().getGraph());
                this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
                this.newUndo();
                this.activeGraphEditorWindow.getGraphEditor().update();
            }
            catch (Exception e) {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Make Maximal Operation", 0);
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void straightLineEmbed() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Straight Line Embed");
                SchnyderEmbeddingOperation.straightLineGridEmbed(this.activeGraphEditorWindow.getGraphEditor().getGraph(), this.activeGraphEditorWindow.getGraphEditor().getDrawWidth(), this.activeGraphEditorWindow.getGraphEditor().getDrawHeight());
                if (this.clearGenerated) {
                    this.activeGraphEditorWindow.getGraphEditor().getGraph().deleteGeneratedEdges();
                }
                this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
                this.newUndo();
                this.activeGraphEditorWindow.getGraphEditor().setPreferredSize();
                this.activeGraphEditorWindow.getGraphEditor().update();
            }
            catch (Exception e) {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Straight Line Embedding Operation", 0);
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void showCoords() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().setShowCoords(true);
            this.activeGraphEditorWindow.getGraphEditor().getGraph().setShowLabels(false);
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
            this.activeGraphEditorWindow.repaint();
        }
    }
    
    public void showLabels() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().setShowCoords(false);
            this.activeGraphEditorWindow.getGraphEditor().getGraph().setShowLabels(true);
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
            this.activeGraphEditorWindow.repaint();
        }
    }
    
    public void showNothing() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().setShowCoords(false);
            this.activeGraphEditorWindow.getGraphEditor().getGraph().setShowLabels(false);
            this.menuAndToolBar.showControls(this.activeGraphEditorWindow.getGraphEditor());
            this.activeGraphEditorWindow.repaint();
        }
    }
    
    public void resetDisplay() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Reset To Default Display");
            this.activeGraphEditorWindow.getGraphEditor().getGraph().resetColors(true);
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void displayDFS() {
        if (this.activeGraphEditorWindow != null) {
            this.showLabels();
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Display Depth First Search");
            DepthFirstSearchOperation.displayDepthFirstSearch(this.activeGraphEditorWindow.getGraphEditor().getGraph());
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void displayBiconnected() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Display Biconnected Components");
            BiconnectivityOperation.displayBiconnectedComponents(this.activeGraphEditorWindow.getGraphEditor().getGraph());
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void displayST() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.showLabels();
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Display ST Numbering");
                STNumberOperation.displayStNumbering(this.activeGraphEditorWindow.getGraphEditor().getGraph());
                this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
                this.newUndo();
                this.activeGraphEditorWindow.getGraphEditor().update();
            }
            catch (Exception e) {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During ST Number Display", 0);
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void displayCanonical() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Make Maximal");
                if (!MakeMaximalOperation.makeMaximal(this.activeGraphEditorWindow.getGraphEditor().getGraph())) {
                    EmbedOperation.embed(this.activeGraphEditorWindow.getGraphEditor().getGraph(), false);
                }
                this.activeGraphEditorWindow.getGraphEditor().update();
                if (this.activeGraphEditorWindow.getDialog() != null) {
                    this.graphWindow.close(this.activeGraphEditorWindow.getDialog());
                }
                final SchnyderDialog ged = new SchnyderDialog(this, this.activeGraphEditorWindow, "Canonical Order Display", "Please Select 3 Nodes to Bound the Outer-Face/Triangle", true, true) {
                    public void actionPerformed(final ActionEvent e) {
                        this.getOwner().getGraphEditor().getGraph().renameMemento("Display Canonical Ordering");
                        GraphController.this.displayCanonicalHelper(this.getOwner(), e.getSource() == this.getRandomButton(), this.getOnEmbedding());
                    }
                    
                    public void dispose() {
                        if (this.getOwner() != null) {
                            this.getOwner().getGraphEditor().getGraph().doneMemento();
                            GraphController.this.newUndo();
                        }
                        super.dispose();
                    }
                };
                ged.disableRunButton();
                this.activeGraphEditorWindow.getGraphEditor().allowTriangleSelection();
                this.activeGraphEditorWindow.setDialog(ged);
                this.graphWindow.showDialog(ged);
            }
            catch (Exception e) {
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Canonical Order Display", 0);
            }
        }
    }
    
    private void displayCanonicalHelper(final GraphEditorWindow editorWindow, final boolean useRandom, final boolean onEmbedding) {
        try {
            this.showLabels();
            Node[] triangleNodes;
            if (useRandom) {
                triangleNodes = editorWindow.getGraphEditor().getGraph().getRandomTriangularFace();
            }
            else {
                triangleNodes = editorWindow.getGraphEditor().getSpecialNodeSelections();
            }
            if (onEmbedding) {
                final int gridNum = editorWindow.getGraphEditor().getGraph().getNumNodes() - 1;
                SchnyderEmbeddingOperation.displayCanonicalOrdering(editorWindow.getGraphEditor().getGraph(), triangleNodes[0], triangleNodes[1], triangleNodes[2], editorWindow.getGraphEditor().getDrawWidth(), editorWindow.getGraphEditor().getDrawHeight());
            }
            else {
                CanonicalOrderOperation.displayCanonicalOrdering(editorWindow.getGraphEditor().getGraph(), triangleNodes[0], triangleNodes[1], triangleNodes[2]);
            }
            editorWindow.getGraphEditor().update();
            editorWindow.getGraphEditor().allowNodeSelection(0);
            this.graphWindow.close(editorWindow.getDialog());
            editorWindow.setDialog(null);
        }
        catch (Exception e) {
            if (!(e instanceof GraphException)) {
                e.printStackTrace();
            }
            this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
            JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Canonical Order Display", 0);
        }
    }
    
    public void displayNormal() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Make Maximal");
                if (!MakeMaximalOperation.makeMaximal(this.activeGraphEditorWindow.getGraphEditor().getGraph())) {
                    EmbedOperation.embed(this.activeGraphEditorWindow.getGraphEditor().getGraph(), false);
                }
                this.activeGraphEditorWindow.getGraphEditor().update();
                if (this.activeGraphEditorWindow.getDialog() != null) {
                    this.graphWindow.close(this.activeGraphEditorWindow.getDialog());
                }
                final SchnyderDialog ged = new SchnyderDialog(this, this.activeGraphEditorWindow, "Normal Labeling Display", "Please Select 3 Nodes to Bound the Outer-Face/Triangle", true, true) {
                    public void actionPerformed(final ActionEvent e) {
                        this.getOwner().getGraphEditor().getGraph().renameMemento("Display Normal Labeling");
                        GraphController.this.displayNormalHelper(this.getOwner(), e.getSource() == this.getRandomButton(), this.getOnEmbedding());
                    }
                    
                    public void dispose() {
                        if (this.getOwner() != null) {
                            this.getOwner().getGraphEditor().getGraph().doneMemento();
                            GraphController.this.newUndo();
                        }
                        super.dispose();
                    }
                };
                ged.disableRunButton();
                this.activeGraphEditorWindow.getGraphEditor().allowTriangleSelection();
                this.activeGraphEditorWindow.setDialog(ged);
                this.graphWindow.showDialog(ged);
            }
            catch (Exception e) {
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Normal Label Display", 0);
            }
        }
    }
    
    private void displayNormalHelper(final GraphEditorWindow editorWindow, final boolean useRandom, final boolean onEmbedding) {
        try {
            Node[] triangleNodes;
            if (useRandom) {
                triangleNodes = editorWindow.getGraphEditor().getGraph().getRandomTriangularFace();
            }
            else {
                triangleNodes = editorWindow.getGraphEditor().getSpecialNodeSelections();
            }
            if (onEmbedding) {
                SchnyderEmbeddingOperation.displayNormalLabeling(editorWindow.getGraphEditor().getGraph(), triangleNodes[0], triangleNodes[1], triangleNodes[2], editorWindow.getGraphEditor().getDrawWidth(), editorWindow.getGraphEditor().getDrawHeight());
            }
            else {
                NormalLabelOperation.displayNormalLabeling(editorWindow.getGraphEditor().getGraph(), triangleNodes[0], triangleNodes[1], triangleNodes[2]);
            }
            editorWindow.getGraphEditor().update();
            editorWindow.getGraphEditor().allowNodeSelection(0);
            this.graphWindow.close(editorWindow.getDialog());
            editorWindow.setDialog(null);
        }
        catch (Exception e) {
            if (!(e instanceof GraphException)) {
                e.printStackTrace();
            }
            this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
            JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Normal Label Display", 0);
        }
    }
    
    public void displaySchnyder() {
        if (this.activeGraphEditorWindow != null) {
            try {
                this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Make Maximal");
                if (!MakeMaximalOperation.makeMaximal(this.activeGraphEditorWindow.getGraphEditor().getGraph())) {
                    EmbedOperation.embed(this.activeGraphEditorWindow.getGraphEditor().getGraph(), false);
                }
                this.activeGraphEditorWindow.getGraphEditor().update();
                if (this.activeGraphEditorWindow.getDialog() != null) {
                    this.graphWindow.close(this.activeGraphEditorWindow.getDialog());
                }
                final SchnyderDialog ged = new SchnyderDialog(this, this.activeGraphEditorWindow, "Schnyder Embedding Display", "Please Select 3 Nodes to Bound the Outer-Face/Triangle", false, true) {
                    public void actionPerformed(final ActionEvent e) {
                        this.getOwner().getGraphEditor().getGraph().renameMemento("Display Schnyder Embedding");
                        GraphController.this.displaySchnyderHelper(this.getOwner(), e.getSource() == this.getRandomButton());
                    }
                    
                    public void dispose() {
                        if (this.getOwner() != null) {
                            this.getOwner().getGraphEditor().getGraph().doneMemento();
                            GraphController.this.newUndo();
                        }
                        super.dispose();
                    }
                };
                ged.disableRunButton();
                this.activeGraphEditorWindow.getGraphEditor().allowTriangleSelection();
                this.activeGraphEditorWindow.setDialog(ged);
                this.graphWindow.showDialog(ged);
            }
            catch (Exception e) {
                if (!(e instanceof GraphException)) {
                    e.printStackTrace();
                }
                this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
                JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Schnyder Embedding Display", 0);
            }
        }
    }
    
    private void displaySchnyderHelper(final GraphEditorWindow editorWindow, final boolean useRandom) {
        try {
            Node[] triangleNodes;
            if (useRandom) {
                triangleNodes = editorWindow.getGraphEditor().getGraph().getRandomTriangularFace();
            }
            else {
                triangleNodes = editorWindow.getGraphEditor().getSpecialNodeSelections();
            }
            final int gridNum = editorWindow.getGraphEditor().getGraph().getNumNodes() - 2;
            SchnyderEmbeddingOperation.displayStraightLineGridEmbedding(editorWindow.getGraphEditor().getGraph(), triangleNodes[0], triangleNodes[1], triangleNodes[2], editorWindow.getGraphEditor().getDrawWidth(), editorWindow.getGraphEditor().getDrawHeight());
            editorWindow.getGraphEditor().setPreferredSize();
            editorWindow.getGraphEditor().update();
            editorWindow.getGraphEditor().allowNodeSelection(0);
            this.graphWindow.close(editorWindow.getDialog());
            editorWindow.setDialog(null);
        }
        catch (Exception e) {
            if (!(e instanceof GraphException)) {
                e.printStackTrace();
            }
            this.activeGraphEditorWindow.getGraphEditor().getGraph().abortMemento();
            JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Schnyder Embedding Display", 0);
        }
    }
    
    public void displayChanTree() {
        if (this.activeGraphEditorWindow != null) {
            if (this.activeGraphEditorWindow.getDialog() != null) {
                this.graphWindow.close(this.activeGraphEditorWindow.getDialog());
            }
            final ChanDialog ged = new ChanDialog(this, this.activeGraphEditorWindow, "Chan Tree Drawing Display", "<html>Please Select which Drawing Method to Use<br>and Select the Root Node of the Tree</html>") {
                public void actionPerformed(final ActionEvent e) {
                    GraphController.this.displayChanTreeHelper(this.getOwner(), this);
                }
            };
            ged.disableRunButton();
            this.activeGraphEditorWindow.getGraphEditor().allowNodeSelection(1);
            this.activeGraphEditorWindow.setDialog(ged);
            this.graphWindow.showDialog(ged);
        }
    }
    
    private void displayChanTreeHelper(final GraphEditorWindow editorWindow, final ChanDialog dialog) {
        try {
            editorWindow.getGraphEditor().getGraph().newMemento("Display Chan Tree Drawing");
            ChanTreeDrawOperation.displayChanTreeDrawing(editorWindow.getGraphEditor().getGraph(), editorWindow.getGraphEditor().getSpecialNodeSelections()[0], dialog.getSelectedMethodNumber(), editorWindow.getGraphEditor().getDrawWidth(), editorWindow.getGraphEditor().getDrawHeight());
            editorWindow.getGraphEditor().setPreferredSize();
            editorWindow.getGraphEditor().update();
            editorWindow.getGraphEditor().allowNodeSelection(0);
            this.graphWindow.close(editorWindow.getDialog());
            editorWindow.setDialog(null);
            editorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
        }
        catch (Exception e) {
            if (!(e instanceof GraphException)) {
                e.printStackTrace();
            }
            editorWindow.getGraphEditor().getGraph().abortMemento();
            JOptionPane.showMessageDialog(this.graphWindow, e.getMessage(), "Error During Chan Tree Drawing Display", 0);
        }
    }
    
    public void displayMST() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().getGraph().newMemento("Display Minimum Spanning Tree");
            MinimumSpanningTreeOperation.drawMinimumSpanningTree(this.activeGraphEditorWindow.getGraphEditor().getGraph());
            this.activeGraphEditorWindow.getGraphEditor().getGraph().doneMemento();
            this.newUndo();
            this.activeGraphEditorWindow.getGraphEditor().update();
        }
    }
    
    public void displayDijkstra() {
        if (this.activeGraphEditorWindow != null) {
            this.activeGraphEditorWindow.getGraphEditor().update();
            final SchnyderDialog ged = new SchnyderDialog(this, this.activeGraphEditorWindow, "Dijkstra Shortest Path", "Please Select a Source and Destination Node") {
                public void actionPerformed(final ActionEvent e) {
                    this.getOwner().getGraphEditor().getGraph().newMemento("Display Dijkstra Shortest Path");
                    GraphController.this.displayDijkstraHelper(this.getOwner());
                    this.getOwner().getGraphEditor().getGraph().doneMemento();
                    GraphController.this.newUndo();
                }
            };
            ged.disableRunButton();
            this.activeGraphEditorWindow.getGraphEditor().allowNodeSelection(2);
            this.activeGraphEditorWindow.setDialog(ged);
            this.graphWindow.showDialog(ged);
        }
    }
    
    private void displayDijkstraHelper(final GraphEditorWindow editorWindow) {
        final Node[] nodeSelections = editorWindow.getGraphEditor().getSpecialNodeSelections();
        DijkstraShortestPathOperation.drawShortestPath(editorWindow.getGraphEditor().getGraph(), nodeSelections[0], nodeSelections[1]);
        editorWindow.getGraphEditor().update();
        editorWindow.getGraphEditor().allowNodeSelection(0);
        this.graphWindow.close(editorWindow.getDialog());
        editorWindow.setDialog(null);
    }
}
