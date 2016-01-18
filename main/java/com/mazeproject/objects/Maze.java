package com.mazeproject.objects;

import com.mazeproject.exceptions.WrongMazeFormatException;
import com.mazeproject.mazeitems.MazeItemManager;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONObject;

public class Maze {
    
    private final int width;
    private final int height;
    private int price = 0;

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
        JSONArray outerArray = new JSONArray();
        for(int i=0 ; i<width ; ++i) {
            JSONArray innerArray = new JSONArray();
            for(int j=0 ; j<height ; ++j) {
                JSONObject ob = new JSONObject();
                MazeElement element = this.getElement(j, i);
                ob.put("r", j);
                ob.put("c", i);
                ob.put("re", element.getRight());
                ob.put("be", element.getBottom());
                ob.put("i", MazeItemManager.getSignByClass(element.getItem().getClass()));
                innerArray.put(ob);
            }
            outerArray.put(innerArray);
        }
        return outerArray.toString();
    }
    
    public static Maze decode(String data) throws WrongMazeFormatException {
        boolean startExists = false;
        boolean finishExists = false;
        double price = 0;
        Maze maze = null;
        int width = 0;
        int height = 0;
        JSONArray outerArray = new JSONArray(data);
        width = outerArray.length();
        if(width < 5 || width > 100) {
            throw new WrongMazeFormatException("Maze width is not between 5 and 100");
        }
        for(int i=0 ; i<width ; ++i) {
            JSONArray innerArray = new JSONArray(outerArray.get(i).toString());
            if(i==0) {
                height = innerArray.length();
                if(height < 5 || height > 100) {
                    throw new WrongMazeFormatException("Maze height is not between 5 and 100");
                }
                maze = new Maze(width, height);
            }
            for(int j=0 ; j<height ; ++j) {
                JSONObject ob = new JSONObject(innerArray.get(j).toString());
                int row = (int)ob.get("r");
                int col = (int)ob.get("c");
                int right = (int)ob.get("re");
                int bottom = (int)ob.get("be");
                String itemCode = ob.get("i").toString();
                itemCode = itemCode.substring(0, 1);
                MazeItem item = MazeItemManager.getItem(itemCode);
                if(itemCode.equals("S")) startExists = true;
                else if(itemCode.equals("F")) finishExists = true;
                price = price
                        +PriceManager.getInstance().getEdgePrice(right)
                        +PriceManager.getInstance().getEdgePrice(bottom)
                        +PriceManager.getInstance().getItemPrice(item.getCode());
                maze.setElement(row, col, new MazeElement(item, row, col, right, bottom));
            }
        }
        if(!startExists) {
            throw new WrongMazeFormatException("Maze has to contain at least one start item");
        }
        if(!finishExists) {
            throw new WrongMazeFormatException("Maze has to contain at least one finish item");
        }
        maze.setPrice((int)price);
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
            MazeCrawler crawler = new MazeCrawler(maze, el);
            crawlers.add(crawler);
        }
        results = executor.invokeAll(crawlers);
        executor.shutdown();
        for(Future<Boolean> result : results) {
            if(!result.get()) {
                return false;
            }
        }
        return true;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public int getPrice(boolean update) {
        if(update) {
            double price = 0;
            for(int i=0 ; i<width ; ++i) {
                for(int j=0 ; j<height ; ++j) {
                    MazeElement element = this.getElement(j, i);
                    price += PriceManager.getInstance().getItemPrice(element.getItem().getCode());
                    price += PriceManager.getInstance().getEdgePrice(element.getRight());
                    price += PriceManager.getInstance().getEdgePrice(element.getBottom());
                }
            }
            this.price = (int)price;
            return this.price;
        }
        return this.price;
    }
    
}
