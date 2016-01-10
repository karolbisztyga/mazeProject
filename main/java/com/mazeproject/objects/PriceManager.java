package com.mazeproject.objects;

public class PriceManager {
    
    private static ThreadLocal<PriceManager> instance;
    
    private PriceManager() {
        
    }
    
    public static PriceManager getInstance() {
        if(PriceManager.instance==null) {
            PriceManager.instance = new ThreadLocal() {
                @Override
                public PriceManager initialValue() {
                    return new PriceManager();
                }
            };
        }
        return PriceManager.instance.get();
    }
    
    public double getEdgePrice(int code) {
        switch(code) {
            case 1: {
                return .25;
            }
            case 2: {
                return 10;
            }
            case 3: {
                return 70;
            }
        }
        return 0;
    }
    
    public double getItemPrice(int code) {
        switch(code) {
            case 1:
            case 2: {
                return 5;
            }
            case 3:
            case 4:
            case 5: {
                return 100;
            }
        }
        return 0;
    }
    
}
