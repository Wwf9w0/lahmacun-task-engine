package org.engine.core;

import org.engine.model.TaskLahmacunResult;

import java.util.Map;

public class ExternalCPUChef<T> {

    private final FlowExecutorCPUWithChef<T> flowExecutorCPUWithChef;

    public ExternalCPUChef(FlowExecutorCPUWithChef<T> flowExecutorCPUWithChef) {
        this.flowExecutorCPUWithChef = flowExecutorCPUWithChef;
    }

    public Map<String, TaskLahmacunResult<?>> execute(int process, int root) {
        return flowExecutorCPUWithChef.execute(process, root);
    }
}