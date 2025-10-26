package org.virtual.annotation;

import org.virtual.dag.FlowGraph;
import org.virtual.virtual.model.EventService;
import org.virtual.virtual.model.VirtualLahmacunTask;

public class EventServiceFlowEcample {

    private final EventService eventService;

    public EventServiceFlowEcample() {
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
        System.out.println("Grapsh return from eventService: " + graph.toString());
        return graph;
    }
}
