package org.virtual.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class IOTaskQueue<T> {
    private final Queue<QueuedTask<T>> ioQueue;

    public IOTaskQueue() {
        this.ioQueue = new ConcurrentLinkedDeque<>();
    }

    public QueuedTask<T> pool() {
        return ioQueue.poll();
    }

    public List<QueuedTask<T>> pollAll() {
        return new ArrayList<>(ioQueue);
    }

    public void offer(List<QueuedTask<T>> ioQueueTasks) {
        for (QueuedTask<T> queuedTask : ioQueueTasks) {
            ioQueue.offer(queuedTask);
        }
    }
}
