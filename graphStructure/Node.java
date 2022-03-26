// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import userInterface.GraphEditor;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public class Node implements NodeInterface
{
    public static int RADIUS;
    public static int LINE_THICKNESS;
    public static int DASH_LENGTH;
    public static double MIN_FOR_SCALE;
    public static Color DEFAULT_COLOR;
    public static Color TEXT_COLOR;
    public static Color SELECTED_COLOR;
    public static Color SPECIAL_SELECTED_COLOR;
    public static boolean OPAQUE_TEXT;
    public static Font drawTextFont;
    private static TextLayout thisTl;
    private String label;
    private Color color;
    private boolean drawX;
    private Location location;
    private Edge accessEdge;
    private boolean isSelected;
    private boolean isSpecialSelected;
    private int index;
    private int numEdges;
    private NodeInterface copy;
    private boolean isAdded;
    private boolean isVisible;
    private NodeExtender extender;
    
    static {
        Node.RADIUS = 5;
        Node.LINE_THICKNESS = 3;
        Node.DASH_LENGTH = 2;
        Node.MIN_FOR_SCALE = 10.0;
        Node.DEFAULT_COLOR = Color.blue;
        Node.TEXT_COLOR = Color.black;
        Node.SELECTED_COLOR = Color.black;
        Node.SPECIAL_SELECTED_COLOR = Color.red;
        Node.OPAQUE_TEXT = true;
        Node.drawTextFont = new Font("Courier", 1, 12);
    }
    
    public boolean isAdded() {
        return this.isAdded;
    }
    
    public void setIsAdded(final boolean added) {
        this.isAdded = added;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color aColor) {
        this.color = aColor;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public void setCopy(final NodeInterface aCopy) {
        this.copy = aCopy;
    }
    
    public NodeInterface getCopy() {
        return this.copy;
    }
    
    public NodeInterface getMasterCopy() {
        if (this.copy == null) {
            return null;
        }
        Node cp;
        for (cp = this; cp.copy != null; cp = (Node)cp.copy) {}
        return cp;
    }
    
    public void setDrawX(final boolean draw) {
        this.drawX = draw;
    }
    
    public boolean getDrawX() {
        return this.drawX;
    }
    
    public int getNumEdges() {
        return this.numEdges;
    }
    
    public void setIsVisible(final boolean v) {
        this.isVisible = v;
    }
    
    protected Node() {
        this.initialize();
    }
    
    protected Node(final String aLabel) {
        this.initialize();
        this.label = aLabel;
    }
    
    public Node(final int x, final int y) {
        this.initialize();
        this.location = new Location(x, y);
    }
    
    protected Node(final Location aPoint) {
        this.initialize();
        this.location = new Location(aPoint);
    }
    
    public Node(final Node aNode) {
        this.initialize();
        this.location = new Location(aNode.getLocation());
        this.label = new String(aNode.label);
        if (aNode.color != null) {
            this.color = new Color(aNode.color.getRGB());
        }
        this.drawX = aNode.drawX;
        this.isSelected = aNode.isSelected;
        this.isVisible = aNode.isVisible;
    }
    
    protected Node(final String aLabel, final Point aPoint) {
        this.initialize();
        this.label = aLabel;
        this.location = new Location(aPoint);
    }
    
    public int getX() {
        return this.location.intX();
    }
    
    public int getY() {
        return this.location.intY();
    }
    
    private void initialize() {
        this.label = "";
        this.location = new Location(0, 0);
        this.accessEdge = null;
        this.isSelected = false;
        this.isSpecialSelected = false;
        this.color = Node.DEFAULT_COLOR;
        this.numEdges = 0;
        this.isVisible = true;
    }
    
    public boolean equals(final Object compareNode) {
        if (compareNode instanceof Node) {
            return this.location.equals(((Node)compareNode).getLocation());
        }
        if (compareNode instanceof Point) {
            return this.location.equals(compareNode);
        }
        return compareNode instanceof Location && this.location.equals(compareNode);
    }
    
    public boolean contains(final Point p, final int radius) {
        final int distance = (p.x - this.location.intX()) * (p.x - this.location.intX()) + (p.y - this.location.intY()) * (p.y - this.location.intY());
        return distance <= radius * radius;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public boolean isSelected() {
        return this.isSelected;
    }
    
    public boolean isSpecialSelected() {
        return this.isSpecialSelected;
    }
    
    public boolean hasNoIncidentEdges() {
        return this.accessEdge == null;
    }
    
    public boolean hasOnlyOneIncidentEdge() {
        return !this.hasNoIncidentEdges() && this.accessEdge.getNextInOrderFrom(this) == this.accessEdge;
    }
    
    public boolean hasOnlyTwoIncidentEdges() {
        return !this.hasNoIncidentEdges() && !this.hasOnlyOneIncidentEdge() && this.accessEdge.getNextInOrderFrom(this).getNextInOrderFrom(this) == this.accessEdge;
    }
    
    public Vector incidentEdges() {
        final Vector edgeVector = new Vector(this.numEdges);
        if (this.accessEdge != null) {
            Edge currentEdge = this.accessEdge;
            do {
                edgeVector.addElement(currentEdge);
                currentEdge = (Edge)currentEdge.getNextInOrderFrom(this);
            } while (currentEdge != this.accessEdge);
        }
        return edgeVector;
    }
    
    public EdgeIterator incidentEdgesIterator() {
        return new EdgeIterator(this, this.accessEdge);
    }
    
    public Vector incidentEdgesInReverse() {
        final Vector edgeVector = new Vector(this.numEdges);
        if (this.accessEdge != null) {
            Edge currentEdge = (Edge)this.accessEdge.getPreviousInOrderFrom(this);
            do {
                edgeVector.addElement(currentEdge);
                currentEdge = (Edge)currentEdge.getPreviousInOrderFrom(this);
            } while (currentEdge != this.accessEdge.getPreviousInOrderFrom(this));
        }
        return edgeVector;
    }
    
    public EdgeIterator incidentEdgeInReverseIterator() {
        return new EdgeIterator(this, this.accessEdge);
    }
    
    public Vector incidentOutgoingEdges() {
        final Vector edgeVector = new Vector(this.numEdges);
        if (this.accessEdge != null) {
            Edge currentEdge = this.accessEdge;
            do {
                if (currentEdge.getDirectedSourceNode() == null || currentEdge.getDirectedSourceNode() == this) {
                    edgeVector.addElement(currentEdge);
                }
                currentEdge = (Edge)currentEdge.getNextInOrderFrom(this);
            } while (currentEdge != this.accessEdge);
        }
        return edgeVector;
    }
    
    public EdgeIterator incidentOutgoingEdgesIterator() {
        return new EdgeIterator(this, this.accessEdge);
    }
    
    public EdgeInterface incidentEdgeWith(final NodeInterface aNode) {
        final Vector edges = this.incidentEdges();
        Edge returnEdge = null;
        for (int i = 0; i < edges.size(); ++i) {
            final Edge currentEdge = edges.elementAt(i);
            if (currentEdge.otherEndFrom(this) == aNode) {
                returnEdge = currentEdge;
            }
        }
        return returnEdge;
    }
    
    public void setLabel(final String newLabel) {
        this.label = newLabel;
    }
    
    public void appendLabel(final String newLabel) {
        this.label = String.valueOf(this.label) + newLabel;
    }
    
    public void setLocation(final Location aLocation) {
        this.location = new Location(aLocation);
    }
    
    public void setLocation(final int x, final int y) {
        this.location = new Location(x, y);
    }
    
    public void setLocation(final double x, final double y) {
        this.location = new Location(x, y);
    }
    
    public void translate(final int transX, final int transY) {
        this.location = new Location(this.location.intX() + transX, this.location.intY() + transY);
    }
    
    public void rotate(final Location referencePoint, final double angle) {
        final double cos = Math.cos(Math.toRadians(angle));
        final double sin = Math.sin(Math.toRadians(angle));
        final double tempX = this.location.doubleX() - referencePoint.doubleX();
        final double tempY = this.location.doubleY() - referencePoint.doubleY();
        this.location.setX(cos * tempX - sin * tempY + referencePoint.doubleX());
        this.location.setY(sin * tempX + cos * tempY + referencePoint.doubleY());
    }
    
    public static double angleBetween(final Location p1, final Location p2, final Location p3) {
        final double ax = p1.intX();
        final double ay = p1.intY();
        final double bx = p2.intX();
        final double by = p2.intY();
        final double cx = p3.intX();
        final double cy = p3.intY();
        final double crossProduct = (ax - bx) * (cy - by) - (ay - by) * (cx - bx);
        final double dotProduct = (ax - bx) * (cx - bx) + (ay - by) * (cy - by);
        double angle;
        final double tan = angle = Math.abs(Math.toDegrees(Math.atan(crossProduct / dotProduct)));
        if (dotProduct < 0.0) {
            angle = 180.0 - angle;
        }
        if (crossProduct < 0.0) {
            angle *= -1.0;
        }
        return angle;
    }
    
    public static double angleBetween(final Node node1, final Node node2, final Node node3, final Node node4) {
        final double x1 = node1.location.intX();
        final double x2 = node2.location.intX();
        final double x3 = node3.location.intX();
        final double x4 = node4.location.intX();
        final double y1 = node1.location.intY();
        final double y2 = node2.location.intY();
        final double y3 = node3.location.intY();
        final double y4 = node4.location.intY();
        double ax;
        double ay;
        double bx;
        double by;
        double cx;
        double cy;
        if (x1 == x3 && y1 == y3) {
            ax = x2;
            ay = y2;
            bx = x1;
            by = y1;
            cx = x4;
            cy = y4;
        }
        else if (x1 == x4 && y1 == y4) {
            ax = x2;
            ay = y2;
            bx = x1;
            by = y1;
            cx = x3;
            cy = y3;
        }
        else if (x2 == x3 && y2 == y3) {
            ax = x1;
            ay = y1;
            bx = x2;
            by = y2;
            cx = x4;
            cy = y4;
        }
        else if (x2 == x4 && y2 == y4) {
            ax = x1;
            ay = y1;
            bx = x2;
            by = y2;
            cx = x3;
            cy = y3;
        }
        else {
            final double numerator = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
            final double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
            if (denominator == 0.0) {
                return 0.0;
            }
            final double temp = numerator / denominator;
            ax = x1;
            ay = y1;
            bx = x1 + temp * (x2 - x1);
            by = y1 + temp * (y2 - y1);
            cx = x3;
            cy = y3;
        }
        final double crossProduct = (ax - bx) * (cy - by) - (ay - by) * (cx - bx);
        final double dotProduct = (ax - bx) * (cx - bx) + (ay - by) * (cy - by);
        double angle;
        final double tan = angle = Math.abs(Math.toDegrees(Math.atan(crossProduct / dotProduct)));
        if (dotProduct < 0.0) {
            angle = 180.0 - angle;
        }
        if (crossProduct < 0.0) {
            angle *= -1.0;
        }
        return angle;
    }
    
    public void scaleBy(final double minX, final double minY, final double xFactor, final double yFactor) {
        double temp = xFactor * (this.location.doubleX() - minX);
        double tempX = this.location.doubleX();
        double tempY = this.location.doubleY();
        if (temp > Node.MIN_FOR_SCALE) {
            tempX = minX + temp;
        }
        temp = yFactor * (this.location.doubleY() - minY);
        if (temp > Node.MIN_FOR_SCALE) {
            tempY = minY + temp;
        }
        this.location = new Location(tempX, tempY);
    }
    
    public void setSelected(final boolean state) {
        this.isSelected = state;
    }
    
    public void setSpecialSelected(final boolean state) {
        this.isSpecialSelected = state;
    }
    
    public void toggleSelected() {
        this.isSelected = !this.isSelected;
    }
    
    public void toggleSpecialSelected() {
        this.isSpecialSelected = !this.isSpecialSelected;
    }
    
    public Edge getAccessEdge() {
        return this.accessEdge;
    }
    
    public void setAccessEdge(final Edge aEdge) {
        this.accessEdge = aEdge;
    }
    
    public boolean hasEdge(final EdgeInterface edge) {
        return this.incidentEdges().contains(edge);
    }
    
    public boolean addIncidentEdge(final EdgeInterface edge) {
        final Edge e = (Edge)edge;
        if (!this.incidentEdges().contains(e)) {
            this.addIncidentEdgeNoCheck(e);
            return true;
        }
        return false;
    }
    
    public void addIncidentEdgeNoCheck(final EdgeInterface edge) {
        final Edge e = (Edge)edge;
        ++this.numEdges;
        if (this.accessEdge == null) {
            (this.accessEdge = e).setNextInOrderFrom(this, this.accessEdge);
            this.accessEdge.setPreviousInOrderFrom(this, this.accessEdge);
        }
        else if (this.accessEdge.getNextInOrderFrom(this) == this.accessEdge) {
            this.accessEdge.setNextInOrderFrom(this, e);
            this.accessEdge.setPreviousInOrderFrom(this, e);
            e.setNextInOrderFrom(this, this.accessEdge);
            e.setPreviousInOrderFrom(this, this.accessEdge);
        }
        else {
            final Edge prev = (Edge)this.accessEdge.getPreviousInOrderFrom(this);
            this.accessEdge.setPreviousInOrderFrom(this, e);
            e.setNextInOrderFrom(this, this.accessEdge);
            e.setPreviousInOrderFrom(this, prev);
            prev.setNextInOrderFrom(this, e);
        }
    }
    
    public void addEdgeBetween(final EdgeInterface edge, final EdgeInterface prev, final EdgeInterface next) {
        ++this.numEdges;
        prev.setNextInOrderFrom(this, edge);
        edge.setPreviousInOrderFrom(this, prev);
        edge.setNextInOrderFrom(this, next);
        next.setPreviousInOrderFrom(this, edge);
        if (this.accessEdge == null) {
            if (edge instanceof Edge) {
                this.accessEdge = (Edge)edge;
            }
            else {
                this.accessEdge = ((EdgeExtender)edge).getRef();
            }
        }
    }
    
    public void resetIncidentEdges() {
        this.numEdges = 0;
        this.accessEdge = null;
    }
    
    public void deleteIncidentEdge(final EdgeInterface edge) {
        final Edge e = (Edge)edge;
        --this.numEdges;
        if (e.getNextInOrderFrom(this) == e) {
            this.accessEdge = null;
        }
        else if (e.getNextInOrderFrom(this).getNextInOrderFrom(this) == e) {
            (this.accessEdge = (Edge)e.getNextInOrderFrom(this)).setNextInOrderFrom(this, this.accessEdge);
            this.accessEdge.setPreviousInOrderFrom(this, this.accessEdge);
        }
        else {
            if (e == this.accessEdge) {
                this.accessEdge = (Edge)this.accessEdge.getNextInOrderFrom(this);
            }
            final Edge prev = (Edge)e.getPreviousInOrderFrom(this);
            final Edge next = (Edge)e.getNextInOrderFrom(this);
            prev.setNextInOrderFrom(this, next);
            next.setPreviousInOrderFrom(this, prev);
        }
    }
    
    public double distanceSquaredFrom(final Node otherNode) {
        final double ax = this.location.doubleX();
        final double ay = this.location.doubleY();
        final double bx = otherNode.location.doubleX();
        final double by = otherNode.location.doubleY();
        return Math.pow(bx - ax, 2.0) + Math.pow(by - ay, 2.0);
    }
    
    public String toString() {
        return String.valueOf(this.label) + "(" + this.location.intX() + "," + this.location.intY() + ")";
    }
    
    public void printAll() {
        final Vector incidentEdges = this.incidentEdges();
        System.out.print("** " + this + " ** ");
        for (int i = 0; i < incidentEdges.size(); ++i) {
            System.out.print(incidentEdges.elementAt(i));
            if (i < incidentEdges.size() - 1) {
                System.out.print(", ");
            }
            else {
                System.out.print("\n");
            }
        }
    }
    
    public Vector neighbours() {
        final Vector result = new Vector(this.numEdges);
        final Enumeration edges = this.incidentEdges().elements();
        while (edges.hasMoreElements()) {
            result.addElement(edges.nextElement().otherEndFrom(this));
        }
        return result;
    }
    
    public void draw(final Graphics2D g2, final boolean drawSelected, final boolean showCoord, final boolean showLabel) {
        this.draw(g2, 0, 0, drawSelected, showCoord, showLabel);
    }
    
    public void draw(final Graphics2D g2, final int xOffset, final int yOffset, final boolean drawSelected, final boolean showCoord, final boolean showLabel) {
        if (this.isVisible) {
            g2.setStroke(new BasicStroke((float)Node.LINE_THICKNESS));
            g2.setColor(this.color);
            final Location aLocation = new Location(this.location.intX() + xOffset, this.location.intY() + yOffset);
            g2.fill(new Ellipse2D.Double(aLocation.intX() - Node.RADIUS, aLocation.intY() - Node.RADIUS, Node.RADIUS * 2, Node.RADIUS * 2));
            g2.setColor(Color.black);
            g2.draw(new Ellipse2D.Double(aLocation.intX() - Node.RADIUS, aLocation.intY() - Node.RADIUS, Node.RADIUS * 2, Node.RADIUS * 2));
            if (this.isSpecialSelected) {
                final float[] dash1 = { (float)Node.DASH_LENGTH };
                g2.setStroke(new BasicStroke((float)Node.LINE_THICKNESS, 0, 0, 10.0f, dash1, 0.0f));
                g2.setColor(Node.SPECIAL_SELECTED_COLOR);
                g2.draw(new Ellipse2D.Double(aLocation.intX() - (Node.RADIUS + 2), aLocation.intY() - (Node.RADIUS + 2), (Node.RADIUS + 2) * 2, (Node.RADIUS + 2) * 2));
            }
            else if (this.isSelected && drawSelected) {
                final float[] dash1 = { (float)Node.DASH_LENGTH };
                g2.setStroke(new BasicStroke((float)Node.LINE_THICKNESS, 0, 0, 10.0f, dash1, 0.0f));
                g2.setColor(Node.SELECTED_COLOR);
                g2.draw(new Ellipse2D.Double(aLocation.intX() - (Node.RADIUS + 2), aLocation.intY() - (Node.RADIUS + 2), (Node.RADIUS + 2) * 2, (Node.RADIUS + 2) * 2));
            }
            if (this.drawX) {
                g2.setStroke(new BasicStroke(Node.LINE_THICKNESS * 2.0f / 3.0f));
                g2.setColor(Color.black);
                g2.drawLine(aLocation.intX() - Node.RADIUS, aLocation.intY() - Node.RADIUS, aLocation.intX() + Node.RADIUS, aLocation.intY() + Node.RADIUS);
                g2.drawLine(aLocation.intX() - Node.RADIUS, aLocation.intY() + Node.RADIUS, aLocation.intX() + Node.RADIUS, aLocation.intY() - Node.RADIUS);
            }
            g2.setStroke(new BasicStroke((float)Node.LINE_THICKNESS));
            if (showLabel) {
                if (this.label.length() > 0) {
                    Node.thisTl = new TextLayout(this.label, Node.drawTextFont, g2.getFontRenderContext());
                    if (Node.OPAQUE_TEXT) {
                        final Rectangle2D bounds = Node.thisTl.getBounds();
                        g2.setColor(GraphEditor.backgroundColor);
                        g2.fill(new Rectangle2D.Double(aLocation.intX() + Node.RADIUS + 3, aLocation.intY() + Node.RADIUS + 2 - bounds.getHeight() + 1.0, bounds.getWidth() + 2.0, bounds.getHeight()));
                    }
                    g2.setColor(Node.TEXT_COLOR);
                    Node.thisTl.draw(g2, (float)(aLocation.intX() + Node.RADIUS + 3), (float)(aLocation.intY() + Node.RADIUS + 2));
                }
            }
            else if (showCoord) {
                Node.thisTl = new TextLayout(String.valueOf(String.valueOf(this.location.intX())) + ", " + String.valueOf(this.location.intY()), Node.drawTextFont, g2.getFontRenderContext());
                if (Node.OPAQUE_TEXT) {
                    final Rectangle2D bounds = Node.thisTl.getBounds();
                    g2.setColor(GraphEditor.backgroundColor);
                    g2.fill(new Rectangle2D.Double(aLocation.intX() + Node.RADIUS + 3, aLocation.intY() + Node.RADIUS + 2 - bounds.getHeight() + 1.0, bounds.getWidth() + 2.0, bounds.getHeight()));
                }
                g2.setColor(Node.TEXT_COLOR);
                Node.thisTl.draw(g2, (float)(aLocation.intX() + Node.RADIUS + 3), (float)(aLocation.intY() + Node.RADIUS + 2));
            }
        }
    }
    
    public void saveTo(final PrintWriter aFile) {
        aFile.println(this.index);
        aFile.println(this.location.doubleX());
        aFile.println(this.location.doubleY());
        aFile.println(this.label);
        aFile.println(this.color.getRGB());
        final Vector incidentEdges = this.incidentEdges();
        for (int i = 0; i < incidentEdges.size(); ++i) {
            aFile.print(incidentEdges.elementAt(i).getIndex());
            if (i < incidentEdges.size() - 1) {
                aFile.print(",");
            }
        }
        aFile.println();
    }
    
    public static Node loadFrom(final BufferedReader aFile, final Vector edgeIndices) throws IOException {
        final Node aNode = new Node();
        aNode.setIndex(Integer.valueOf(aFile.readLine()));
        aNode.setLocation(new Location(Double.valueOf(aFile.readLine()), Double.valueOf(aFile.readLine())));
        aNode.setLabel(aFile.readLine());
        aNode.setColor(new Color(Integer.valueOf(aFile.readLine())));
        final String edgeIndexString = aFile.readLine();
        final StringTokenizer st = new StringTokenizer(edgeIndexString, ",");
        final Vector indices = new Vector();
        while (st.hasMoreTokens()) {
            indices.addElement(Integer.valueOf(st.nextToken()));
        }
        edgeIndices.addElement(indices);
        return aNode;
    }
    
    public void setExtender(final NodeExtender ex) {
        this.extender = ex;
    }
    
    public NodeExtender getExtender() {
        return this.extender;
    }
    
    public Node getNode() {
        return this;
    }
}
