// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import graphStructure.Edge;
import graphStructure.Graph;
import graphStructure.Location;
import graphStructure.Node;
import userInterface.fileUtils.GIFOutputStream;
import userInterface.modes.*;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.Vector;

public class GraphEditor extends JPanel
{
    private static Cursor rotateCursor;
    private static Cursor normalCursor;
    private static Cursor moveCursor;
    private static Cursor diagResizeCursor;
    private static Cursor horiResizeCursor;
    private static Cursor vertResizeCursor;
    public static Color borderColor;
    public static Color backgroundColor;
    public static int DRAW_BUFFER;
    private GraphEditorInfoWindow infoWindow;
    private GraphEditorLogWindow logWindow;
    private GraphEditorListener listener;
    private Line2D.Double lineToDraw;
    private Ellipse2D.Double pointToDraw;
    private Rectangle2D.Double rectangleToDraw;
    private Polygon polygonToDraw;
    private QuadCurve2D.Double curveToDraw;
    private Color lineToDrawColor;
    private Color polygonToDrawColor;
    private Color curveToDrawColor;
    private BufferedImage bufferedImage;
    private int imageOffsetX;
    private int imageOffsetY;
    private AffineTransform affineTransform;
    private boolean createImage;
    private Vector nodesToRedraw;
    private Vector edgesToRedraw;
    private Rectangle2D.Double oldBounds;
    private double angle;
    static /* synthetic */ Class class$0;
    
    static {
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final Toolkit defaultToolkit2 = Toolkit.getDefaultToolkit();
        Class class$0;
        if ((class$0 = GraphEditor.class$0) == null) {
            try {
                class$0 = (GraphEditor.class$0 = Class.forName("userInterface.GraphEditor"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        GraphEditor.rotateCursor = defaultToolkit.createCustomCursor(defaultToolkit2.getImage(class$0.getResource("/images/RotateCursor.gif")), new Point(16, 16), "Rotate");
        GraphEditor.normalCursor = Cursor.getPredefinedCursor(0);
        GraphEditor.moveCursor = Cursor.getPredefinedCursor(13);
        GraphEditor.diagResizeCursor = Cursor.getPredefinedCursor(5);
        GraphEditor.horiResizeCursor = Cursor.getPredefinedCursor(11);
        GraphEditor.vertResizeCursor = Cursor.getPredefinedCursor(9);
        GraphEditor.borderColor = Color.lightGray;
        GraphEditor.backgroundColor = Color.white;
        GraphEditor.DRAW_BUFFER = 15;
    }
    
    public GraphEditor(final GraphController controller, final GraphEditorInfoWindow infoWindow, final GraphEditorLogWindow logWindow) {
        final Graph newGraph = new Graph();
        newGraph.setGridArea(3, this.getDrawHeight(), 3, this.getDrawWidth(), false);
        this.initialize(controller, newGraph, infoWindow, logWindow);
    }
    
    public GraphEditor(final GraphController controller, final Graph g, final GraphEditorInfoWindow infoWindow, final GraphEditorLogWindow logWindow) {
        this.initialize(controller, g, infoWindow, logWindow);
    }
    
    public void changeToNormalCursor() {
        this.setCursor(GraphEditor.normalCursor);
    }
    
    public void changeToDiagonalResizeCursor() {
        this.setCursor(GraphEditor.diagResizeCursor);
    }
    
    public void changeToHorizontalResizeCursor() {
        this.setCursor(GraphEditor.horiResizeCursor);
    }
    
    public void changeToVerticalResizeCursor() {
        this.setCursor(GraphEditor.vertResizeCursor);
    }
    
    public void changeToRotateCursor() {
        this.setCursor(GraphEditor.rotateCursor);
    }
    
    public void changeToMoveCursor() {
        this.setCursor(GraphEditor.moveCursor);
    }
    
    private void initialize(final GraphController controller, final Graph g, final GraphEditorInfoWindow infoWindow, final GraphEditorLogWindow logWindow) {
        this.listener = new EditListener(g, this, controller);
        this.infoWindow = infoWindow;
        this.logWindow = logWindow;
        this.setBackground(GraphEditor.borderColor);
        this.setPreferredSize();
        this.lineToDraw = null;
        this.pointToDraw = null;
        this.polygonToDraw = null;
        this.curveToDraw = null;
        this.addEventHandlers();
        this.updateShapes();
        this.bufferedImage = null;
        this.createImage = true;
        this.nodesToRedraw = null;
        this.edgesToRedraw = null;
        this.affineTransform = null;
    }
    
    public void changeToEditMode() {
        if (!this.isInEditMode()) {
            this.removeEventHandlers();
            this.initShapes();
            this.listener = new EditListener(this.listener);
            this.addEventHandlers();
            this.updateShapes();
            this.repaint();
        }
    }
    
    public boolean isInEditMode() {
        return this.listener.isEditListener();
    }
    
    public void changeToMoveMode() {
        if (!this.isInMoveMode()) {
            this.removeEventHandlers();
            this.initShapes();
            this.listener = new MoveListener(this.listener);
            this.addEventHandlers();
            this.updateShapes();
            this.repaint();
        }
    }
    
    public boolean isInMoveMode() {
        return this.listener.isMoveListener();
    }
    
    public void changeToRotateMode() {
        if (!this.isInRotateMode()) {
            this.removeEventHandlers();
            this.initShapes();
            this.listener = new RotateListener(this.listener);
            this.addEventHandlers();
            this.updateShapes();
            this.repaint();
        }
    }
    
    public boolean isInRotateMode() {
        return this.listener.isRotateListener();
    }
    
    public void changeToResizeMode() {
        if (!this.isInResizeMode()) {
            this.removeEventHandlers();
            this.initShapes();
            this.listener = new ResizeListener(this.listener);
            this.addEventHandlers();
            this.updateShapes();
            this.repaint();
        }
    }
    
    public boolean isInGridMode() {
        return this.listener.isGridListener();
    }
    
    public void changeToGridMode() {
        String gridString = null;
        if (this.getGraph().getGridRows() > 0) {
            gridString = (String)JOptionPane.showInputDialog(this.getGraphController().getGraphWindow(), "Use commas to separate the number of rows and columns", "Set Grid Size", -1, null, null, String.valueOf(String.valueOf(this.getGraph().getGridRows()) + "," + this.getGraph().getGridCols()));
        }
        else {
            gridString = (String)JOptionPane.showInputDialog(this.getGraphController().getGraphWindow(), "Use commas to separate the number of rows and columns", "Set Grid Size", -1, null, null, String.valueOf(String.valueOf(this.getGraph().getNumNodes() - 1) + "," + (this.getGraph().getNumNodes() - 1)));
        }
        int rows = 0;
        int cols = 0;
        if (gridString != null) {
            try {
                StringTokenizer tok = new StringTokenizer(gridString, ",");
                if (tok.countTokens() != 2) {
                    throw new NumberFormatException("Rows and Columns must be separated by a comma");
                }
                rows = Integer.parseInt(tok.nextToken());
                cols = Integer.parseInt(tok.nextToken());
                if (rows < 2 || cols < 2) {
                    throw new NumberFormatException("Rows and Columns must both be greater than 1");
                }
                final String sizeString = (String)JOptionPane.showInputDialog(this.getGraphController().getGraphWindow(), "Use commas to separate the height and width of the rows and columns", "Set Grid Size", -1, null, null, String.valueOf(String.valueOf(this.getDrawWidth() / (cols - 1)) + "," + this.getDrawHeight() / (rows - 1)));
                int rowHeight = 0;
                int colWidth = 0;
                if (sizeString != null) {
                    try {
                        tok = new StringTokenizer(sizeString, ",");
                        if (tok.countTokens() != 2) {
                            throw new NumberFormatException("Row and Column width must be separated by a comma");
                        }
                        colWidth = Integer.parseInt(tok.nextToken());
                        rowHeight = Integer.parseInt(tok.nextToken());
                        if (rowHeight < 2 || colWidth < 2) {
                            throw new NumberFormatException("Rows and Column width must both be greater than 1");
                        }
                        this.getGraph().setGrid(rows, rowHeight, cols, colWidth, false);
                        if (!this.isInGridMode()) {
                            this.removeEventHandlers();
                            this.initShapes();
                            this.listener = new GridListener(this.listener);
                            this.addEventHandlers();
                            this.updateShapes();
                            this.repaint();
                        }
                        this.setPreferredSize();
                    }
                    catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this.getGraphController().getGraphWindow(), "The width of the rows and columns for the grid must be integers greater than 1 separated by a comma", "Unable to switch to grid mode", 0);
                    }
                }
            }
            catch (NumberFormatException nfe2) {
                JOptionPane.showMessageDialog(this.getGraphController().getGraphWindow(), "The number of rows and columns for the grid must be integers greater than 1 separated by a comma", "Unable to switch to grid mode", 0);
            }
        }
    }
    
    public boolean isInResizeMode() {
        return this.listener.isResizeListener();
    }
    
    public String getModeString() {
        return this.listener.getModeString();
    }
    
    public String getShowString() {
        return this.getGraph().getShowString();
    }
    
    public void allowNodeSelection(final int numSelectionsAllowed) {
        this.listener.allowNodeSelection(numSelectionsAllowed);
    }
    
    public void allowTriangleSelection() {
        this.listener.allowTriangleSelection();
    }
    
    public Node[] getSpecialNodeSelections() {
        final Node[] specialNodeSelections = new Node[this.listener.getSpecialNodeSelections().size()];
        this.listener.getSpecialNodeSelections().toArray(specialNodeSelections);
        return specialNodeSelections;
    }
    
    private void initShapes() {
        this.lineToDraw = null;
        this.pointToDraw = null;
        this.rectangleToDraw = null;
        this.polygonToDraw = null;
        this.curveToDraw = null;
    }
    
    public void setGraph(final Graph g) {
        this.listener.setGraph(g);
        this.setPreferredSize();
    }
    
    public Graph getGraph() {
        return this.listener.getGraph();
    }
    
    public GraphController getGraphController() {
        return this.listener.getGraphController();
    }
    
    public void setPreferredSize() {
        final Rectangle2D.Double bounds = this.getGraph().getBounds(2 * GraphEditor.DRAW_BUFFER, 2 * GraphEditor.DRAW_BUFFER);
        final int gridWidth = this.getGraph().getGridWidth() + 2 * GraphEditor.DRAW_BUFFER;
        final int gridHeight = this.getGraph().getGridHeight() + 2 * GraphEditor.DRAW_BUFFER;
        final Dimension dim = this.getPreferredSize();
        if (this.getGraph().getDrawGrid() && (gridWidth > (int)bounds.getMaxX() || gridHeight > (int)bounds.getMaxY())) {
            if (gridWidth != dim.width || gridHeight != dim.height) {
                this.setPreferredSize(new Dimension(gridWidth, gridHeight));
            }
        }
        else if (bounds.getMaxX() != dim.width || bounds.getMaxY() != dim.height) {
            this.setPreferredSize(new Dimension((int)bounds.getMaxX(), (int)bounds.getMaxY()));
        }
    }
    
    public int getDrawWidth() {
        return this.getWidth() - 2 * GraphEditor.DRAW_BUFFER;
    }
    
    public int getDrawHeight() {
        return this.getHeight() - 2 * GraphEditor.DRAW_BUFFER;
    }
    
    public void saveImage(final String fileName) {
        final BufferedImage imageToSave = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        this.paint(imageToSave.getGraphics());
        if (imageToSave != null) {
            try {
                final FileOutputStream out = new FileOutputStream(fileName);
                try {
                    if (fileName.substring(fileName.lastIndexOf(46) + 1).equalsIgnoreCase("gif")) {
                        GIFOutputStream.writeGIF(out, imageToSave);
                    }
                    else {
                        final JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder((OutputStream)out);
                        final JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(imageToSave);
                        param.setQuality(1.0f, false);
                        encoder.setJPEGEncodeParam(param);
                        encoder.encode(imageToSave);
                    }
                    out.close();
                }
                catch (IOException ioe) {
                    System.out.println("Error writing image to file");
                    ioe.printStackTrace();
                }
            }
            catch (FileNotFoundException fnfe) {
                System.out.println("Could not locate the file to save the image to!\n");
                fnfe.printStackTrace();
            }
        }
    }
    
    private void addEventHandlers() {
        this.addMouseListener(this.listener);
        this.addMouseMotionListener(this.listener);
        this.addKeyListener(this.listener);
        if (this.listener instanceof FocusListener) {
            this.addFocusListener((FocusListener)this.listener);
        }
        if (this.listener instanceof AncestorListener) {
            this.addAncestorListener((AncestorListener)this.listener);
        }
    }
    
    private void removeEventHandlers() {
        this.removeMouseListener(this.listener);
        this.removeMouseMotionListener(this.listener);
        this.removeKeyListener(this.listener);
        if (this.listener instanceof FocusListener) {
            this.removeFocusListener((FocusListener)this.listener);
        }
        if (this.listener instanceof AncestorListener) {
            this.removeAncestorListener((AncestorListener)this.listener);
        }
    }
    
    public void setLineToDraw(final Line2D.Double lineToDraw) {
        this.lineToDraw = lineToDraw;
    }
    
    public void setLineToDrawColor(final Color aColor) {
        this.lineToDrawColor = aColor;
    }
    
    public void setPointToDraw(final Ellipse2D.Double pointToDraw) {
        this.pointToDraw = pointToDraw;
    }
    
    public void setRectangleToDraw(final Rectangle2D.Double rectangleToDraw) {
        this.rectangleToDraw = rectangleToDraw;
    }
    
    public Rectangle2D.Double getRectangleToDraw() {
        return this.rectangleToDraw;
    }
    
    public void setPolygonToDraw(final Polygon polygonToDraw) {
        this.polygonToDraw = polygonToDraw;
    }
    
    public Polygon getPolygonToDraw() {
        return this.polygonToDraw;
    }
    
    public void setPolygonToDrawColor(final Color aColor) {
        this.polygonToDrawColor = aColor;
    }
    
    public void setCurveToDraw(final QuadCurve2D.Double curveToDraw) {
        this.curveToDraw = curveToDraw;
    }
    
    public QuadCurve2D.Double getCurveToDraw() {
        return this.curveToDraw;
    }
    
    public void setCurveToDrawColor(final Color aColor) {
        this.curveToDrawColor = aColor;
    }
    
    public void redo() {
        this.getGraph().redo();
        this.updateShapes();
        this.repaint();
    }
    
    public void undo() {
        this.getGraph().undo();
        this.updateShapes();
        this.repaint();
    }
    
    public void updateShapes() {
        if (this.isInGridMode()) {
            this.getGraph().setDrawGrid(true);
        }
        else if (this.isInResizeMode()) {
            if (this.getGraph().getNumNodes() > 1) {
                (this.rectangleToDraw = this.getGraph().getBounds(2, 2)).setRect(this.rectangleToDraw.getX() + GraphEditor.DRAW_BUFFER - 1.0, this.rectangleToDraw.getY() + GraphEditor.DRAW_BUFFER - 1.0, this.rectangleToDraw.getWidth(), this.rectangleToDraw.getHeight());
            }
            else {
                this.rectangleToDraw = null;
            }
            this.pointToDraw = null;
            this.getGraph().setDrawGrid(false);
        }
        else if (this.isInRotateMode()) {
            if (this.getGraph().getNumNodes() > 1) {
                final Location location = this.getGraph().getCenterPointLocation();
                this.pointToDraw = new Ellipse2D.Double(location.intX() - Node.RADIUS + GraphEditor.DRAW_BUFFER, location.intY() - Node.RADIUS + GraphEditor.DRAW_BUFFER, Node.RADIUS * 2, Node.RADIUS * 2);
            }
            else {
                this.pointToDraw = null;
            }
            this.rectangleToDraw = null;
            this.getGraph().setDrawGrid(false);
        }
        else {
            this.pointToDraw = null;
            this.rectangleToDraw = null;
            this.getGraph().setDrawGrid(false);
        }
    }
    
    public void startTranslateNode(final Node aNode) {
        (this.nodesToRedraw = new Vector(1)).addElement(aNode);
        if (aNode.getNumEdges() > 0) {
            (this.edgesToRedraw = new Vector(aNode.getNumEdges())).addAll(aNode.incidentEdges());
        }
        this.createImage(true);
    }
    
    public void startTranslateNodes(final Vector nodes) {
        this.nodesToRedraw = nodes;
        this.edgesToRedraw = this.getGraph().getEdges(nodes);
        this.createImage(true);
    }
    
    public void stopTranslateNodes() {
        this.nodesToRedraw = null;
        this.edgesToRedraw = null;
        this.createImage(true);
    }
    
    public void startTranslateEdge(final Edge anEdge) {
        (this.edgesToRedraw = new Vector(1)).addElement(anEdge);
    }
    
    public void stopTranslateEdge() {
        this.edgesToRedraw = null;
        this.createImage(true);
    }
    
    public void startTranslate() {
        this.createImage(true);
    }
    
    public void translate(final int dx, final int dy) {
        this.getGraph().translate(dx, dy, false);
        this.affineTransform = AffineTransform.getTranslateInstance(dx + this.imageOffsetX + GraphEditor.DRAW_BUFFER, dy + this.imageOffsetY + GraphEditor.DRAW_BUFFER);
    }
    
    public void stopTranslate() {
        this.affineTransform = null;
        this.createImage(true);
    }
    
    public void startRotate() {
        this.createImage(true);
        this.angle = 0.0;
    }
    
    public void rotate(final Location pivotPoint, final double angle) {
        this.angle += angle;
        this.getGraph().rotate(pivotPoint, angle, false);
        (this.affineTransform = AffineTransform.getTranslateInstance(this.imageOffsetX + GraphEditor.DRAW_BUFFER, this.imageOffsetY + GraphEditor.DRAW_BUFFER)).rotate(Math.toRadians(this.angle), pivotPoint.intX() - this.imageOffsetX, pivotPoint.intY() - this.imageOffsetY);
    }
    
    public void stopRotate() {
        this.affineTransform = null;
        this.createImage(true);
    }
    
    public void startScaleTo() {
        this.oldBounds = this.getGraph().getBounds();
        this.createImage(true);
    }
    
    public void scaleTo(final Rectangle2D.Double newBounds) {
        this.getGraph().scaleTo(newBounds, false);
        (this.affineTransform = AffineTransform.getTranslateInstance(this.imageOffsetX + GraphEditor.DRAW_BUFFER, this.imageOffsetY + GraphEditor.DRAW_BUFFER)).scale(newBounds.width / this.oldBounds.width, newBounds.height / this.oldBounds.height);
    }
    
    public void stopScaleTo() {
        this.affineTransform = null;
        this.getGraph().refreshEdgeCurves();
        this.createImage(true);
    }
    
    private void createImage(final boolean forceCreation) {
        final Graph g = this.getGraph();
        this.createImage(g, g.getBounds(), forceCreation);
    }
    
    private void createImage(final Graph aGraph, final Rectangle2D.Double bounds, final boolean forceCreation) {
        if (aGraph.hasChangedSinceLastDraw() || forceCreation) {
            this.bufferedImage = new BufferedImage((int)bounds.getWidth() + 2 * (Node.RADIUS + 2) + 200, (int)bounds.getHeight() + 2 * (Node.RADIUS + 2) + 20, 2);
            this.imageOffsetX = (int)(bounds.getMinX() - Node.RADIUS - 2.0);
            this.imageOffsetY = (int)(bounds.getMinY() - Node.RADIUS - 2.0);
            final Graphics2D g2i = this.bufferedImage.createGraphics();
            g2i.setColor(new Color(255, 255, 255, 0));
            g2i.fillRect(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
            if (this.nodesToRedraw != null) {
                for (int i = 0; i < this.nodesToRedraw.size(); ++i) {
                    this.nodesToRedraw.elementAt(i).setIsVisible(false);
                }
            }
            if (this.edgesToRedraw != null) {
                for (int i = 0; i < this.edgesToRedraw.size(); ++i) {
                    this.edgesToRedraw.elementAt(i).setIsVisible(false);
                }
            }
            aGraph.draw(g2i, -1 * this.imageOffsetX, -1 * this.imageOffsetY);
            if (this.nodesToRedraw != null) {
                for (int i = 0; i < this.nodesToRedraw.size(); ++i) {
                    this.nodesToRedraw.elementAt(i).setIsVisible(true);
                }
            }
            if (this.edgesToRedraw != null) {
                for (int i = 0; i < this.edgesToRedraw.size(); ++i) {
                    this.edgesToRedraw.elementAt(i).setIsVisible(true);
                }
            }
        }
    }
    
    public void paint(final Graphics aPen) {
        if (this.getGraph().logChangedSinceLastDraw()) {
            this.logWindow.update();
        }
        super.paint(aPen);
        final Graphics2D g2 = (Graphics2D)aPen;
        final Graph aGraph = this.listener.getGraph();
        final Rectangle2D.Double bounds = aGraph.getBounds();
        g2.setColor(GraphEditor.backgroundColor);
        g2.fillRect(GraphEditor.DRAW_BUFFER, GraphEditor.DRAW_BUFFER, this.getWidth() - 2 * GraphEditor.DRAW_BUFFER, this.getHeight() - 2 * GraphEditor.DRAW_BUFFER);
        if (this.getGraph().getDrawGrid()) {
            this.getGraph().drawGrid(g2, GraphEditor.DRAW_BUFFER, GraphEditor.DRAW_BUFFER);
        }
        this.createImage(aGraph, bounds, false);
        if (this.affineTransform == null) {
            g2.drawImage(this.bufferedImage, this.imageOffsetX + GraphEditor.DRAW_BUFFER, this.imageOffsetY + GraphEditor.DRAW_BUFFER, this);
        }
        else {
            g2.drawImage(this.bufferedImage, this.affineTransform, this);
        }
        if (this.edgesToRedraw != null) {
            for (int i = 0; i < this.edgesToRedraw.size(); ++i) {
                this.edgesToRedraw.elementAt(i).draw(g2, GraphEditor.DRAW_BUFFER, GraphEditor.DRAW_BUFFER, aGraph.getDrawSelected());
            }
        }
        if (this.nodesToRedraw != null) {
            for (int i = 0; i < this.nodesToRedraw.size(); ++i) {
                this.nodesToRedraw.elementAt(i).draw(g2, GraphEditor.DRAW_BUFFER, GraphEditor.DRAW_BUFFER, aGraph.getDrawSelected(), aGraph.getShowCoords(), aGraph.getShowLabels());
            }
        }
        if (this.rectangleToDraw != null) {
            g2.setStroke(new BasicStroke((float)Edge.THICKNESS));
            g2.setColor(Color.green);
            g2.draw(this.rectangleToDraw);
        }
        if (this.polygonToDraw != null) {
            g2.setStroke(new BasicStroke((float)Edge.THICKNESS));
            g2.setColor(this.polygonToDrawColor);
            g2.fill(this.polygonToDraw);
        }
        if (this.curveToDraw != null) {
            g2.setStroke(new BasicStroke((float)Edge.THICKNESS));
            g2.setColor(this.curveToDrawColor);
            g2.draw(this.curveToDraw);
        }
        if (this.lineToDraw != null) {
            if (this.lineToDraw.getX2() < GraphEditor.DRAW_BUFFER || this.lineToDraw.getX2() > this.getWidth() - GraphEditor.DRAW_BUFFER || this.lineToDraw.getY2() < GraphEditor.DRAW_BUFFER || this.lineToDraw.getY2() > this.getHeight() - GraphEditor.DRAW_BUFFER) {
                final float[] dash1 = { (float)Edge.GENERATED_DASH_LENGTH };
                g2.setStroke(new BasicStroke((float)Edge.THICKNESS, 0, 0, 10.0f, dash1, 0.0f));
            }
            else {
                g2.setStroke(new BasicStroke((float)Edge.THICKNESS));
            }
            g2.setColor(this.lineToDrawColor);
            g2.draw(this.lineToDraw);
        }
        if (this.pointToDraw != null) {
            g2.setStroke(new BasicStroke((float)Node.LINE_THICKNESS));
            g2.setColor(Color.green);
            g2.draw(this.pointToDraw);
        }
        this.revalidate();
    }
    
    public void update() {
        this.updateShapes();
        this.infoWindow.update();
        this.setPreferredSize();
        this.repaint();
    }
    
    public void prepareForClose() {
        this.listener.prepareForClose();
    }
}
