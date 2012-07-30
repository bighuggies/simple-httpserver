package me.atshughson.softeng325.a01.buffer;

public class Producer implements Runnable {
	
	private Buffer<Integer> fBuffer;
	private int fNumberOfItemsToProduce;
	
	public Producer(Buffer<Integer> buffer, int numberOfItemsToProduce) {
		fBuffer = buffer;
		fNumberOfItemsToProduce = numberOfItemsToProduce;
	}
	
	public void run() {
		try {
			for(int i = 0; i < fNumberOfItemsToProduce; i++) {
				fBuffer.put(new Integer(i));
			}
		} catch(InterruptedException e) {
			System.err.println(e);
		}
	}
}
