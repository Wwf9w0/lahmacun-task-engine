package org.engine.scheduler;

import java.util.function.Supplier;

public class SchedulerHelper<T> {
    private final SchedulerService scService;

    public SchedulerHelper(SchedulerService scService) {
        this.scService = scService;
    }

    public void register(String key, SchedulerBuilder sb, Supplier<T> supplier) {
        scService.makePlanned(key, sb, supplier);
    }
}
