package com.mazeproject.objects;

import com.mazeproject.exceptions.WrongMazeFormat;
import com.mazeproject.mazeitems.MazeItemFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class Maze {
    
    private final int width;
    private final int height;

    private final MazeElement [][]elements;
    
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("new maze width=" + width + ", height=" + height);
        
        this.elements = new MazeElement[width][height];
        for(int i=0 ; i<width ; ++i) {
            for(int j=0 ; j<height ; ++j) {
                elements[i][j] = null;
            }
        }
    }
    
    public MazeElement getElement(int row, int col) {
        return this.elements[col][row];
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
    
    public static Maze decode(String data) throws WrongMazeFormat {
        boolean startExists = false;
        boolean finishExists = false;
        double price = 0;
        Maze maze = null;
        int width = 0;
        int height = 0;
        JSONArray outerArray = new JSONArray(data);
        width = outerArray.length();
        if(width < 5 || width > 100) {
            throw new WrongMazeFormat("Maze width is not between 5 and 100");
        }
        for(int i=0 ; i<outerArray.length() ; ++i) {
            JSONArray innerArray = new JSONArray(outerArray.get(i).toString());
            if(i==0) {
                height = innerArray.length();
                if(height < 5 || height > 100) {
                    throw new WrongMazeFormat("Maze height is not between 5 and 100");
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
                System.out.println("r:"+row+",c:"+col+",item:"+
                        itemCode+",riht:"+right+",bottom:"+bottom);
                maze.setElement(row, col, new MazeElement(item, right, bottom));
            }
        }
        return maze;
    }
    
}
