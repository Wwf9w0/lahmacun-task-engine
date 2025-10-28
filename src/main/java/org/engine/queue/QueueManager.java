package org.engine.queue;

import org.engine.model.QueuedTaskModel;
import org.engine.queue.external.ExternalCPUQueue;
import org.engine.queue.external.ExternalIOQueue;

import java.util.List;

public class QueueManager<T> {

    private final IOTaskQueue<T> ioTaskQueue;
    private final CPUTaskQueue<T> cpuTaskQueue;
    private final ExternalCPUQueue<T> externalCPUQueue;
    private final ExternalIOQueue<T> externalIOQueue;

    public QueueManager() {
        this.ioTaskQueue = new IOTaskQueue<>();
        this.cpuTaskQueue = new CPUTaskQueue<>();
        this.externalIOQueue = new ExternalIOQueue<>();
        this.externalCPUQueue = new ExternalCPUQueue<>();
    }

    public int put(List<QueuedTaskModel<T>> queuedTask, int process) {
        if (process == 1) {
            return cpuTaskQueue.offer(queuedTask);
        } else {
            return ioTaskQueue.offer(queuedTask);
        }
    }

    public List<QueuedTaskModel<T>> pollAll(int process, int root) {
        if (process == 1 && root == 1) {
            return cpuTaskQueue.pollAll();
        } else if (process == 1 && root == 0) {
            return externalCPUQueue.pollAll();
        } else if (process == 0 && root == 1) {
            return ioTaskQueue.pollAll();
        }
        return externalIOQueue.pollAll();
    }
}
