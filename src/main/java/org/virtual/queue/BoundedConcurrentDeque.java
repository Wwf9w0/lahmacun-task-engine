package org.virtual.queue;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedConcurrentDeque<E> {

    private final ConcurrentLinkedDeque<E> deque = new ConcurrentLinkedDeque<>();
    private final AtomicInteger currentSize = new AtomicInteger(0);
    private final int capacity;

    public BoundedConcurrentDeque(int capacity) {
        this.capacity = capacity;
    }

    public boolean offer(E e) {
        while (true) {
            int size = currentSize.get();
            if (size >= capacity) {
                return false;
            }
            if (currentSize.compareAndSet(size, size + 1)) {
                deque.offer(e);
                return true;
            }
        }

    }

    public E poll() {
        E item = deque.poll();
        if (item != null) {
            currentSize.decrementAndGet();
        }
        return item;
    }

    public int size() {
        return deque.size();
    }


    public boolean isFUll() {
        return currentSize.get() >= capacity;
    }

    public boolean isEmpty() {
        return currentSize.get() == 0;
    }

}
