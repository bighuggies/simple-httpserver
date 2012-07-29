package me.atshughson.softeng325.a01.buffer;

public class Semaphore {
	
    private int fCounter;
    
    public Semaphore(int counter) {
    	if(counter < 0) {
    		throw new IllegalArgumentException();
    	}
        fCounter = counter;
    }
    
    public synchronized void p() throws InterruptedException {
    	while(fCounter == 0) {
    		// Suspend the calling thread.
    		wait();
    	}
    	fCounter--;
    }
    
    public synchronized void v() {
        fCounter++;
        
        // Wake one thread, if any, that is suspended on a call to p().
        notify();
    }
}