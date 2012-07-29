package me.atshughson.softeng325.a01.buffer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Program {
	
	public static void main(String[] args) {
		final int CONSUMERS = 200;
		final int DATA_SIZE = 100000;
		final int BUFFER_SIZE = 100;
		
		// Create shared buffer used to store Integer objects.
		Buffer<Integer> buffer = new BufferImplWithSemaphore<Integer>(BUFFER_SIZE);
		
		// Create a Set to store all items retrieved by consumer threads.
		Set<Integer> allItemsConsumed = new HashSet<Integer>();
		
		// Create producer and consumer tasks.
		Producer producer = new Producer(buffer, DATA_SIZE);
		Consumer[] consumers = new Consumer[CONSUMERS];
		for(int i = 0; i < consumers.length; i++) {
			consumers[i] = new Consumer(buffer);
		}
		
		// Create threads for running producer and consumer tasks.
		Thread producerThread = new Thread(producer);
		producerThread.setName("Producer");
		
		Thread[] consumerThreads = new Thread[CONSUMERS];
		for(int i = 0; i < consumerThreads.length; i++) {
			consumerThreads[i] = new Thread(consumers[i]);
			consumerThreads[i].setName("Consumer-" + i);
		}
		
		// Create BufferMonitor thread.
		Thread bufferMonitorThread = new Thread(new BufferMonitor<Integer>(buffer));
		
		// Start producer and consumer threads.
		producerThread.start();
		for(int i = 0; i < consumerThreads.length; i++) {
			consumerThreads[i].start();
		}
		System.out.println("Threads started");
		
		try {
			// Wait for the producer to finish populating the buffer.
			producerThread.join();
			System.out.println("Joined with producer");
			
			// Wait for the buffer to be emptied before killing consumers.
			// Start the BufferMonitor thread to poll the buffer - this thread
			// will terminate only when the buffer is empty.
			bufferMonitorThread.start();
			bufferMonitorThread.join();
			System.out.println("Joined with buffer monitor thread - buffer is empty");
			
			// Request consumers to gracefully terminate.
			for(int i = 0; i < consumerThreads.length; i++) {
				consumerThreads[i].interrupt();
			}
			
			// Synchronise with the death of each consumer thread and process its
			// consumed data.
			for(int i = 0; i < consumerThreads.length; i++) {
				consumerThreads[i].join();
				System.out.println("Joined with " + consumerThreads[i].getName());
				List<Integer> itemsConsumed = consumers[i].itemsConsumed();
				allItemsConsumed.addAll(itemsConsumed);
			}
			System.out.println("Joined with consumers");
			
			// Ensure that allItemsConsumed contains DATA_SIZE unique items.
			if(allItemsConsumed.size() == DATA_SIZE) {
				System.out.println("Functionally correct");
			} else {
				System.out.println("Functionally incorrect");
			}
		} catch(InterruptedException e) {
			System.err.println("Main program thread interrupted");
			System.exit(1);
		}
		
	}
}

