package org.engine.queue.external;

import org.engine.model.QueuedTaskModel;
import org.engine.queue.BoundedBlockingQueue;

import java.util.ArrayList;
import java.util.List;

public class ExternalCPUQueue<T> {
    private final BoundedBlockingQueue<QueuedTaskModel<T>> externalCpuQueue;

    private static final int CPU_QUEUE_CAPACITY = Runtime.getRuntime().availableProcessors() * 1000;

    public ExternalCPUQueue() {
        this.externalCpuQueue = new BoundedBlockingQueue<>(CPU_QUEUE_CAPACITY);
    }

    public QueuedTaskModel<T> poll() {
        try {
            return externalCpuQueue.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for CPU task", e);
        }
    }

    public List<QueuedTaskModel<T>> pollAll() {
        List<QueuedTaskModel<T>> batch = new ArrayList<>();
        QueuedTaskModel<T> task;
        while ((task = poll()) != null) {
            batch.add(task);
        }
        return batch;
    }

    public void offer(List<QueuedTaskModel<T>> tasks) {
        for (QueuedTaskModel<T> task : tasks) {
            try {
                task.setRootQueue(0);
                boolean push = externalCpuQueue.offer(task);
                if (!push) {
                    System.err.println("[CPUExternalQueue] Queue is full");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
