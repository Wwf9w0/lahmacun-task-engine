package org.virtual.annotation;

import org.virtual.dag.FlowGraph;
import org.virtual.model.UserService;
import org.virtual.core.VirtualLahmacunTask;

public class UserFlowServiceExample {

    private final UserService userService;

    public UserFlowServiceExample() {
        UserService userService = new UserService();
        this.userService = userService;
    }

    @VirtualFlow
    public FlowGraph userTaskGraph() {
        FlowGraph graph = new FlowGraph();
        graph.addNode("FetchUsersProfile", () -> new VirtualLahmacunTask<>(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return userService.getProfile();
        }));
        graph.addNode("TransformData", () -> new VirtualLahmacunTask<>(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "TransformData";
        }));
        graph.addNode("PushToDB", () -> new VirtualLahmacunTask<>(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Data pushed to DB";
        }));
        graph.addNode("SendNotification", () -> new VirtualLahmacunTask<>(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Notification sent";
        }));

        graph.addEdge("FetchUsersProfile", "TransformData");
        graph.addEdge("FetchUsersProfile", "SendNotification");
        graph.addEdge("TransformData", "PushToDB");

        graph.validate();
        System.out.println("Grapsh return from userService: " + graph.toString());
        return graph;
    }
}
