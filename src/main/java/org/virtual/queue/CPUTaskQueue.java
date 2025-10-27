package org.virtual.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CPUTaskQueue<T> {
    private final BlockingQueue<QueuedTask<T>> cpuQueue;

    public CPUTaskQueue() {
        this.cpuQueue = new LinkedBlockingQueue<>();
    }

    public QueuedTask<T> take() {
        try {
            return cpuQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for CPU task", e);
        }
    }

    public List<QueuedTask<T>> takeAll() {
        return new ArrayList<>(cpuQueue);
    }


    public void putCpuQueue(List<QueuedTask<T>> cpuQueueTask) {
        try {
            for (QueuedTask<T> queuedTask : cpuQueueTask) {
                cpuQueue.put(queuedTask);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while adding CPU task", e);
        }
    }
}
