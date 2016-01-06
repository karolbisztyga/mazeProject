package com.mazeproject.objects;

public class MazeElement {
    
    /**
     * 0    -   Floor
     * 1    -   Start
     * 2    -   Finish
     * 3    -   Trap
     * 4    -   Hole
     * 5    -   Portal
     */
    
    /**
     * 0    -   Empty
     * 1    -   Wall
     * 2    -   Door
     * 3    -   SecretDoor
     */
    
    private MazeItem item;
    private int right;
    private int bottom;

    public MazeElement(MazeItem item, int right, int bottom) {
        this.item = item;
        this.right = right;
        this.bottom = bottom;
    }
    
    public MazeElement() {
        this(null,0,0);
    }
    
}