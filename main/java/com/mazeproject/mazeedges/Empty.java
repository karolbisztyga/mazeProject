package com.mazeproject.mazeedges;

import com.mazeproject.objects.MazeEdge;

public class Empty implements MazeEdge {

    @Override
    public int getCode() {
        return 6;
    }

    @Override
    public void activity() {
        
    }
    
}
