package com.mazeproject.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Stack;
import java.util.concurrent.Callable;

public class MazeCrawler implements Callable<Boolean> {
    
    public enum DIRECTION {
        NONE,
        UP,
        RIGHT,
        BOTTOM,
        LEFT,
    };
    
    private final Maze maze;
    private final MazeElement startElement;
    private boolean finished = false;
    
    public MazeCrawler(Maze maze, MazeElement startElement) {
        this.maze = maze;
        this.startElement = startElement;
    }


    @Override
    public Boolean call() throws Exception {
        try {
            this.crawle(null, this.startElement, DIRECTION.NONE);
            TimeUnit.SECONDS.sleep(1);
        } catch(InterruptedException e) {} 
        return this.finished;
    }
    
    private void crawle(
            Stack<MazeElement> visitedEleemnts,
            MazeElement currentElement, 
            DIRECTION currentDirection) throws InterruptedException {
        Thread.sleep(1);
        if(visitedEleemnts == null) {
            visitedEleemnts = new Stack<>();
        }
        visitedEleemnts.add(currentElement);
        if(currentElement.getItem().getCode()==2) {
            this.finished = true;
            Thread.currentThread().interrupt();
        }
        MazeElement elementUp =
                this.maze.getElement(currentElement.getRow()-1,currentElement.getCol());
        MazeElement elementLeft =
                this.maze.getElement(currentElement.getRow(),currentElement.getCol()-1);
        MazeElement elementRight =
                this.maze.getElement(currentElement.getRow(),currentElement.getCol()+1);
        MazeElement elementBottom =
                this.maze.getElement(currentElement.getRow()+1,currentElement.getCol());
        Map<MazeElement, DIRECTION> options = new HashMap<>();
        if(currentDirection!=DIRECTION.BOTTOM && elementUp!=null && elementUp.getBottom()!=1) {
            options.put(elementUp,DIRECTION.UP);
        }
        if(currentDirection!=DIRECTION.RIGHT && elementLeft!=null && elementLeft.getRight()!=1) {
            options.put(elementLeft,DIRECTION.LEFT);
        }
        if(currentDirection!=DIRECTION.UP && elementBottom!=null && currentElement.getBottom()!=1) {
            options.put(elementBottom,DIRECTION.BOTTOM);
        }
        if(currentDirection!=DIRECTION.LEFT && elementRight!=null && currentElement.getRight()!=1) {
            options.put(elementRight,DIRECTION.RIGHT);
        }
        
        for (MazeElement nextElement : options.keySet()) {
            if(visitedEleemnts.contains(nextElement)) {
                continue;
            }
            DIRECTION nextDirection = options.get(nextElement);
            this.crawle(visitedEleemnts, nextElement, nextDirection);
        }
    }
    
}
