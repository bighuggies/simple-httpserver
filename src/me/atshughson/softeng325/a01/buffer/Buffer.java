package me.atshughson.softeng325.a01.buffer;

public interface Buffer<T> {
	public void put(T element) throws InterruptedException;
	public T get() throws InterruptedException;
	public boolean isFull() throws InterruptedException;
	public boolean isEmpty() throws InterruptedException;
}

