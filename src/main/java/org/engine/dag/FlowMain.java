package org.engine.dag;

import org.engine.virtual.model.EventService;
import org.engine.virtual.model.UserService;

public class FlowMain {

    public static void main(String[] args) {
        FlowGraph graph = new FlowGraph();
        UserService userService = new UserService();
        EventService eventService = new EventService();

        graph.addNode("UserService", () -> userService.getProfile());
        graph.addNode("EventService", () -> eventService.eventList());
        graph.addEdge("UserService", "EventService");
        graph.validate();

        System.out.println("Roots: " + graph.getRoots());
        graph.getNodes().forEach(System.out::println);
    }
}
