package com.mazeproject.objects;

public class Maze {
    
    private final int width;
    private final int height;

    private final MazeElement [][]elements;
    
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        
        this.elements = new MazeElement[width][height];
        for(int i=0 ; i<width ; ++i) {
            for(int j=0 ; j<height ; ++j) {
                elements[i][j] = null;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public String encode() {
        return "";
    }
    
    public static Maze decode() {
        return null;
    }
    
}
