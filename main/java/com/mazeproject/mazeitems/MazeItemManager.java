package com.mazeproject.mazeitems;

import com.mazeproject.objects.MazeItem;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

public class MazeItemManager {
    
    private static final BidiMap mazeItems = new DualHashBidiMap();
    
    static {
        MazeItemManager.mazeItems.put("S", Start.class);
        MazeItemManager.mazeItems.put("F", Finish.class);
        MazeItemManager.mazeItems.put("f", Floor.class);
        MazeItemManager.mazeItems.put("T", Trap.class);
        MazeItemManager.mazeItems.put("P", Portal.class);
    }
    
    private static MazeItem getFloor() {
        return new Floor();
    }
    
    private static MazeItem getStart() {
        return new Start();
    }
    
    private static MazeItem getFinish() {
        return new Finish();
    }
    
    public static MazeItem getItem(String sign) {
        MazeItem item = null;
        switch(sign) {
            case "f": {
                item = MazeItemManager.getFloor();
                break;
            }
            case "S": {
                item = MazeItemManager.getStart();
                break;
            }
            case "F": {
                item = MazeItemManager.getFinish();
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
        return item;
    }
    
    public static String getSignByClass(Class itemClass) {
        return (String)MazeItemManager.mazeItems.getKey(itemClass);
    }
    
}
