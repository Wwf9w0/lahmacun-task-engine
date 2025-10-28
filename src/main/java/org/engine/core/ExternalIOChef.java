package org.engine.core;

import org.engine.model.TaskLahmacunResult;

import java.util.Map;

public class ExternalIOChef<T> {

    private final FlowExecutorIOWithChef<T> flowExecutorIOWithChef;

    public ExternalIOChef(FlowExecutorIOWithChef<T> flowExecutorIOWithChef) {
        this.flowExecutorIOWithChef = flowExecutorIOWithChef;
    }

    public Map<String, TaskLahmacunResult<?>> execute(int process, int root) {
        return flowExecutorIOWithChef.execute(process, root);
    }
}