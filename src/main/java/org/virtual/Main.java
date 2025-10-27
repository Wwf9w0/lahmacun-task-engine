package org.virtual;

import org.virtual.core.VirtualLahmacunFlowRuntime;
import org.virtual.queue.QueueManager;

public class Main<T> {

    public static void main(String[] args) throws Exception {
        var queueManager = new QueueManager<>();
        var runtime = new VirtualLahmacunFlowRuntime<>(queueManager);
        runtime.startAllFlows("org.virtual.annotation");
    }
}
