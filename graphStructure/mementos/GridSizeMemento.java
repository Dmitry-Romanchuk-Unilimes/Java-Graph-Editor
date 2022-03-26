// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Graph;

public class GridSizeMemento implements MementoInterface
{
    private static int NO_TYPE;
    private static int CHANGE_GRID_SIZE_TYPE;
    private int gridRows;
    private int gridCols;
    private int gridRowHeight;
    private int gridColWidth;
    private Graph target;
    private int type;
    
    static {
        GridSizeMemento.NO_TYPE = 0;
        GridSizeMemento.CHANGE_GRID_SIZE_TYPE = 1;
    }
    
    private GridSizeMemento(final Graph target) {
        this.target = target;
        this.gridRows = target.getGridRows();
        this.gridCols = target.getGridCols();
        this.gridRowHeight = target.getGridRowHeight();
        this.gridColWidth = target.getGridColWidth();
        this.type = GridSizeMemento.NO_TYPE;
    }
    
    public static GridSizeMemento createGridSizeMemento(final Graph target) {
        final GridSizeMemento toReturn = new GridSizeMemento(target);
        toReturn.type = GridSizeMemento.CHANGE_GRID_SIZE_TYPE;
        return toReturn;
    }
    
    public void apply(final Graph graph) {
        if (this.type == GridSizeMemento.NO_TYPE) {
            return;
        }
        if (this.type == GridSizeMemento.CHANGE_GRID_SIZE_TYPE) {
            final int tempGridRows = this.target.getGridRows();
            final int tempGridCols = this.target.getGridCols();
            final int tempGridRowHeight = this.target.getGridRowHeight();
            final int tempGridColWidth = this.target.getGridColWidth();
            this.target.setGrid(this.gridRows, this.gridRowHeight, this.gridCols, this.gridColWidth, false);
            this.gridRows = tempGridRows;
            this.gridCols = tempGridCols;
            this.gridRowHeight = tempGridRowHeight;
            this.gridColWidth = tempGridColWidth;
        }
    }
    
    public String toString() {
        if (this.type == GridSizeMemento.CHANGE_GRID_SIZE_TYPE) {
            return "ChangeGridSize: " + this.target + " " + this.gridRows + " " + this.gridCols + " " + this.gridRowHeight + " " + this.gridColWidth;
        }
        return "Unknown: " + this.target;
    }
    
    public boolean isUseless() {
        return false;
    }
}
