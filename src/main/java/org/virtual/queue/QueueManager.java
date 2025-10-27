package org.virtual.queue;

import java.util.List;

public class QueueManager<T> {

    private final IOTaskQueue<T> ioTaskQueue;
    private final CPUTaskQueue<T> cpuTaskQueue;

    public QueueManager() {
        this.ioTaskQueue = new IOTaskQueue<>();
        this.cpuTaskQueue = new CPUTaskQueue<>();
    }

    public void put(List<QueuedTask<T>>  queuedTask, int process) {
        if (process == 1) {
            cpuTaskQueue.putCpuQueue(queuedTask);
        } else {
            ioTaskQueue.offer(queuedTask);
        }
    }

    public List<QueuedTask<T>> pollAll(int process) {
        if (process == 1) {
            return cpuTaskQueue.takeAll();
        } else {
            return ioTaskQueue.pollAll();
        }
    }
}
