// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import dataStructure.DoublyLinkedList;
import graphStructure.mementos.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

public class Graph
{
    private String label;
    private Vector nodes;
    private boolean drawSelected;
    private Color drawColor;
    private String filePath;
    private DoublyLinkedList mementos;
    private MementoGrouper currentMemento;
    private boolean showCoords;
    private boolean showLabels;
    private boolean trackUndos;
    private boolean hasChangedSinceLastSave;
    private boolean hasChangedSinceLastDraw;
    private boolean logChangedSinceLastDraw;
    private Vector logEntries;
    private LogEntry currentLogEntry;
    private Graph parent;
    private boolean drawGrid;
    private int gridRows;
    private int gridCols;
    private int gridColWidth;
    private int gridRowHeight;
    
    public Graph() {
        this.label = "";
        this.nodes = new Vector();
        this.drawColor = null;
        this.drawSelected = true;
        this.filePath = "";
        this.mementos = new DoublyLinkedList();
        this.currentMemento = null;
        this.trackUndos = true;
        this.hasChangedSinceLastSave = false;
        this.hasChangedSinceLastDraw = true;
        this.showCoords = false;
        this.showLabels = true;
        this.logEntries = new Vector();
        this.currentLogEntry = null;
        this.parent = null;
        this.drawGrid = false;
        this.gridRows = -1;
        this.gridCols = -1;
    }
    
    public Graph(final String aLabel) {
        this();
        this.label = aLabel;
    }
    
    public Graph(final String aLabel, final Vector initialNodes) {
        this();
        this.label = aLabel;
        this.nodes = initialNodes;
    }
    
    public Graph(final Graph aGraph) {
        this.parent = aGraph;
        this.label = new String(aGraph.label);
        this.drawSelected = aGraph.drawSelected;
        this.trackUndos = aGraph.trackUndos;
        this.hasChangedSinceLastSave = aGraph.hasChangedSinceLastSave;
        this.hasChangedSinceLastDraw = true;
        if (aGraph.drawColor == null) {
            this.drawColor = null;
        }
        else {
            this.drawColor = new Color(aGraph.drawColor.getRGB());
        }
        this.filePath = new String(aGraph.filePath);
        this.nodes = new Vector();
        this.mementos = new DoublyLinkedList();
        this.currentMemento = null;
        this.showCoords = aGraph.showCoords;
        this.showLabels = aGraph.showLabels;
        this.logEntries = new Vector();
        this.currentLogEntry = null;
    }
    
    public String getShowString() {
        if (this.showCoords) {
            return "Show Coordinates";
        }
        if (this.showLabels) {
            return "Show Labels";
        }
        return "Show Nothing";
    }
    
    public void shareMementos(final Graph aGraph) {
        this.mementos = aGraph.mementos;
    }
    
    public void setTrackUndos(final boolean tu) {
        this.trackUndos = tu;
        this.initUndo();
    }
    
    private void initUndo() {
        this.mementos = new DoublyLinkedList();
        this.currentMemento = null;
    }
    
    public boolean getTrackUndos() {
        return this.trackUndos;
    }
    
    public void setDrawSelected(final boolean draw) {
        this.drawSelected = draw;
    }
    
    public boolean getDrawSelected() {
        return this.drawSelected;
    }
    
    public void setDrawColor(final Color aColor) {
        this.drawColor = aColor;
    }
    
    public Color getDrawColor() {
        return this.drawColor;
    }
    
    public void setShowCoords(final boolean c) {
        this.showCoords = c;
        this.hasChangedSinceLastDraw = true;
    }
    
    public boolean getShowCoords() {
        return this.showCoords;
    }
    
    public void setShowLabels(final boolean l) {
        this.showLabels = l;
        this.hasChangedSinceLastDraw = true;
    }
    
    public boolean getShowLabels() {
        return this.showLabels;
    }
    
    public String getFilePath() {
        return this.filePath;
    }
    
    public String getFileName() {
        if (this.filePath.indexOf(92) == -1) {
            return this.filePath.substring(0, this.filePath.lastIndexOf(46));
        }
        return this.filePath.substring(this.filePath.lastIndexOf(92) + 1, this.filePath.lastIndexOf(46));
    }
    
    public void setFilePath(final String fp) {
        this.filePath = fp;
    }
    
    public boolean hasChangedSinceLastSave() {
        return this.hasChangedSinceLastSave;
    }
    
    public boolean hasChangedSinceLastDraw() {
        return this.hasChangedSinceLastDraw;
    }
    
    public boolean logChangedSinceLastDraw() {
        return this.logChangedSinceLastDraw;
    }
    
    public void markForRepaint() {
        this.hasChangedSinceLastDraw = true;
    }
    
    private int getRowHeight(final int numRows, final int height) {
        return height / (numRows - 1);
    }
    
    private int getColWidth(final int numCols, final int width) {
        return width / (numCols - 1);
    }
    
    public void setGrid(final int numRows, final int rowHeight, final int numCols, final int colWidth, final boolean addMemento) {
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(GridSizeMemento.createGridSizeMemento(this));
        }
        this.gridRows = numRows;
        this.gridRowHeight = rowHeight;
        this.gridCols = numCols;
        this.gridColWidth = colWidth;
    }
    
    public void setGridArea(int numRows, final int height, int numCols, final int width, final boolean addMemento) {
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(GridSizeMemento.createGridSizeMemento(this));
        }
        if (numRows <= 1) {
            numRows = 2;
        }
        this.gridRows = numRows;
        this.gridRowHeight = this.getRowHeight(numRows, height);
        if (numCols <= 1) {
            numCols = 2;
        }
        this.gridCols = numCols;
        this.gridColWidth = this.getColWidth(numCols, width);
    }
    
    public int getGridRows() {
        return this.gridRows;
    }
    
    public int getGridCols() {
        return this.gridCols;
    }
    
    public int getGridColWidth() {
        return this.gridColWidth;
    }
    
    public int getGridRowHeight() {
        return this.gridRowHeight;
    }
    
    public void setDrawGrid(final boolean draw) {
        this.drawGrid = draw;
    }
    
    public boolean getDrawGrid() {
        return this.drawGrid && this.gridRows >= 2 && this.gridCols >= 2;
    }
    
    public int getGridHeight() {
        return (this.gridRows - 1) * this.gridRowHeight;
    }
    
    public int getGridWidth() {
        return (this.gridCols - 1) * this.gridColWidth;
    }
    
    public void drawGrid(final Graphics2D g2, final int xOffset, final int yOffset) {
        int x = 0 + xOffset;
        int y = 0 + yOffset;
        g2.setColor(Color.gray);
        for (int i = 0; i < this.gridRows; ++i) {
            y = i * this.gridRowHeight + yOffset;
            g2.drawLine(xOffset, y, this.gridColWidth * (this.gridCols - 1) + xOffset, y);
        }
        for (int i = 0; i < this.gridCols; ++i) {
            x = i * this.gridColWidth + xOffset;
            g2.drawLine(x, yOffset, x, this.gridRowHeight * (this.gridRows - 1) + yOffset);
        }
    }
    
    public Location getClosestGridLocation(final Location location) {
        int row = (int)Math.round(location.doubleY() / this.gridRowHeight);
        int col = (int)Math.round(location.doubleX() / this.gridColWidth);
        if (row > this.gridRows - 1) {
            row = this.gridRows - 1;
        }
        if (col > this.gridCols - 1) {
            col = this.gridCols - 1;
        }
        return new Location(col * this.gridColWidth, row * this.gridRowHeight);
    }
    
    public boolean isOnGrid(final Location location) {
        return location.intX() % this.gridColWidth == 0 && location.intY() % this.gridRowHeight == 0;
    }
    
    public void newMemento(final String title) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.trackUndos) {
            if (this.currentMemento != null) {
                this.mementos.enqueueAfterCurrent(this.currentMemento);
            }
            this.currentMemento = new MementoGrouper(title);
        }
    }
    
    public void renameMemento(final String title) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.trackUndos) {
            this.currentMemento.setTitle(title);
        }
    }
    
    public void doneMemento() {
        if (this.trackUndos && this.currentMemento != null) {
            this.currentMemento.removeUselessMementos();
            if (this.currentMemento.size() > 0) {
                this.mementos.enqueueAfterCurrent(this.currentMemento);
            }
            this.currentMemento = null;
        }
    }
    
    public void undoMemento() {
        if (this.currentMemento != null) {
            this.currentMemento.apply(this);
        }
    }
    
    public void abortMemento() {
        if (this.trackUndos) {
            this.currentMemento = null;
        }
    }
    
    public boolean hasMoreUndos() {
        return this.mementos.getCurrent() != null;
    }
    
    public void undo() {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.trackUndos) {
            if (this.mementos.getCurrent() != null) {
                ((MementoGrouper)this.mementos.getCurrent()).apply(this);
                this.mementos.toPrev();
            }
            else {
                this.mementos.toHead();
            }
        }
    }
    
    public MementoGrouper peekUndo() {
        return (MementoGrouper)this.mementos.getCurrent();
    }
    
    public boolean hasMoreRedos() {
        return this.mementos.hasNext();
    }
    
    public void redo() {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.trackUndos) {
            if (this.mementos.hasNext()) {
                this.mementos.toNext();
                ((MementoGrouper)this.mementos.getCurrent()).apply(this);
            }
            else {
                this.mementos.toTail();
            }
        }
    }
    
    public MementoGrouper peekRedo() {
        if (this.mementos.hasNext()) {
            this.mementos.toNext();
            final MementoGrouper mg = (MementoGrouper)this.mementos.getCurrent();
            this.mementos.toPrev();
            return mg;
        }
        return null;
    }
    
    public String getLogString() {
        String returnString = "";
        for (int i = 0; i < this.logEntries.size(); ++i) {
            returnString = String.valueOf(returnString) + "Log Entry " + (i + 1) + "---------------------" + this.logEntries.elementAt(i).infoString() + "---------------------\n\n";
        }
        return returnString;
    }
    
    public Vector getLogEntries() {
        return this.logEntries;
    }
    
    public LogEntry startLogEntry(final String operationName) {
        if (this.parent != null) {
            final LogEntry newEntry = new LogEntry(operationName, this, System.currentTimeMillis());
            this.parent.startLogEntry(newEntry);
            return newEntry;
        }
        final LogEntry newEntry = new LogEntry(operationName, this, System.currentTimeMillis());
        if (this.currentLogEntry == null) {
            this.logEntries.addElement(newEntry);
        }
        else {
            this.currentLogEntry.addSubEntry(newEntry);
        }
        return this.currentLogEntry = newEntry;
    }
    
    private void startLogEntry(final LogEntry logEntry) {
        if (this.parent != null) {
            this.parent.startLogEntry(logEntry);
        }
        else {
            if (this.currentLogEntry == null) {
                this.logEntries.addElement(logEntry);
            }
            else {
                this.currentLogEntry.addSubEntry(logEntry);
            }
            this.currentLogEntry = logEntry;
        }
    }
    
    public void stopLogEntry(final LogEntry logEntry) {
        if (this.parent != null) {
            this.parent.stopLogEntry(logEntry);
            return;
        }
        this.logChangedSinceLastDraw = true;
        logEntry.updateTimeTaken(System.currentTimeMillis());
        this.currentLogEntry = logEntry.getParentEntry();
    }
    
    public Rectangle2D.Double getBounds() {
        return this.getBounds(0, 0);
    }
    
    public Rectangle2D.Double getBounds(final int xAdd, final int yAdd) {
        return this.getBounds(this.nodes, xAdd, yAdd);
    }
    
    public Rectangle2D.Double getBounds(final Vector someNodes) {
        return this.getBounds(someNodes, 0, 0);
    }
    
    public Rectangle2D.Double getBounds(final Vector someNodes, final int xAdd, final int yAdd) {
        double minX = 0.0;
        double minY = 0.0;
        double maxX = 0.0;
        double maxY = 0.0;
        if (!someNodes.isEmpty()) {
            Location aLocation = someNodes.firstElement().getLocation();
            minX = (maxX = aLocation.doubleX());
            minY = (maxY = aLocation.doubleY());
            for (int i = 1; i < someNodes.size(); ++i) {
                aLocation = someNodes.elementAt(i).getLocation();
                if (aLocation.doubleX() < minX) {
                    minX = aLocation.doubleX();
                }
                else if (aLocation.doubleX() > maxX) {
                    maxX = aLocation.doubleX();
                }
                if (aLocation.doubleY() < minY) {
                    minY = aLocation.doubleY();
                }
                else if (aLocation.doubleY() > maxY) {
                    maxY = aLocation.doubleY();
                }
            }
            final Vector edges = this.getCurvedEdges(someNodes);
            for (int j = 0; j < edges.size(); ++j) {
                final Rectangle2D rect = edges.elementAt(j).getQuadCurve().getBounds2D();
                if (rect.getMinX() < minX) {
                    minX = rect.getMinX();
                }
                if (rect.getMaxX() > maxX) {
                    maxX = rect.getMaxX();
                }
                if (rect.getMinY() < minY) {
                    minY = rect.getMinY();
                }
                if (rect.getMaxY() > maxY) {
                    maxY = rect.getMaxY();
                }
            }
        }
        return new Rectangle2D.Double(minX, minY, maxX - minX + xAdd, maxY - minY + yAdd);
    }
    
    public Location getCenterPointLocation() {
        int xAcc = 0;
        int yAcc = 0;
        if (this.nodes.size() > 0) {
            for (int i = 0; i < this.nodes.size(); ++i) {
                final Node currentNode = this.nodes.elementAt(i);
                xAcc += currentNode.getX();
                yAcc += currentNode.getY();
            }
            return new Location(xAcc / this.nodes.size(), yAcc / this.nodes.size());
        }
        return new Location(0, 0);
    }
    
    public static Node partitionAroundMedianX(final Vector pNodes, final Vector lesser, final Vector greater) {
        final Node mNode = getMedianXNode(pNodes);
        if (pNodes.size() > 1) {
            if (pNodes.size() % 2 == 1) {
                lesser.ensureCapacity(pNodes.size() / 2);
                greater.ensureCapacity(pNodes.size() / 2);
                lesser.removeAllElements();
                greater.removeAllElements();
            }
            else {
                lesser.ensureCapacity(pNodes.size() / 2);
                greater.ensureCapacity(pNodes.size() / 2 - 1);
                lesser.removeAllElements();
                greater.removeAllElements();
            }
            for (int i = 0; i < pNodes.size(); ++i) {
                final Node currentNode = pNodes.elementAt(i);
                if (currentNode.getX() < mNode.getX()) {
                    lesser.addElement(currentNode);
                }
                else if (currentNode.getX() > mNode.getX()) {
                    greater.addElement(currentNode);
                }
            }
            for (int i = 0; i < pNodes.size(); ++i) {
                final Node currentNode = pNodes.elementAt(i);
                if (currentNode != mNode && currentNode.getX() == mNode.getX()) {
                    if (lesser.size() < pNodes.size() / 2) {
                        lesser.addElement(currentNode);
                    }
                    else {
                        greater.addElement(currentNode);
                    }
                }
            }
        }
        return mNode;
    }
    
    public Node getMedianXNode() {
        return getMedianXNode(this.nodes);
    }
    
    public static Node getMedianXNode(final Vector sNodes) {
        if (sNodes.size() > 0) {
            final Node[] mNodes = new Node[sNodes.size()];
            sNodes.toArray(mNodes);
            return quickSelectX(mNodes, mNodes.length / 2);
        }
        return null;
    }
    
    private static Node quickSelectX(final Node[] mNodes, final int k) {
        if (mNodes.length == 1) {
            return mNodes[0];
        }
        final Node medianNode = getMedianOfMediansXNode(mNodes);
        int lesserCount = 0;
        int equalCount = 0;
        int greaterCount = 0;
        for (int i = 0; i < mNodes.length; ++i) {
            if (mNodes[i].getX() < medianNode.getX()) {
                ++lesserCount;
            }
            else if (mNodes[i].getX() > medianNode.getX()) {
                ++greaterCount;
            }
        }
        final Node[] lesserNodes = new Node[lesserCount];
        final Node[] equalNodes = new Node[mNodes.length - lesserCount - greaterCount];
        final Node[] greaterNodes = new Node[greaterCount];
        equalCount = (lesserCount = (greaterCount = 0));
        for (int j = 0; j < mNodes.length; ++j) {
            if (mNodes[j].getX() < medianNode.getX()) {
                lesserNodes[lesserCount++] = mNodes[j];
            }
            else if (mNodes[j].getX() > medianNode.getX()) {
                greaterNodes[greaterCount++] = mNodes[j];
            }
            else {
                equalNodes[equalCount++] = mNodes[j];
            }
        }
        if (k < lesserNodes.length) {
            return quickSelectX(lesserNodes, k);
        }
        if (k < lesserNodes.length + equalNodes.length) {
            return medianNode;
        }
        return quickSelectX(greaterNodes, k - lesserNodes.length - equalNodes.length);
    }
    
    public static Node getMedianOfMediansXNode(Node[] mNodes) {
        do {
            mNodes = findMediansX(mNodes);
        } while (mNodes.length != 1);
        return mNodes[0];
    }
    
    private static Node[] findMediansX(final Node[] mNodes) {
        final int leftOver = mNodes.length % 5;
        final Node[] temp = new Node[5];
        int i = 0;
        int j = 0;
        int m = 0;
        Node[] medians;
        if (leftOver == 0) {
            medians = new Node[mNodes.length / 5];
        }
        else {
            medians = new Node[mNodes.length / 5 + 1];
        }
        i = 0;
        m = 0;
        while (i < mNodes.length) {
            if (i > 0 && i % 5 == 0) {
                medians[m++] = temp[2];
            }
            Node currentNode = mNodes[i];
            Node switcherNode = null;
            for (j = 0; j < i % 5; ++j) {
                if (temp[j].getX() > currentNode.getX()) {
                    switcherNode = temp[j];
                    break;
                }
            }
            temp[j] = currentNode;
            if (switcherNode != null) {
                while (j < i % 5) {
                    currentNode = temp[j + 1];
                    temp[j + 1] = switcherNode;
                    switcherNode = currentNode;
                    ++j;
                }
            }
            ++i;
        }
        if (leftOver != 0) {
            medians[m] = temp[leftOver / 2];
        }
        else {
            medians[m] = temp[2];
        }
        return medians;
    }
    
    public static Node partitionAroundMedianY(final Vector pNodes, final Vector lesser, final Vector greater) {
        final Node mNode = getMedianYNode(pNodes);
        if (pNodes.size() > 1) {
            if (pNodes.size() % 2 == 1) {
                lesser.ensureCapacity(pNodes.size() / 2);
                greater.ensureCapacity(pNodes.size() / 2);
                lesser.removeAllElements();
                greater.removeAllElements();
            }
            else {
                lesser.ensureCapacity(pNodes.size() / 2);
                greater.ensureCapacity(pNodes.size() / 2 - 1);
                lesser.removeAllElements();
                greater.removeAllElements();
            }
            for (int i = 0; i < pNodes.size(); ++i) {
                final Node currentNode = pNodes.elementAt(i);
                if (currentNode.getY() < mNode.getY()) {
                    lesser.addElement(currentNode);
                }
                else if (currentNode.getY() > mNode.getY()) {
                    greater.addElement(currentNode);
                }
            }
            for (int i = 0; i < pNodes.size(); ++i) {
                final Node currentNode = pNodes.elementAt(i);
                if (currentNode != mNode && currentNode.getY() == mNode.getY()) {
                    if (lesser.size() < pNodes.size() / 2) {
                        lesser.addElement(currentNode);
                    }
                    else {
                        greater.addElement(currentNode);
                    }
                }
            }
        }
        return mNode;
    }
    
    public Node getMedianYNode() {
        return getMedianYNode(this.nodes);
    }
    
    public static Node getMedianYNode(final Vector sNodes) {
        if (sNodes.size() > 0) {
            final Node[] mNodes = new Node[sNodes.size()];
            sNodes.toArray(mNodes);
            return quickSelectY(mNodes, mNodes.length / 2);
        }
        return null;
    }
    
    private static Node quickSelectY(final Node[] mNodes, final int k) {
        if (mNodes.length == 1) {
            return mNodes[0];
        }
        final Node medianNode = getMedianOfMediansYNode(mNodes);
        int lesserCount = 0;
        int equalCount = 0;
        int greaterCount = 0;
        for (int i = 0; i < mNodes.length; ++i) {
            if (mNodes[i].getY() < medianNode.getY()) {
                ++lesserCount;
            }
            else if (mNodes[i].getY() > medianNode.getY()) {
                ++greaterCount;
            }
        }
        final Node[] lesserNodes = new Node[lesserCount];
        final Node[] equalNodes = new Node[mNodes.length - lesserCount - greaterCount];
        final Node[] greaterNodes = new Node[greaterCount];
        equalCount = (lesserCount = (greaterCount = 0));
        for (int j = 0; j < mNodes.length; ++j) {
            if (mNodes[j].getY() < medianNode.getY()) {
                lesserNodes[lesserCount++] = mNodes[j];
            }
            else if (mNodes[j].getY() > medianNode.getY()) {
                greaterNodes[greaterCount++] = mNodes[j];
            }
            else {
                equalNodes[equalCount++] = mNodes[j];
            }
        }
        if (k < lesserNodes.length) {
            return quickSelectY(lesserNodes, k);
        }
        if (k < lesserNodes.length + equalNodes.length) {
            return medianNode;
        }
        return quickSelectY(greaterNodes, k - lesserNodes.length - equalNodes.length);
    }
    
    public static Node getMedianOfMediansYNode(Node[] mNodes) {
        do {
            mNodes = findMediansY(mNodes);
        } while (mNodes.length != 1);
        return mNodes[0];
    }
    
    private static Node[] findMediansY(final Node[] mNodes) {
        final int leftOver = mNodes.length % 5;
        final Node[] temp = new Node[5];
        int i = 0;
        int j = 0;
        int m = 0;
        Node[] medians;
        if (leftOver == 0) {
            medians = new Node[mNodes.length / 5];
        }
        else {
            medians = new Node[mNodes.length / 5 + 1];
        }
        i = 0;
        m = 0;
        while (i < mNodes.length) {
            if (i > 0 && i % 5 == 0) {
                medians[m++] = temp[2];
            }
            Node currentNode = mNodes[i];
            Node switcherNode = null;
            for (j = 0; j < i % 5; ++j) {
                if (temp[j].getY() > currentNode.getY()) {
                    switcherNode = temp[j];
                    break;
                }
            }
            temp[j] = currentNode;
            if (switcherNode != null) {
                while (j < i % 5) {
                    currentNode = temp[j + 1];
                    temp[j + 1] = switcherNode;
                    switcherNode = currentNode;
                    ++j;
                }
            }
            ++i;
        }
        if (leftOver != 0) {
            medians[m] = temp[leftOver / 2];
        }
        else {
            medians[m] = temp[2];
        }
        return medians;
    }
    
    public Graph copy() {
        return this.copyNodes(this.nodes);
    }
    
    public Graph copy(final boolean keepReferences) {
        return this.copyNodes(this.nodes, keepReferences);
    }
    
    public Graph copyNodes(final Vector nodeVector) {
        return this.copyNodes(nodeVector, false);
    }
    
    public Graph copyNodes(final Vector nodeVector, final boolean keepReferences) {
        final Graph newGraph = new Graph(this);
        final Vector sourceEdges = this.getEdges(nodeVector);
        final Vector copyNodes = new Vector(this.nodes.size());
        final Vector copyEdges = new Vector(sourceEdges.size());
        for (int i = 0; i < nodeVector.size(); ++i) {
            final Node currentNode = nodeVector.elementAt(i);
            copyNodes.addElement(currentNode.getCopy());
            final Node newNode = new Node(currentNode);
            newNode.setCopy(currentNode);
            currentNode.setCopy(newNode);
            newGraph.addNode(newNode);
        }
        for (int i = 0; i < sourceEdges.size(); ++i) {
            final Edge currentEdge = sourceEdges.elementAt(i);
            copyEdges.addElement(currentEdge.getCopy());
            Edge newEdge;
            if (currentEdge.getDirectedSourceNode() != null) {
                newEdge = new Edge(currentEdge, currentEdge.getDirectedSourceNode().getCopy(), currentEdge.getStartNode().getCopy(), currentEdge.getEndNode().getCopy());
            }
            else {
                newEdge = new Edge(currentEdge, null, currentEdge.getStartNode().getCopy(), currentEdge.getEndNode().getCopy());
            }
            newEdge.setCopy(currentEdge);
            currentEdge.setCopy(newEdge);
        }
        for (int i = 0; i < nodeVector.size(); ++i) {
            final Node currentNode = nodeVector.elementAt(i);
            final Vector incidentEdges = currentNode.incidentEdges();
            for (int j = 0; j < incidentEdges.size(); ++j) {
                final Edge currentEdge = incidentEdges.elementAt(j);
                currentNode.getCopy().addIncidentEdgeNoCheck(currentEdge.getCopy());
            }
        }
        for (int i = 0; i < nodeVector.size(); ++i) {
            final Node currentNode = nodeVector.elementAt(i);
            if (!keepReferences) {
                currentNode.getCopy().setCopy(null);
            }
            currentNode.setCopy(copyNodes.elementAt(i));
        }
        for (int i = 0; i < sourceEdges.size(); ++i) {
            final Edge currentEdge = sourceEdges.elementAt(i);
            if (!keepReferences) {
                currentEdge.getCopy().setCopy(null);
            }
            currentEdge.setCopy(copyEdges.elementAt(i));
        }
        return newGraph;
    }
    
    public Graph copyNode(final Node aNode) {
        return this.copyNode(aNode, false, false);
    }
    
    public Graph copyNode(final Node aNode, final boolean keepCopyReferences) {
        return this.copyNode(aNode, keepCopyReferences, false);
    }
    
    public Graph copyNode(final Node aNode, final boolean keepCopyReferences, final boolean updateCopyReferences) {
        final Graph newGraph = new Graph(this);
        final Node newNode = new Node(aNode);
        if (updateCopyReferences) {
            newNode.setCopy(aNode.getCopy());
        }
        else {
            newNode.setCopy(aNode);
        }
        newGraph.addNode(newNode);
        if (!keepCopyReferences) {
            newNode.setCopy(null);
        }
        return newGraph;
    }
    
    public Graph copyEdges(final Vector edges) {
        return this.copyEdges(edges, false);
    }
    
    public Graph copyEdges(final Vector edges, final boolean keepCopyReferences) {
        final Graph newGraph = new Graph(this);
        final Vector sourceNodes = new Vector(2 * edges.size());
        for (int j = 0; j < edges.size(); ++j) {
            final Edge currentEdge = edges.elementAt(j);
            currentEdge.setIsAdded(false);
            ((Node)currentEdge.getStartNode()).setIsAdded(false);
            ((Node)currentEdge.getEndNode()).setIsAdded(false);
        }
        for (int j = 0; j < edges.size(); ++j) {
            final Edge currentEdge = edges.elementAt(j);
            if (!((Node)currentEdge.getStartNode()).isAdded()) {
                ((Node)currentEdge.getStartNode()).setIsAdded(true);
                sourceNodes.addElement(currentEdge.getStartNode());
            }
            if (!((Node)currentEdge.getEndNode()).isAdded()) {
                ((Node)currentEdge.getEndNode()).setIsAdded(true);
                sourceNodes.addElement(currentEdge.getEndNode());
            }
        }
        final Vector copyNodes = new Vector(sourceNodes.size());
        for (int i = 0; i < sourceNodes.size(); ++i) {
            final Node currentNode = sourceNodes.elementAt(i);
            final Node newNode = new Node(currentNode);
            newNode.setCopy(currentNode);
            copyNodes.addElement(currentNode.getCopy());
            currentNode.setCopy(newNode);
            newGraph.addNode(newNode);
        }
        for (int j = 0; j < edges.size(); ++j) {
            final Edge currentEdge = edges.elementAt(j);
            if (!currentEdge.isAdded()) {
                currentEdge.setIsAdded(true);
                Edge newEdge;
                if (currentEdge.getDirectedSourceNode() != null) {
                    newEdge = new Edge(currentEdge, currentEdge.getDirectedSourceNode().getCopy(), currentEdge.getStartNode().getCopy(), currentEdge.getEndNode().getCopy());
                }
                else {
                    newEdge = new Edge(currentEdge, null, currentEdge.getStartNode().getCopy(), currentEdge.getEndNode().getCopy());
                }
                newEdge.setCopy(currentEdge);
                currentEdge.getStartNode().getCopy().addIncidentEdgeNoCheck(newEdge);
                currentEdge.getEndNode().getCopy().addIncidentEdgeNoCheck(newEdge);
            }
        }
        for (int i = 0; i < sourceNodes.size(); ++i) {
            final Node currentNode = sourceNodes.elementAt(i);
            if (!keepCopyReferences) {
                currentNode.getCopy().setCopy(null);
            }
            currentNode.setCopy(copyNodes.elementAt(i));
            currentNode.setIsAdded(false);
        }
        final Vector newEdges = newGraph.getEdges();
        for (int j = 0; j < newEdges.size(); ++j) {
            final Edge currentEdge = edges.elementAt(j);
            final Edge newEdge = newEdges.elementAt(j);
            if (!keepCopyReferences) {
                newEdge.setCopy(null);
            }
            currentEdge.setIsAdded(false);
        }
        return newGraph;
    }
    
    public void resetCopyData() {
        final Enumeration enumNodes = this.nodes.elements();
        while (enumNodes.hasMoreElements()) {
            final Node aNode = enumNodes.nextElement();
            aNode.setCopy(null);
            final Enumeration enumEdges = aNode.incidentEdges().elements();
            while (enumEdges.hasMoreElements()) {
                final Edge anEdge = enumEdges.nextElement();
                anEdge.setCopy(null);
            }
        }
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public Vector getNodes() {
        return this.nodes;
    }
    
    public Node getNodeAt(final int index) {
        if (this.nodes.isEmpty() || index < 0 || index > this.nodes.size() - 1) {
            return null;
        }
        return this.nodes.elementAt(index);
    }
    
    public int getNumNodes() {
        return this.nodes.size();
    }
    
    public void setLabel(final String newLabel) {
        this.label = newLabel;
    }
    
    public boolean edgeNumbersAreInSync() {
        return this.getNumEdges() == this.getEdges().size();
    }
    
    public void makeAllEdgesStraight() {
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            final Edge anEdge = edges.elementAt(i);
            if (anEdge.isCurved()) {
                anEdge.makeStraight();
            }
        }
    }
    
    public int getNumEdges() {
        return this.getNumEdges(this.nodes);
    }
    
    public int getNumEdges(final Vector nodeVector) {
        int numEdges = 0;
        final Enumeration allNodes = nodeVector.elements();
        while (allNodes.hasMoreElements()) {
            numEdges += allNodes.nextElement().getNumEdges();
        }
        return numEdges / 2;
    }
    
    public int getNumGeneratedEdges() {
        int numGenerated = 0;
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            if (edges.elementAt(i).isGenerated()) {
                ++numGenerated;
            }
        }
        return numGenerated;
    }
    
    public int getNumCurvedEdges() {
        int numCurved = 0;
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            if (edges.elementAt(i).isCurved()) {
                ++numCurved;
            }
        }
        return numCurved;
    }
    
    public Vector getEdges() {
        return this.getEdges(this.nodes);
    }
    
    public Vector getEdges(final Vector nodeVector) {
        return this.getEdges(nodeVector, false);
    }
    
    public Vector getCurvedEdges(final Vector nodeVector) {
        return this.getEdges(nodeVector, true);
    }
    
    private Vector getEdges(final Vector nodeVector, final boolean onlyCurved) {
        final int numEdges = this.getNumEdges(nodeVector);
        final Vector edges = new Vector(numEdges);
        Enumeration allNodes = nodeVector.elements();
        while (allNodes.hasMoreElements()) {
            final Enumeration someEdges = allNodes.nextElement().incidentEdges().elements();
            while (someEdges.hasMoreElements()) {
                someEdges.nextElement().setIsAdded(false);
            }
        }
        allNodes = nodeVector.elements();
        while (allNodes.hasMoreElements()) {
            final Enumeration someEdges = allNodes.nextElement().incidentEdges().elements();
            while (someEdges.hasMoreElements()) {
                final Edge anEdge = someEdges.nextElement();
                if (!anEdge.isAdded()) {
                    if (!onlyCurved || anEdge.isCurved()) {
                        edges.addElement(anEdge);
                    }
                    anEdge.setIsAdded(true);
                }
            }
        }
        allNodes = nodeVector.elements();
        while (allNodes.hasMoreElements()) {
            final Enumeration someEdges = allNodes.nextElement().incidentEdges().elements();
            while (someEdges.hasMoreElements()) {
                someEdges.nextElement().setIsAdded(false);
            }
        }
        return edges;
    }
    
    public Node[] getRandomTriangularFace() {
        final Node[] triangleNodes = { this.nodes.firstElement(), null, null };
        final Edge tempEdge = triangleNodes[0].incidentEdges().firstElement();
        triangleNodes[1] = (Node)tempEdge.otherEndFrom(triangleNodes[0]);
        triangleNodes[2] = (Node)tempEdge.getNextInOrderFrom(triangleNodes[0]).otherEndFrom(triangleNodes[0]);
        if (triangleNodes[2] == tempEdge.getPreviousInOrderFrom(triangleNodes[1]).otherEndFrom(triangleNodes[1])) {
            return triangleNodes;
        }
        return null;
    }
    
    public String toString() {
        return String.valueOf(this.label) + "(" + this.nodes.size() + " nodes, " + this.getNumEdges() + " edges)";
    }
    
    public void printAll() {
        for (int i = 0; i < this.nodes.size(); ++i) {
            this.nodes.elementAt(i).printAll();
        }
    }
    
    public void addNode(final Node aNode) {
        this.addNode(aNode, true);
    }
    
    public void addNode(final Node aNode, final boolean addMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        this.nodes.addElement(aNode);
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(NodeMemento.createCreateMemento(aNode));
        }
    }
    
    public Node createNode(final Location aPoint) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final Node newNode = new Node(aPoint);
        this.nodes.addElement(newNode);
        if (this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(NodeMemento.createCreateMemento(newNode));
        }
        return newNode;
    }
    
    public void translateNode(final Node aNode, final int dx, final int dy, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final boolean memento = this.currentMemento != null && this.trackUndos && createMemento;
        if (memento) {
            this.currentMemento.addMemento(NodeMovementMemento.createMoveMemento(aNode));
        }
        aNode.translate(dx, dy);
        final Vector edges = aNode.incidentEdges();
        for (int i = 0; i < edges.size(); ++i) {
            final Edge anEdge = edges.elementAt(i);
            if (memento) {
                this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
            }
            anEdge.update();
        }
    }
    
    public void relocateNode(final NodeInterface aNode, final Location aLocation, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final boolean memento = this.currentMemento != null && this.trackUndos && createMemento;
        if (memento) {
            this.currentMemento.addMemento(NodeMovementMemento.createMoveMemento(aNode));
        }
        aNode.setLocation(aLocation);
    }
    
    public void translateNodes(final Vector someNodes, final int dx, final int dy, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        this.translate(someNodes, dx, dy, createMemento);
    }
    
    private void translate(final Vector someNodes, final int dx, final int dy, final boolean createMemento) {
        final boolean memento = this.currentMemento != null && this.trackUndos && createMemento;
        for (int i = 0; i < someNodes.size(); ++i) {
            final Node aNode = someNodes.elementAt(i);
            if (memento) {
                this.currentMemento.addMemento(NodeMovementMemento.createMoveMemento(aNode));
            }
            aNode.translate(dx, dy);
        }
        final Vector edges = this.getEdges(this.nodes);
        for (int j = 0; j < edges.size(); ++j) {
            final Edge anEdge = edges.elementAt(j);
            if (memento) {
                this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
            }
            anEdge.update();
        }
    }
    
    public void relocateEdge(final Edge anEdge, final Location newLocation, final boolean createMemento) {
        if (this.currentMemento != null && this.trackUndos && createMemento) {
            this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
        }
        anEdge.setCenterLocation(newLocation);
    }
    
    public void curveEdge(final Edge anEdge, final int dx, final int dy, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.currentMemento != null && this.trackUndos && createMemento) {
            this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
        }
        anEdge.makeCurved();
        anEdge.translate(dx, dy);
    }
    
    public void orthogonalizeEdge(final Edge anEdge, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.currentMemento != null && this.trackUndos && createMemento) {
            this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
        }
        anEdge.makeOrthogonal();
    }
    
    public void straightenEdge(final EdgeInterface anEdge, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.currentMemento != null && this.trackUndos && createMemento) {
            this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
        }
        anEdge.makeStraight();
    }
    
    public void straightenEdges(final boolean createMemento) {
        this.straightenEdges(this.getEdges(), createMemento);
    }
    
    public void straightenEdges(final Vector edges, final boolean createMemento) {
        for (int i = 0; i < edges.size(); ++i) {
            this.straightenEdge(edges.elementAt(i), createMemento);
        }
    }
    
    public void updateEdge(final EdgeInterface anEdge, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (this.currentMemento != null && this.trackUndos && createMemento) {
            this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
        }
        anEdge.update();
    }
    
    public void updateEdges(final Vector edges, final boolean createMemento) {
        for (int i = 0; i < edges.size(); ++i) {
            this.updateEdge(edges.elementAt(i), createMemento);
        }
    }
    
    public void refreshEdgeCurves() {
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            edges.elementAt(i).update();
        }
    }
    
    public void updateEdgeCurveAngles() {
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            edges.elementAt(i).initCurveAngles();
        }
    }
    
    public void refreshOrthogonalEdges(final Vector edges) {
        for (int i = 0; i < edges.size(); ++i) {
            final Edge anEdge = edges.elementAt(i);
            if (anEdge.isOrthogonal()) {
                final Location location = anEdge.getOrthogonalLocation();
                if (location == null) {
                    anEdge.setCenterLocation(anEdge.getNormalLocation());
                }
                else {
                    anEdge.setCenterLocation(location);
                }
            }
            else {
                anEdge.update();
            }
        }
    }
    
    public void changeNodeLabel(final NodeInterface aNode, final String label, final boolean createMemento) {
        if (!label.equals(aNode.getLabel())) {
            this.hasChangedSinceLastSave = true;
            this.hasChangedSinceLastDraw = true;
            if (this.currentMemento != null && this.trackUndos && createMemento) {
                this.currentMemento.addMemento(NodeLabelMemento.createLabelMemento(aNode));
            }
            aNode.setLabel(label);
        }
    }
    
    public void changeNodeDrawX(final NodeInterface aNode, final boolean drawX, final boolean createMemento) {
        if (drawX != aNode.getDrawX()) {
            this.hasChangedSinceLastSave = true;
            this.hasChangedSinceLastDraw = true;
            if (this.currentMemento != null && this.trackUndos && createMemento) {
                this.currentMemento.addMemento(NodeDrawXMemento.createDrawXMemento(aNode));
            }
            aNode.setDrawX(drawX);
        }
    }
    
    public void changeNodeColor(final NodeInterface aNode, final Color aColor, final boolean createMemento) {
        if (!aColor.equals(aNode.getColor())) {
            this.hasChangedSinceLastSave = true;
            this.hasChangedSinceLastDraw = true;
            if (this.currentMemento != null && this.trackUndos && createMemento) {
                this.currentMemento.addMemento(NodeColorMemento.createColorMemento(aNode));
            }
            aNode.setColor(aColor);
        }
    }
    
    public void changeEdgeColor(final EdgeInterface anEdge, final Color aColor, final boolean createMemento) {
        if (!aColor.equals(anEdge.getColor())) {
            this.hasChangedSinceLastSave = true;
            this.hasChangedSinceLastDraw = true;
            if (this.currentMemento != null && this.trackUndos && createMemento) {
                this.currentMemento.addMemento(EdgeColorMemento.createColorMemento(anEdge));
            }
            anEdge.setColor(aColor);
        }
    }
    
    public void changeEdgeDirection(final EdgeInterface anEdge, final NodeInterface sourceNode, final boolean createMemento) {
        if ((sourceNode != null && !sourceNode.equals(anEdge.getDirectedSourceNode())) || (sourceNode == null && anEdge.getDirectedSourceNode() != null)) {
            this.hasChangedSinceLastSave = true;
            this.hasChangedSinceLastDraw = true;
            if (this.currentMemento != null && this.trackUndos && createMemento && ((sourceNode != null && !sourceNode.equals(anEdge.getDirectedSourceNode())) || (sourceNode == null && anEdge.getDirectedSourceNode() != null))) {
                this.currentMemento.addMemento(EdgeDirectionMemento.createDirectionMemento(anEdge));
            }
            anEdge.setDirectedFrom(sourceNode);
        }
    }
    
    public boolean isTriangle(final Node sourceNode, final Edge firstEdge, final Edge secondEdge) {
        final Node firstNode = (Node)firstEdge.otherEndFrom(sourceNode);
        final Node secondNode = (Node)secondEdge.otherEndFrom(sourceNode);
        return firstEdge.getPreviousInOrderFrom(firstNode).otherEndFrom(firstNode) == secondNode;
    }
    
    public boolean isInQuadrilateral(final Edge anEdge) {
        final Node firstNode = (Node)anEdge.getStartNode();
        final Node secondNode = (Node)anEdge.otherEndFrom(firstNode);
        return anEdge.getNextInOrderFrom(firstNode).otherEndFrom(firstNode) == anEdge.getPreviousInOrderFrom(secondNode).otherEndFrom(secondNode) && anEdge.getPreviousInOrderFrom(firstNode).otherEndFrom(firstNode) == anEdge.getNextInOrderFrom(secondNode).otherEndFrom(secondNode);
    }
    
    public void flip(final Edge anEdge) {
        final Node firstNode = (Node)anEdge.getStartNode();
        final Node secondNode = (Node)anEdge.getEndNode();
        final Node newFirstNode = (Node)((Edge)anEdge.getPreviousInOrderFrom(firstNode)).otherEndFrom(firstNode);
        final Node newSecondNode = (Node)((Edge)anEdge.getNextInOrderFrom(firstNode)).otherEndFrom(firstNode);
        final Edge newFirstPrevEdge = (Edge)anEdge.getNextInOrderFrom(secondNode);
        final Edge newSecondPrevEdge = (Edge)anEdge.getNextInOrderFrom(firstNode);
        final Edge newEdge = new Edge(anEdge, null, newFirstNode, newSecondNode);
        this.deleteEdge(anEdge);
        this.addEdge(newEdge, newFirstPrevEdge, newSecondPrevEdge);
    }
    
    public void addEdge(final EdgeInterface newEdge, final EdgeInterface startPrevEdge, final EdgeInterface endPrevEdge) {
        this.addEdge(newEdge, startPrevEdge, endPrevEdge, true);
    }
    
    public void addEdge(final EdgeInterface newEdge, final EdgeInterface startPrevEdge, final EdgeInterface endPrevEdge, final boolean addMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final NodeInterface startNode = newEdge.getStartNode();
        final NodeInterface endNode = newEdge.getEndNode();
        startNode.addEdgeBetween(newEdge, startPrevEdge, startPrevEdge.getNextInOrderFrom(startNode));
        endNode.addEdgeBetween(newEdge, endPrevEdge, endPrevEdge.getNextInOrderFrom(endNode));
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            Edge a;
            if (newEdge instanceof EdgeExtender) {
                a = ((EdgeExtender)newEdge).getRef();
            }
            else {
                a = (Edge)newEdge;
            }
            Edge b;
            if (startPrevEdge instanceof EdgeExtender) {
                b = ((EdgeExtender)startPrevEdge).getRef();
            }
            else {
                b = (Edge)startPrevEdge;
            }
            Edge c;
            if (endPrevEdge instanceof EdgeExtender) {
                c = ((EdgeExtender)endPrevEdge).getRef();
            }
            else {
                c = (Edge)endPrevEdge;
            }
            this.currentMemento.addMemento(EdgeBetweenMemento.createCreateMemento(a, b, c));
        }
    }
    
    private void addEdge(final Node start, final Node end, final boolean addMemento) {
        final Edge anEdge = new Edge(start, end);
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (start.addIncidentEdge(anEdge) && end.addIncidentEdge(anEdge) && addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(EdgeBetweenMemento.createCreateMemento(anEdge, (Edge)anEdge.getPreviousInOrderFrom(start), (Edge)anEdge.getPreviousInOrderFrom(end)));
        }
        if (!this.edgeNumbersAreInSync()) {
            System.out.println("error2: " + anEdge);
        }
    }
    
    public void addEdge(final Node start, final Node end) {
        this.addEdge(start, end, true);
    }
    
    public void addEdgeNoCheck(final Edge anEdge) {
        this.addEdgeNoCheck(anEdge, true);
    }
    
    public void addEdgeNoCheck(final Edge anEdge, final boolean addMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        anEdge.getStartNode().addIncidentEdgeNoCheck(anEdge);
        anEdge.getEndNode().addIncidentEdgeNoCheck(anEdge);
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(EdgeBetweenMemento.createCreateMemento(anEdge, (Edge)anEdge.getPreviousInOrderFrom(anEdge.getStartNode()), (Edge)anEdge.getPreviousInOrderFrom(anEdge.getEndNode())));
        }
    }
    
    public void addEdgeNoCheck(final NodeInterface aNode, final EdgeInterface anEdge) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        aNode.addIncidentEdgeNoCheck(anEdge);
    }
    
    public void addEdgeNoCheck(final Node start, final Node end) {
        this.addEdgeNoCheck(start, end, true);
    }
    
    public void addEdgeNoCheck(final Node start, final Node end, final boolean addMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final Edge anEdge = new Edge(start, end);
        start.addIncidentEdgeNoCheck(anEdge);
        end.addIncidentEdgeNoCheck(anEdge);
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(EdgeBetweenMemento.createCreateMemento(anEdge, (Edge)anEdge.getPreviousInOrderFrom(start), (Edge)anEdge.getPreviousInOrderFrom(end)));
        }
        if (!this.edgeNumbersAreInSync()) {
            System.out.println("error4: " + anEdge);
        }
    }
    
    public void addGeneratedEdgeNoCheck(final Node start, final Node end) {
        this.addGeneratedEdgeNoCheck(start, end, true);
    }
    
    public void addGeneratedEdgeNoCheck(final Node start, final Node end, final boolean addMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final Edge anEdge = new Edge(start, end);
        anEdge.setIsGenerated(true);
        start.addIncidentEdgeNoCheck(anEdge);
        end.addIncidentEdgeNoCheck(anEdge);
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(EdgeBetweenMemento.createCreateMemento(anEdge, (Edge)anEdge.getPreviousInOrderFrom(start), (Edge)anEdge.getPreviousInOrderFrom(end)));
        }
    }
    
    public void addEdge(final String startLabel, final String endLabel) {
        final Node start = this.nodeNamed(startLabel);
        final Node end = this.nodeNamed(endLabel);
        if (start != null && end != null) {
            this.addEdge(start, end);
        }
    }
    
    public void deleteEdge(final Edge anEdge) {
        this.deleteEdge(anEdge, true);
    }
    
    public void deleteEdge(final Edge anEdge, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (createMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(EdgeBetweenMemento.createDeleteMemento(anEdge, (Edge)anEdge.getPreviousInOrderFrom(anEdge.getStartNode()), (Edge)anEdge.getPreviousInOrderFrom(anEdge.getEndNode())));
        }
        anEdge.getStartNode().deleteIncidentEdge(anEdge);
        anEdge.getEndNode().deleteIncidentEdge(anEdge);
    }
    
    public void deleteNode(final Node aNode) {
        this.deleteNode(aNode, true);
    }
    
    public void deleteNode(final Node aNode, final boolean addMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final Enumeration someEdges = aNode.incidentEdgesInReverse().elements();
        while (someEdges.hasMoreElements()) {
            final Edge anEdge = someEdges.nextElement();
            this.deleteEdge(anEdge, addMemento);
        }
        this.nodes.removeElement(aNode);
        if (addMemento && this.currentMemento != null && this.trackUndos) {
            this.currentMemento.addMemento(NodeMemento.createDeleteMemento(aNode));
        }
    }
    
    public void makeGeneratedEdgePermanent(final Edge anEdge) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        if (anEdge.isGenerated()) {
            anEdge.setIsGenerated(false);
            if (this.currentMemento != null && this.trackUndos) {
                this.currentMemento.addMemento(EdgeMemento.createPreserveGeneratedMemento(anEdge));
            }
        }
    }
    
    public void makeGeneratedEdgesPermanent() {
        final Enumeration enumEdges = this.getEdges().elements();
        while (enumEdges.hasMoreElements()) {
            this.makeGeneratedEdgePermanent(enumEdges.nextElement());
        }
    }
    
    public void deleteGeneratedEdges() {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        final Enumeration enumNodes = this.nodes.elements();
        while (enumNodes.hasMoreElements()) {
            final Node currentNode = enumNodes.nextElement();
            final Vector edges = currentNode.incidentEdges();
            for (int i = 0; i < edges.size(); ++i) {
                final Edge currentEdge = edges.elementAt(i);
                if (currentEdge.isGenerated()) {
                    this.deleteEdge(currentEdge);
                }
            }
        }
    }
    
    public void removeEdgeDirections() {
        this.removeEdgeDirections(true);
    }
    
    public void removeEdgeDirections(final boolean createMemento) {
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            this.changeEdgeDirection(edges.elementAt(i), null, createMemento);
        }
    }
    
    public void clearNodeLabels() {
        this.clearNodeLabels(true);
    }
    
    public void clearNodeLabels(final boolean createMemento) {
        for (int i = 0; i < this.nodes.size(); ++i) {
            this.changeNodeLabel(this.nodes.elementAt(i), "", createMemento);
        }
    }
    
    public Node nodeNamed(final String aLabel) {
        for (int i = 0; i < this.nodes.size(); ++i) {
            final Node aNode = this.nodes.elementAt(i);
            if (aNode.getLabel().equals(aLabel)) {
                return aNode;
            }
        }
        return null;
    }
    
    public Node nodeAt(final Point p) {
        return this.nodeAt(new Location(p));
    }
    
    public Node nodeAt(final Location p) {
        for (int i = 0; i < this.nodes.size(); ++i) {
            final Node aNode = this.nodes.elementAt(i);
            final int distance = (p.intX() - aNode.getLocation().intX()) * (p.intX() - aNode.getLocation().intX()) + (p.intY() - aNode.getLocation().intY()) * (p.intY() - aNode.getLocation().intY());
            if (distance <= Node.RADIUS * Node.RADIUS) {
                return aNode;
            }
        }
        return null;
    }
    
    public Edge edgeAt(final Location p) {
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            final Edge anEdge = edges.elementAt(i);
            final int midPointX = anEdge.getCenterLocation().intX();
            final int midPointY = anEdge.getCenterLocation().intY();
            final int distance = (p.intX() - midPointX) * (p.intX() - midPointX) + (p.intY() - midPointY) * (p.intY() - midPointY);
            if (distance <= Node.RADIUS * Node.RADIUS) {
                return anEdge;
            }
        }
        return null;
    }
    
    public Vector getNodesInRectangle(final Rectangle2D.Double rect) {
        final Vector nodesToReturn = new Vector();
        for (int i = 0; i < this.nodes.size(); ++i) {
            final Node aNode = this.nodes.elementAt(i);
            if (rect.contains(aNode.getX(), aNode.getY())) {
                nodesToReturn.addElement(aNode);
            }
        }
        return nodesToReturn;
    }
    
    public Vector getEdgesInRectangle(final Rectangle2D.Double rect) {
        final Vector edgesToReturn = new Vector();
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            final Edge anEdge = edges.elementAt(i);
            if (rect.contains(anEdge.getCenterLocation().intX(), anEdge.getCenterLocation().intY())) {
                edgesToReturn.addElement(anEdge);
            }
        }
        return edgesToReturn;
    }
    
    public Vector selectedNodes() {
        final Vector selected = new Vector();
        final Enumeration allNodes = this.nodes.elements();
        while (allNodes.hasMoreElements()) {
            final Node aNode = allNodes.nextElement();
            if (aNode.isSelected()) {
                selected.addElement(aNode);
            }
        }
        return selected;
    }
    
    public Vector selectedEdges() {
        final Vector selected = new Vector();
        final Enumeration allEdges = this.getEdges().elements();
        while (allEdges.hasMoreElements()) {
            final Edge anEdge = allEdges.nextElement();
            if (anEdge.isSelected()) {
                selected.addElement(anEdge);
            }
        }
        return selected;
    }
    
    public void unselectAll() {
        this.hasChangedSinceLastDraw = true;
        final Enumeration highlightedEdges = this.selectedEdges().elements();
        while (highlightedEdges.hasMoreElements()) {
            highlightedEdges.nextElement().setSelected(false);
        }
        final Enumeration highlightedNodes = this.selectedNodes().elements();
        while (highlightedNodes.hasMoreElements()) {
            highlightedNodes.nextElement().setSelected(false);
        }
    }
    
    public void deleteSelected() {
        this.hasChangedSinceLastDraw = true;
        final Enumeration highlightedEdges = this.selectedEdges().elements();
        while (highlightedEdges.hasMoreElements()) {
            this.deleteEdge(highlightedEdges.nextElement());
        }
        final Enumeration highlightedNodes = this.selectedNodes().elements();
        while (highlightedNodes.hasMoreElements()) {
            this.deleteNode(highlightedNodes.nextElement());
        }
    }
    
    public void toggleEdgeSelection(final Edge anEdge) {
        this.hasChangedSinceLastDraw = true;
        anEdge.toggleSelected();
    }
    
    public void toggleNodeSelection(final Node aNode) {
        this.hasChangedSinceLastDraw = true;
        aNode.toggleSelected();
    }
    
    public void selectNodes(final Vector sNodes) {
        this.hasChangedSinceLastDraw = true;
        for (int i = 0; i < sNodes.size(); ++i) {
            sNodes.elementAt(i).setSelected(true);
        }
    }
    
    public void selectEdges(final Vector sEdges) {
        this.hasChangedSinceLastDraw = true;
        for (int i = 0; i < sEdges.size(); ++i) {
            sEdges.elementAt(i).setSelected(true);
        }
    }
    
    public void deleteAll() {
        this.hasChangedSinceLastDraw = true;
        final Enumeration nodeEnum = new Vector(this.nodes).elements();
        while (nodeEnum.hasMoreElements()) {
            this.deleteNode(nodeEnum.nextElement());
        }
    }
    
    public void resetColors(final boolean createMemento) {
        this.hasChangedSinceLastDraw = true;
        for (int i = 0; i < this.nodes.size(); ++i) {
            final Node aNode = this.nodes.elementAt(i);
            this.changeNodeColor(aNode, Node.DEFAULT_COLOR, createMemento);
            this.changeNodeDrawX(aNode, false, createMemento);
        }
        final Vector edges = this.getEdges();
        for (int j = 0; j < edges.size(); ++j) {
            this.changeEdgeColor(edges.elementAt(j), Edge.DEFAULT_COLOR, createMemento);
        }
    }
    
    public void draw(final Graphics2D g2) {
        this.draw(g2, 0, 0);
    }
    
    public void draw(final Graphics2D g2, final int xOffset, final int yOffset) {
        this.hasChangedSinceLastDraw = false;
        this.logChangedSinceLastDraw = false;
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            edges.elementAt(i).draw(g2, xOffset, yOffset, this.drawSelected);
        }
        for (int i = 0; i < this.nodes.size(); ++i) {
            this.nodes.elementAt(i).draw(g2, xOffset, yOffset, this.drawSelected, this.showCoords, this.showLabels);
        }
    }
    
    public void rotate(final Location pivotPoint, final double angle, final boolean createMemento) {
        final boolean memento = this.currentMemento != null && this.trackUndos && createMemento;
        for (int i = 0; i < this.nodes.size(); ++i) {
            final Node currentNode = this.nodes.elementAt(i);
            if (memento) {
                this.currentMemento.addMemento(NodeMovementMemento.createMoveMemento(currentNode));
            }
            currentNode.rotate(pivotPoint, angle);
        }
        final Vector edges = this.getEdges();
        for (int j = 0; j < edges.size(); ++j) {
            final Edge anEdge = edges.elementAt(j);
            if (memento) {
                this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
            }
            anEdge.rotate(pivotPoint, angle);
            anEdge.update();
        }
    }
    
    public void translate(final int dx, final int dy, final boolean createMemento) {
        this.hasChangedSinceLastSave = true;
        this.hasChangedSinceLastDraw = true;
        this.translate(this.nodes, dx, dy, createMemento);
    }
    
    public void saveTo(final PrintWriter aFile) {
        aFile.println(this.label);
        aFile.println(this.gridRows);
        aFile.println(this.gridRowHeight);
        aFile.println(this.gridCols);
        aFile.println(this.gridColWidth);
        aFile.println(this.nodes.size());
        this.enumerateNodeAndEdgeIndices();
        for (int i = 0; i < this.nodes.size(); ++i) {
            this.nodes.elementAt(i).saveTo(aFile);
        }
        final Vector edges = this.getEdges();
        aFile.println(edges.size());
        for (int j = 0; j < edges.size(); ++j) {
            edges.elementAt(j).saveTo(aFile);
        }
        this.hasChangedSinceLastSave = false;
    }
    
    public static Graph loadFrom(final BufferedReader aFile) throws IOException {
        final Graph aGraph = new Graph(aFile.readLine());
        aGraph.gridRows = Integer.valueOf(aFile.readLine());
        aGraph.gridRowHeight = Integer.valueOf(aFile.readLine());
        aGraph.gridCols = Integer.valueOf(aFile.readLine());
        aGraph.gridColWidth = Integer.valueOf(aFile.readLine());
        final int numNodes = Integer.valueOf(aFile.readLine());
        final Vector allEdgeIndices = new Vector();
        for (int i = 0; i < numNodes; ++i) {
            aGraph.addNode(Node.loadFrom(aFile, allEdgeIndices));
        }
        final int numEdges = Integer.valueOf(aFile.readLine());
        final Vector edges = new Vector(numEdges);
        for (int j = 0; j < numEdges; ++j) {
            edges.addElement(Edge.loadFrom(aFile, aGraph.nodes));
        }
        for (int k = 0; k < numNodes; ++k) {
            final Node aNode = aGraph.nodes.elementAt(k);
            final Vector edgeIndices = allEdgeIndices.elementAt(k);
            for (int l = 0; l < edgeIndices.size(); ++l) {
                aNode.addIncidentEdgeNoCheck(edges.elementAt(edgeIndices.elementAt(l) - 1));
            }
        }
        return aGraph;
    }
    
    public boolean hasNodes() {
        return this.nodes.size() != 0;
    }
    
    public void enumerateNodeAndEdgeIndices() {
        for (int i = 0; i < this.nodes.size(); ++i) {
            this.nodes.elementAt(i).setIndex(i + 1);
        }
        final Vector edges = this.getEdges();
        for (int j = 0; j < edges.size(); ++j) {
            edges.elementAt(j).setIndex(j + 1);
        }
    }
    
    public EdgeInterface[] sortEdges() {
        return this.sortEdges(this.getEdges());
    }
    
    public EdgeInterface[] sortEdges(final Vector edges) {
        final int[] count = new int[this.nodes.size()];
        final EdgeInterface[] sortedEdges = new EdgeInterface[edges.size()];
        final EdgeInterface[] sortedEdges2 = new EdgeInterface[edges.size()];
        int i;
        for (i = 0, i = 0; i < this.nodes.size(); ++i) {
            count[i] = 0;
            this.nodes.elementAt(i).setIndex(i + 1);
        }
        for (i = 0; i < edges.size(); ++i) {
            final int[] array = count;
            final int n = edges.elementAt(i).getHigherIndex() - 1;
            ++array[n];
        }
        for (i = 1; i < this.nodes.size(); ++i) {
            final int[] array2 = count;
            final int n2 = i;
            array2[n2] += count[i - 1];
        }
        for (i = edges.size() - 1; i >= 0; --i) {
            sortedEdges[count[edges.elementAt(i).getHigherIndex() - 1] - 1] = edges.elementAt(i);
            final int[] array3 = count;
            final int n3 = edges.elementAt(i).getHigherIndex() - 1;
            --array3[n3];
        }
        for (i = 0; i < this.nodes.size(); ++i) {
            count[i] = 0;
        }
        for (i = 0; i < edges.size(); ++i) {
            final int[] array4 = count;
            final int n4 = sortedEdges[i].getLowerIndex() - 1;
            ++array4[n4];
        }
        for (i = 1; i < this.nodes.size(); ++i) {
            final int[] array5 = count;
            final int n5 = i;
            array5[n5] += count[i - 1];
        }
        for (i = edges.size() - 1; i >= 0; --i) {
            sortedEdges2[count[sortedEdges[i].getLowerIndex() - 1] - 1] = sortedEdges[i];
            final int[] array6 = count;
            final int n6 = sortedEdges[i].getLowerIndex() - 1;
            --array6[n6];
        }
        return sortedEdges2;
    }
    
    public void deleteAllEdges() {
        final Vector edges = this.getEdges();
        for (int i = 0; i < edges.size(); ++i) {
            final Edge edge = edges.elementAt(i);
            if (this.currentMemento != null && this.trackUndos) {
                this.currentMemento.addMemento(EdgeBetweenMemento.createChangeMemento(edge));
            }
        }
        for (int j = 0; j < this.nodes.size(); ++j) {
            final Node node = this.nodes.elementAt(j);
            if (this.currentMemento != null && this.trackUndos) {
                this.currentMemento.addMemento(NodeChangeMemento.createChangeMemento(node));
            }
            node.resetIncidentEdges();
        }
    }
    
    public boolean checkForDuplicateEdges() {
        final EdgeInterface[] sortedEdges = this.sortEdges();
        for (int i = 0; i < sortedEdges.length - 1; ++i) {
            if (sortedEdges[i].equals(sortedEdges[i + 1])) {
                return true;
            }
        }
        return false;
    }
    
    public void scaleTo(final Rectangle2D.Double newBounds, final boolean createMemento) {
        final boolean memento = this.currentMemento != null && this.trackUndos && createMemento;
        if (!this.nodes.isEmpty()) {
            final Rectangle2D.Double oldBounds = this.getBounds();
            final double xFactor = newBounds.getWidth() / oldBounds.getWidth();
            final double yFactor = newBounds.getHeight() / oldBounds.getHeight();
            for (int i = 0; i < this.nodes.size(); ++i) {
                final Node currentNode = this.nodes.elementAt(i);
                if (memento) {
                    this.currentMemento.addMemento(NodeMovementMemento.createMoveMemento(currentNode));
                }
                currentNode.scaleBy(oldBounds.getMinX(), oldBounds.getMinY(), xFactor, yFactor);
            }
            final Vector edges = this.getEdges();
            for (int j = 0; j < edges.size(); ++j) {
                final Edge anEdge = edges.elementAt(j);
                if (memento) {
                    this.currentMemento.addMemento(EdgeMovementMemento.createMoveMemento(anEdge));
                }
                anEdge.scaleBy(oldBounds.getMinX(), oldBounds.getMinY(), xFactor, yFactor);
            }
        }
    }
    
    public Vector createNodeExtenders(final Class NodeExtenderClass) {
        final Vector newVector = new Vector(this.nodes.size());
        for (int i = 0; i < this.nodes.size(); ++i) {
            final Node currentNode = this.nodes.elementAt(i);
            try {
                final NodeExtender currentNodeExtender = NodeExtenderClass.newInstance();
                currentNode.setExtender(currentNodeExtender);
                currentNodeExtender.setRef(currentNode);
                newVector.addElement(currentNodeExtender);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newVector;
    }
    
    public Vector createEdgeExtenders(final Class EdgeExtenderClass) {
        final Vector edges = this.getEdges();
        final Vector newVector = new Vector(edges.size());
        for (int i = 0; i < edges.size(); ++i) {
            final Edge currentEdge = edges.elementAt(i);
            try {
                final EdgeExtender currentEdgeExtender = EdgeExtenderClass.newInstance();
                currentEdge.setExtender(currentEdgeExtender);
                currentEdgeExtender.setRef(currentEdge);
                newVector.addElement(currentEdgeExtender);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newVector;
    }
    
    public Vector getNodeExtenders() {
        final Vector newVector = new Vector(this.nodes.size());
        for (int i = 0; i < this.nodes.size(); ++i) {
            final Node currentNode = this.nodes.elementAt(i);
            final NodeExtender currentNodeExtender = currentNode.getExtender();
            if (currentNodeExtender != null) {
                newVector.addElement(currentNodeExtender);
            }
        }
        return newVector;
    }
    
    public Vector getEdgeExtenders() {
        final Vector edges = this.getEdges();
        final Vector newVector = new Vector(edges.size());
        for (int i = 0; i < edges.size(); ++i) {
            final Edge currentEdge = edges.elementAt(i);
            final EdgeExtender currentEdgeExtender = currentEdge.getExtender();
            if (currentEdgeExtender != null) {
                newVector.addElement(currentEdgeExtender);
            }
        }
        return newVector;
    }
    
    public Vector getEdgeExtenders(final Vector nodeVector) {
        final Vector edges = this.getEdges(nodeVector);
        final Vector newVector = new Vector(edges.size());
        for (int i = 0; i < edges.size(); ++i) {
            final Edge currentEdge = edges.elementAt(i);
            final EdgeExtender currentEdgeExtender = currentEdge.getExtender();
            if (currentEdgeExtender != null) {
                newVector.addElement(currentEdgeExtender);
            }
        }
        return newVector;
    }
    
    public void permuteNodeOrder() {
        final Random rand = new Random();
        for (int i = 0; i < this.nodes.size(); ++i) {
            final int j = rand.nextInt(this.nodes.size());
            final Object temp = this.nodes.elementAt(i);
            this.nodes.setElementAt(this.nodes.elementAt(j), i);
            this.nodes.setElementAt(temp, j);
        }
    }
}
