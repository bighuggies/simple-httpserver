package me.atshughson.softeng325.a01.buffer;

import java.util.ArrayList;
import java.util.List;

public class Consumer implements Runnable {
	
	private Buffer<Integer> fBuffer;
	private List<Integer> fItemsConsumed;

	public Consumer(Buffer<Integer> buffer) {
		fBuffer = buffer;
		fItemsConsumed = new ArrayList<Integer>();
	}
		
	public void run() {
		boolean finished = false;

		while(!finished) {
			try {
				// Get next item of work from the buffer, blocking if necessary.
				Integer i = fBuffer.get();
				
				// Record item retrieved from the buffer.
				fItemsConsumed.add(i);
			} catch(InterruptedException e) {
				// Record that this thread has been interrupted. An 
				// InterruptedException will be thrown if this thread is
				// interrupted when calling wait() or prior to calling wait().
				finished = true;
			} 
		}
	}
	
	public List<Integer> itemsConsumed() {
		return fItemsConsumed;
	}

}

