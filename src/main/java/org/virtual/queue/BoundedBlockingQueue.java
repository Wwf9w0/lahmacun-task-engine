package org.virtual.queue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedBlockingQueue<E> {
    private final LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<>();
    private final AtomicInteger currentSize = new AtomicInteger(0);
    private final int capacity;

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public boolean offer(E e) throws InterruptedException {
        while (true) {
            int size = currentSize.get();
            if (size >= capacity) {
                return false;
            }
            if (currentSize.compareAndSet(size, size + 1)) {
                queue.offer(e);
                return true;
            }
        }
    }

    public E poll() throws InterruptedException {
        E item = queue.poll();
        if (item != null) {
            currentSize.decrementAndGet();
        }
        return item;
    }

    public int size() {
        return queue.size();
    }

    public boolean isFull() {
        return currentSize.get() >= capacity;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
