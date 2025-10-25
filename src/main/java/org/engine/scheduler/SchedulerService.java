package org.engine.scheduler;

import org.engine.core.VirtualThreadOven;
import org.engine.virtual.VirtuaLahmacunFlow;
import org.engine.virtual.VirtuaLahmacunlChef;
import org.engine.virtual.VirtualLahmacunTask;

import java.util.function.Supplier;

public class SchedulerService<T> {
    private final SchedulerOperation schedulerOperation;

    public SchedulerService(SchedulerOperation schedulerOperation) {
        this.schedulerOperation = schedulerOperation;
    }

    public T job(String cron, T data, Supplier<T> supplier) {
        assert cron != null;
        VirtualThreadOven oven = new VirtualThreadOven();
        VirtuaLahmacunlChef chef = new VirtuaLahmacunlChef(oven);
        VirtuaLahmacunFlow flow = new VirtuaLahmacunFlow(chef);
        VirtualLahmacunTask<T> task = flow.addTask(supplier);
        chef.waitAll();
        flow.bakeAll();
        data = task.join();
        return data;
    }

    public void makePlanned(String key, SchedulerBuilder schedulerBuilder, Supplier<T> supplier) {
        assert key != null;
        assert schedulerBuilder != null;
        assert supplier != null;
        schedulerOperation.setSchedulerToTask(key, schedulerBuilder);
    }


    public SchedulerBuilder getPlan(String key) {
        return schedulerOperation.getSchedulerToTask(key);
    }

    public void runScheduler() {
        schedulerOperation.of();
    }
}
