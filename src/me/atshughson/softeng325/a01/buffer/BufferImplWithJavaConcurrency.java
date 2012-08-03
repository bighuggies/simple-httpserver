package me.atshughson.softeng325.a01.buffer;

public class BufferImplWithJavaConcurrency<T> implements Buffer<T> {
    // Buffer data structure variables.
    private int fCapacity; // Number of elements that can be stored.
    private int fFront; // Index of element at the front of the buffer.
    private int fBack; // Index of next free slot within the buffer.
    private T[] fElements; // Array to store elements.

    @SuppressWarnings("unchecked")
    public BufferImplWithJavaConcurrency(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        fCapacity = capacity;
        fElements = (T[]) new Object[fCapacity];
        fFront = 0;
        fBack = 0;
    }

    synchronized public T get() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }

        T result = fElements[fFront];
        fElements[fFront] = null;
        fFront = (fFront + 1) % fCapacity;

        notify();

        return result;
    }

    synchronized public void put(T element) throws InterruptedException {
        while (isFull()) {
            wait();
        }

        fElements[fBack] = element;
        fBack = (fBack + 1) % fCapacity;

        notify();
    }

    public boolean isFull() throws InterruptedException {
        return fFront == fBack && fElements[fFront] != null;
    }

    public boolean isEmpty() throws InterruptedException {
        return fFront == fBack && fElements[fFront] == null;
    }
}
