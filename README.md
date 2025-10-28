# VirtualFlow 

## Description

VirtualFlow is a Java-based DAG (Directed Acyclic Graph) task execution system. It manages task dependencies similarly to Airflow and ensures tasks are executed in the correct order.

This project models nodes and their dependencies to manage workflows. Cycle detection and root node identification guarantee the DAG's validity.

---
##  Installation

```java
        <dependency>
            <groupId>org.engine</groupId>
            <artifactId>lahmacun-virtual-core</artifactId>
            <version>1.1.1</version>
        </dependency>
```

Spring Boot Configuration

Add these beans to your configuration class:

```java
    @Bean
    public QueueManager<Object> queueManager() {
        return new QueueManager<>();
    }

    @Bean
    public VirtualLahmacunRuntimeExecutor<?> virtualLahmacunFlowRuntimeExecutor() {
        return new VirtualLahmacunRuntimeExecutor<>(queueManager());
    }
```

Then inject and use:

private final VirtualLahmacunRuntimeExecutor<?> virtualLahmacunRuntimeExecutor;
```java
public void processFlow(FlowGraph graph, int type) {
try {
virtualLahmacunRuntimeExecutor.startAllFlows(graph, type);
} catch (Exception e) {
System.err.println("[ProcessFlow] couldn't run! " + e.getMessage());
}
}
```
## FlowGraph Structure

### 1️ FlowNode

Each task is represented by a `FlowNode`.

```java
    private  final VirtualLahmacunRuntimeExecutor<?> virtualLahmacunRuntimeExecutor;

    public void processFlow(FlowGraph graph, int type) {
        try {
        virtualLahmacunRuntimeExecutor.startAllFlows(graph, type);
        }catch (Exception e){
            System.err.println("[ProcessFlow] couldn't run!.: " + e.getMessage());
        };
```

 Core Concepts

 FlowNode

Represents an individual unit of work.

```java
FlowNode node = new FlowNode("FetchUsers", () -> System.out.println("Fetching users..."));
```

Properties:
```java
id: Unique identifier of the node

task: Runnable or Supplier task to execute

nextNodes: Dependent nodes

prevNodes: Dependencies
```

FlowGraph

Represents the entire DAG (Directed Acyclic Graph).

```java
Key Methods:

addNode(String, Supplier<T>): Adds a node

addEdge(String, String): Connects dependencies

validate(): Ensures no cycles exist

hasCycle(): Detects cycles
```

 Example DAG

```java
FetchUsers -> TransformData -> PushToDB
FetchUsers -> SendNotification


FetchUsers is the root node.

PushToDB depends on TransformData.
```
SendNotification executes independently after FetchUsers.

Example Code

```java
public void buildFlowGraph() {
    System.out.println("Flow starting...");

    UserLocationRequest request = new UserLocationRequest();
    request.setMemberNo("123");
    request.setLatitude(3.3);
    request.setLongitude(3.3);
    request.setTimestamp(System.currentTimeMillis());

    FlowGraph graph = new FlowGraph();
    graph.addNode("FindUser", () -> new VirtualLahmacunTask<>(() ->
            userLocationService.findNearestUsers(request.getLongitude(), request.getLatitude(), 5)));
    graph.addNode("ProcessUserLocation", () -> new VirtualLahmacunTask<>(() ->
            userLocationService.processUserLocation(request)));
    graph.addNode("SendToWebSocket", () -> new VirtualLahmacunTask<>(() ->
            userLocationService.sendUserLocationFromWebSocket(request)));

    graph.validate();
    graph.addEdge("FindUser", "ProcessUserLocation");
    graph.addEdge("FindUser", "SendToWebSocket");

    System.out.println("Graph nodes: " + graph.getNodes());
    System.out.println("Flow ending...");

    lahmacunFlowService.processFlow(graph, 0);
}
```

 Example Output

```java
--- VirtualFlow Runtime Results ---
FindUser -> SUCCESS | [UserLocationResponse(...)] | 0
ProcessUserLocation -> SUCCESS | true | 0
SendToWebSocket -> SUCCESS | true | 0
Completed Flows -> 0 ms
```

Architecture Overview

```java
Component	Description
FlowGraph	Stores DAG nodes and edges
FlowNode	Represents a single executable task
VirtualThreadOven	Handles IO-bound virtual thread tasks
DedicatedPoolThreadOven	Manages CPU-bound thread pool tasks
TaskQueue (CPU/IO)	Manages queued tasks concurrently
VirtualLahmacunRuntimeExecutor	Orchestrates all task execution and flow logic
```

 Technologies

```java
Java 21 (Project Loom)

Virtual Threads

CompletableFuture

BlockingQueue / ConcurrentLinkedDeque

```


🧾 Notes

```java
Uses LinkedHashMap to preserve task order

validate() ensures DAG integrity

Tasks execute in parallel when dependencies are ready

Built-in timeout and concurrency control

Handles millions of tasks efficiently
```
