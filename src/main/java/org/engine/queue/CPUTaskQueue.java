package org.engine.queue;

import org.engine.model.QueuedTaskModel;
import org.engine.queue.external.ExternalCPUQueue;

import java.util.ArrayList;
import java.util.List;

public class CPUTaskQueue<T> {

    private final BoundedBlockingQueue<QueuedTaskModel<T>> cpuQueue;
    private final ExternalCPUQueue<T> cpuExternalQueue;
    private static final int CPU_QUEUE_CAPACITY = Runtime.getRuntime().availableProcessors() * 1000;

    public CPUTaskQueue() {
        this.cpuQueue = new BoundedBlockingQueue<>(CPU_QUEUE_CAPACITY);
        this.cpuExternalQueue = new ExternalCPUQueue<>();
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

    public int offer(List<QueuedTaskModel<T>> cpuQueueTasks) {
        try {
            for (QueuedTaskModel<T> queuedTask : cpuQueueTasks) {
                boolean push = cpuQueue.offer(queuedTask);
                if (!push) {
                    cpuExternalQueue.offer(cpuQueueTasks);
                    System.err.println("[CPUTaskQueue] Queue is full. Task sent to external Queue -> [CPUExternalQueue] " + queuedTask.getNodeId());
                    return 0;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while adding CPU task", e);
        }
        return 1;
    }
}
