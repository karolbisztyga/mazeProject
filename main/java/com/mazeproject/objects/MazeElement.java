package com.mazeproject.objects;

public interface MazeElement {
    
    /**
     * 0    -   Floor
     * 1    -   Start
     * 2    -   Finish
     * 3    -   Trap
     * 4    -   Hole
     * 5    -   Portal
     * 
     * 6    -   Empty
     * 7    -   Wall
     * 8    -   Door
     * 9    -   SecretDoor
     */
    public int getCode();
    public void activity();
    
}