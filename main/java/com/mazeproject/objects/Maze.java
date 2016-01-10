package com.mazeproject.objects;

import com.mazeproject.exceptions.WrongMazeFormatException;
import com.mazeproject.mazeitems.MazeItemFactory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;

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
    
    public MazeElement getElement(int row, int col) {
        try {
            return this.elements[col][row];
        } catch(ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    
    public void setElement(int row, int col, MazeElement el) {
        this.elements[col][row] = el;
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
    
    public static Maze decode(String data) throws WrongMazeFormatException {
        boolean startExists = false;
        boolean finishExists = false;
        //double price = 0;
        Maze maze = null;
        int width = 0;
        int height = 0;
        JSONArray outerArray = new JSONArray(data);
        width = outerArray.length();
        if(width < 5 || width > 100) {
            throw new WrongMazeFormatException("Maze width is not between 5 and 100");
        }
        for(int i=0 ; i<outerArray.length() ; ++i) {
            JSONArray innerArray = new JSONArray(outerArray.get(i).toString());
            if(i==0) {
                height = innerArray.length();
                if(height < 5 || height > 100) {
                    throw new WrongMazeFormatException("Maze height is not between 5 and 100");
                }
                maze = new Maze(width, height);
            }
            for(int j=0 ; j<innerArray.length() ; ++j) {
                JSONObject ob = new JSONObject(innerArray.get(j).toString());
                //System.out.println(ob.toString());
                int row = (int)ob.get("row");
                int col = (int)ob.get("col");
                String itemCode = ob.get("item").toString();
                MazeItem item = null;
                switch(itemCode) {
                    case "false": {
                        item = MazeItemFactory.getFloor();
                        break;
                    }
                    case "S": {
                        item = MazeItemFactory.getStart();
                        startExists = true;
                        break;
                    }
                    case "F": {
                        item = MazeItemFactory.getFinish();
                        finishExists = true;
                        break;
                    }
                    case "T": {
                        //...
                        break;
                    }
                    case "P": {
                        //...
                        break;
                    }
                }
                int right = (int)ob.get("rightEdge");
                int bottom = (int)ob.get("bottomEdge");
                /*price = price
                        +PriceManager.getInstance().getEdgePrice(right)
                        +PriceManager.getInstance().getEdgePrice(bottom)
                        +PriceManager.getInstance().getItemPrice(item.getCode());
                /*System.out.println("r:"+row+",c:"+col+",item:"+
                        itemCode+",riht:"+right+",bottom:"+bottom);*/
                maze.setElement(row, col, new MazeElement(item, row, col, right, bottom));
            }
        }
        if(!startExists) {
            throw new WrongMazeFormatException("Maze has to contain at least one start item");
        }
        if(!finishExists) {
            throw new WrongMazeFormatException("Maze has to contain at least one finish item");
        }
        return maze;
    }
    
    /**
     * @param maze
     * @return if Maze is possible to complete
     * @throws com.mazeproject.exceptions.WrongMazeFormatException
     */
    public static boolean goThroughMaze(Maze maze) 
            throws WrongMazeFormatException, InterruptedException, ExecutionException {
        List<MazeElement> startPoints = new ArrayList<>();
        for(int i=0 ; i<maze.getWidth() ; ++i) {
            for(int j=0 ; j<maze.getHeight() ; ++j) {
                MazeElement el = maze.getElement(j, i);
                if(el.getItem().getCode() == 1) {
                    startPoints.add(el);
                }
            }
        }
        if(startPoints.isEmpty()) {
            throw new WrongMazeFormatException("Maze has to contain at least one start item");
        }
        ExecutorService executor = Executors.newCachedThreadPool();
        ArrayDeque<MazeCrawler> crawlers = new ArrayDeque<>();
        List<Future<Boolean>> results = new ArrayList<>();
        for(MazeElement el : startPoints) {
            MazeCrawler crawler = new MazeCrawler(maze, el, executor);
            crawlers.add(crawler);
        }
        results = executor.invokeAll(crawlers);
        executor.shutdown();
        for(Future<Boolean> result : results) {
            if(result.get()) {
                return true;
            }
        }
        return false;
    }
    
}
