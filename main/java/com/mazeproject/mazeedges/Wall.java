package com.mazeproject.mazeedges;

import com.mazeproject.objects.MazeEdge;

public class Wall implements MazeEdge {

    @Override
    public int getCode() {
        return 7;
    }

    @Override
    public void activity() {
        
    }
    
}
