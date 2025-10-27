package org.virtual.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class IOTaskQueue<T> {
    private final BoundedConcurrentDeque<QueuedTask<T>> ioQueue;
    private static final int IO_QUEUE_CAPACITY = 100_000;

    public IOTaskQueue() {
        this.ioQueue = new BoundedConcurrentDeque<>(IO_QUEUE_CAPACITY);
    }

    public QueuedTask<T> pool() {
        return ioQueue.poll();
    }

    public List<QueuedTask<T>> pollAll() {
        return new ArrayList<>(ioQueue.size());
    }

    public void offer(List<QueuedTask<T>> ioQueueTasks) {
        for (QueuedTask<T> queuedTask : ioQueueTasks) {
            ioQueue.offer(queuedTask);
        }
    }
}
