package org.virtual.annotation;

import org.virtual.dag.FlowGraph;
import org.virtual.model.EventService;
import org.virtual.core.VirtualLahmacunTask;

public class EventFlowServiceExample {

    private final EventService eventService;

    public EventFlowServiceExample() {
        this.eventService = new EventService();
    }

    @VirtualFlow
    public FlowGraph getAllEvents() {
        FlowGraph graph = new FlowGraph();
        graph.addNode("TransformData", () -> new VirtualLahmacunTask<>(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "TransformData";
        }));
        graph.addNode("EventList", () -> new VirtualLahmacunTask<>(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return eventService.eventList();
        }));
        graph.addEdge("EventList", "TransformData");

        graph.validate();
        System.out.println("Graph return from eventService: " + graph);
        return graph;
    }
}
