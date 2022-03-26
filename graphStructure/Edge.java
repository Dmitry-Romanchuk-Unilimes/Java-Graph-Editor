// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class Edge implements EdgeInterface
{
    public static int THICKNESS;
    public static int GENERATED_DASH_LENGTH;
    public static int SELECTED_DASH_LENGTH;
    public static int ARROW_WIDTH;
    public static int ARROW_HEIGHT;
    public static double CURVE_INTERVAL;
    public static Color DEFAULT_COLOR;
    public static Color SELECTED_COLOR;
    private Color color;
    private boolean isSelected;
    private boolean isAdded;
    private boolean isGenerated;
    private boolean isCurved;
    private boolean isOrthogonal;
    private boolean isOrthogonalLeftFromStart;
    private HalfEdge startEdge;
    private HalfEdge endEdge;
    private EdgeInterface copy;
    private Node directedSourceNode;
    private Location centerLocation;
    private double startControlAngle;
    private double endControlAngle;
    private int index;
    private EdgeExtender extender;
    private boolean isVisible;
    
    static {
        Edge.THICKNESS = 4;
        Edge.GENERATED_DASH_LENGTH = 10;
        Edge.SELECTED_DASH_LENGTH = 2;
        Edge.ARROW_WIDTH = 6;
        Edge.ARROW_HEIGHT = 20;
        Edge.CURVE_INTERVAL = 0.01;
        Edge.DEFAULT_COLOR = Color.blue;
        Edge.SELECTED_COLOR = Color.black;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color aColor) {
        this.color = aColor;
    }
    
    public void setIsAdded(final boolean aAdded) {
        this.isAdded = aAdded;
    }
    
    public boolean isAdded() {
        return this.isAdded;
    }
    
    public boolean isGenerated() {
        return this.isGenerated;
    }
    
    public void setIsGenerated(final boolean generated) {
        this.isGenerated = generated;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public boolean isCurved() {
        return this.isCurved;
    }
    
    public void setIsCurved(final boolean curved) {
        this.isCurved = curved;
    }
    
    public void makeCurved() {
        this.isCurved = true;
        this.isOrthogonal = false;
        this.initCurveAngles();
    }
    
    public boolean isOrthogonal() {
        return this.isOrthogonal;
    }
    
    public void setIsOrthogonal(final boolean orth) {
        this.isOrthogonal = orth;
    }
    
    public void makeOrthogonal() {
        this.isOrthogonal = true;
        this.isCurved = false;
        this.initOrthogonalBendLocation();
    }
    
    private void initOrthogonalBendLocation() {
        final Location center = this.getOrthogonalLocation();
        if (center == null) {
            this.makeStraight();
        }
        else {
            this.centerLocation = center;
        }
    }
    
    public void setCopy(final EdgeInterface aCopy) {
        this.copy = aCopy;
    }
    
    public EdgeInterface getCopy() {
        return this.copy;
    }
    
    public EdgeInterface getMasterCopy() {
        if (this.copy == null) {
            return null;
        }
        Edge cp;
        for (cp = this; cp.copy != null; cp = (Edge)cp.copy) {}
        return cp;
    }
    
    public void setIsVisible(final boolean v) {
        this.isVisible = v;
    }
    
    protected Edge(final NodeInterface start, final NodeInterface end) {
        this.initialize(start, end);
        this.color = Edge.DEFAULT_COLOR;
        this.centerLocation = new Location((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
    }
    
    public Edge(final Edge anEdge, final NodeInterface dNode, final NodeInterface start, final NodeInterface end) {
        this.initialize(start, end);
        if (anEdge.color != null) {
            this.color = new Color(anEdge.color.getRGB());
        }
        else {
            this.color = Edge.DEFAULT_COLOR;
        }
        this.isSelected = anEdge.isSelected;
        this.isGenerated = anEdge.isGenerated;
        this.isCurved = anEdge.isCurved;
        this.isOrthogonal = anEdge.isOrthogonal;
        this.isOrthogonalLeftFromStart = anEdge.isOrthogonalLeftFromStart;
        this.centerLocation = new Location(anEdge.getCenterLocation());
        this.startControlAngle = anEdge.startControlAngle;
        this.endControlAngle = anEdge.endControlAngle;
        this.directedSourceNode = (Node)dNode;
        this.isVisible = anEdge.isVisible;
    }
    
    public boolean equals(final Object object) {
        try {
            final Edge compareEdge = (Edge)object;
            return (((Node)this.getStartNode()).equals(compareEdge.getStartNode()) && ((Node)this.getEndNode()).equals(compareEdge.getEndNode())) || (((Node)this.getEndNode()).equals(compareEdge.getStartNode()) && ((Node)this.getStartNode()).equals(compareEdge.getEndNode()));
        }
        catch (ClassCastException cce) {
            return false;
        }
    }
    
    public Node getDirectedSourceNode() {
        return this.directedSourceNode;
    }
    
    public void setDirectedFrom(final NodeInterface directedSourceNode) {
        if (directedSourceNode == null) {
            this.directedSourceNode = null;
        }
        else if (this.getStartNode() == directedSourceNode || this.getEndNode() == directedSourceNode) {
            this.directedSourceNode = (Node)directedSourceNode;
        }
    }
    
    public boolean isBetween(final NodeInterface firstNode, final NodeInterface secondNode) {
        return (((Node)this.getStartNode()).equals(firstNode) && ((Node)this.getEndNode()).equals(secondNode)) || (((Node)this.getStartNode()).equals(secondNode) && ((Node)this.getEndNode()).equals(firstNode));
    }
    
    private void initialize(final NodeInterface start, final NodeInterface end) {
        this.isSelected = false;
        this.isGenerated = false;
        this.isCurved = false;
        this.isOrthogonal = false;
        this.isOrthogonalLeftFromStart = false;
        this.isAdded = false;
        this.startEdge = null;
        this.endEdge = null;
        this.startControlAngle = 0.0;
        this.endControlAngle = 0.0;
        this.startEdge = new HalfEdge((Node)start, this, null);
        this.endEdge = new HalfEdge((Node)end, this, this.startEdge);
        this.startEdge.setTwinEdge(this.endEdge);
        this.isVisible = true;
    }
    
    public NodeInterface getStartNode() {
        return this.startEdge.getSourceNode();
    }
    
    public NodeInterface getEndNode() {
        return this.endEdge.getSourceNode();
    }
    
    public HalfEdge getStartHalfEdge() {
        return this.startEdge;
    }
    
    public HalfEdge getEndHalfEdge() {
        return this.endEdge;
    }
    
    public HalfEdge getHalfEdgeFrom(final Node aNode) {
        if (aNode == this.getStartNode()) {
            return this.startEdge;
        }
        if (aNode == this.getEndNode()) {
            return this.endEdge;
        }
        return null;
    }
    
    public HalfEdge getHalfEdgeTo(final Node aNode) {
        if (aNode == this.getEndNode()) {
            return this.startEdge;
        }
        if (aNode == this.getStartNode()) {
            return this.endEdge;
        }
        return null;
    }
    
    public void setNextInOrderFrom(final NodeInterface sourceNode, final EdgeInterface nextEdge) {
        this.getHalfEdgeFrom((Node)sourceNode).setPrevious(((Edge)nextEdge).getHalfEdgeTo((Node)sourceNode));
    }
    
    public void setPreviousInOrderFrom(final NodeInterface sourceNode, final EdgeInterface prevEdge) {
        this.getHalfEdgeTo((Node)sourceNode).setNext(((Edge)prevEdge).getHalfEdgeFrom((Node)sourceNode));
    }
    
    public EdgeInterface getNextInOrderFrom(final NodeInterface sourceNode) {
        return this.getHalfEdgeFrom((Node)sourceNode).getPrevious().getParentEdge();
    }
    
    public EdgeInterface getPreviousInOrderFrom(final NodeInterface sourceNode) {
        return this.getHalfEdgeTo((Node)sourceNode).getNext().getParentEdge();
    }
    
    public int getLowerIndex() {
        return Math.min(((Node)this.getStartNode()).getIndex(), ((Node)this.getEndNode()).getIndex());
    }
    
    public int getHigherIndex() {
        return Math.max(((Node)this.getStartNode()).getIndex(), ((Node)this.getEndNode()).getIndex());
    }
    
    public boolean isSelected() {
        return this.isSelected;
    }
    
    public void setSelected(final boolean state) {
        this.isSelected = state;
    }
    
    public void toggleSelected() {
        this.isSelected = !this.isSelected;
    }
    
    public String toString() {
        return String.valueOf(((Node)this.getStartNode()).toString()) + " --> " + ((Node)this.getEndNode()).toString();
    }
    
    public NodeInterface otherEndFrom(final NodeInterface aNode) {
        if (this.getStartNode() == aNode) {
            return this.getEndNode();
        }
        return this.getStartNode();
    }
    
    public Vector edgesFromSameCycle() {
        final Vector edgeVector = new Vector();
        HalfEdge he = this.startEdge;
        do {
            edgeVector.addElement(he.getParentEdge());
            he = he.getNext();
        } while (he != this.startEdge);
        return edgeVector;
    }
    
    public Vector edgesFromSameCycleOnOtherSide() {
        final Vector edgeVector = new Vector();
        HalfEdge he = this.endEdge;
        do {
            edgeVector.addElement(he.getParentEdge());
            he = he.getNext();
        } while (he != this.endEdge);
        return edgeVector;
    }
    
    public boolean isDirected() {
        return this.directedSourceNode != null;
    }
    
    public QuadCurve2D.Double getQuadCurve() {
        final Location sLocation = ((Node)this.getStartNode()).getLocation();
        final Location eLocation = ((Node)this.getEndNode()).getLocation();
        return new QuadCurve2D.Double(sLocation.intX(), sLocation.intY(), this.centerLocation.intX() * 2 - sLocation.intX() / 2 - eLocation.intX() / 2, this.centerLocation.intY() * 2 - sLocation.intY() / 2 - eLocation.intY() / 2, eLocation.intX(), eLocation.intY());
    }
    
    public void draw(final Graphics2D g2, final boolean drawSelected) {
        this.draw(g2, 0, 0, drawSelected);
    }
    
    public void draw(final Graphics2D g2, final int xOffset, final int yOffset, final boolean drawSelected) {
        if (this.isVisible && !this.getStartNode().getLocation().equals(this.getEndNode().getLocation())) {
            Location sLocation = ((Node)this.getStartNode()).getLocation();
            Location eLocation = ((Node)this.getEndNode()).getLocation();
            Location cLocation = this.centerLocation;
            sLocation = new Location(sLocation.intX() + xOffset, sLocation.intY() + yOffset);
            eLocation = new Location(eLocation.intX() + xOffset, eLocation.intY() + yOffset);
            cLocation = new Location(cLocation.intX() + xOffset, cLocation.intY() + yOffset);
            final float[] dash1 = { (float)Edge.SELECTED_DASH_LENGTH };
            if (this.isSelected && drawSelected) {
                g2.setStroke(new BasicStroke(2.0f * Edge.THICKNESS, 0, 0, 10.0f, dash1, 0.0f));
                g2.setColor(Edge.SELECTED_COLOR);
                if (this.isCurved) {
                    this.drawCurved(g2, sLocation, eLocation, cLocation);
                }
                else if (this.isOrthogonal) {
                    this.drawOrthogonal(g2, sLocation, eLocation, cLocation);
                }
                else {
                    this.drawStraight(g2, sLocation, eLocation);
                }
            }
            g2.setColor(this.color);
            if (this.isGenerated) {
                dash1[0] = (float)Edge.GENERATED_DASH_LENGTH;
                g2.setStroke(new BasicStroke((float)Edge.THICKNESS, 0, 0, 10.0f, dash1, 0.0f));
            }
            else {
                g2.setStroke(new BasicStroke((float)Edge.THICKNESS));
            }
            if (this.isCurved) {
                this.drawCurved(g2, sLocation, eLocation, cLocation);
            }
            else if (this.isOrthogonal) {
                this.drawOrthogonal(g2, sLocation, eLocation, cLocation);
            }
            else {
                this.drawStraight(g2, sLocation, eLocation);
            }
            g2.setStroke(new BasicStroke((float)Edge.THICKNESS));
            g2.setColor(this.color);
            if (this.isDirected()) {
                Location dLocation = this.directedSourceNode.getLocation();
                dLocation = new Location(dLocation.intX() + xOffset, dLocation.intY() + yOffset);
                this.drawDirected(g2, xOffset, yOffset);
            }
            else {
                if (this.isSelected && drawSelected) {
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.setColor(Edge.SELECTED_COLOR);
                    g2.fill(new Ellipse2D.Double(cLocation.intX() - (Edge.THICKNESS + 1), cLocation.intY() - (Edge.THICKNESS + 1), (Edge.THICKNESS + 1) * 2, (Edge.THICKNESS + 1) * 2));
                    g2.setColor(this.color);
                }
                g2.fill(new Ellipse2D.Double(cLocation.intX() - Edge.THICKNESS, cLocation.intY() - Edge.THICKNESS, Edge.THICKNESS * 2, Edge.THICKNESS * 2));
            }
        }
    }
    
    private void drawDirected(final Graphics2D g2, final int xOffset, final int yOffset) {
        g2.fill(this.getDirectionArrow(this.directedSourceNode, xOffset, yOffset));
    }
    
    public Polygon getDirectionArrow(final Node directionSource, final int xOffset, final int yOffset) {
        return this.getDirectionArrow(directionSource, xOffset, yOffset, 0, 0);
    }
    
    public Polygon getDirectionArrow(final Node directionSource, final int xOffset, final int yOffset, final int extraWidth, final int extraHeight) {
        final double edgeAngle = -1.0 * this.getDirectedAngle(directionSource);
        double midPointX = this.centerLocation.intX() + xOffset;
        double midPointY = this.centerLocation.intY() + yOffset;
        final int width = Edge.ARROW_WIDTH + extraWidth;
        final int height = Edge.ARROW_HEIGHT + extraHeight;
        midPointX = (int)Math.round(midPointX - height / 2.0 * Math.cos(Math.toRadians(edgeAngle)));
        midPointY = (int)Math.round(midPointY - height / 2.0 * Math.sin(Math.toRadians(edgeAngle)));
        final int[] triX = new int[3];
        final int[] triY = new int[3];
        triX[0] = (int)Math.round(midPointX + width * Math.cos(Math.toRadians(edgeAngle + 90.0)));
        triX[1] = (int)Math.round(midPointX + width * Math.cos(Math.toRadians(edgeAngle - 90.0)));
        triX[2] = (int)Math.round(midPointX + height * Math.cos(Math.toRadians(edgeAngle)));
        triY[0] = (int)Math.round(midPointY + width * Math.sin(Math.toRadians(edgeAngle + 90.0)));
        triY[1] = (int)Math.round(midPointY + width * Math.sin(Math.toRadians(edgeAngle - 90.0)));
        triY[2] = (int)Math.round(midPointY + height * Math.sin(Math.toRadians(edgeAngle)));
        return new Polygon(triX, triY, 3);
    }
    
    private void drawCurved(final Graphics2D g2, final Location sLocation, final Location eLocation, final Location cLocation) {
        g2.draw(new QuadCurve2D.Double(sLocation.intX(), sLocation.intY(), cLocation.intX() * 2 - sLocation.intX() / 2 - eLocation.intX() / 2, cLocation.intY() * 2 - sLocation.intY() / 2 - eLocation.intY() / 2, eLocation.intX(), eLocation.intY()));
    }
    
    public QuadCurve2D.Double getCurve() {
        return this.getCurve(0, 0);
    }
    
    public QuadCurve2D.Double getCurve(final int xOffset, final int yOffset) {
        final int[] cPoint = { this.getStartNode().getLocation().intX() + xOffset, this.getStartNode().getLocation().intY() + yOffset, 0, 0, this.getEndNode().getLocation().intX() + xOffset, this.getEndNode().getLocation().intY() + yOffset };
        cPoint[2] = (this.centerLocation.intX() + xOffset) * 2 - cPoint[0] / 2 - cPoint[4] / 2;
        cPoint[3] = (this.centerLocation.intY() + yOffset) * 2 - cPoint[1] / 2 - cPoint[5] / 2;
        return new QuadCurve2D.Double(cPoint[0], cPoint[1], cPoint[2], cPoint[3], cPoint[4], cPoint[5]);
    }
    
    public Polygon getBend() {
        return this.getBend(0, 0);
    }
    
    public Polygon getBend(final int xOffset, final int yOffset) {
        final int[] triX = new int[3];
        final int[] triY = new int[3];
        triX[0] = this.getStartNode().getLocation().intX() + xOffset;
        triX[1] = this.centerLocation.intX() + xOffset;
        triX[2] = this.getEndNode().getLocation().intX() + xOffset;
        triY[0] = this.getStartNode().getLocation().intY() + yOffset;
        triY[1] = this.centerLocation.intY() + yOffset;
        triY[2] = this.getEndNode().getLocation().intY() + yOffset;
        return new Polygon(triX, triY, 3);
    }
    
    private void drawStraight(final Graphics2D g2, final Location sLocation, final Location eLocation) {
        g2.draw(new Line2D.Double(sLocation.intX(), sLocation.intY(), eLocation.intX(), eLocation.intY()));
    }
    
    private void drawOrthogonal(final Graphics2D g2, final Location sLocation, final Location eLocation, final Location cLocation) {
        g2.draw(new Line2D.Double(sLocation.intX(), sLocation.intY(), cLocation.intX(), cLocation.intY()));
        g2.draw(new Line2D.Double(cLocation.intX(), cLocation.intY(), eLocation.intX(), eLocation.intY()));
    }
    
    public void saveTo(final PrintWriter aFile) {
        aFile.println(this.index);
        aFile.println(((Node)this.getStartNode()).getIndex());
        aFile.println(((Node)this.getEndNode()).getIndex());
        if (this.isDirected()) {
            aFile.println(this.directedSourceNode.getIndex());
        }
        else {
            aFile.println("-1");
        }
        aFile.println(this.centerLocation.doubleX());
        aFile.println(this.centerLocation.doubleY());
        aFile.println(this.isCurved);
        aFile.println(this.isOrthogonal);
        aFile.println(this.isGenerated);
        aFile.println(this.color.getRGB());
    }
    
    public static Edge loadFrom(final BufferedReader aFile, final Vector nodeVector) throws IOException {
        final int index = Integer.valueOf(aFile.readLine());
        final int startIndex = Integer.valueOf(aFile.readLine());
        final int endIndex = Integer.valueOf(aFile.readLine());
        final Edge anEdge = new Edge(nodeVector.elementAt(startIndex - 1), nodeVector.elementAt(endIndex - 1));
        anEdge.setIndex(index);
        final int directedSourceIndex = Integer.valueOf(aFile.readLine());
        if (directedSourceIndex != -1) {
            if (directedSourceIndex == startIndex) {
                anEdge.setDirectedFrom(anEdge.getStartNode());
            }
            else {
                if (directedSourceIndex != endIndex) {
                    throw new IOException("Direction source was not an end node of an edge");
                }
                anEdge.setDirectedFrom(anEdge.getEndNode());
            }
        }
        anEdge.setCenterLocation(new Location(Double.valueOf(aFile.readLine()), Double.valueOf(aFile.readLine())));
        anEdge.isCurved = Boolean.valueOf(aFile.readLine());
        anEdge.isOrthogonal = Boolean.valueOf(aFile.readLine());
        if (anEdge.isCurved) {
            anEdge.initCurveAngles();
        }
        else {
            anEdge.getClass();
        }
        anEdge.setIsGenerated(Boolean.valueOf(aFile.readLine()));
        anEdge.setColor(new Color(Integer.valueOf(aFile.readLine())));
        return anEdge;
    }
    
    public double getSlope() {
        final double rise = Math.abs(((Node)this.getStartNode()).getLocation().intY() - ((Node)this.getEndNode()).getLocation().intY());
        final double run = Math.abs(((Node)this.getStartNode()).getLocation().intX() - ((Node)this.getEndNode()).getLocation().intX());
        if (run == 0.0) {
            return Double.MAX_VALUE;
        }
        return rise / run;
    }
    
    public double getStraightLength() {
        final int ax = ((Node)this.getStartNode()).getLocation().intX();
        final int ay = ((Node)this.getStartNode()).getLocation().intY();
        final int bx = ((Node)this.getEndNode()).getLocation().intX();
        final int by = ((Node)this.getEndNode()).getLocation().intY();
        return Math.sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay));
    }
    
    public double getLength() {
        if (this.isCurved) {
            double length = 0.0;
            final Location sLocation = ((Node)this.getStartNode()).getLocation();
            Location cLocation = this.centerLocation;
            final Location eLocation = ((Node)this.getEndNode()).getLocation();
            cLocation = new Location(2.0 * cLocation.doubleX() - sLocation.doubleX() / 2.0 - eLocation.doubleX() / 2.0, 2.0 * cLocation.doubleY() - sLocation.doubleY() / 2.0 - eLocation.doubleY() / 2.0);
            double currentX = sLocation.doubleX();
            double currentY = sLocation.doubleY();
            for (double step = 0.0; step < 1.0; step += Edge.CURVE_INTERVAL) {
                final double nextX = (sLocation.doubleX() - 2.0 * cLocation.doubleX() + eLocation.doubleX()) * Math.pow(step, 2.0) + (2.0 * cLocation.doubleX() - 2.0 * sLocation.doubleX()) * step + sLocation.doubleX();
                final double nextY = (sLocation.doubleY() - 2.0 * cLocation.doubleY() + eLocation.doubleY()) * Math.pow(step, 2.0) + (2.0 * cLocation.doubleY() - 2.0 * sLocation.doubleY()) * step + sLocation.doubleY();
                length += Math.sqrt((nextX - currentX) * (nextX - currentX) + (nextY - currentY) * (nextY - currentY));
                currentX = nextX;
                currentY = nextY;
            }
            return length;
        }
        if (this.isOrthogonal) {
            int ax = this.getStartNode().getLocation().intX();
            int ay = this.getStartNode().getLocation().intY();
            int bx = this.centerLocation.intX();
            int by = this.centerLocation.intY();
            final double length2 = Math.sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay));
            ax = this.centerLocation.intX();
            ay = this.centerLocation.intY();
            bx = this.getEndNode().getLocation().intX();
            by = this.getEndNode().getLocation().intY();
            return length2 + Math.sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay));
        }
        return this.getStraightLength();
    }
    
    public void setCenterLocation(final Location aLocation) {
        this.centerLocation = new Location(aLocation);
    }
    
    public void initCurveAngles() {
        this.startControlAngle = Node.angleBetween(this.centerLocation, this.getStartNode().getLocation(), this.getEndNode().getLocation());
        this.endControlAngle = Node.angleBetween(this.centerLocation, this.getEndNode().getLocation(), this.getStartNode().getLocation());
    }
    
    public void translate(final int transX, final int transY) {
        this.centerLocation = new Location(this.centerLocation.intX() + transX, this.centerLocation.intY() + transY);
    }
    
    public void rotate(final Location referencePoint, final double angle) {
        final double cos = Math.cos(Math.toRadians(angle));
        final double sin = Math.sin(Math.toRadians(angle));
        final double tempX = this.centerLocation.doubleX() - referencePoint.doubleX();
        final double tempY = this.centerLocation.doubleY() - referencePoint.doubleY();
        this.centerLocation.setX(cos * tempX - sin * tempY + referencePoint.doubleX());
        this.centerLocation.setY(sin * tempX + cos * tempY + referencePoint.doubleY());
    }
    
    public void scaleBy(final double minX, final double minY, final double xFactor, final double yFactor) {
        double temp = xFactor * (this.centerLocation.doubleX() - minX);
        double tempX = this.centerLocation.doubleX();
        double tempY = this.centerLocation.doubleY();
        if (temp > Node.MIN_FOR_SCALE) {
            tempX = minX + temp;
        }
        temp = yFactor * (this.centerLocation.doubleY() - minY);
        if (temp > Node.MIN_FOR_SCALE) {
            tempY = minY + temp;
        }
        this.centerLocation = new Location(tempX, tempY);
    }
    
    public void update() {
        if (this.isCurved) {
            final Location s = this.getLocationAtAngleFrom((Node)this.getStartNode(), this.startControlAngle);
            final Location e = this.getLocationAtAngleFrom((Node)this.getEndNode(), this.endControlAngle);
            this.centerLocation = getIntersectionLocation(this.getStartNode().getLocation(), s, this.getEndNode().getLocation(), e);
        }
        else if (this.isOrthogonal) {
            this.centerLocation = this.getOrthogonalLocation();
            if (this.centerLocation == null) {
                this.makeStraight();
            }
        }
        else {
            this.centerLocation = this.getNormalLocation();
        }
    }
    
    public static Location getIntersectionLocation(final Location l1, final Location l2, final Location l3, final Location l4) {
        final double x43 = l4.doubleX() - l3.doubleX();
        final double y43 = l4.doubleY() - l3.doubleY();
        final double x44 = l2.doubleX() - l1.doubleX();
        final double y44 = l2.doubleY() - l1.doubleY();
        final double u = (x43 * (l1.doubleY() - l3.doubleY()) - y43 * (l1.doubleX() - l3.doubleX())) / (y43 * x44 - x43 * y44);
        return new Location(l1.doubleX() + u * x44, l1.doubleY() + u * y44);
    }
    
    public Location getNormalLocation() {
        return new Location((this.getStartNode().getX() + this.getEndNode().getX()) / 2, (this.getStartNode().getY() + this.getEndNode().getY()) / 2);
    }
    
    public Location getOrthogonalLocation() {
        final double turnOrientation = this.getTurnOrientation();
        final Location s = this.getStartNode().getLocation();
        final Location e = this.getEndNode().getLocation();
        final Location c = this.getCenterLocation();
        if (s.intX() == e.intX() || s.intY() == e.intY()) {
            return null;
        }
        if (this.isLeftTurn(turnOrientation)) {
            this.isOrthogonalLeftFromStart = true;
            if ((s.intX() < e.intX() && s.intY() < e.intY()) || (s.intX() > e.intX() && s.intY() > e.intY())) {
                return new Location(e.intX(), s.intY());
            }
            return new Location(s.intX(), e.intY());
        }
        else {
            if (!this.isRightTurn(turnOrientation)) {
                return null;
            }
            this.isOrthogonalLeftFromStart = false;
            if ((s.intX() < e.intX() && s.intY() > e.intY()) || (s.intX() > e.intX() && s.intY() < e.intY())) {
                return new Location(e.intX(), s.intY());
            }
            return new Location(s.intX(), e.intY());
        }
    }
    
    public double getTurnOrientation() {
        final Location s = this.getStartNode().getLocation();
        final Location e = this.getEndNode().getLocation();
        final Location c = this.getCenterLocation();
        return (e.doubleX() - s.doubleX()) * (c.doubleY() - s.doubleY()) - (e.doubleY() - s.doubleY()) * (c.doubleX() - s.doubleX());
    }
    
    public boolean isLeftTurn(final double turnOrientation) {
        return turnOrientation < 0.0;
    }
    
    public boolean isRightTurn(final double turnOrientation) {
        return turnOrientation > 0.0;
    }
    
    public Location getCenterLocation() {
        return this.centerLocation;
    }
    
    public void makeStraight() {
        this.isCurved = false;
        this.isOrthogonal = false;
        this.update();
    }
    
    public void setExtender(final EdgeExtender ex) {
        this.extender = ex;
    }
    
    public EdgeExtender getExtender() {
        return this.extender;
    }
    
    public boolean hasZeroLength() {
        return this.getStartNode().getLocation().equals(this.getEndNode().getLocation());
    }
    
    public boolean intersects(final Edge edge) {
        if (this.hasZeroLength()) {
            if (edge.hasZeroLength() && this.getStartNode().getLocation().equals(edge.getStartNode().getLocation())) {
                return true;
            }
            if (this.getStartNode().getLocation().equals(edge.getStartNode().getLocation()) || this.getStartNode().getLocation().equals(edge.getEndNode().getLocation())) {
                return true;
            }
        }
        final int x1 = this.getStartNode().getX();
        final int y1 = this.getStartNode().getY();
        final int x2 = this.getEndNode().getX();
        final int y2 = this.getEndNode().getY();
        final int x3 = edge.getStartNode().getX();
        final int y3 = edge.getStartNode().getY();
        final int x4 = edge.getEndNode().getX();
        final int y4 = edge.getEndNode().getY();
        final double x5 = x2 - x1;
        final double y5 = y2 - y1;
        final double x6 = x4 - x3;
        final double y6 = y4 - y3;
        final double x7 = x3 - x1;
        final double y7 = y3 - y1;
        final double denominator = x5 * y6 - y5 * x6;
        final double numerator1 = x7 * y6 - y7 * x6;
        final double numerator2 = x7 * y5 - y7 * x5;
        if (denominator != 0.0) {
            final double determinant1 = numerator1 / denominator;
            final double determinant2 = numerator2 / denominator;
            return determinant1 >= 0.0 && determinant1 <= 1.0 && determinant2 >= 0.0 && determinant2 <= 1.0 && (x1 != x3 || y1 != y3) && (x1 != x4 || y1 != y4) && (x2 != x3 || y2 != y3) && (x2 != x4 || y2 != y4);
        }
        if (numerator1 != 0.0 && numerator2 != 0.0) {
            return false;
        }
        if (x1 != x2) {
            final double minX1 = Math.min(x1, x2);
            final double maxX1 = Math.max(x1, x2);
            final double minX2 = Math.min(x3, x4);
            final double maxX2 = Math.max(x3, x4);
            return maxX2 >= minX1 && minX2 <= maxX1 && maxX2 != minX1 && minX2 != maxX1;
        }
        final double minY1 = Math.min(y1, y2);
        final double maxY1 = Math.max(y1, y2);
        final double minY2 = Math.min(y3, y4);
        final double maxY2 = Math.max(y3, y4);
        return maxY2 >= minY1 && minY2 <= maxY1 && maxY2 != minY1 && minY2 != maxY1;
    }
    
    public Location getLocationAtAngleFrom(final Node pivotEndNode, final double angle) {
        double totalAngle = this.getAngleFrom(pivotEndNode);
        if (totalAngle == -1.0) {
            return null;
        }
        totalAngle += angle;
        final double slope = -1.0 * Math.tan(Math.toRadians(totalAngle));
        final double intercept = pivotEndNode.getLocation().doubleY() - slope * pivotEndNode.getLocation().doubleX();
        return new Location(pivotEndNode.getLocation().doubleX() + 5000.0, slope * (pivotEndNode.getLocation().doubleX() + 5000.0) + intercept);
    }
    
    public double getDirectedAngle(final Node directionSource) {
        if (directionSource == this.getStartNode()) {
            return this.getAngleFrom((Node)this.getStartNode());
        }
        if (directionSource == this.getEndNode()) {
            return this.getAngleFrom((Node)this.getEndNode());
        }
        return -1.0;
    }
    
    public double getAngleFrom(final Node aNode) {
        if (aNode != this.getStartNode() && aNode != this.getEndNode()) {
            return -1.0;
        }
        final Node otherNode = (Node)this.otherEndFrom(aNode);
        return this.getAngleFrom(otherNode.getLocation().intX(), otherNode.getLocation().intY(), aNode.getLocation().intX(), aNode.getLocation().intY(), aNode.getLocation().intX() + 100.0, aNode.getLocation().intY());
    }
    
    private double getAngleFrom(final double ax, final double ay, final double bx, final double by, final double cx, final double cy) {
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
        if (angle < 0.0) {
            angle += 360.0;
        }
        return angle;
    }
    
    public Edge getEdge() {
        return this;
    }
}
