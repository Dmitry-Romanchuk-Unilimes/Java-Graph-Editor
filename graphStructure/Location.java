// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure;

import java.awt.*;

public class Location
{
    private double X;
    private double Y;
    private int x;
    private int y;
    
    public Location(final Point aPoint) {
        final int x = aPoint.x;
        this.x = x;
        this.X = x;
        final int y = aPoint.y;
        this.y = y;
        this.Y = y;
    }
    
    public Location(final Location aLocation) {
        this.x = aLocation.intX();
        this.y = aLocation.intY();
        this.X = aLocation.doubleX();
        this.Y = aLocation.doubleY();
    }
    
    public Location(final int x, final int y) {
        this.x = x;
        this.X = x;
        this.y = y;
        this.Y = y;
    }
    
    public Location(final double X, final double Y) {
        this.X = X;
        this.Y = Y;
        this.x = (int)Math.round(X);
        this.y = (int)Math.round(Y);
    }
    
    public int intX() {
        return this.x;
    }
    
    public int intY() {
        return this.y;
    }
    
    public double doubleX() {
        return this.X;
    }
    
    public double doubleY() {
        return this.Y;
    }
    
    public void setX(final double newX) {
        this.X = newX;
        this.x = (int)Math.round(newX);
    }
    
    public void setY(final double newY) {
        this.Y = newY;
        this.y = (int)Math.round(newY);
    }
    
    public void translate(final int dx, final int dy) {
        this.X += dx;
        this.x += dx;
        this.Y += dy;
        this.y += dy;
    }
    
    public boolean equals(final Object o) {
        if (o instanceof Location) {
            final Location l = (Location)o;
            return l.X == this.X && l.Y == this.Y;
        }
        if (o instanceof Point) {
            final Point p = (Point)o;
            return p.x == this.x && p.y == this.y;
        }
        return false;
    }
    
    public String toString() {
        return "Location [" + this.X + " (" + this.x + "), " + this.Y + " (" + this.y + ")]";
    }
}
