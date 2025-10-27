package org.virtual.queue;

import org.virtual.model.QueuedTaskModel;

import java.util.ArrayList;
import java.util.List;

public class CPUTaskQueue<T> {

    private final BoundedBlockingQueue<QueuedTaskModel<T>> cpuQueue;
    private static final int CPU_QUEUE_CAPACITY = Runtime.getRuntime().availableProcessors() * 1000;
    //16 core → 16,000 task

    public CPUTaskQueue() {
        this.cpuQueue = new BoundedBlockingQueue<>(CPU_QUEUE_CAPACITY);
    }

    public QueuedTaskModel<T> poll() {
        try {
            return cpuQueue.poll();
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

    public void offer(List<QueuedTaskModel<T>> cpuQueueTask) {
        try {
            for (QueuedTaskModel<T> queuedTask : cpuQueueTask) {
                boolean push = cpuQueue.offer(queuedTask);
                if (!push) {
                    System.err.println("[IOTaskQueue] Queue is full. Task rejected: " + queuedTask.getNodeId());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while adding CPU task", e);
        }
    }
}
