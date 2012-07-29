package me.atshughson.softeng325.a01.buffer;

public class BufferImplWithSemaphore<T> implements Buffer<T> {
	// Buffer data structure variables.
	private int fCapacity;		// Number of elements that can be stored.
	private int fFront;			// Index of element at the front of the buffer.
	private int fBack;			// Index of next free slot within the buffer.
	private T[] fElements;		// Array to store elements.
	
	// Concurrency control variables.
	private Semaphore fEmptySlots;	// Number of vacant slots in the buffer.
	private Semaphore fFullSlots;	// Number of used slots in the buffer.
	private Semaphore fMutex;		// Semaphore used to ensure mutual exclusion.

	public BufferImplWithSemaphore(int capacity) {
		if(capacity <= 0) {
			throw new IllegalArgumentException();
		}
		
		fCapacity = capacity;
		fElements = (T[]) new Object[fCapacity];
		fFront = 0;
		fBack = 0;
		
		fEmptySlots = new Semaphore(fCapacity);
		fFullSlots = new Semaphore(0);
		fMutex = new Semaphore(1);

	}
	
	public T get() throws InterruptedException {
		// Wait if necessary for a slot to be filled.
		fFullSlots.p();
		
		// Lock buffer's elements.
		fMutex.p();
		
		T result = fElements[fFront];
		fElements[fFront] = null;
		fFront = (fFront + 1) % fCapacity;
		
		// Release buffer's lock.
		fMutex.v();
		
		// Signal to an individual producer thread, if one is suspended on the 
		// fEmptySlots Semaphore, that a slot has been emptied.
		fEmptySlots.v();

		return result;
	}

	public void put(T element) throws InterruptedException {
		// Wait if necessary for an empty slot.
		fEmptySlots.p();
		
		// Lock buffer's elements.
		fMutex.p();
		
		fElements[fBack] = element;
		fBack = (fBack + 1) % fCapacity;
		
		// Release buffer's lock.
		fMutex.v();
		
		// Signal to an individual consumer thread, if one is suspended on the 
		// fFullSlots Semaphore, that a slot has been filled.
		fFullSlots.v();
	}
	
	public boolean isFull() throws InterruptedException {
		boolean result = false;
		fMutex.p();
		result = fFront == fBack && fElements[fFront] != null;
		fMutex.v();
		return result;
	}
	
	public boolean isEmpty() throws InterruptedException {
		boolean result = false;
		fMutex.p();
		result =  fFront == fBack && fElements[fFront] == null;
		fMutex.v();
		return result;
	}
}

