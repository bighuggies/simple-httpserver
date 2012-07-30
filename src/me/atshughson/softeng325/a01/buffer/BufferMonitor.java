package me.atshughson.softeng325.a01.buffer;

public class BufferMonitor<T> implements Runnable {

	private Buffer<T> fBuffer;
	
	public BufferMonitor(Buffer<T> buffer) {
		fBuffer = buffer;
	}
	
	public void run() {
		try {
			while(!fBuffer.isEmpty()) {
				Thread.sleep(100);
			} 
		} catch(InterruptedException e) {
			// No action necessary.
			System.err.println(e);
		}	
	}
}

