package org.virtual.annotation;

import org.virtual.core.VirtualLahmacunChef;
import org.virtual.core.VirtualThreadOven;
import org.virtual.virtual.model.TaskLahmacunResult;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        VirtualThreadOven oven = new VirtualThreadOven();
        VirtualLahmacunChef chef = new VirtualLahmacunChef(oven);

        UserFlowServiceExample service = new UserFlowServiceExample();
        EventFlowServiceExample eventService = new EventFlowServiceExample();
        FlowMethodExecutor executor = new FlowMethodExecutor(chef);

        Map<String, TaskLahmacunResult<?>> results = executor.execute(service, eventService);

        System.out.println("\n--- Task Results ---");
        results.forEach((id, r) -> System.out.println(id + " -> " + r.status() + " -> " + r.result()));

        oven.close();

    }
}
