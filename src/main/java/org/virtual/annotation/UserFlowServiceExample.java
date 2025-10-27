package org.virtual.annotation;

import org.virtual.dag.FlowGraph;
import org.virtual.model.TaskType;
import org.virtual.model.UserService;
import org.virtual.core.VirtualLahmacunTask;

public class UserFlowServiceExample {

    private final UserService userService;

    public UserFlowServiceExample() {
        this.userService = new UserService();
    }

    @VirtualFlow
    @FlowType(TaskType.CPU)
    public FlowGraph userTaskGraph() {
        FlowGraph graph = new FlowGraph();
        graph.addNode("FetchUsersProfile", () -> new VirtualLahmacunTask<>(userService::getProfile));
        graph.addNode("TransformData", () -> new VirtualLahmacunTask<>(() -> "TransformData"));
        graph.addNode("PushToDB", () -> new VirtualLahmacunTask<>(() -> "Data pushed to DB"));
        graph.addNode("SendNotification", () -> new VirtualLahmacunTask<>(() -> "Notification sent"));
        graph.addEdge("FetchUsersProfile", "TransformData");
        graph.addEdge("FetchUsersProfile", "SendNotification");
        graph.addEdge("TransformData", "PushToDB");

        graph.validate();
        System.out.println("Graph return from userService: " + graph);
        return graph;
    }
}
