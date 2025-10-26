package org.engine.dag;

import org.engine.core.FlowExecutorWithChef;
import org.engine.core.VirtuaLahmacunlChef;
import org.engine.core.VirtualThreadOven;
import org.engine.virtual.model.EventService;
import org.engine.virtual.model.TaskLahmacunResult;
import org.engine.virtual.model.UserService;

import java.util.Map;

public class FlowMain {

    public static void main(String[] args) throws Exception {

        VirtualThreadOven oven = new VirtualThreadOven();
        VirtuaLahmacunlChef chef = new VirtuaLahmacunlChef(oven);

        UserService userService = new UserService();
        EventService eventService = new EventService();

        FlowGraph graph = new FlowGraph();

        graph.addNode("FetchUsersProfile", () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return userService.getProfile();
        });

        graph.addNode("TransformData", () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "TransformData";
        });
        graph.addNode("FetchEventList", () -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return eventService.eventList();
        });
        graph.addNode("PushToDB", () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Data pushed to DB";
        });
        graph.addNode("SendNotification", () -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Notification sent";
        });

        graph.addEdge("FetchUsersProfile", "FetchEventList");
        graph.addEdge("FetchUsersProfile", "SendNotification");
        graph.addEdge("TransformData", "PushToDB");

        graph.validate();

        FlowExecutorWithChef executor = new FlowExecutorWithChef(graph, chef);
        Map<String, TaskLahmacunResult<?>> results = executor.execute();

        System.out.println("\n--- Task Results ---");
        results.forEach((id, r) -> System.out.println(id + ": " + r.status() + " -> " + r.result()));

        oven.close();
    }
}




