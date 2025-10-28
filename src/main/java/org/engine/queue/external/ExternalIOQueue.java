package org.engine.queue.external;

import org.engine.model.QueuedTaskModel;
import org.engine.queue.BoundedConcurrentDeque;

import java.util.ArrayList;
import java.util.List;

public class ExternalIOQueue<T> {

    private final BoundedConcurrentDeque<QueuedTaskModel<T>> ioExternalQueue;
    private static final int IO_EXTERNAL_QUEUE_CAPACITY = 100_000;

    public ExternalIOQueue() {
        this.ioExternalQueue = new BoundedConcurrentDeque<>(IO_EXTERNAL_QUEUE_CAPACITY);
    }

    public QueuedTaskModel<T> pool() {
        try {
            return ioExternalQueue.poll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<QueuedTaskModel<T>> pollAll() {
        List<QueuedTaskModel<T>> batch = new ArrayList<>();
        QueuedTaskModel<T> task = null;
        while ((task = pool()) != null) {
            batch.add(task);
        }
        return batch;
    }

    public void offer(List<QueuedTaskModel<T>> tasks) {
        for (QueuedTaskModel<T> task : tasks) {
            try {
                task.setRootQueue(0);
                boolean push = ioExternalQueue.offer(task);
                if (!push) {
                    System.err.println("[IOExternalQueue] Queue is full");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
