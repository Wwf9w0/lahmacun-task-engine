package org.virtual.annotation;

import org.virtual.dag.FlowGraph;
import org.virtual.model.EventService;
import org.virtual.core.VirtualLahmacunTask;
import org.virtual.model.TaskType;

public class EventFlowServiceExample {

    private final EventService eventService;

    public EventFlowServiceExample() {
        this.eventService = new EventService();
    }

    @VirtualFlow
    @FlowType(TaskType.IO)
    public FlowGraph getAllEvents() {
        FlowGraph graph = new FlowGraph();
        graph.addNode("TransformData", () -> new VirtualLahmacunTask<>(() -> "TransformData"));
        graph.addNode("EventList", () -> new VirtualLahmacunTask<>(eventService::eventList));
        graph.addEdge("EventList", "TransformData");
        graph.validate();
        System.out.println("Graph return from eventService: " + graph);
        return graph;
    }
}
