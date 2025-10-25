package org.engine.scheduler;

import org.engine.virtual.LahmacunTaskStatus;
import org.engine.virtual.TaskLahmacunResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SchedulerOperation<T> {
    private final Map<String, SchedulerBuilder<T>> lc = new ConcurrentHashMap<>();
    private final SchedulerService scService;
    private final Supplier<T> supplier;

    public SchedulerOperation(SchedulerService scService, Supplier<T> supplier) {
        this.scService = scService;
        this.supplier = supplier;
    }

    public void setSchedulerToTask(String key, SchedulerBuilder<T> schedulerBuilder) {
        lc.put(key, schedulerBuilder);
    }

    public SchedulerBuilder getSchedulerToTask(String key) {
        return lc.get(key);
    }

    public TaskLahmacunResult<T> of() {
        if (!lc.isEmpty()) {
            for (Map.Entry<String, SchedulerBuilder<T>> entry : lc.entrySet()) {
                SchedulerBuilder<T> sb = entry.getValue();
                String key = entry.getKey();
                String cron = sb.toString();
                T d = sb.getData();
                long ms = sb.getTimeoutMillis();
                scService.job(cron, d, supplier);
                return new TaskLahmacunResult<>(key, LahmacunTaskStatus.SUCCESS, d, null, ms);
            }
        }
        return new TaskLahmacunResult<>(null, null, null, null, 0);
    }
}
