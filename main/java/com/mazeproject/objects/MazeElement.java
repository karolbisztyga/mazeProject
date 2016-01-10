package com.mazeproject.objects;

public class MazeElement {
    
    /**
     * items
     * 0    -   Floor
     * 1    -   Start
     * 2    -   Finish
     * 3    -   Trap
     * 4    -   Hole
     * 5    -   Portal
     */
    
    /**
     * edges
     * 0    -   Empty
     * 1    -   Wall
     * 2    -   Door
     * 3    -   SecretDoor
     */
    
    private MazeItem item;
    private int right;
    private int bottom;
    private int row;
    private int col;

    public MazeElement(MazeItem item, int row, int col, int right, int bottom) {
        this.item = item;
        this.right = right;
        this.bottom = bottom;
        this.row = row;
        this.col = col;
    }

    public MazeItem getItem() {
        return item;
    }

    public void setItem(MazeItem item) {
        this.item = item;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
    
    
}