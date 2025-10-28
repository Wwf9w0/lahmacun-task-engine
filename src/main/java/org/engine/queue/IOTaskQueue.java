package org.engine.queue;

import org.engine.model.QueuedTaskModel;
import org.engine.queue.external.ExternalIOQueue;

import java.util.ArrayList;
import java.util.List;

public class IOTaskQueue<T> {

    private final BoundedConcurrentDeque<QueuedTaskModel<T>> ioQueue;
    private final ExternalIOQueue<T> ioExternalQueue;
    private static final int IO_QUEUE_CAPACITY = 100_000;

    public IOTaskQueue() {
        this.ioQueue = new BoundedConcurrentDeque<>(IO_QUEUE_CAPACITY);
        this.ioExternalQueue = new ExternalIOQueue<>();
    }

    public QueuedTaskModel<T> pool() {
        try {
            return ioQueue.poll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<QueuedTaskModel<T>> pollAll() {
        List<QueuedTaskModel<T>> batch = new ArrayList<>();
        QueuedTaskModel<T> task;
        while ((task = pool()) != null) {
            batch.add(task);
        }
        return batch;
    }

    public int offer(List<QueuedTaskModel<T>> ioQueueTasks) {
        for (QueuedTaskModel<T> queuedTask : ioQueueTasks) {
            try {
                boolean push = ioQueue.offer(queuedTask);
                if (!push) {
                    ioExternalQueue.offer(ioQueueTasks);
                    System.out.println("[IOTaskQueue] Queue is full. Task sent to external Queue -> [IOExternalQueue] : " + queuedTask.getNodeId());
                    return 0;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return 1;
    }
}
