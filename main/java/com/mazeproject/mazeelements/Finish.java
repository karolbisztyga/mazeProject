package com.mazeproject.mazeelements;

import com.mazeproject.objects.MazeEdge;
import com.mazeproject.objects.MazeField;

public class Finish implements MazeField {

    @Override
    public int getCode() {
        return 2;
    }

    @Override
    public void activity() {
        
    }

    @Override
    public MazeEdge getRightEdge() {
        return null;
    }

    @Override
    public MazeEdge getBottomEdge() {
        return null;
    }
}