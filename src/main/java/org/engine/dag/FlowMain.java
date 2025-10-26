package org.engine.dag;

public class FlowMain {

    public static void main(String[] args) {
        FlowGraph graph = new FlowGraph();

        graph.addNode("FetchUsers", () -> System.out.println("Fetching users..."));
        graph.addNode("TransformData", () -> System.out.println("Transforming data..."));
        graph.addNode("PushToDB", () -> System.out.println("Pushing data to DB..."));
        graph.addNode("SendNotification", () -> System.out.println("Sending notification..."));

        graph.addEdge("FetchUsers", "TransformData");
        graph.addEdge("FetchUsers", "SendNotification");
        graph.addEdge("TransformData", "PushToDB");

        graph.validate();

        System.out.println("Roots: " + graph.getRoots());
        for (FlowNode node : graph.getNodes()) {
            System.out.println(node);
        }
    }
}
