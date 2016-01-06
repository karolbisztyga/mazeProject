package com.mazeproject.objects;

public class PriceManager {
    
    private ThreadLocal<PriceManager> instance;
    
    private PriceManager() {
        
    }
    
    public PriceManager getInstance() {
        if(this.instance==null) {
            this.instance = new ThreadLocal() {
                @Override
                public PriceManager initialValue() {
                    return new PriceManager();
                }
            };
        }
        return this.instance.get();
    }
    /*
    public double getEdgePrice(int code) {
        
    }
    
    public double getFieldPrice() {
        
    }
    */
}
