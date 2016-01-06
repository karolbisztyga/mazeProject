package com.mazeproject.mazeitems;

import com.mazeproject.objects.MazeItem;

public class MazeItemFactory {
    
    public static MazeItem getFloor() {
        return new Floor();
    }
    
    public static MazeItem getStart() {
        return new Start();
    }
    
    public static MazeItem getFinish() {
        return new Finish();
    }
    
}
